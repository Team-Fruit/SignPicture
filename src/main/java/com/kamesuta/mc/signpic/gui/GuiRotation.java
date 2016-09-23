package com.kamesuta.mc.signpic.gui;

import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.MButton;
import com.kamesuta.mc.bnnwidget.component.MNumber;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.position.RArea;
import com.kamesuta.mc.signpic.image.meta.ImageRotation;
import com.kamesuta.mc.signpic.image.meta.ImageRotation.Rotate;
import com.kamesuta.mc.signpic.image.meta.ImageRotation.RotateType;

public class GuiRotation extends WPanel {
	protected ImageRotation rotation;

	protected RotationEditor editor;
	protected RotationPanel panel;

	public GuiRotation(final R position) {
		super(position);
		this.editor = new RotationEditor(new RArea(Coord.left(0), Coord.top(0), Coord.right(20), Coord.height(20)));
		this.panel = new RotationPanel(new RArea(Coord.left(0), Coord.top(20), Coord.right(0), Coord.bottom(0)));
	}

	@Override
	protected void initWidget(final WEvent ev, final Area pgp) {
		add(this.editor);
		add(this.panel);
	}

	public GuiRotation setRotation(final ImageRotation rotation) {
		this.rotation = rotation;
		this.panel.update();
		return this;
	}

	protected void add(final Rotate rotate) {
		this.rotation.rotates.add(rotate);
		this.panel.update();
	}

	protected void remove() {
		if (!this.rotation.rotates.isEmpty()) {
			this.rotation.rotates.remove(this.rotation.rotates.size()-1);
			this.panel.update();
		}
	}

	protected class RotationEditor extends WPanel {
		public RotationEditor(final R position) {
			super(position);
		}

		@Override
		protected void initWidget(final WEvent ev, final Area pgp) {
			final Type type = new Type(new RArea(Coord.left(0), Coord.top(0), Coord.width(20), Coord.bottom(0)), RotateType.X);
			add(type);
			final MNumber number = new MNumber(new RArea(Coord.left(20), Coord.top(0), Coord.right(60), Coord.bottom(0)), 20);
			add(number);
			add(new MButton(new RArea(Coord.right(40), Coord.top(0), Coord.width(20), Coord.bottom(0)), "\u25cb") {
				@Override
				protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
					if (NumberUtils.isNumber(number.field.getText()))
						GuiRotation.this.add(new Rotate(type.getType(), NumberUtils.toFloat(number.field.getText())));
					return true;
				}
			});
			add(new MButton(new RArea(Coord.right(20), Coord.top(0), Coord.width(20), Coord.bottom(0)), "\u00d7") {
				@Override
				protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
					GuiRotation.this.remove();
					return true;
				}
			});
		}
	}

	protected class RotationPanel extends WPanel {
		public RotationPanel(final R position) {
			super(position);
		}

		@Override
		protected void initWidget(final WEvent ev, final Area pgp) {
			update();
		}

		protected void update() {
			this.widgets.clear();
			if (GuiRotation.this.rotation != null) {
				int i = 0;
				for (final Rotate rotate : GuiRotation.this.rotation.rotates) {
					add(new RotationElement(new RArea(Coord.left(0), Coord.top(i*20), Coord.right(0), Coord.height(20)), rotate));
					i++;
				}
			}
			initWidgets(null, null);
		}
	}

	public static class RotationElement extends WPanel {
		protected Rotate rotate;

		public RotationElement(final R position, final Rotate rotate) {
			super(position);
			this.rotate = rotate;
		}

		@Override
		protected void initWidget(final WEvent ev, final Area pgp) {
			add(new MButton(new RArea(Coord.left(0), Coord.top(0), Coord.width(20), Coord.bottom(0)), "\u28691") {
				@Override
				protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {

					return true;
				}
			});
			add(new MButton(new RArea(Coord.left(20), Coord.top(0), Coord.width(20), Coord.bottom(0)), "\u28693") {
				@Override
				protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {

					return true;
				}
			});
			add(new Type(new RArea(Coord.left(40), Coord.top(0), Coord.width(20), Coord.bottom(0)), RotateType.X));
			add(new MNumber(new RArea(Coord.left(60), Coord.top(0), Coord.right(0), Coord.bottom(0)), 20));
		}
	}

	public static class Type extends MButton {
		protected RotateType type;

		public Type(final R position, final RotateType type) {
			super(position, type.name());
			this.type = type;
		}

		@Override
		protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
			final Area a = getGuiPosition(pgp);
			if (a.pointInside(p)) {
				next();
			}
			return super.onClicked(ev, pgp, p, button);
		}

		protected RotateType nextType(RotateType type) {
			final RotateType[] rotateTypes = RotateType.values();
			if (rotateTypes.length > 0) {
				RotateType rotateType = rotateTypes[0];
				boolean next = false;
				for (final RotateType r : rotateTypes) {
					if (next) {
						rotateType = r;
						break;
					}
					if (r == type)
						next = true;
				}
				type = rotateType;
			}
			return type;
		}

		protected void next() {
			this.type = nextType(this.type);
			setText(this.type.name());
		}

		public RotateType getType() {
			return this.type;
		}
	}
}

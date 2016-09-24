package com.kamesuta.mc.signpic.gui;

import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.Maps;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.MButton;
import com.kamesuta.mc.bnnwidget.component.MNumber;
import com.kamesuta.mc.bnnwidget.motion.EasingMotion;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.position.RArea;
import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.image.meta.ImageRotation;
import com.kamesuta.mc.signpic.image.meta.ImageRotation.Rotate;
import com.kamesuta.mc.signpic.image.meta.ImageRotation.RotateType;

public class GuiRotation extends WPanel {
	protected ImageRotation rotation;

	protected RotationEditor editor;
	protected RotationPanel panel;

	public GuiRotation(final R position) {
		super(position);
		this.editor = new RotationEditor(new RArea(Coord.left(0), Coord.top(0), Coord.right(0), Coord.height(20)));
		this.panel = new RotationPanel(new RArea(Coord.left(0), Coord.top(20), Coord.right(0), Coord.bottom(0)));
	}

	@Override
	protected void initWidget() {
		add(this.editor);
		add(this.panel);
	}

	public GuiRotation setRotation(final ImageRotation rotation) {
		this.rotation = rotation;
		this.panel.update();
		return this;
	}

	protected void add(final Rotate rotate) {
		this.panel.add(rotate);
		this.panel.update();
	}

	protected void remove() {
		this.panel.remove();
		this.panel.update();
	}

	protected void onUpdate() {}

	protected class RotationEditor extends WPanel {
		protected Rotate rotate;

		public RotationEditor(final R position) {
			super(position);
			initRotate();
		}

		protected void initRotate() {
			this.rotate = new Rotate(RotateType.X, 0);
		}

		@Override
		protected void initWidget() {
			final Type type = new Type(new RArea(Coord.left(0), Coord.top(0), Coord.width(20), Coord.bottom(0)), this.rotate);
			add(type);
			final MNumber number = new MNumber(new RArea(Coord.left(20), Coord.top(0), Coord.right(40), Coord.bottom(0)), 20);
			add(number);
			add(new MButton(new RArea(Coord.right(20), Coord.top(0), Coord.width(20), Coord.bottom(0)), "\u25cb") {
				@Override
				protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
					if (NumberUtils.isNumber(number.field.getText())) {
						RotationEditor.this.rotate.rotate = NumberUtils.toFloat(number.field.getText());
						GuiRotation.this.add(RotationEditor.this.rotate);
						initRotate();
					}
					return true;
				}
			});
			add(new MButton(new RArea(Coord.right(0), Coord.top(0), Coord.width(20), Coord.bottom(0)), "\u00d7") {
				@Override
				protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
					GuiRotation.this.remove();
					return true;
				}
			});
		}
	}

	protected class RotationPanel extends WPanel {
		private final Map<Rotate, RotationElement> map = Maps.newHashMap();

		public RotationPanel(final R position) {
			super(position);
		}

		@Override
		protected void initWidget() {
			for (final Rotate rotate : GuiRotation.this.rotation.rotates) {
				addWidget(rotate);
			}
			update();
		}

		public void add(final Rotate rotate) {
			GuiRotation.this.rotation.rotates.add(rotate);
			addWidget(rotate);
		}

		public void remove() {
			if (!GuiRotation.this.rotation.rotates.isEmpty()) {
				final Rotate rotate = GuiRotation.this.rotation.rotates.remove(GuiRotation.this.rotation.rotates.size()-1);
				remove(this.map.get(rotate));
			}
		}

		private void addWidget(final Rotate rotate) {
			final Coord top = Coord.top(-1*20);
			final RotationElement element = new RotationElement(new RArea(Coord.left(0), top, Coord.right(0), Coord.height(20)), top, GuiRotation.this.rotation, rotate);
			element.init();
			this.map.put(rotate, element);
			add(element);
		}

		public void update() {
			int i = 0;
			for (final Rotate rotate : GuiRotation.this.rotation.rotates) {
				final RotationElement element = this.map.get(rotate);
				element.top.motion.stop().add(EasingMotion.easeInCirc.move(.5f, ++i*20)).start();
			}
			onUpdate();
			Reference.logger.info(GuiRotation.this.rotation.rotates);
		}

		protected class RotationElement extends WPanel {
			protected ImageRotation imageRotation;
			protected Rotate rotate;
			protected Coord top;

			public RotationElement(final R position, final Coord top, final ImageRotation imageRotation, final Rotate rotate) {
				super(position);
				this.top = top;
				this.imageRotation = imageRotation;
				this.rotate = rotate;
			}

			@Override
			protected void initWidget() {
				add(new MButton(new RArea(Coord.left(0), Coord.top(0), Coord.width(20), Coord.bottom(0)), "\u2191") {
					@Override
					protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
						final int i = GuiRotation.this.rotation.rotates.indexOf(RotationElement.this.rotate);
						if (i!=-1 && i>0) {
							final Rotate prev = GuiRotation.this.rotation.rotates.get(i-1);
							GuiRotation.this.rotation.rotates.set(i-1, RotationElement.this.rotate);
							GuiRotation.this.rotation.rotates.set(i, prev);
							RotationPanel.this.update();
						}
						return true;
					}
				});
				add(new MButton(new RArea(Coord.left(20), Coord.top(0), Coord.width(20), Coord.bottom(0)), "\u2193") {
					@Override
					protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
						final int i = GuiRotation.this.rotation.rotates.indexOf(RotationElement.this.rotate);
						if (i!=-1 && i<GuiRotation.this.rotation.rotates.size() - 1) {
							final Rotate next = GuiRotation.this.rotation.rotates.get(i+1);
							GuiRotation.this.rotation.rotates.set(i+1, RotationElement.this.rotate);
							GuiRotation.this.rotation.rotates.set(i, next);
							RotationPanel.this.update();
						}
						return true;
					}
				});
				add(new Type(new RArea(Coord.left(40), Coord.top(0), Coord.width(20), Coord.bottom(0)), this.rotate) {
					@Override
					protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
						super.onClicked(ev, pgp, p, button);

						return true;
					}
				});
				add(new MNumber(new RArea(Coord.left(60), Coord.top(0), Coord.right(0), Coord.bottom(0)), 20).setNumber(this.rotate.rotate));
			}
		}
	}

	public static class Type extends MButton {
		protected Rotate rotate;

		public Type(final R position, final Rotate rotate) {
			super(position, rotate.type.name());
			this.rotate = rotate;
		}

		@Override
		protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
			next();
			return true;
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
			this.rotate.type = nextType(this.rotate.type);
			setText(this.rotate.type.name());
		}

		public RotateType getType() {
			return this.rotate.type;
		}
	}
}

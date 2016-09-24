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
	protected RotationEditor editor;
	protected RotationPanel panel;

	public GuiRotation(final R position, final ImageRotation rotation) {
		super(position);
		this.editor = new RotationEditor(new RArea(Coord.left(0), Coord.top(0), Coord.right(0), Coord.height(20)));
		this.panel = new RotationPanel(new RArea(Coord.left(0), Coord.top(20), Coord.right(0), Coord.bottom(0)), rotation);
	}

	@Override
	protected void initWidget() {
		add(this.editor);
		add(this.panel);
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
		public RotationEditor(final R position) {
			super(position);
		}

		@Override
		protected void initWidget() {
			add(new MButton(new RArea(Coord.right(20), Coord.top(0), Coord.width(20), Coord.bottom(0)), "\u25cb") {
				@Override
				protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
					GuiRotation.this.add(new Rotate(RotateType.X, 0));
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
		protected final ImageRotation rotation;

		public RotationPanel(final R position, final ImageRotation rotation) {
			super(position);
			this.rotation = rotation;
			for (final Rotate rotate : rotation.rotates) {
				addWidget(rotate);
			}
		}

		@Override
		protected void initWidget() {
			update();
		}

		public void add(final Rotate rotate) {
			this.rotation.rotates.add(0, rotate);
			addWidget(rotate);
		}

		public void remove() {
			if (!this.rotation.rotates.isEmpty()) {
				final Rotate rotate = this.rotation.rotates.remove(this.rotation.rotates.size()-1);
				remove(this.map.get(rotate));
			}
		}

		public void up(final Rotate rotate) {
			final int i = RotationPanel.this.rotation.rotates.indexOf(rotate);
			if (i!=-1 && i>0) {
				final Rotate prev = RotationPanel.this.rotation.rotates.get(i-1);
				this.rotation.rotates.set(i-1, rotate);
				this.rotation.rotates.set(i, prev);
				this.update();
			}
		}

		public void down(final Rotate rotate) {
			final int i = RotationPanel.this.rotation.rotates.indexOf(rotate);
			if (i!=-1 && i<RotationPanel.this.rotation.rotates.size() - 1) {
				final Rotate next = RotationPanel.this.rotation.rotates.get(i+1);
				this.rotation.rotates.set(i+1, rotate);
				this.rotation.rotates.set(i, next);
				this.update();
			}
		}

		private void addWidget(final Rotate rotate) {
			final Coord top = Coord.top(-2*20);
			final RotationElement element = new RotationElement(new RArea(Coord.left(0), top, Coord.right(0), Coord.height(20)), top, rotate);
			element.init();
			this.map.put(rotate, element);
			add(element);
		}

		public void update() {
			int i = 0;
			for (final Rotate rotate : this.rotation.rotates) {
				final RotationElement element = this.map.get(rotate);
				element.top.motion.stop().add(EasingMotion.easeInCirc.move(.5f, ++i*20)).start();
			}
			onUpdate();
			Reference.logger.info(this.rotation.rotates);
		}

		protected class RotationElement extends WPanel {
			protected Rotate rotate;
			protected Coord top;

			public RotationElement(final R position, final Coord top, final Rotate rotate) {
				super(position);
				this.top = top;
				this.rotate = rotate;
			}

			@Override
			protected void initWidget() {
				add(new MButton(new RArea(Coord.left(0), Coord.top(0), Coord.width(20), Coord.bottom(0)), "\u2191") {
					@Override
					protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
						up(RotationElement.this.rotate);
						return true;
					}
				});
				add(new MButton(new RArea(Coord.left(20), Coord.top(0), Coord.width(20), Coord.bottom(0)), "\u2193") {
					@Override
					protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
						down(RotationElement.this.rotate);
						return true;
					}
				});
				add(new Type(new RArea(Coord.left(40), Coord.top(0), Coord.width(20), Coord.bottom(0)), this.rotate));
				add(new MNumber(new RArea(Coord.left(60), Coord.top(0), Coord.right(0), Coord.bottom(0)), 20) {
					@Override
					protected void onNumberChanged(final String oldText, final String newText) {
						if (NumberUtils.isNumber(newText)) {
							RotationElement.this.rotate.rotate = NumberUtils.toFloat(newText);
							onUpdate();
						}
					};
				}.setNumber(this.rotate.rotate));
			}

			protected class Type extends MButton {
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
					onUpdate();
					setText(this.rotate.type.name());
				}

				public RotateType getType() {
					return this.rotate.type;
				}
			}
		}
	}
}

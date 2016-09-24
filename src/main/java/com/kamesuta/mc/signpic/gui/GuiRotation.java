package com.kamesuta.mc.signpic.gui;

import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.Maps;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.MButton;
import com.kamesuta.mc.bnnwidget.component.MNumber;
import com.kamesuta.mc.bnnwidget.motion.BlankMotion;
import com.kamesuta.mc.bnnwidget.motion.EasingMotion;
import com.kamesuta.mc.bnnwidget.motion.MCoord;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.position.RArea;
import com.kamesuta.mc.signpic.image.meta.ImageRotation;
import com.kamesuta.mc.signpic.image.meta.ImageRotation.Rotate;
import com.kamesuta.mc.signpic.image.meta.ImageRotation.RotateType;

public class GuiRotation extends WPanel {
	protected RotationEditor editor;
	protected RotationPanel panel;

	public GuiRotation(final R position, final ImageRotation rotation) {
		super(position);
		this.editor = new RotationEditor(new RArea(MCoord.pleft(0).start(), Coord.top(0), Coord.pwidth(1f), Coord.height(15)));
		this.panel = new RotationPanel(new RArea(Coord.left(0), Coord.top(15), Coord.right(0), Coord.bottom(0)), rotation);
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
			final MCoord top = MCoord.ptop(-1f).add(EasingMotion.easeInBack.move(.25f, 0f)).start();
			add(new MButton(new RArea(Coord.left(15), top, Coord.width(15), Coord.pheight(1f)), "\u25cb") {
				@Override
				protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
					GuiRotation.this.add(new Rotate(RotateType.X, 0));
					return true;
				}

				@Override
				public boolean onCloseRequest() {
					top.stop().add(EasingMotion.easeInBack.move(.25f, -1f));
					return false;
				}

				@Override
				public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
					return top.isFinished();
				}
			});
			add(new MButton(new RArea(Coord.left(0), top, Coord.width(15), Coord.pheight(1f)), "\u00d7") {
				@Override
				protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
					GuiRotation.this.remove();
					return true;
				}

				@Override
				public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
					return top.isFinished();
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
			for (final ListIterator<Rotate> itr = rotation.rotates.listIterator(); itr.hasNext();) {
				final int n = itr.nextIndex();
				final Rotate rotate = itr.next();
				addWidget(rotate, n);
			}
		}

		@Override
		protected void initWidget() {
			update();
		}

		public void add(final Rotate rotate) {
			final int n = this.rotation.rotates.size();
			if (n < 10) {
				this.rotation.rotates.add(rotate);
				addWidget(rotate, n);
			}
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
				this.rotation.rotates.set(i, prev);
				this.rotation.rotates.set(i-1, rotate);
				this.update();
			}
		}

		public void down(final Rotate rotate) {
			final int i = RotationPanel.this.rotation.rotates.indexOf(rotate);
			if (i!=-1 && i<RotationPanel.this.rotation.rotates.size() - 1) {
				final Rotate next = RotationPanel.this.rotation.rotates.get(i+1);
				this.rotation.rotates.set(i, next);
				this.rotation.rotates.set(i+1, rotate);
				this.update();
			}
		}

		private void addWidget(final Rotate rotate, final int n) {
			final float t = n*15;
			final MCoord left = MCoord.pleft(-1f).add(new BlankMotion(t/15f*.025f)).add(EasingMotion.easeOutBack.move(.25f, 0f)).start();
			final MCoord top = MCoord.top(t);
			final RotationElement element = new RotationElement(new RArea(left, top, Coord.pwidth(1f), Coord.height(15)), left, top, rotate);
			this.map.put(rotate, element);
			add(element);
		}

		public void update() {
			int i = 0;
			for (final Rotate rotate : this.rotation.rotates) {
				final RotationElement element = this.map.get(rotate);
				element.top.stop().add(EasingMotion.easeInCirc.move(.25f, i++*15)).start();
			}
			onUpdate();
		}

		protected class RotationElement extends WPanel {
			protected Rotate rotate;
			protected MCoord left;
			protected MCoord top;

			public RotationElement(final R position, final MCoord left, final MCoord top, final Rotate rotate) {
				super(position);
				this.left = left;
				this.top = top;
				this.rotate = rotate;
			}

			@Override
			protected void initWidget() {
				add(new MButton(new RArea(Coord.left(15*0), Coord.top(0), Coord.width(15), Coord.bottom(0)), "\u2191") {
					@Override
					protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
						up(RotationElement.this.rotate);
						return true;
					}
				});
				add(new MButton(new RArea(Coord.left(15*1), Coord.top(0), Coord.width(15), Coord.bottom(0)), "\u2193") {
					@Override
					protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
						down(RotationElement.this.rotate);
						return true;
					}
				});
				add(new Type(new RArea(Coord.left(15*2), Coord.top(0), Coord.width(15), Coord.bottom(0)), this.rotate));
				add(new MNumber(new RArea(Coord.left(15*3), Coord.top(0), Coord.right(0), Coord.bottom(0)), 15) {
					@Override
					protected void onNumberChanged(final String oldText, final String newText) {
						if (NumberUtils.isNumber(newText)) {
							RotationElement.this.rotate.rotate = NumberUtils.toFloat(newText);
							onUpdate();
						}
					}
				}.setNumber(this.rotate.rotate));
			}

			@Override
			public boolean onCloseRequest() {
				this.left.stop().add(new BlankMotion(this.top.get()/15*.025f)).add(EasingMotion.easeInBack.move(.25f, -1f)).start();
				return false;
			}

			@Override
			public boolean onClosing(final WEvent ev, final Area pgp, final Point p) {
				return this.left.isFinished();
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

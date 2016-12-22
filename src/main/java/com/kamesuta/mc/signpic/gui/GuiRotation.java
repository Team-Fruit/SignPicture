package com.kamesuta.mc.signpic.gui;

import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.Maps;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.MButton;
import com.kamesuta.mc.bnnwidget.component.MLabel;
import com.kamesuta.mc.bnnwidget.component.MNumber;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.motion.Motion;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.image.meta.RotationData.RotateType;
import com.kamesuta.mc.signpic.image.meta.RotationData.RotationBuilder;
import com.kamesuta.mc.signpic.image.meta.RotationData.RotationBuilder.ImageRotate;

import net.minecraft.client.resources.I18n;

public class GuiRotation extends WPanel {
	protected RotationEditor editor;
	protected RotationPanel panel;

	public GuiRotation(final R position, final RotationBuilder rotation) {
		super(position);
		final VMotion left = V.pm(-1).add(Easings.easeOutBack.move(.25f, 0f)).start();
		this.editor = new RotationEditor(new R(Coord.left(left), Coord.top(0), Coord.pwidth(1f), Coord.height(15))) {
			@Override
			public boolean onCloseRequest() {
				left.stop().add(Easings.easeInBack.move(.25f, -1f));
				return false;
			}

			@Override
			public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
				return left.isFinished();
			}
		};
		this.panel = new RotationPanel(new R(Coord.left(0), Coord.top(15), Coord.right(0), Coord.bottom(0)), rotation);
	}

	@Override
	protected void initWidget() {
		final VMotion label = V.pm(-1f).add(Easings.easeOutBack.move(.25f, 0f)).start();
		add(new MLabel(new R(Coord.left(label), Coord.pwidth(1f), Coord.top(15*0), Coord.height(15))) {
			@Override
			public boolean onCloseRequest() {
				label.stop().add(Easings.easeInBack.move(.25f, -1f));
				return false;
			}

			@Override
			public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
				return label.isFinished();
			}
		}.setText(I18n.format("signpic.gui.editor.rotation.category")));
		add(this.editor);
		add(this.panel);
	}

	protected void add(final ImageRotate rotate) {
		this.panel.add(rotate);
		this.panel.update();
	}

	protected void remove() {
		this.panel.remove();
		this.panel.update();
	}

	protected void onUpdate() {
	}

	protected class RotationEditor extends WPanel {
		public RotationEditor(final R position) {
			super(position);
		}

		@Override
		protected void initWidget() {
			add(new MButton(new R(Coord.ptop(0), Coord.right(15), Coord.width(15), Coord.pheight(1f))) {
				@Override
				protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
					GuiRotation.this.add(new ImageRotate(RotateType.X, 0));
					return true;
				}
			}.setText(I18n.format("signpic.gui.editor.rotation.add")));
			add(new MButton(new R(Coord.ptop(0), Coord.right(0), Coord.width(15), Coord.pheight(1f))) {
				@Override
				protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
					GuiRotation.this.remove();
					return true;
				}
			}.setText(I18n.format("signpic.gui.editor.rotation.remove")));
		}
	}

	protected class RotationPanel extends WPanel {
		private final Map<ImageRotate, RotationElement> map = Maps.newHashMap();
		protected final RotationBuilder rotation;

		public RotationPanel(final R position, final RotationBuilder rotation) {
			super(position);
			this.rotation = rotation;
			for (final ListIterator<ImageRotate> itr = rotation.rotates.listIterator(); itr.hasNext();) {
				final int n = itr.nextIndex();
				final ImageRotate rotate = itr.next();
				addWidget(rotate, n);
			}
		}

		@Override
		protected void initWidget() {
			update();
		}

		public void add(final ImageRotate rotate) {
			final int n = this.rotation.rotates.size();
			if (n<3) {
				this.rotation.rotates.add(rotate);
				addWidget(rotate, n);
			}
		}

		public void remove() {
			if (!this.rotation.rotates.isEmpty()) {
				final ImageRotate rotate = this.rotation.rotates.remove(this.rotation.rotates.size()-1);
				remove(this.map.get(rotate));
			}
		}

		public void up(final ImageRotate rotate) {
			final int i = RotationPanel.this.rotation.rotates.indexOf(rotate);
			if (i!=-1&&i>0) {
				final ImageRotate prev = RotationPanel.this.rotation.rotates.get(i-1);
				this.rotation.rotates.set(i, prev);
				this.rotation.rotates.set(i-1, rotate);
				this.update();
			}
		}

		public void down(final ImageRotate rotate) {
			final int i = RotationPanel.this.rotation.rotates.indexOf(rotate);
			if (i!=-1&&i<RotationPanel.this.rotation.rotates.size()-1) {
				final ImageRotate next = RotationPanel.this.rotation.rotates.get(i+1);
				this.rotation.rotates.set(i, next);
				this.rotation.rotates.set(i+1, rotate);
				this.update();
			}
		}

		private void addWidget(final ImageRotate rotate, final int n) {
			final float t = n*15;
			final VMotion left = V.pm(-1f).add(Motion.blank(t/15f*.025f)).add(Easings.easeOutBack.move(.25f, 0f)).start();
			final VMotion top = V.am(t);
			final RotationElement element = new RotationElement(new R(Coord.left(left), Coord.top(top), Coord.pwidth(1f), Coord.height(15)), left, top, rotate);
			this.map.put(rotate, element);
			add(element);
		}

		public void update() {
			int i = 0;
			for (final ImageRotate rotate : this.rotation.rotates) {
				final RotationElement element = this.map.get(rotate);
				element.top.stop().add(Easings.easeInCirc.move(.25f, i++*15)).start();
			}
			onUpdate();
		}

		protected class RotationElement extends WPanel {
			protected ImageRotate rotate;
			protected VMotion left;
			protected VMotion top;

			public RotationElement(final R position, final VMotion left, final VMotion top, final ImageRotate rotate) {
				super(position);
				this.left = left;
				this.top = top;
				this.rotate = rotate;
			}

			@Override
			protected void initWidget() {
				add(new Type(new R(Coord.left(15*0), Coord.top(0), Coord.width(15), Coord.bottom(0)), this.rotate));
				add(new MNumber(new R(Coord.left(15*1), Coord.top(0), Coord.right(15*2), Coord.bottom(0)), 15) {
					{
						if (RotationElement.this.rotate.rotate!=0f)
							setNumber(RotationElement.this.rotate.rotate);
					}

					@Override
					protected void onNumberChanged(final String oldText, final String newText) {
						if (NumberUtils.isNumber(newText))
							RotationElement.this.rotate.rotate = NumberUtils.toFloat(newText);
						else
							RotationElement.this.rotate.rotate = 0;
						onUpdate();
					}

					@Override
					protected boolean negClicked() {
						final boolean b = super.negClicked();
						if (NumberUtils.toFloat(this.field.getText())==0f)
							this.field.setText("");
						return b;
					}

					@Override
					protected boolean posClicked() {
						final boolean b = super.posClicked();
						if (NumberUtils.toFloat(this.field.getText())==0f)
							this.field.setText("");
						return b;
					}
				}.setNegLabel(I18n.format("signpic.gui.editor.rotation.neg")).setPosLabel(I18n.format("signpic.gui.editor.rotation.pos")).setUnknownLabel(I18n.format("signpic.gui.editor.rotation.unknown")));
				add(new MButton(new R(Coord.right(15*1), Coord.top(0), Coord.width(15), Coord.bottom(0))) {
					@Override
					protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
						up(RotationElement.this.rotate);
						return true;
					}
				}.setText(I18n.format("signpic.gui.editor.rotation.up")));
				add(new MButton(new R(Coord.right(15*0), Coord.top(0), Coord.width(15), Coord.bottom(0))) {
					@Override
					protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
						down(RotationElement.this.rotate);
						return true;
					}
				}.setText(I18n.format("signpic.gui.editor.rotation.down")));
			}

			@Override
			public boolean onCloseRequest() {
				this.left.stop().add(Motion.blank(this.top.get()/15*.025f)).add(Easings.easeInBack.move(.25f, -1f)).start();
				return false;
			}

			@Override
			public boolean onClosing(final WEvent ev, final Area pgp, final Point p) {
				return this.left.isFinished();
			}

			protected class Type extends MButton {
				protected ImageRotate rotate;

				public Type(final R position, final ImageRotate rotate) {
					super(position);
					setText(rotate.type.name());
					this.rotate = rotate;
				}

				@Override
				protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
					next();
					return true;
				}

				protected RotateType nextType(RotateType type) {
					final RotateType[] rotateTypes = RotateType.values();
					if (rotateTypes.length>0) {
						RotateType rotateType = rotateTypes[0];
						boolean next = false;
						for (final RotateType r : rotateTypes) {
							if (next) {
								rotateType = r;
								break;
							}
							if (r==type)
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

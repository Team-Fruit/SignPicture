package com.kamesuta.mc.signpic.gui;

import java.util.ListIterator;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.Maps;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.FontLabel;
import com.kamesuta.mc.bnnwidget.component.MButton;
import com.kamesuta.mc.bnnwidget.component.MNumber;
import com.kamesuta.mc.bnnwidget.font.WFont;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.motion.Motion;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.attr.prop.RotationData.RotateType;
import com.kamesuta.mc.signpic.attr.prop.RotationData.RotationBuilder;
import com.kamesuta.mc.signpic.attr.prop.RotationData.RotationBuilder.ImageRotate;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.resources.I18n;

public abstract class GuiRotation extends WPanel {
	protected @Nonnull RefRotation rotation;
	protected @Nonnull RotationEditor editor;
	protected @Nullable RotationPanel panel;

	public GuiRotation(final @Nonnull R position, final @Nonnull RefRotation rotation) {
		super(position);
		this.rotation = rotation;
		final VMotion left = V.pm(-1).add(Easings.easeOutBack.move(.25f, 0f)).start();
		this.editor = new RotationEditor(new R(Coord.left(left), Coord.top(0), Coord.pwidth(1f), Coord.height(15))) {
			@Override
			public boolean onCloseRequest() {
				left.stop().add(Easings.easeInBack.move(.25f, -1f));
				return false;
			}

			@Override
			public boolean onClosing(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point mouse) {
				return left.isFinished();
			}
		};
	}

	public static interface RefRotation {
		@Nonnull
		RotationBuilder rotation();

		boolean isFirst();
	}

	protected abstract void onUpdate();

	@Override
	protected void initWidget() {
		final VMotion label = V.pm(-1f).add(Easings.easeOutBack.move(.25f, 0f)).start();
		add(new FontLabel(new R(Coord.left(label), Coord.pwidth(1f), Coord.top(15*0), Coord.height(15)), WFont.fontRenderer) {
			@Override
			public boolean onCloseRequest() {
				label.stop().add(Easings.easeInBack.move(.25f, -1f));
				return false;
			}

			@Override
			public boolean onClosing(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point mouse) {
				return label.isFinished();
			}
		}.setText(I18n.format("signpic.gui.editor.rotation.category")));
		add(this.editor);
		add(this.panel = new RotationPanel(new R(Coord.left(0), Coord.top(15), Coord.right(0), Coord.bottom(0))));
	}

	@SubscribeEvent
	public void onChanged(final @Nullable PropertyChangeEvent ev) {
		if (this.panel!=null)
			remove(this.panel);
		add(this.panel = new RotationPanel(new R(Coord.left(0), Coord.top(15), Coord.right(0), Coord.bottom(0))));
	}

	@Override
	public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final Point p) {
		ev.bus.register(this);
		super.update(ev, pgp, p);
	}

	protected void add(final @Nonnull ImageRotate rotate) {
		final RotationPanel panel = this.panel;
		if (panel!=null) {
			panel.add(rotate);
			panel.update();
		}
	}

	protected void remove() {
		final RotationPanel panel = this.panel;
		if (panel!=null) {
			panel.remove();
			panel.update();
		}
	}

	protected class RotationEditor extends WPanel {
		public RotationEditor(final @Nonnull R position) {
			super(position);
		}

		@Override
		protected void initWidget() {
			add(new MButton(new R(Coord.ptop(0), Coord.right(15), Coord.width(15), Coord.pheight(1f))) {
				@Override
				protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
					GuiRotation.this.add(new ImageRotate(RotateType.X, 0));
					return true;
				}
			}.setText(I18n.format("signpic.gui.editor.rotation.add")));
			add(new MButton(new R(Coord.ptop(0), Coord.right(0), Coord.width(15), Coord.pheight(1f))) {
				@Override
				protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
					GuiRotation.this.remove();
					return true;
				}
			}.setText(I18n.format("signpic.gui.editor.rotation.remove")));
		}
	}

	protected class RotationPanel extends WPanel {
		private final @Nonnull Map<ImageRotate, RotationElement> map = Maps.newHashMap();

		public RotationPanel(final @Nonnull R position) {
			super(position);
			final RotationBuilder rb = GuiRotation.this.rotation.rotation();
			for (final ListIterator<ImageRotate> itr = rb.rotates.listIterator(); itr.hasNext();) {
				final int n = itr.nextIndex();
				final ImageRotate rotate = itr.next();
				addWidget(rotate, n);
			}
		}

		@Override
		protected void initWidget() {
			// onUpdate();
		}

		public void add(final @Nonnull ImageRotate rotate) {
			final RotationBuilder rb = GuiRotation.this.rotation.rotation();
			final int n = rb.rotates.size();
			if (n<3) {
				rb.rotates.add(rotate);
				addWidget(rotate, n);
			}
		}

		public void remove() {
			final RotationBuilder rb = GuiRotation.this.rotation.rotation();
			if (!rb.rotates.isEmpty()) {
				final ImageRotate rotate = rb.rotates.remove(rb.rotates.size()-1);
				remove(this.map.get(rotate));
			}
		}

		public void up(final @Nonnull ImageRotate rotate) {
			final RotationBuilder rb = GuiRotation.this.rotation.rotation();
			final int i = rb.rotates.indexOf(rotate);
			if (i!=-1&&i>0) {
				final ImageRotate prev = rb.rotates.get(i-1);
				rb.rotates.set(i, prev);
				rb.rotates.set(i-1, rotate);
				this.update();
			}
		}

		public void down(final @Nonnull ImageRotate rotate) {
			final RotationBuilder rb = GuiRotation.this.rotation.rotation();
			final int i = rb.rotates.indexOf(rotate);
			if (i!=-1&&i<rb.rotates.size()-1) {
				final ImageRotate next = rb.rotates.get(i+1);
				rb.rotates.set(i, next);
				rb.rotates.set(i+1, rotate);
				this.update();
			}
		}

		private void addWidget(final @Nonnull ImageRotate rotate, final int n) {
			final float t = n*15;
			final VMotion left = V.pm(-1f).add(Motion.blank(t/15f*.025f)).add(Easings.easeOutBack.move(.25f, 0f)).start();
			final VMotion top = V.am(t);
			final RotationElement element = new RotationElement(new R(Coord.left(left), Coord.top(top), Coord.pwidth(1f), Coord.height(15)), left, top, rotate);
			this.map.put(rotate, element);
			add(element);
		}

		public void update() {
			final RotationBuilder rb = GuiRotation.this.rotation.rotation();
			int i = 0;
			for (final ImageRotate rotate : rb.rotates) {
				final RotationElement element = this.map.get(rotate);
				element.top.stop().add(Easings.easeInCirc.move(.25f, i++*15)).start();
			}
			onUpdate();
		}

		protected class RotationElement extends WPanel {
			protected @Nonnull ImageRotate rotate;
			protected @Nonnull VMotion left;
			protected @Nonnull VMotion top;

			public RotationElement(final @Nonnull R position, final @Nonnull VMotion left, final @Nonnull VMotion top, final @Nonnull ImageRotate rotate) {
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
					protected void onNumberChanged(final @Nonnull String oldText, final @Nonnull String newText) {
						if (NumberUtils.isNumber(newText)) {
							final float f = NumberUtils.toFloat(newText);
							RotationElement.this.rotate.rotate = GuiRotation.this.rotation.isFirst() ? (f%8+8)%8 : f;
						} else
							RotationElement.this.rotate.rotate = 0;
						if (this.initialized)
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
					protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
						up(RotationElement.this.rotate);
						return true;
					}
				}.setText(I18n.format("signpic.gui.editor.rotation.up")));
				add(new MButton(new R(Coord.right(15*0), Coord.top(0), Coord.width(15), Coord.bottom(0))) {
					@Override
					protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
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
			public boolean onClosing(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
				return this.left.isFinished();
			}

			protected class Type extends MButton {
				protected ImageRotate rotate;

				public Type(final @Nonnull R position, final @Nonnull ImageRotate rotate) {
					super(position);
					setText(rotate.type.name());
					this.rotate = rotate;
				}

				@Override
				protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
					next();
					return true;
				}

				protected RotateType nextType(@Nonnull RotateType type) {
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

				public @Nonnull RotateType getType() {
					return this.rotate.type;
				}
			}
		}
	}
}

package com.kamesuta.mc.signpic.gui;
/*
import java.util.Iterator;
import java.util.List;
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
import com.kamesuta.mc.signpic.attr.AttrWriters;
import com.kamesuta.mc.signpic.attr.AttrWriters.AttrWriter;
import com.kamesuta.mc.signpic.attr.IPropBuilder;
import com.kamesuta.mc.signpic.attr.prop.RotationData.RotationBuilder;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.resources.I18n;

public abstract class GuiVariable extends WPanel {
	protected @Nonnull AttrWriters attrs;
	protected @Nonnull RotationEditor editor;
	protected @Nullable RotationPanel panel;

	public GuiVariable(final @Nonnull R position, final @Nonnull AttrWriters attr) {
		super(position);
		this.attrs = attr;
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
		add(this.panel = new RotationPanel(new R(Coord.left(0), Coord.top(15), Coord.right(0), Coord.bottom(0)), this.attrs.getFrame(0)));
	}

	@SubscribeEvent
	public void onChanged(final @Nullable PropertyChangeEvent ev) {
		if (this.panel!=null)
			remove(this.panel);
		add(this.panel = new RotationPanel(new R(Coord.left(0), Coord.top(15), Coord.right(0), Coord.bottom(0)), this.attrs.getFrame(0)));
	}

	@Override
	public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final Point p) {
		ev.bus.register(this);
		super.update(ev, pgp, p);
	}

	protected void add(final @Nonnull IPropBuilder<?, ?> prop) {
		final RotationPanel panel = this.panel;
		if (panel!=null) {
			panel.add(prop);
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
					GuiVariable.this.add(GuiVariable.this.attrs.getFrame(0).size);
					return true;
				}
			}.setText(I18n.format("signpic.gui.editor.rotation.add")));
			add(new MButton(new R(Coord.ptop(0), Coord.right(0), Coord.width(15), Coord.pheight(1f))) {
				@Override
				protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
					GuiVariable.this.remove();
					return true;
				}
			}.setText(I18n.format("signpic.gui.editor.rotation.remove")));
		}
	}

	protected class RotationPanel extends WPanel {
		protected @Nonnull AttrWriter attr;
		private final @Nonnull Map<IPropBuilder<?, ?>, RotationElement> map = Maps.newHashMap();

		public RotationPanel(final @Nonnull R position, final @Nonnull AttrWriter attr) {
			super(position);
			this.attr = attr;
			final List<IPropBuilder<?, ?>> writers = attr.getWriters();
			int i = 0;
			for (final Iterator<IPropBuilder<?, ?>> itr = writers.iterator(); itr.hasNext(); i++) {
				final IPropBuilder<?, ?> prop = itr.next();
				addWidget(prop, i);
			}
		}

		@Override
		protected void initWidget() {
			// onUpdate();
		}

		public void add(final @Nonnull IPropBuilder<?, ?> prop) {
			final List<IPropBuilder<?, ?>> writers = this.attr.getWriters();
			final int n = writers.size();
			if (n<3) {
				writers.add(prop);
				addWidget(prop, n);
			}
		}

		public void remove() {
			final List<IPropBuilder<?, ?>> writers = this.attr.getWriters();
			if (!writers.isEmpty()) {
				final IPropBuilder<?, ?> writer = writers.remove(writers.size()-1);
				final RotationElement el = this.map.get(writer);
				if (el!=null)
					remove(el);
			}
		}

		public void up(final @Nonnull IPropBuilder<?, ?> prop) {
			final List<IPropBuilder<?, ?>> writers = this.attr.getWriters();
			final int i = writers.indexOf(prop);
			if (i!=-1&&i>0) {
				final IPropBuilder<?, ?> prev = writers.get(i-1);
				writers.set(i, prev);
				writers.set(i-1, prop);
				this.update();
			}
		}

		public void down(final @Nonnull IPropBuilder<?, ?> prop) {
			final List<IPropBuilder<?, ?>> writers = this.attr.getWriters();
			final int i = writers.indexOf(prop);
			if (i!=-1&&i<writers.size()-1) {
				final IPropBuilder<?, ?> next = writers.get(i+1);
				writers.set(i, next);
				writers.set(i+1, prop);
				this.update();
			}
		}

		private void addWidget(final @Nonnull IPropBuilder<?, ?> prop, final int n) {
			final float t = n*15;
			final VMotion left = V.pm(-1f).add(Motion.blank(t/15f*.025f)).add(Easings.easeOutBack.move(.25f, 0f)).start();
			final VMotion top = V.am(t);
			final RotationElement element = new RotationElement(new R(Coord.left(left), Coord.top(top), Coord.pwidth(1f), Coord.height(15)), left, top, prop);
			this.map.put(prop, element);
			add(element);
		}

		public void update() {
			final List<IPropBuilder<?, ?>> writers = this.attr.getWriters();
			int i = 0;
			for (final IPropBuilder<?, ?> writer : writers) {
				final RotationElement element = this.map.get(writer);
				element.top.stop().add(Easings.easeInCirc.move(.25f, i++*15)).start();
			}
			onUpdate();
		}

		protected class RotationElement extends WPanel {
			protected @Nonnull IPropBuilder<?, ?> prop;
			protected @Nonnull VMotion left;
			protected @Nonnull VMotion top;

			public RotationElement(final @Nonnull R position, final @Nonnull VMotion left, final @Nonnull VMotion top, final @Nonnull IPropBuilder<?, ?> prop) {
				super(position);
				this.left = left;
				this.top = top;
				this.prop = prop;
			}

			@Override
			protected void initWidget() {
				add(new Type(new R(Coord.left(15*0), Coord.top(0), Coord.width(15), Coord.bottom(0)), this.prop));
				add(new MNumber(new R(Coord.left(15*1), Coord.top(0), Coord.right(15*2), Coord.bottom(0)), 15) {
					{
						if (RotationElement.this.prop.rotate!=0f)
						 	setNumber(RotationElement.this.prop.rotate);
					}

					@Override
					protected void onNumberChanged(final @Nonnull String oldText, final @Nonnull String newText) {
						if (NumberUtils.isNumber(newText)) {
							final float f = NumberUtils.toFloat(newText);
							;// RotationElement.this.prop.rotate = GuiVariable.this.rotation.isFirst() ? (f%8+8)%8 : f;
						} else
							;// RotationElement.this.prop.rotate = 0;
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
						up(RotationElement.this.prop);
						return true;
					}
				}.setText(I18n.format("signpic.gui.editor.rotation.up")));
				add(new MButton(new R(Coord.right(15*0), Coord.top(0), Coord.width(15), Coord.bottom(0))) {
					@Override
					protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
						down(RotationElement.this.prop);
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
				protected IPropBuilder<?, ?> prop;

				public Type(final @Nonnull R position, final @Nonnull IPropBuilder<?, ?> prop) {
					super(position);
					final StringBuilder stb = new StringBuilder();
					for (final String str : RotationPanel.this.attr.getIds(prop))
						stb.append(str);
					setText(stb.toString());
					this.prop = prop;
				}

				@Override
				protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
					next();
					return true;
				}

				protected IPropBuilder<?, ?> nextType(@Nonnull IPropBuilder<?, ?> prop) {
					final List<IPropBuilder<?, ?>> rotateTypes = RotationPanel.this.attr.getWriters();
					if (rotateTypes.size()>0) {
						IPropBuilder<?, ?> rotateType = rotateTypes.get(0);
						boolean next = false;
						for (final IPropBuilder<?, ?> r : rotateTypes) {
							if (next) {
								rotateType = r;
								break;
							}
							if (r==prop)
								next = true;
						}
						prop = rotateType;
					}
					return prop;
				}

				protected void next() {
					this.prop = nextType(this.prop);
					onUpdate();
					final StringBuilder stb = new StringBuilder();
					for (final String str : RotationPanel.this.attr.getIds(this.prop))
						stb.append(str);
					setText(stb.toString());
				}
			}
		}
	}
}
*/
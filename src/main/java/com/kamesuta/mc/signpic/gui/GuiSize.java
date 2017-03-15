package com.kamesuta.mc.signpic.gui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.FontLabel;
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
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.attr.prop.SizeData.SizeBuilder;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.resources.I18n;

public abstract class GuiSize extends WPanel {
	public GuiSize(final @Nonnull R position) {
		super(position);
	}

	protected @Nonnull abstract SizeBuilder size();

	protected abstract void onUpdate();

	@Override
	protected void initWidget() {
		final VMotion label = V.pm(-1f).start();
		add(new FontLabel(new R(Coord.left(label), Coord.pwidth(1f), Coord.top(15*0), Coord.height(15)), WFont.fontRenderer) {
			@Override
			public void onAdded() {
				super.onAdded();
				label.stop().add(Easings.easeOutBack.move(.25f, 0f));
			}

			@Override
			public boolean onCloseRequest() {
				label.stop().add(Easings.easeInBack.move(.25f, -1f));
				return false;
			}

			@Override
			public boolean onClosing(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point mouse) {
				return label.isFinished();
			}
		}.setText(I18n.format("signpic.gui.editor.size.category")));
		final VMotion w = V.pm(-1f).start();
		add(new SizeElement(new R(Coord.left(w), Coord.pwidth(1f), Coord.top(15*1), Coord.height(15)), w, 0) {
			{
				this.label.setText(I18n.format("signpic.gui.editor.size.width"));
				this.number.setNegLabel(I18n.format("signpic.gui.editor.size.width.neg"));
				this.number.setPosLabel(I18n.format("signpic.gui.editor.size.width.pos"));
				this.number.setUnknownLabel(I18n.format("signpic.gui.editor.size.width.unknown"));
			}

			@Override
			public void onAdded() {
				super.onAdded();
				addDelay(this.left.stop()).add(Easings.easeOutBack.move(.25f, 0f));
			}

			@Override
			protected void set(final float f) {
				size().width = f;
				onUpdate();
			}

			@Override
			protected final float get() {
				return size().width;
			}

			@Override
			protected @Nonnull VMotion addDelay(final @Nonnull VMotion c) {
				return c.add(Motion.blank(1*.025f));
			}
		});
		final VMotion h = V.pm(-1f).start();
		add(new SizeElement(new R(Coord.left(h), Coord.pwidth(1f), Coord.top(15*2), Coord.height(15)), h, 1) {
			{
				this.label.setText(I18n.format("signpic.gui.editor.size.height"));
				this.number.setNegLabel(I18n.format("signpic.gui.editor.size.height.neg"));
				this.number.setPosLabel(I18n.format("signpic.gui.editor.size.height.pos"));
				this.number.setUnknownLabel(I18n.format("signpic.gui.editor.size.height.unknown"));
			}

			@Override
			protected void initWidget() {
				super.initWidget();
			}

			@Override
			public void onAdded() {
				super.onAdded();
				addDelay(this.left.stop()).add(Easings.easeOutBack.move(.25f, 0f));
			}

			@Override
			protected void set(final float f) {
				size().height = f;
				onUpdate();
			}

			@Override
			protected final float get() {
				return size().height;
			}

			@Override
			protected @Nonnull VMotion addDelay(final @Nonnull VMotion c) {
				return c.add(Motion.blank(2*.025f));
			}
		});
	}

	protected abstract class SizeElement extends WPanel {
		public @Nonnull FontLabel label;
		public @Nonnull MNumber number;
		protected @Nonnull VMotion left;

		public SizeElement(final @Nonnull R position, final @Nonnull VMotion left, final int i) {
			super(position);
			this.label = new FontLabel(new R(Coord.left(0), Coord.width(15f), Coord.top(0), Coord.pheight(1f)), WFont.fontRenderer);
			this.number = new MNumber(new R(Coord.left(15), Coord.right(0), Coord.top(0), Coord.pheight(1f)), 15) {
				@Override
				protected void onNumberChanged(final @Nonnull String oldText, final @Nonnull String newText) {
					if (NumberUtils.isNumber(newText))
						set(NumberUtils.toFloat(newText));
					else
						set(SizeData.Unknown);
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
			};
			this.left = left;
		}

		@Override
		protected void initWidget() {
			add(this.label);
			onChanged(null);
			add(this.number);
		}

		@SubscribeEvent
		public void onChanged(final @Nullable PropertyChangeEvent ev) {
			this.number.setNumber(get());
		}

		@Override
		public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
			ev.bus.register(this);
			super.update(ev, pgp, p);
		}

		protected abstract float get();

		protected abstract void set(float f);

		protected @Nonnull VMotion addDelay(final @Nonnull VMotion c) {
			return c;
		}

		@Override
		public boolean onCloseRequest() {
			addDelay(this.left.stop()).add(Easings.easeInBack.move(.25f, -1f)).start();
			return false;
		}

		@Override
		public boolean onClosing(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
			return this.left.isFinished();
		}
	}
}
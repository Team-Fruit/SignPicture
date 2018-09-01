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
import com.kamesuta.mc.signpic.attr.prop.OffsetData.OffsetBuilder;
import com.kamesuta.mc.signpic.attr.prop.OffsetData.OffsetPropBuilder;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.resources.I18n;

public abstract class GuiOffset extends WPanel {
	public GuiOffset(final @Nonnull R position) {
		super(position);
	}

	protected @Nonnull abstract OffsetBuilder offset();

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
		}.setText(I18n.format("signpic.gui.editor.offset.category")));
		final VMotion x = V.pm(-1f);
		add(new OffsetElement(new R(Coord.left(x), Coord.pwidth(1f), Coord.top(15*1), Coord.height(15)), x, 0) {
			{
				this.label.setText(I18n.format("signpic.gui.editor.offset.x"));
				this.number.setNegLabel(I18n.format("signpic.gui.editor.offset.x.neg"));
				this.number.setPosLabel(I18n.format("signpic.gui.editor.offset.x.pos"));
				this.number.setUnknownLabel(I18n.format("signpic.gui.editor.offset.x.unknown"));
			}

			@Override
			protected void initWidget() {
				addDelay(this.left).add(Easings.easeOutBack.move(.25f, 0f)).start();
				super.initWidget();
			}

			@Override
			protected @Nonnull VMotion addDelay(final @Nonnull VMotion c) {
				return c.add(Motion.blank(1*.025f));
			}

			@Override
			protected @Nonnull OffsetPropBuilder offsetprop() {
				return offset().x;
			}
		});
		final VMotion y = V.pm(-1f);
		add(new OffsetElement(new R(Coord.left(y), Coord.pwidth(1f), Coord.top(15*2), Coord.height(15)), y, 1) {
			{
				this.label.setText(I18n.format("signpic.gui.editor.offset.y"));
				this.number.setNegLabel(I18n.format("signpic.gui.editor.offset.y.neg"));
				this.number.setPosLabel(I18n.format("signpic.gui.editor.offset.y.pos"));
				this.number.setUnknownLabel(I18n.format("signpic.gui.editor.offset.y.unknown"));
			}

			@Override
			protected void initWidget() {
				addDelay(this.left).add(Easings.easeOutBack.move(.25f, 0f)).start();
				super.initWidget();
			}

			@Override
			protected @Nonnull VMotion addDelay(final @Nonnull VMotion c) {
				return c.add(Motion.blank(2*.025f));
			}

			@Override
			protected @Nonnull OffsetPropBuilder offsetprop() {
				return offset().y;
			}
		});
		final VMotion z = V.pm(-1f);
		add(new OffsetElement(new R(Coord.left(z), Coord.pwidth(1f), Coord.top(15*3), Coord.height(15)), z, 2) {
			{
				this.label.setText(I18n.format("signpic.gui.editor.offset.z"));
				this.number.setNegLabel(I18n.format("signpic.gui.editor.offset.z.neg"));
				this.number.setPosLabel(I18n.format("signpic.gui.editor.offset.z.pos"));
				this.number.setUnknownLabel(I18n.format("signpic.gui.editor.offset.z.unknown"));
			}

			@Override
			protected void initWidget() {
				addDelay(this.left).add(Easings.easeOutBack.move(.25f, 0f)).start();
				super.initWidget();
			}

			@Override
			protected @Nonnull VMotion addDelay(final @Nonnull VMotion c) {
				return c.add(Motion.blank(3*.025f));
			}

			@Override
			protected @Nonnull OffsetPropBuilder offsetprop() {
				return offset().z;
			}
		});
	}

	protected abstract class OffsetElement extends WPanel {
		public @Nonnull FontLabel label;
		public @Nonnull MNumber number;
		protected @Nonnull VMotion left;

		public OffsetElement(final @Nonnull R position, final @Nonnull VMotion left, final int i) {
			super(position);
			this.label = new FontLabel(new R(Coord.left(0), Coord.width(15f), Coord.top(0), Coord.pheight(1f)), WFont.fontRenderer);
			this.number = new MNumber(new R(Coord.left(15), Coord.right(0), Coord.top(0), Coord.pheight(1f)), 15) {
				@Override
				protected void onNumberChanged(final @Nonnull String oldText, final @Nonnull String newText) {
					if (NumberUtils.isNumber(newText))
						set(NumberUtils.toFloat(newText));
					else
						set(0);
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
			final float f = get();
			if (f!=0f)
				this.number.setNumber(f);
		}

		@Override
		public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
			ev.bus.register(this);
			super.update(ev, pgp, p);
		}

		protected abstract @Nonnull OffsetPropBuilder offsetprop();

		protected void set(final float f) {
			offsetprop().set(f);
			onUpdate();
		}

		protected final float get() {
			return offsetprop().get();
		}

		protected VMotion addDelay(final @Nonnull VMotion c) {
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
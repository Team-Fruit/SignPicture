package com.kamesuta.mc.signpic.gui;

import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
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
import com.kamesuta.mc.signpic.image.meta.ImageOffset;

import net.minecraft.client.resources.I18n;

public class GuiOffset extends WPanel {
	protected ImageOffset offset;

	public GuiOffset(final R position, final ImageOffset offset) {
		super(position);
		this.offset = offset;
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
			protected void set(final float f) {
				GuiOffset.this.offset.setX(f);
				onUpdate();
			}

			@Override
			protected final float get() {
				return GuiOffset.this.offset.getX();
			}

			@Override
			protected VMotion addDelay(final VMotion c) {
				return c.add(Motion.blank(1*.025f));
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
			protected void set(final float f) {
				GuiOffset.this.offset.setY(f);
				onUpdate();
			}

			@Override
			protected final float get() {
				return GuiOffset.this.offset.getY();
			}

			@Override
			protected VMotion addDelay(final VMotion c) {
				return c.add(Motion.blank(2*.025f));
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
			protected void set(final float f) {
				GuiOffset.this.offset.setZ(f);
				onUpdate();
			}

			@Override
			protected final float get() {
				return GuiOffset.this.offset.getZ();
			}

			@Override
			protected VMotion addDelay(final VMotion c) {
				return c.add(Motion.blank(3*.025f));
			}
		});
	}

	protected void onUpdate() {
	}

	protected abstract class OffsetElement extends WPanel {
		public MLabel label;
		public MNumber number;
		protected VMotion left;

		public OffsetElement(final R position, final VMotion left, final int i) {
			super(position);
			this.label = new MLabel(new R(Coord.left(0), Coord.width(15f), Coord.top(0), Coord.pheight(1f)));
			this.number = new MNumber(new R(Coord.left(15), Coord.right(0), Coord.top(0), Coord.pheight(1f)), 15) {
				@Override
				protected void onNumberChanged(final String oldText, final String newText) {
					if (NumberUtils.isNumber(newText))
						set(NumberUtils.toFloat(newText));
					else
						set(0);
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
			final float f = get();
			if (f!=0f)
				this.number.setNumber(f);
			add(this.number);
		}

		protected abstract float get();

		protected abstract void set(float f);

		protected VMotion addDelay(final VMotion c) {
			return c;
		}

		@Override
		public boolean onCloseRequest() {
			addDelay(this.left.stop()).add(Easings.easeInBack.move(.25f, -1f)).start();
			return false;
		}

		@Override
		public boolean onClosing(final WEvent ev, final Area pgp, final Point p) {
			return this.left.isFinished();
		}
	}
}
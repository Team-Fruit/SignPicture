package com.kamesuta.mc.signpic.gui;

import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.MLabel;
import com.kamesuta.mc.bnnwidget.component.MNumber;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.motion.MCoord;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.position.RArea;
import com.kamesuta.mc.signpic.image.meta.ImageOffset;

public class GuiOffset extends WPanel {
	protected ImageOffset offset;

	public GuiOffset(final R position, final ImageOffset offset) {
		super(position);
		this.offset = offset;
	}

	@Override
	protected void initWidget() {
		final MCoord x = MCoord.pleft(-1f).add(Easings.easeOutBack.move(.25f, 0f)).start();
		add(new OffsetElement(new RArea(x, Coord.pwidth(1f), Coord.top(15*0), Coord.height(15)), x, 0, "X", "⇦", "⇨") {
			@Override
			protected void set(final float f) {
				GuiOffset.this.offset.x = f;
				onUpdate();
			}

			@Override
			protected final float get() {
				return GuiOffset.this.offset.x;
			}
		});
		final MCoord y = MCoord.pleft(-1f).add(Easings.easeOutBack.move(.25f, 0f)).start();
		add(new OffsetElement(new RArea(y, Coord.pwidth(1f), Coord.top(15*1), Coord.height(15)), y, 1, "Y", "⇩", "⇧") {
			@Override
			protected void set(final float f) {
				GuiOffset.this.offset.y = f;
				onUpdate();
			}

			@Override
			protected final float get() {
				return GuiOffset.this.offset.y;
			}
		});
		final MCoord z = MCoord.pleft(-1f).add(Easings.easeOutBack.move(.25f, 0f)).start();
		add(new OffsetElement(new RArea(z, Coord.pwidth(1f), Coord.top(15*2), Coord.height(15)), z, 2, "Z", "↗", "↙") {
			@Override
			protected void set(final float f) {
				GuiOffset.this.offset.z = f;
				onUpdate();
			}

			@Override
			protected final float get() {
				return GuiOffset.this.offset.z;
			}
		});
	}

	protected void onUpdate() {}

	protected abstract class OffsetElement extends WPanel {
		protected String label;
		protected String neg;
		protected String pos;
		protected MCoord left;

		public OffsetElement(final R position, final MCoord left, final int i, final String label, final String neg, final String pos) {
			super(position);
			this.label = label;
			this.neg = neg;
			this.pos = pos;
			this.left = left;
		}

		@Override
		protected void initWidget() {
			add(new MLabel(new RArea(Coord.left(0), Coord.width(15f), Coord.top(0), Coord.pheight(1f)), this.label));
			final MNumber n = new MNumber(new RArea(Coord.left(15), Coord.right(0), Coord.top(0), Coord.pheight(1f)), 15) {
				@Override
				protected void onNumberChanged(final String oldText, final String newText) {
					if (NumberUtils.isNumber(newText)) {
						set(NumberUtils.toFloat(newText));
						onUpdate();
					}
				}
			}.setNumber(get());
			n.neg.setText(this.neg);
			n.pos.setText(this.pos);
			add(n);
		}

		protected abstract float get();

		protected abstract void set(float f);

		@Override
		public boolean onCloseRequest() {
			this.left.stop()/*.add(new BlankMotion(this.top.get()/15*.025f))*/.add(Easings.easeInBack.move(.25f, -1f)).start();
			return false;
		}

		@Override
		public boolean onClosing(final WEvent ev, final Area pgp, final Point p) {
			return this.left.isFinished();
		}
	}
}
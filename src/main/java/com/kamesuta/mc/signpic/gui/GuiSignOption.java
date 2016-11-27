package com.kamesuta.mc.signpic.gui;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.MButton;
import com.kamesuta.mc.bnnwidget.component.MLabel;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.entry.Entry;

public class GuiSignOption extends WFrame {
	protected final Entry entry;

	public GuiSignOption(final Entry entry) {
		this.entry = entry;
	}

	@Override
	protected void initWidget() {
		add(new WPanel(new R()) {
			@Override
			protected void initWidget() {
				add(new WPanel(new R(Coord.pleft(.5f), Coord.ptop(.5f), Coord.width(100f), Coord.height(100f))) {
					@Override
					protected void initWidget() {
						add(new WPanel(new R(Coord.pleft(-.5f), Coord.ptop(-.5f))) {
							@Override
							protected void initWidget() {
								add(new MLabel(new R(Coord.top(0f), Coord.height(20f))).setText("Sign Option"));
								add(new MButton(new R(Coord.top(25f), Coord.height(20f))) {
									@Override
									protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
										if (GuiSignOption.this.entry.isValid()) {
											GuiSignOption.this.entry.content().markDirty();
											return true;
										}
										return false;
									}
								}.setText("Reload"));
								add(new MButton(new R(Coord.top(50f), Coord.height(20f))) {
									@Override
									protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
										if (GuiSignOption.this.entry.isValid()) {
											GuiSignOption.this.entry.content().markDirtyWithCache();
											return true;
										}
										return false;
									}
								}.setText("Cache Reload"));
							}
						});
					}
				});
			}
		});
	}
}

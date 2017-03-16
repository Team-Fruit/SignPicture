package com.kamesuta.mc.signpic.plugin.gui.list;

import javax.annotation.Nonnull;

import com.kamesuta.mc.bnnwidget.WList;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.plugin.SignData;
import com.kamesuta.mc.signpic.plugin.gui.GuiManager;

public class GuiList extends WPanel {

	protected final @Nonnull GuiManager manager;
	protected final @Nonnull WPanel scrollPane;

	public GuiList(final R position, final GuiManager manager) {
		super(position);
		this.manager = manager;
		this.scrollPane = new WPanel(new R(Coord.left(0), Coord.right(15))) {
			@Override
			protected void initWidget() {
				add(new WList<SignData, GuiListElement>(new R(), GuiList.this.manager.data) {
					@Override
					protected GuiListElement createWidget(final SignData t, final int i) {
						return new GuiListElement(new R(Coord.top(i*30), Coord.height(30)), t);
					}
				});
			}
		};
	}

	@Override
	protected void initWidget() {
		add(this.scrollPane);
	}
}

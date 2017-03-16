package com.kamesuta.mc.signpic.plugin.gui;

import java.awt.Font;
import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WBox;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.font.FontSet;
import com.kamesuta.mc.bnnwidget.font.FontStyle;
import com.kamesuta.mc.bnnwidget.font.TrueTypeFont;
import com.kamesuta.mc.bnnwidget.font.WFont;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.bnnwidget.util.NotifyCollections;
import com.kamesuta.mc.bnnwidget.util.NotifyCollections.IModCount;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.gui.GuiTask;
import com.kamesuta.mc.signpic.plugin.SignData;
import com.kamesuta.mc.signpic.plugin.gui.list.GuiList;
import com.kamesuta.mc.signpic.plugin.packet.PacketHandler;
import com.kamesuta.mc.signpic.plugin.packet.SignPicturePacket;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiManager extends WFrame {
	public static @Nonnull ResourceLocation logo = new ResourceLocation("signpic", "textures/plugin/logo.png");
	public static @Nonnull ResourceLocation background = new ResourceLocation("signpic", "textures/plugin/background.png");
	public static @Nonnull WFont font;
	public static @Nonnull ManagerType type = ManagerType.LIST;

	static {
		final FontSet fontSet = new FontSet.Builder().addName("tahoma").addName("aller").setStyle(Font.PLAIN).build();
		final FontStyle style = new FontStyle.Builder().setFont(fontSet).build();
		font = new TrueTypeFont(style);
	}

	public @Nonnull String key;
	public final int size;
	public final @Nonnull IModCount<SignData> data;

	public final @Nonnull GuiManagerSarchBox sarchBox;
	public final @Nonnull WBox box;

	public GuiManager(final @Nullable GuiScreen parent, final @Nonnull String data, final @Nonnull String size) {
		super(parent);
		this.key = data;
		this.size = NumberUtils.toInt(size);
		this.data = new NotifyCollections.NotifyArrayList<SignData>(new ArrayList(this.size));
		this.sarchBox = new GuiManagerSarchBox(new R(Coord.height(25)));
		this.box = new WBox(new R(Coord.left(5), Coord.right(100), Coord.top(40), Coord.bottom(30)));
		get(0, 100);
	}

	public GuiManager(final @Nonnull String data, final @Nonnull String size) {
		this(null, data, size);
	}

	public void data(final @Nonnull String token, final @Nonnull String s) {
		final SignData d = Client.gson.fromJson(s, SignData.class);
		this.data.add(d);
	}

	public void get(final int from, final int to) {
		for (int i = from; i<to; i++) {
			if (i>this.size)
				return;
			PacketHandler.instance.sendPacket(new SignPicturePacket("data", this.key, String.valueOf(i)));
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		setGuiPauseGame(false);
	}

	@Override
	protected void initWidget() {
		add(new WBase(new R()) {
			VMotion m = V.pm(1);

			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float opacity) {
				OpenGL.glColor4f(1f, 1f, 1f, 1f);
				texture().bindTexture(background);
				WRenderer.startTexture();
				drawTexture(getGuiPosition(pgp), null, null);
				WRenderer.startShape();
				OpenGL.glColor4f(0f, 0f, 0f, this.m.get());
				draw(getGuiPosition(pgp));
			}

			@Override
			public void onAdded() {
				this.m.stop().add(Easings.easeLinear.move(.2f, .2f)).start();
			}

			@Override
			public boolean onCloseRequest() {
				this.m.stop().add(Easings.easeLinear.move(.15f, .5f)).start();
				return false;
			}

			@Override
			public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
				return this.m.isFinished();
			}
		});
		add(this.sarchBox);
		add(this.box);
		add(new GuiTask(new R(Coord.width(100), Coord.right(0), Coord.top(25))) {
			{
				this.showtime.pause();
				this.showtime.set(-1);
			}

			@Override
			protected void initWidget() {
				this.show = true;
				this.right.stop().add(Easings.easeOutQuart.move(.7f, 1f)).start();
				super.initWidget();
			}
		});
		add(new WBase(new R(Coord.left(5), Coord.width(70.24f), Coord.bottom(5), Coord.height(20))) {
			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
				super.draw(ev, pgp, p, frame, popacity);

				OpenGL.glColor4f(1f, 1f, 1f, getGuiOpacity(popacity));
				texture().bindTexture(logo);
				WRenderer.startTexture();
				drawTexture(getGuiPosition(pgp), null, null);
			}
		});

		switch (type) {
			case LIST:
				this.box.add(new GuiList(new R(), this));
				break;
		}
	}
}

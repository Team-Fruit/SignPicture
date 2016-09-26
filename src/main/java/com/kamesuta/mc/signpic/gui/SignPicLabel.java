package com.kamesuta.mc.signpic.gui;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.image.meta.ImageSize.ImageSizes;
import com.kamesuta.mc.signpic.render.RenderHelper;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class SignPicLabel extends WBase {
	public static final ResourceLocation defaultTexture = new ResourceLocation("signpic", "textures/logo.png");
	protected EntryId entryId;
	protected ContentManager manager;

	public SignPicLabel(final R position, final ContentManager manager) {
		super(position);
		this.manager = manager;
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
		final Area a = getGuiPosition(pgp);
		final EntryId entryId = getEntryId();
		if (entryId != null) {
			final Entry entry = entryId.entry();
			if (entry.isValid()) {
				final Content content = entry.content();
				if (content == null || StringUtils.isEmpty(content.id.id())) {
					RenderHelper.startTexture();
					GlStateManager.color(1f, 1f, 1f, .2f);
					texture().bindTexture(defaultTexture);
					drawTexturedModalRect(a);
				} else {
					GlStateManager.disableCull();
					GlStateManager.pushMatrix();

					final ImageSize size1 = new ImageSize().setAspectSize(entry.meta.size, content.image.getSize());
					final ImageSize size2 = new ImageSize().setSize(ImageSizes.INNER, size1, new ImageSize().setArea(a));
					final ImageSize size = new ImageSize().setImageSize(size2).scale(1f/100f);

					translate(a);
					GlStateManager.translate((a.w()-size2.width)/2f, (a.h()-size2.height)/2f, 0f);
					GlStateManager.scale(100, 100, 1f);
					Client.renderer.renderImage(content, size, 1f);

					GlStateManager.popMatrix();
					GlStateManager.enableCull();
				}
			}
		}
	}

	public EntryId getEntryId() {
		return this.entryId;
	}

	public SignPicLabel setEntryId(final EntryId entryId) {
		this.entryId = entryId;
		return this;
	}
}

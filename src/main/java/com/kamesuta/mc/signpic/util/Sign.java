package com.kamesuta.mc.signpic.util;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.image.meta.ImageMeta;
import com.kamesuta.mc.signpic.preview.SignEntity;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.tileentity.TileEntitySign;

public class Sign {
	public static int maxText = 15*4;

	public final SignEntity preview = new SignEntity();
	public String id = "";
	public final ImageMeta meta = new ImageMeta();

	public Sign() {
	}

	public Sign parseText(final String text) {
		if (hasMeta(text)) {
			this.id = extractId(text);
			this.meta.parse(extractMeta(text));
		}
		return this;
	}

	public static String extractMeta(final String src) {
		return StringUtils.substring(src, StringUtils.lastIndexOf(src, "[")+1, StringUtils.length(src)-1);
	}

	public static String extractId(final String src) {
		return StringUtils.substring(src, 0, StringUtils.lastIndexOf(src, "["));
	}

	public static boolean hasMeta(final String text) {
		return text!=null && StringUtils.endsWith(text, "]") && StringUtils.contains(text, "[");
	}

	public static boolean hasId(final String id) {
		return StringUtils.isEmpty(id) || StringUtils.containsOnly(id, "!") || StringUtils.containsOnly(id, "$");
	}

	public String getID() {
		String id = this.id;
		if (StringUtils.contains(id, "http://"))
			id = StringUtils.substring(id, 7, StringUtils.length(id));
		else if (StringUtils.contains(id, "https://"))
			id = "$" + StringUtils.substring(id, 8, StringUtils.length(id));
		return id;
	}

	public String getURL() {
		String id = this.id;
		if (!StringUtils.startsWith(id, "!") && !hasId(id))
			if (StringUtils.startsWith(id, "$"))
				id = "https://" + StringUtils.substring(id, 1);
			else if (!StringUtils.startsWith(id, "http://") && !StringUtils.startsWith(id, "https://"))
				id = "http://" + id;
		return id;
	}

	public Sign parseSignText(final String[] sign) {
		return parseText(StringUtils.join(sign));
	}

	public Sign parseSignEntity(final TileEntitySign tile) {
		return parseSignText(tile.signText);
	}

	public String text() {
		return getID() + this.meta;
	}

	public boolean isPlaceable() {
		return StringUtils.length(text()) < maxText;
	}

	public boolean isVaild() {
		return !hasId(this.id);
	}

	public String[] toSignText() {
		final String text = text();
		final String[] sign = new String[4];
		for (int i=0; i<4; i++) {
			if (16*i <= StringUtils.length(text))
				sign[i] = StringUtils.substring(text, 15*i, Math.min(15*(i+1), text.length()));
			else
				sign[i] = "";
		}
		return sign;
	}

	public Sign writeToEntity(final TileEntitySign tile) {
		tile.signText = toSignText();
		return this;
	}

	public Sign updatePreview() {
		return writeToEntity(this.preview.getTileEntity());
	}

	public Sign sendSign(final TileEntitySign sourceentity) {
		writeToEntity(sourceentity);
		sourceentity.markDirty();
		final NetHandlerPlayClient nethandlerplayclient = Client.mc.getNetHandler();
		if (nethandlerplayclient != null)
			nethandlerplayclient.addToSendQueue(new C12PacketUpdateSign(sourceentity.xCoord, sourceentity.yCoord, sourceentity.zCoord, sourceentity.signText));
		sourceentity.setEditable(true);
		return this;
	}

	@Override
	public String toString() {
		return String.format("Sign [id=%s, meta=%s]", this.id, this.meta);
	}
}

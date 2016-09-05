package com.kamesuta.mc.signpic.util;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.image.meta.ImageMeta;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.tileentity.TileEntitySign;

public class Sign {
	public static int maxText = 15*4;

	public String id = "";
	public final ImageMeta meta = new ImageMeta();

	public Sign() {
	}

	public Sign parseText(final String text) {
		if (text!=null && StringUtils.endsWith(text, "]") && StringUtils.contains(text, "[")) {
			final int start = StringUtils.lastIndexOf(text, "[");
			this.id = StringUtils.substring(text, 0, start);
			this.meta.parseMeta(StringUtils.substring(text, start+1, StringUtils.length(text)-1));
		}
		return this;
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
		if (!StringUtils.startsWith(id, "!") && !isEmpty(id))
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
		return !isEmpty(this.id);
	}

	public boolean isEmpty(final String s) {
		return StringUtils.isEmpty(this.id) || StringUtils.containsOnly(this.id, "!") || StringUtils.containsOnly(this.id, "$");
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

	public void sendSign(final TileEntitySign sourceentity) {
		sourceentity.signText = toSignText();
		sourceentity.markDirty();
		final NetHandlerPlayClient nethandlerplayclient = FMLClientHandler.instance().getClient().getNetHandler();
		if (nethandlerplayclient != null)
			nethandlerplayclient.addToSendQueue(new C12PacketUpdateSign(sourceentity.xCoord, sourceentity.yCoord, sourceentity.zCoord, sourceentity.signText));
		sourceentity.setEditable(true);
	}

	@Override
	public String toString() {
		return String.format("Sign [id=%s, meta=%s]", this.id, this.meta);
	}
}

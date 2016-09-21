package com.kamesuta.mc.signpic.util;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.image.meta.ImageMeta;
import com.kamesuta.mc.signpic.preview.SignEntity;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.tileentity.TileEntitySign;

public class Sign {
	public static int maxText = 15*4;

	public final SignEntity preview = new SignEntity();
	public EntryId id = null;
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

	public String text() {
		return getID() + this.meta;
	}

	public boolean isPlaceable() {
		return StringUtils.length(text()) < maxText;
	}

	public boolean isVaild() {
		return !hasContentId(this.id);
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

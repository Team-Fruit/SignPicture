package com.kamesuta.mc.signpic.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.image.ImageOffset;
import com.kamesuta.mc.signpic.image.ImageSize;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.tileentity.TileEntitySign;

public class Sign {
	public String id;
	public ImageSize size;
	public final ImageOffset offset = new ImageOffset();

	public Sign() {
	}

	public Sign(final String id, final ImageSize size) {
		this.id = id;
		this.size = size;
	}

	public Sign parseText(final String text) {
		if (text!=null && text.endsWith("]") && text.contains("[")) {
			final int start = text.lastIndexOf("[");
			setId(text.substring(0, start));
			final Map<String, String> meta = parseMeta(text.substring(start+1, text.length()-1));
			this.size = ImageSize.parseSize(meta);
			this.offset.parseOffset(meta);
		}
		return this;
	}

	public Sign setId(final String id) {
		this.id = id;
		return this;
	}

	public Sign setSize(final ImageSize size) {
		this.size = size;
		return this;
	}

	public String getURL() {
		String id = this.id;
		if (!id.startsWith("!") && !isEmpty(id))
			if (id.startsWith("$"))
				id = "https://" + id.substring(1);
			else if (!id.startsWith("http://") && !id.startsWith("https://"))
				id = "http://" + id;
		return id;
	}

	public Sign parseSignText(final String[] sign) {
		return parseText(StringUtils.join(sign));
	}

	public Sign parseSignEntity(final TileEntitySign tile) {
		return parseSignText(tile.signText);
	}

	protected static final Pattern p = Pattern.compile("(?:([^\\d-\\+Ee\\.]?)([\\d-\\+Ee\\.]*)?)+?");

	protected static Map<String, String> parseMeta(final String src) {
		final Matcher m = p.matcher(src);
		final Map<String, String> map = new HashMap<String, String>();
		while(m.find()){
			if (2 <= m.groupCount()) {
				final String key = m.group(1);
				final String value = m.group(2);
				if (!StringUtils.isEmpty(key) || !StringUtils.isEmpty(value))
					map.put(key, value);
			}
		}
		return map;
	}

	public String text() {
		String id = this.id;
		if (id.contains("http://"))
			id = id.replace("http://", "");
		else if (id.contains("https://"))
			id = id.replace("https://", "$");
		return id + "[" + this.size.text() + this.offset.text() + "]";
	}

	public boolean isIdVaild() {
		return !isEmpty(this.id);
	}

	public boolean isSizeVaild() {
		return this.size!=null;
	}

	public boolean isVaild() {
		return isIdVaild() && isSizeVaild();
	}

	public boolean isEmpty(final String s) {
		return StringUtils.isEmpty(this.id) || StringUtils.containsOnly(this.id, "!") || StringUtils.containsOnly(this.id, "$");
	}

	public String[] toSignText() {
		final String text = text();
		final String[] sign = new String[4];
		for (int i=0; i<4; i++) {
			if (16*i <= text.length())
				sign[i] = text.substring(15*i, Math.min(15*i+15, text.length()));
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
		return String.format("Sign [id=%s, size=%s]", this.id, this.size);
	}
}

package com.kamesuta.mc.signpic.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.image.ImageSize;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.tileentity.TileEntitySign;

public class Sign {
	public String id;
	public ImageSize size;

	public Sign() {
	}

	public Sign(final String id, final ImageSize size) {
		this.id = id;
		this.size = size;
	}

	public static Sign parseText(final String text) {
		final Sign sign = new Sign();
		if (text!=null && text.endsWith("]") && text.contains("[")) {
			final int start = text.lastIndexOf("[");
			String id = text.substring(0, start);
			if (!id.startsWith("!"))
				if (id.startsWith("$"))
					id = "https://" + id.substring(1);
				else if (!id.startsWith("http://") && !id.startsWith("https://"))
					id = "http://" + id;
			sign.id = id;
			final Map<String, String> meta = parseMeta(text.substring(start+1, text.length()-1));
			sign.size = ImageSize.parseSize(meta.containsKey("") ? meta.get("") : "");
		}

		return sign;
	}

	public static Sign parseSignText(final String[] sign) {
		return parseText(StringUtils.join(sign));
	}

	public static Sign parseSignEntity(final TileEntitySign tile) {
		return parseSignText(tile.signText);
	}

	protected static final Pattern p = Pattern.compile("(?:([^\\dx]?)(\\d*x\\d*|\\d*))+?");

	protected static Map<String, String> parseMeta(final String src) {
		final Matcher m = p.matcher(src);
		final Map<String, String> map = new HashMap<String, String>();
		while(m.find()){
			if (2 <= m.groupCount()) {
				final String key = m.group(1);
				final String value = m.group(2);
				if (!key.isEmpty() || !value.isEmpty())
					map.put(key, value);
			}
		}
		return map;
	}

	public boolean isVaild() {
		return this.id!=null && this.size!=null;
	}

	public String text() {
		String id = id();
		if (id.contains("http://"))
			id = id.replaceFirst("http://", "");
		else if (id.contains("https://"))
			id = id.replaceFirst("https://", "$");
		return id + size().text();
	}

	public String id() {
		if (!isVaild()) throw new IllegalStateException("Invaild Sign: " + this);
		return this.id;
	}

	public ImageSize size() {
		if (!isVaild()) throw new IllegalStateException("Invaild Sign: " + this);
		return this.size;
	}

	public String[] toSignText() {
		final String text = text();
		final String[] sign = new String[4];
		for (int i=0; i<4; i++) {
			if (16*i <= text.length())
				sign[i] = text.substring(16*i, Math.min(16*i+15, text.length()));
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

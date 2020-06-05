package net.teamfruit.signpic.image;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.util.ResourceLocation;
import net.teamfruit.signpic.Client;
import net.teamfruit.signpic.attr.prop.SizeData;
import net.teamfruit.signpic.compat.Compat.CompatTextureUtil;

public class ResourceImageTexture implements ImageTexture {
	protected static final @Nonnull ResourceLocation Null = Client.mc.renderEngine.getDynamicTextureLocation("null", CompatTextureUtil.missingTexture);

	protected @Nonnull ResourceLocation location;

	public ResourceImageTexture(final @Nonnull ResourceLocation location) {
		this.location = location;
	}

	@Override
	public void bind() {
		if (!StringUtils.isEmpty(this.location.getResourcePath()))
			Client.mc.renderEngine.bindTexture(this.location);
		else
			Client.mc.renderEngine.bindTexture(Null);
	}

	@Override
	public @Nonnull SizeData getSize() {
		return SizeData.DefaultSize;
	}

	@Override
	public boolean hasMipmap() {
		return false;
	}
}

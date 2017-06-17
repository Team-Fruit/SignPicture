package com.kamesuta.mc.signpic.image;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.util.Timer;

public class RemoteImageTexture {

	protected final @Nonnull Timer timer = new Timer();
	protected final @Nonnull List<Pair<Float, DynamicImageTexture>> textures;
	protected int currenttexture = 0;

	public RemoteImageTexture(final @Nonnull List<Pair<Float, DynamicImageTexture>> textures) {
		this.textures = textures;
	}

	public @Nonnull ImageTexture get() {
		if (this.textures.size()==1)
			return this.textures.get(0).getRight().load();
		else if (this.textures.size()>1) {
			final Pair<Float, DynamicImageTexture> texframe = this.textures.get(this.currenttexture);
			texframe.getRight().load();
			if (this.timer.getTime()>texframe.getLeft()) {
				this.timer.reset();
				this.currenttexture = this.currenttexture<this.textures.size()-1 ? this.currenttexture+1 : 0;
			}
			return this.textures.get(this.currenttexture).getRight();
		} else
			return DynamicImageTexture.NULL;
	}

	public @Nonnull List<Pair<Float, DynamicImageTexture>> getAll() {
		return this.textures;
	}

	public void delete() {
		final Iterator<Pair<Float, DynamicImageTexture>> itr = this.textures.iterator();
		while (itr.hasNext()) {
			final Pair<Float, DynamicImageTexture> t = itr.next();
			t.getRight().delete();
			itr.remove();
		}
	}
}
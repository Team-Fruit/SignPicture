package com.kamesuta.mc.signpic.image;

import java.util.Iterator;
import java.util.List;

import org.lwjgl.util.Timer;

public class ImageTextures {

	protected final Timer timer = new Timer();
	protected final List<ImageTexture> textures;
	protected int currentTextureId = 0;

	public ImageTextures(final List<ImageTexture> images) {
		this.textures = images;
	}

	public ImageTexture get() {
		if (this.textures.size()==1) {
			return this.textures.get(0).load();
		} else if (this.textures.size()>1) {
			final ImageTexture texture = this.textures.get(this.currentTextureId).load();
			if(this.timer.getTime() > texture.delay){
				this.timer.set(0);
				this.currentTextureId = (this.currentTextureId<this.textures.size()-1)?this.currentTextureId+1:0;
			}
			return this.textures.get(this.currentTextureId);
		} else {
			return ImageTexture.NULL;
		}
	}

	public List<ImageTexture> getAll() {
		return this.textures;
	}

	public void delete() {
		final Iterator<ImageTexture> itr = this.textures.iterator();
		while(itr.hasNext()){
			final ImageTexture t = itr.next();
			t.delete();
			itr.remove();
		}
	}
}
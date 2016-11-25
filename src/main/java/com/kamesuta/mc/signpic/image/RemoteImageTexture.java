package com.kamesuta.mc.signpic.image;

import java.util.Iterator;
import java.util.List;

import org.lwjgl.util.Timer;

public class RemoteImageTexture {

	protected final Timer timer = new Timer();
	protected final List<DynamicImageTexture> textures;
	protected int currenttexture = 0;

	public RemoteImageTexture(final List<DynamicImageTexture> images) {
		this.textures = images;
	}

	public ImageTexture get() {
		if (this.textures.size()==1) {
			return this.textures.get(0).load();
		} else if (this.textures.size()>1) {
			final DynamicImageTexture texture = this.textures.get(this.currenttexture).load();
			if(this.timer.getTime() > texture.delay){
				this.timer.reset();
				this.currenttexture = (this.currenttexture<this.textures.size()-1)?this.currenttexture+1:0;
			}
			return this.textures.get(this.currenttexture);
		} else {
			return DynamicImageTexture.NULL;
		}
	}

	public List<DynamicImageTexture> getAll() {
		return this.textures;
	}

	public void delete() {
		final Iterator<DynamicImageTexture> itr = this.textures.iterator();
		while(itr.hasNext()){
			final DynamicImageTexture t = itr.next();
			t.delete();
			itr.remove();
		}
	}
}
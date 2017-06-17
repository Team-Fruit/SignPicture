package com.kamesuta.mc.signpic.entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.attr.AttrReaders;
import com.kamesuta.mc.signpic.attr.AttrWriters;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.gui.GuiImage;
import com.kamesuta.mc.signpic.mode.CurrentMode;

public class Entry {
	public final @Nonnull EntryId id;
	public final @Nullable ContentId contentId;
	private @Nullable GuiImage gui;
	private final boolean valid;
	private final boolean outdated;

	private transient @Nullable AttrReaders meta;
	private @Nullable String cmetacache;

	protected Entry(final @Nonnull EntryId id) {
		this.id = id;
		this.valid = id.isValid();
		this.outdated = id.isOutdated();
		this.contentId = id.getContentId();
	}

	public @Nonnull GuiImage getGui() {
		if (this.gui!=null)
			return this.gui;
		return this.gui = new GuiImage(this);
	}

	public @Nullable Content getContent() {
		if (CurrentMode.instance.isState(CurrentMode.State.HIDE))
			return ContentId.hideContent.content();
		else if (this.contentId!=null)
			return this.contentId.content();
		else
			return null;
	}

	public boolean isNotSupported() {
		final AttrReaders meta = getMeta();
		return meta.hasInvalidMeta()||this.id.getPrePrefix()!=null;
	}

	public boolean isOutdated() {
		return this.outdated;
	}

	public boolean isValid() {
		return this.valid;
	}

	public @Nonnull AttrReaders getMeta() {
		final Content cntnt = getContent();
		final String newmeta = cntnt!=null ? cntnt.imagemeta : null;
		if (this.contentId!=null&&newmeta!=null)
			if (!StringUtils.equals(this.cmetacache, newmeta)) {
				final String meta1 = this.id.getMetaSource();
				if (meta1!=null)
					this.meta = new AttrReaders(meta1+newmeta);
				this.cmetacache = newmeta;
			}
		if (this.meta==null)
			this.meta = this.id.getMeta();
		if (this.meta!=null)
			return this.meta;
		return this.meta = AttrReaders.Blank;
	}

	public @Nullable AttrWriters getMetaBuilder() {
		final Content cntnt = getContent();
		final String newmeta = cntnt!=null ? cntnt.imagemeta : null;
		if (this.contentId!=null&&newmeta!=null)
			return new AttrWriters().parse(this.id.getMetaSource()+newmeta);
		return this.id.getMetaBuilder();
	}
}

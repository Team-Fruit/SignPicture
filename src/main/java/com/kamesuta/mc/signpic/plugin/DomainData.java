package com.kamesuta.mc.signpic.plugin;

import java.util.Date;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DomainData {

	private @Nullable Integer id;

	private @Nullable Date createDate;

	private @Nullable Date updateDate;

	public @Nonnull Integer getId() {
		if (this.id!=null)
			return this.id;
		return this.id = 0;
	}

	public @Nonnull Date getCreateDate() {
		if (this.createDate!=null)
			return this.createDate;
		return this.createDate = new Date();
	}

	public @Nonnull Date getUpdateDate() {
		if (this.updateDate!=null)
			return this.updateDate;
		return this.updateDate = new Date();
	}

	public void setId(final @Nonnull Integer id) {
		this.id = id;
	}

	public void setCreateDate(final @Nonnull Date createDate) {
		this.createDate = createDate;
	}

	public void setUpdateDate(final @Nonnull Date updateDate) {
		this.updateDate = updateDate;
	}
}

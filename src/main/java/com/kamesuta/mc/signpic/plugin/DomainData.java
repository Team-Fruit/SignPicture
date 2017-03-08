package com.kamesuta.mc.signpic.plugin;

import java.util.Date;

import javax.annotation.Nullable;

public class DomainData {

	private int id;

	private @Nullable Date createDate;

	private @Nullable Date updateDate;

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public @Nullable Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(final Date date) {
		this.createDate = date;
	}

	public @Nullable Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(final Date date) {
		this.updateDate = date;
	}
}

package com.kamesuta.mc.signpic.state;

import javax.annotation.Nonnull;

import com.kamesuta.mc.signpic.render.StateRender.LoadingCircle;
import com.kamesuta.mc.signpic.render.StateRender.LoadingCircleType;

public enum StateType {
	INIT("signpic.state.init", LoadingCircle.INIT, LoadingCircleType.WAIT),
	INITALIZED("signpic.state.initalized", LoadingCircle.INIT, LoadingCircleType.WAIT),
	DOWNLOADING("signpic.state.downloading", LoadingCircle.DOWNLOAD, LoadingCircleType.RUN),
	DOWNLOADED("signpic.state.downloaded", LoadingCircle.DOWNLOAD, LoadingCircleType.WAIT),
	LOADING("signpic.state.loading", LoadingCircle.CONTENTLOAD, LoadingCircleType.RUN),
	LOADED("signpic.state.loaded", LoadingCircle.CONTENTLOAD, LoadingCircleType.WAIT),
	AVAILABLE("signpic.state.available"),
	ERROR("signpic.state.error"),
	;

	public final @Nonnull String msg;
	public final @Nonnull LoadingCircle circle;
	public final @Nonnull LoadingCircleType speed;
	private StateType(final @Nonnull String s, final @Nonnull LoadingCircle circle, final @Nonnull LoadingCircleType speed) {
		this.msg = s;
		this.circle = circle;
		this.speed = speed;
	}

	private StateType(final @Nonnull String s) {
		this(s, LoadingCircle.DEFAULT, LoadingCircleType.DEFAULT);
	}
}

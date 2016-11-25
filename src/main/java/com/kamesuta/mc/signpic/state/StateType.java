package com.kamesuta.mc.signpic.state;

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

	public final String msg;
	public final LoadingCircle circle;
	public final LoadingCircleType speed;
	private StateType(final String s, final LoadingCircle circle, final LoadingCircleType speed) {
		this.msg = s;
		this.circle = circle;
		this.speed = speed;
	}

	private StateType(final String s) {
		this(s, LoadingCircle.DEFAULT, LoadingCircleType.DEFAULT);
	}
}

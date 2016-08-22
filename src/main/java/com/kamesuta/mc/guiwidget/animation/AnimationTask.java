package com.kamesuta.mc.guiwidget.animation;

import java.util.Deque;
import java.util.LinkedList;

public class AnimationTask {
	public static final Animation Default = new Animation(0);

	protected Deque<Animation> task;
	protected Animation currentTask;

	public AnimationTask() {
		this.task = new LinkedList<Animation>();
		this.currentTask = Default;
	}

	public AnimationTask animate(final Animation animation) {
		this.task.offerFirst(animation);
		return this;
	}

	public AnimationTask stop() {
		this.task.peekLast().finish();
		this.task.clear();
		return this;
	}

	public Animation getAnimation() {
		if (this.currentTask.hasFinished()) {
			final Animation animation = this.task.poll();
			if (animation != null)
				this.currentTask = animation;
		}
		return this.currentTask;
	}
}

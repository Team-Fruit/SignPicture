package com.kamesuta.mc.bnnwidget;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;

public interface WCommon {
	void onAdded();

	void onInit(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point mouse);

	void draw(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point mouse, float frame, float popacity);

	void update(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point mouse);

	boolean keyTyped(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point mouse, char c, int keycode);

	boolean mouseScrolled(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point mouse, int scroll);

	boolean mouseMoved(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point mouse, int button);

	boolean mouseClicked(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point mouse, int button);

	boolean mouseDragged(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point mouse, int button, long time);

	boolean mouseReleased(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point mouse, int button);

	boolean onCloseRequest();

	boolean onClosing(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point mouse);

	@Nullable
	WCommon top(@Nonnull WEvent ev, @Nonnull Area pgp, @Nonnull Point point);
}

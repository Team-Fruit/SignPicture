package net.teamfruit.bnnwidget.component;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.teamfruit.bnnwidget.OverridablePoint;
import net.teamfruit.bnnwidget.ShortestFloatFormatter;
import net.teamfruit.bnnwidget.WEvent;
import net.teamfruit.bnnwidget.WPanel;
import net.teamfruit.bnnwidget.component.MChatTextField.CharacterFilter;
import net.teamfruit.bnnwidget.position.Area;
import net.teamfruit.bnnwidget.position.Coord;
import net.teamfruit.bnnwidget.position.Point;
import net.teamfruit.bnnwidget.position.R;

public class MNumber extends WPanel {
	/**
	 * マイナスボタン
	 */
	public @Nonnull MButton neg;
	/**
	 * テキストフィールド
	 */
	public @Nonnull MChatTextField field;
	/**
	 * プラスボタン
	 */
	public @Nonnull MButton pos;

	public MNumber(final @Nonnull R position, final float buttonwidth) {
		super(position);
		this.neg = new MButton(new R(Coord.left(0), Coord.width(buttonwidth), Coord.top(0), Coord.bottom(0))) {
			@Override
			protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
				return negClicked();
			}
		}.setText("-");
		add(this.neg);
		this.field = new MChatTextField(new R(Coord.left(buttonwidth), Coord.right(buttonwidth), Coord.top(0), Coord.bottom(0))) {
			@Override
			protected void onTextChanged(final String oldText) {
				onNumberChanged(oldText, getText());
			}
		}.setFilter(CharacterFilter.WhiteListFilter.createFromString("+-.eE0123456789")).setWatermark(I18n.format("signpic.gui.editor.number.unknown"));
		add(this.field);
		this.pos = new MButton(new R(Coord.right(0), Coord.width(buttonwidth), Coord.top(0), Coord.bottom(0))) {
			@Override
			protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
				return posClicked();
			}
		}.setText("+");
		add(this.pos);
	}

	/**
	 * マイナスボタンがクリックされた際に呼ばれます
	 * @return イベントを受け取った場合はtrue
	 */
	protected boolean negClicked() {
		float f;
		if (GuiScreen.isShiftKeyDown())
			f = .1f;
		else if (GuiScreen.isCtrlKeyDown())
			f = .01f;
		else
			f = 1f;
		MNumber.this.field.setText(ShortestFloatFormatter.format(NumberUtils.toFloat(MNumber.this.field.getText())-f));
		return true;
	}

	/**
	 * プラスボタンがクリックされた際に呼ばれます
	 * @return イベントを受け取った場合はtrue
	 */
	protected boolean posClicked() {
		float f;
		if (GuiScreen.isShiftKeyDown())
			f = .1f;
		else if (GuiScreen.isCtrlKeyDown())
			f = .01f;
		else
			f = 1f;
		MNumber.this.field.setText(ShortestFloatFormatter.format(NumberUtils.toFloat(MNumber.this.field.getText())+f));
		return true;
	}

	/**
	 * 数値を設定します
	 * @param f 数値
	 * @return this
	 */
	public @Nonnull MNumber setNumber(final float f) {
		this.field.setText(Float.isNaN(f) ? "" : ShortestFloatFormatter.format(f));
		return this;
	}

	/**
	 * プラスボタンのラベルを変更します
	 * @param s ラベル
	 * @return this
	 */
	public @Nonnull MNumber setPosLabel(final @Nonnull String s) {
		if (!StringUtils.isEmpty(s)&&!StringUtils.contains(s, "signpic."))
			this.pos.setText(s);
		return this;
	}

	/**
	 * マイナスボタンのラベルを変更します
	 * @param s ラベル
	 * @return this
	 */
	public @Nonnull MNumber setNegLabel(final @Nonnull String s) {
		if (!StringUtils.isEmpty(s)&&!StringUtils.contains(s, "signpic."))
			this.neg.setText(s);
		return this;
	}

	/**
	 * 数値がないときの透かしを設定します
	 * @param s 透かし
	 * @return this
	 */
	public @Nonnull MNumber setUnknownLabel(final @Nonnull String s) {
		if (!StringUtils.isEmpty(s)&&!StringUtils.contains(s, "signpic."))
			this.field.setWatermark(s);
		return this;
	}

	/**
	 * 数値が変更されたときに呼ばれます
	 * @param oldText 変更前
	 * @param newText 変更後
	 */
	@OverridablePoint
	protected void onNumberChanged(final @Nonnull String oldText, final @Nonnull String newText) {
	}
}

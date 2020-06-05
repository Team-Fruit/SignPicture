package net.teamfruit.emojicord.compat;

import javax.annotation.Nonnull;

public interface CompatBaseVertex {

	/**
	 * 実際に描画します
	 */
	void draw();

	/**
	 * 描画を開始します。
	 * @param mode GL描画モード
	 * @return this
	 */
	@Nonnull
	CompatBaseVertex begin(int mode);

	/**
	 * テクスチャ描画を開始します。
	 * @param mode GL描画モード
	 * @return this
	 */
	@Nonnull
	CompatBaseVertex beginTexture(int mode);

	/**
	 * 頂点の設定を開始します。
	 * <p>
	 * この後続けて色やテクスチャなどの設定を行うことができます。
	 * @param x X座標
	 * @param y Y座標
	 * @param z Z座標
	 * @return this
	 */
	@Nonnull
	CompatBaseVertex pos(double x, double y, double z);

	/**
	 * テクスチャのマッピング
	 * @param u U座標
	 * @param v V座標
	 * @return this
	 */
	@Nonnull
	CompatBaseVertex tex(double u, double v);

	/**
	 * 頂点の色
	 * @param red 赤(0～1)
	 * @param green 緑(0～1)
	 * @param blue 青(0～1)
	 * @param alpha アルファ(0～1)
	 * @return this
	 */
	@Nonnull
	CompatBaseVertex color(float red, float green, float blue, float alpha);

	/**
	 * 頂点の色
	 * @param red 赤(0～255)
	 * @param green 緑(0～255)
	 * @param blue 青(0～255)
	 * @param alpha アルファ(0～255)
	 * @return this
	 */
	@Nonnull
	CompatBaseVertex color(int red, int green, int blue, int alpha);

	/**
	 * 法線
	 * @param nx X法線
	 * @param ny Y法線
	 * @param nz Z法線
	 * @return
	 */
	@Nonnull
	CompatBaseVertex normal(float nx, float ny, float nz);
}
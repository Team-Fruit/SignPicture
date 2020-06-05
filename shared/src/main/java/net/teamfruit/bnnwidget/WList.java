package net.teamfruit.bnnwidget;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.teamfruit.bnnwidget.OverridablePoint;
import net.teamfruit.bnnwidget.WCommon;
import net.teamfruit.bnnwidget.position.Area;
import net.teamfruit.bnnwidget.position.Point;
import net.teamfruit.bnnwidget.position.R;
import net.teamfruit.bnnwidget.util.NotifyCollection;

/**
 * リストを監視し、ウィジェットに反映します。
 * @param <T> 監視リストの型
 * @param <W> 対応ウィジェット型
 * @author TeamFruit
 */
public abstract class WList<T, W extends WCommon> extends WTypedPanel<W> {
	/**
	 * 監視するリスト
	 */
	protected @Nonnull NotifyCollection<T> check;

	public WList(final @Nonnull R position, final @Nonnull NotifyCollection<T> check) {
		super(position);
		this.check = check;
	}

	/**
	 * 監視するリストを設定します
	 * @param check 監視するリスト
	 */
	public void setList(final @Nonnull NotifyCollection<T> check) {
		this.check = check;
		this.cachedModCount = -1;
		this.toT.clear();
		this.toW.clear();
		this.Tindex.clear();
		this.cws.clear();
		this.removelist.addAll(getContainer());
	}

	/**
	 * 追加操作はサポートされていません
	 */
	@Override
	public boolean add(final @Nonnull W widget) {
		return false;
	}

	/**
	 * 消去操作はサポートされていません
	 */
	@Override
	public boolean remove(final @Nonnull W widget) {
		return false;
	}

	int cachedModCount = -1;

	@Override
	public void update(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p) {
		final int mod = this.check.getModCount();
		if (mod!=this.cachedModCount) {
			this.cachedModCount = mod;
			update();
		}
		super.update(ev, pgp, p);
	}

	private final @Nonnull Map<W, T> toT = Maps.newHashMap();
	private final @Nonnull Map<T, W> toW = Maps.newHashMap();
	private final @Nonnull Map<T, Integer> Tindex = Maps.newHashMap();
	private final @Nonnull Set<W> cws = Sets.newHashSet();

	/**
	 * 監視リストの変更を反映します
	 */
	@SuppressWarnings("unlikely-arg-type")
	public void update() {
		final List<W> ws = getContainer();
		this.cws.clear();
		this.cws.addAll(ws);
		int it = -1;
		synchronized (this.check) {
			for (final T t : this.check) {
				it++;
				W w = this.toW.get(t);
				if (w==null) {
					w = createWidget(t, it);
					this.toT.put(w, t);
					this.toW.put(t, w);
				}
				this.cws.remove(w);
				if (!ws.contains(w)) {
					this.Tindex.put(t, it);
					super.add(w);
					onAdded(t, w);
				}
				final int it0 = this.Tindex.get(t);
				if (it!=it0)
					onMoved(t, w, it0, it);
			}
		}
		for (final W w : this.cws) {
			final T t = this.toT.get(w);
			if (t!=null) {
				super.remove(w);
				onRemove(t, w);
				this.toT.remove(t);
				this.toW.remove(w);
				this.Tindex.remove(t);
			}
		}
	}

	/**
	 * 監視リストの要素をもとに対応コンポーネントを生成します
	 * @param t 監視リストの要素
	 * @param i インデックス
	 * @return 対応コンポーネント
	 */
	protected abstract @Nonnull W createWidget(@Nonnull T t, int i);

	/**
	 * 対応コンポーネントが追加される際に呼ばれます
	 * @param t 監視リストの要素
	 * @param w 対応コンポーネント
	 */
	@OverridablePoint
	protected void onAdded(final @Nonnull T t, final @Nonnull W w) {
	}

	/**
	 * 対応コンポーネントが消去される際に呼ばれます
	 * @param t 監視リストの要素
	 * @param w 対応コンポーネント
	 */
	@OverridablePoint
	protected void onRemove(final @Nonnull T t, final @Nonnull W w) {
	}

	/**
	 * 対応コンポーネントが移動される際に呼ばれます
	 * @param t 監視リストの要素
	 * @param w 対応コンポーネント
	 * @param from 移動前のインデックス
	 * @param to 移動後のインデックス
	 */
	@OverridablePoint
	protected void onMoved(final @Nonnull T t, final @Nonnull W w, final int from, final int to) {
	}
}

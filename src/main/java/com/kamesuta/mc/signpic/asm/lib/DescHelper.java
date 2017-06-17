package com.kamesuta.mc.signpic.asm.lib;

import javax.annotation.Nonnull;

import org.objectweb.asm.Type;

public class DescHelper {
	/**
	 {@link #toDesc(Object)}をMethodのDescriptor用に使えるようにしたもの。
	 下手なクラスをここに入れようとするとまずいので確信がない限りStringで入れるべき。
	
	 @param returnType {@link String}型か、{@link Class}型で目的のMethodの返り値の型を指定する。
	 @param rawDesc {@link String}型か、{@link Class}型でMethodの引数たちの型を指定する。
	 @throws IllegalArgumentException 引数に{@link String}型か、{@link Class}型以外が入ったら投げられる。
	 @return Javaバイトコードで扱われる形の文字列に変換されたDescriptor。
	 */
	public static @Nonnull String toDesc(final @Nonnull Object returnType, final @Nonnull Object... rawDesc) {
		final StringBuilder sb = new StringBuilder("(");
		for (final Object o : rawDesc)
			if (o!=null)
				sb.append(toDesc(o));
		sb.append(')');
		sb.append(toDesc(returnType));
		return sb.toString();
	}

	/**
	 {@link Class#forName}とか{@link Class#getCanonicalName()}したりするとまだ読み込まれてなかったりしてまずいので安全策。
	 下手なクラスをここに入れようとするとまずいので確信がない限りStringで入れるべき。
	
	 @param raw {@link String}型か、{@link Class}型でASM用の文字列に変換したいクラスを指定する。
	 @throws IllegalArgumentException {@param raw}に{@link String}型か、{@link Class}型以外が入ったら投げられる。
	 @return Javaバイトコードで扱われる形の文字列に変換されたクラス。
	 */
	public static @Nonnull String toDesc(final @Nonnull Object raw) {
		if (raw instanceof Class) {
			final Class<?> clazz = (Class<?>) raw;
			return Type.getDescriptor(clazz);
		} else if (raw instanceof String) {
			String desc = (String) raw;
			desc = desc.replace('.', '/');
			desc = desc.matches("L.+;") ? desc : "L"+desc+";";
			return desc;
		} else
			throw new IllegalArgumentException();
	}
}

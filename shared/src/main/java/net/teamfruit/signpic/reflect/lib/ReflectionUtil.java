package net.teamfruit.signpic.reflect.lib;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.ImmutableList;

import net.teamfruit.signpic.asm.lib.RefName;

@SuppressWarnings("unchecked")
public class ReflectionUtil {
	public static <T> Class<T> $class(final _String _class) throws Exception {
		Exception exception = null;
		for (final String $_class : _class.get())
			try {
				return (Class<T>) Class.forName($_class);
			} catch (final Exception e) {
				exception = e;
			}
		if (exception!=null)
			throw exception;
		throw new NullPointerException();
	}

	public static <T> Class<T> $class(final _String _class, final ClassLoader $classloader) throws Exception {
		Exception exception = null;
		for (final String $_class : _class.get())
			try {
				return (Class<T>) Class.forName($_class, true, $classloader);
			} catch (final Exception e) {
				exception = e;
			}
		if (exception!=null)
			throw exception;
		throw new NullPointerException();
	}

	public static @Nullable <T> Class<T> $$class(final _String _class) {
		if (_class==null)
			return null;
		try {
			return $class(_class);
		} catch (final Exception e) {
		}
		return null;
	}

	public static @Nullable <T> Class<T> $$class(final _String _class, final ClassLoader $classloader) {
		if (_class==null)
			return null;
		try {
			return $class(_class, $classloader);
		} catch (final Exception e) {
		}
		return null;
	}

	public static <T> T $new(final Class<?> $class) throws Exception {
		return (T) $class.newInstance();
	}

	public static @Nullable <T> T $$new(final Class<?> $class) {
		if ($class==null)
			return null;
		try {
			return $new($class);
		} catch (final Exception e) {
		}
		return null;
	}

	public static <T> Constructor<T> $constructor(final Class<?> $class, final Class<?>... $paramtypes) throws Exception {
		return (Constructor<T>) $class.getConstructor($paramtypes);
	}

	public static <T> Constructor<T> $pconstructor(final Class<?> $class, final Class<?>... $paramtypes) throws Exception {
		final Constructor<?> $new = $class.getDeclaredConstructor($paramtypes);
		$new.setAccessible(true);
		return (Constructor<T>) $new;
	}

	public static @Nullable <T> Constructor<T> $$constructor(final @Nullable Class<?> $class, final Class<?>... $paramtypes) {
		if ($class==null||ArrayUtils.contains($paramtypes, null))
			return null;
		try {
			return $constructor($class, $paramtypes);
		} catch (final Exception e) {
		}
		return null;
	}

	public static @Nullable <T> Constructor<T> $$pconstructor(final @Nullable Class<?> $class, final Class<?>... $paramtypes) {
		if ($class==null||ArrayUtils.contains($paramtypes, null))
			return null;
		try {
			return $pconstructor($class, $paramtypes);
		} catch (final Exception e) {
		}
		return null;
	}

	public static Method $method(final Class<?> $class, final _String _method, final Class<?>... $paramtypes) throws Exception {
		Exception exception = null;
		for (final String $_method : _method.get())
			try {
				return $class.getMethod($_method, $paramtypes);
			} catch (final Exception e) {
				exception = e;
			}
		if (exception!=null)
			throw exception;
		throw new NullPointerException();
	}

	public static Method $pmethod(final Class<?> $class, final _String _method, final Class<?>... $paramtypes) throws Exception {
		Exception exception = null;
		for (final String $_method : _method.get())
			try {
				Class<?> $$class = $class;
				Method $method = null;
				do
					try {
						$method = $$class.getDeclaredMethod($_method, $paramtypes);
						break;
					} catch (final Exception e) {
					}
				while (($$class = $$class.getSuperclass())!=null);
				if ($method==null)
					throw new NoSuchMethodException();
				$method.setAccessible(true);
				return $method;
			} catch (final Exception e) {
				exception = e;
			}
		if (exception!=null)
			throw exception;
		throw new NullPointerException();
	}

	public static @Nullable Method $$method(final @Nullable Class<?> $class, final _String _method, final Class<?>... $paramtypes) {
		if ($class==null||ArrayUtils.contains($paramtypes, null))
			return null;
		try {
			return $method($class, _method, $paramtypes);
		} catch (final Exception e) {
		}
		return null;
	}

	public static @Nullable Method $$pmethod(final @Nullable Class<?> $class, final _String _method, final Class<?>... $paramtypes) {
		if ($class==null||ArrayUtils.contains($paramtypes, null))
			return null;
		try {
			return $pmethod($class, _method, $paramtypes);
		} catch (final Exception e) {
		}
		return null;
	}

	public static @Nullable Method $searchmethod(final Class<?> $class, final @Nullable ModifierMatcher $matcher, final Class<?> $returntype, final Class<?>... $paramtypes) throws Exception {
		final Method[] methods = $class.getMethods();
		for (final Method method : methods)
			if ($returntype.equals(method.getReturnType())&&Arrays.equals($paramtypes, method.getParameterTypes())&&($matcher==null||$matcher.match(method.getModifiers())))
				return method;
		throw new NoSuchMethodException();
	}

	public static @Nullable Method $psearchmethod(Class<?> $class, final @Nullable ModifierMatcher $matcher, final Class<?> $returntype, final Class<?>... $paramtypes) throws Exception {
		Method $method = null;
		c: do {
			final Method[] methods = $class.getDeclaredMethods();
			for (final Method method : methods)
				if ($returntype.equals(method.getReturnType())&&Arrays.equals($paramtypes, method.getParameterTypes())&&($matcher==null||$matcher.match(method.getModifiers()))) {
					$method = method;
					break c;
				}
		} while (($class = $class.getSuperclass())!=null);
		if ($method==null)
			throw new NoSuchMethodException();
		$method.setAccessible(true);
		return $method;
	}

	public static @Nullable Method $$searchmethod(final Class<?> $class, final @Nullable ModifierMatcher $matcher, final Class<?> $returntype, final Class<?>... $paramtypes) {
		if ($class==null||$returntype==null||ArrayUtils.contains($paramtypes, null))
			return null;
		try {
			return $searchmethod($class, $matcher, $returntype, $paramtypes);
		} catch (final Exception e) {
		}
		return null;
	}

	public static @Nullable Method $$psearchmethod(final Class<?> $class, final @Nullable ModifierMatcher $matcher, final Class<?> $returntype, final Class<?>... $paramtypes) {
		if ($class==null||$returntype==null||ArrayUtils.contains($paramtypes, null))
			return null;
		try {
			return $psearchmethod($class, $matcher, $returntype, $paramtypes);
		} catch (final Exception e) {
		}
		return null;
	}

	public static Field $field(final Class<?> $class, final _String _field) throws Exception {
		Exception exception = null;
		for (final String $_field : _field.get())
			try {
				return $class.getField($_field);
			} catch (final Exception e) {
				exception = e;
			}
		if (exception!=null)
			throw exception;
		throw new NullPointerException();
	}

	public static Field $pfield(final Class<?> $class, final _String _field) throws Exception {
		Exception exception = null;
		for (final String $_field : _field.get())
			try {
				Class<?> $$class = $class;
				Field $field = null;
				do
					try {
						$field = $$class.getDeclaredField($_field);
						break;
					} catch (final Exception e) {
					}
				while (($$class = $$class.getSuperclass())!=null);
				if ($field==null)
					throw new NoSuchFieldException();
				$field.setAccessible(true);
				return $field;
			} catch (final Exception e) {
				exception = e;
			}
		if (exception!=null)
			throw exception;
		throw new NullPointerException();
	}

	public static @Nullable Field $$field(final @Nullable Class<?> $class, final _String _field) {
		if ($class==null)
			return null;
		try {
			return $field($class, _field);
		} catch (final Exception e) {
		}
		return null;
	}

	public static @Nullable Field $$pfield(final @Nullable Class<?> $class, final _String _field) {
		if ($class==null)
			return null;
		try {
			return $pfield($class, _field);
		} catch (final Exception e) {
		}
		return null;
	}

	public static @Nullable Field $searchfield(final Class<?> $class, final @Nullable ModifierMatcher $matcher, final Class<?> $type) throws Exception {
		final Field[] fields = $class.getFields();
		for (final Field field : fields)
			if ($type.equals(field.getType())&&($matcher==null||$matcher.match(field.getModifiers())))
				return field;
		throw new NoSuchMethodException();
	}

	public static @Nullable Field $psearchfield(Class<?> $class, final @Nullable ModifierMatcher $matcher, final Class<?> $type) throws Exception {
		Field $field = null;
		c: do {
			final Field[] fields = $class.getDeclaredFields();
			for (final Field field : fields)
				if ($type.equals(field.getType())&&($matcher==null||$matcher.match(field.getModifiers()))) {
					field.setAccessible(true);
					$field = field;
					break c;
				}
		} while (($class = $class.getSuperclass())!=null);
		if ($field==null)
			throw new NoSuchFieldException();
		$field.setAccessible(true);
		return $field;
	}

	public static @Nullable Field $$searchfield(final Class<?> $class, final @Nullable ModifierMatcher $matcher, final Class<?> $type) {
		if ($class==null||$type==null)
			return null;
		try {
			return $searchfield($class, $matcher, $type);
		} catch (final Exception e) {
		}
		return null;
	}

	public static @Nullable Field $$psearchfield(final Class<?> $class, final @Nullable ModifierMatcher $matcher, final Class<?> $type) {
		if ($class==null||$type==null)
			return null;
		try {
			return $psearchfield($class, $matcher, $type);
		} catch (final Exception e) {
		}
		return null;
	}

	public static _Class _class(final _String _class) throws Exception {
		return new _Class($class(_class));
	}

	public static _Class _class(final _String _class, final ClassLoader $classloader) throws Exception {
		return new _Class($class(_class, $classloader));
	}

	public static @Nullable _Class __class(final _String _class) {
		final Class<?> obj = $$class(_class);
		if (obj==null)
			return null;
		return new _Class(obj);
	}

	public static @Nullable _Class __class(final _String _class, final ClassLoader $classloader) {
		final Class<?> obj = $$class(_class, $classloader);
		if (obj==null)
			return null;
		return new _Class(obj);
	}

	public static _Constructor _constructor(final Class<?> $class, final Class<?>... $paramtypes) throws Exception {
		return new _Constructor($constructor($class, $paramtypes));
	}

	public static _Constructor _pconstructor(final Class<?> $class, final Class<?>... $paramtypes) throws Exception {
		return new _Constructor($pconstructor($class, $paramtypes));
	}

	public static @Nullable _Constructor __constructor(final @Nullable Class<?> $class, final Class<?>... $paramtypes) {
		final Constructor<?> obj = $$constructor($class, $paramtypes);
		if (obj==null)
			return null;
		return new _Constructor(obj);
	}

	public static @Nullable _Constructor __pconstructor(final @Nullable Class<?> $class, final Class<?>... $paramtypes) {
		final Constructor<?> obj = $$pconstructor($class, $paramtypes);
		if (obj==null)
			return null;
		return new _Constructor(obj);
	}

	public static _Method _method(final Class<?> $class, final _String _method, final Class<?>... $paramtypes) throws Exception {
		return new _Method($method($class, _method, $paramtypes));
	}

	public static _Method _pmethod(final Class<?> $class, final _String _method, final Class<?>... $paramtypes) throws Exception {
		return new _Method($pmethod($class, _method, $paramtypes));
	}

	public static @Nullable _Method __method(final @Nullable Class<?> $class, final _String _method, final Class<?>... $paramtypes) {
		final Method obj = $$method($class, _method, $paramtypes);
		if (obj==null)
			return null;
		return new _Method(obj);
	}

	public static @Nullable _Method __pmethod(final @Nullable Class<?> $class, final _String _method, final Class<?>... $paramtypes) {
		final Method obj = $$pmethod($class, _method, $paramtypes);
		if (obj==null)
			return null;
		return new _Method(obj);
	}

	public static _Method _searchmethod(final Class<?> $class, final @Nullable ModifierMatcher $matcher, final Class<?> $returntype, final Class<?>... $paramtypes) throws Exception {
		return new _Method($searchmethod($class, $matcher, $returntype, $paramtypes));
	}

	public static _Method _psearchmethod(final Class<?> $class, final @Nullable ModifierMatcher $matcher, final Class<?> $returntype, final Class<?>... $paramtypes) throws Exception {
		return new _Method($psearchmethod($class, $matcher, $returntype, $paramtypes));
	}

	public static @Nullable _Method __searchmethod(final Class<?> $class, final @Nullable ModifierMatcher $matcher, final Class<?> $returntype, final Class<?>... $paramtypes) {
		final Method obj = $$searchmethod($class, $matcher, $returntype, $paramtypes);
		if (obj==null)
			return null;
		return new _Method(obj);
	}

	public static @Nullable _Method __psearchmethod(final Class<?> $class, final @Nullable ModifierMatcher $matcher, final Class<?> $returntype, final Class<?>... $paramtypes) {
		final Method obj = $$psearchmethod($class, $matcher, $returntype, $paramtypes);
		if (obj==null)
			return null;
		return new _Method(obj);
	}

	public static _Field _field(final Class<?> $class, final _String _field) throws Exception {
		return new _Field($field($class, _field));
	}

	public static _Field _pfield(final Class<?> $class, final _String _field) throws Exception {
		return new _Field($pfield($class, _field));
	}

	public static @Nullable _Field __field(final @Nullable Class<?> $class, final _String _field) {
		final Field obj = $$field($class, _field);
		if (obj==null)
			return null;
		return new _Field(obj);
	}

	public static @Nullable _Field __pfield(final @Nullable Class<?> $class, final _String _field) {
		final Field obj = $$pfield($class, _field);
		if (obj==null)
			return null;
		return new _Field(obj);
	}

	public static _Field _searchfield(final Class<?> $class, final @Nullable ModifierMatcher $matcher, final Class<?> $type) throws Exception {
		return new _Field($searchfield($class, $matcher, $type));
	}

	public static _Field _psearchfield(final Class<?> $class, final @Nullable ModifierMatcher $matcher, final Class<?> $type) throws Exception {
		return new _Field($psearchfield($class, $matcher, $type));
	}

	public static @Nullable _Field __searchfield(final Class<?> $class, final @Nullable ModifierMatcher $matcher, final Class<?> $type) {
		final Field obj = $$searchfield($class, $matcher, $type);
		if (obj==null)
			return null;
		return new _Field(obj);
	}

	public static @Nullable _Field __psearchfield(final Class<?> $class, final @Nullable ModifierMatcher $matcher, final Class<?> $type) {
		final Field obj = $$psearchfield($class, $matcher, $type);
		if (obj==null)
			return null;
		return new _Field(obj);
	}

	public static _String $(final String _string) {
		return new SingleName(_string);
	}

	public static _String $(final String _primary, final String... _strings) {
		return new _MultiName(_primary, _strings);
	}

	public static _String $(final RefName refName) {
		return new DeobfName(refName);
	}

	public static _String $deobf(final String mcpName, final String srgName) {
		return $(RefName.deobName(mcpName, srgName));
	}

	public static ModifierMatcher $matcher() {
		return new ModifierMatcher();
	}

	public static interface _String {
		ImmutableList<String> get();
	}

	private static class SingleName implements _String {
		private ImmutableList<String> _strings;

		public SingleName(final String _string) {
			this._strings = ImmutableList.of(_string);
		}

		@Override
		public ImmutableList<String> get() {
			return this._strings;
		}
	}

	private static class _MultiName implements _String {
		private ImmutableList<String> _strings;

		public _MultiName(final String _primary, final String... _strings) {
			this._strings = new ImmutableList.Builder<String>().add(_primary).add(_strings).build();
		}

		@Override
		public ImmutableList<String> get() {
			return this._strings;
		}
	}

	private static class DeobfName extends SingleName {
		public DeobfName(final RefName refName) {
			super(refName.name());
		}
	}

	public static class _Class {
		private final Class<?> $class;

		public _Class(final Class<?> $class) {
			this.$class = $class;
		}

		public <T> Class<T> $class() {
			return (Class<T>) this.$class;
		}

		public <T> T $new() throws Exception {
			return ReflectionUtil.$new(this.$class);
		}

		public @Nullable <T> T $$new() {
			return ReflectionUtil.$$new(this.$class);
		}

		public <T> Constructor<T> $constructor(final Class<?>... $paramtypes) throws Exception {
			return ReflectionUtil.$constructor(this.$class, $paramtypes);
		}

		public <T> Constructor<T> $pconstructor(final Class<?>... $paramtypes) throws Exception {
			return ReflectionUtil.$pconstructor(this.$class, $paramtypes);
		}

		public @Nullable <T> Constructor<T> $$constructor(final Class<?>... $paramtypes) {
			return ReflectionUtil.$$constructor(this.$class, $paramtypes);
		}

		public @Nullable <T> Constructor<T> $$pconstructor(final Class<?>... $paramtypes) {
			return ReflectionUtil.$$pconstructor(this.$class, $paramtypes);
		}

		public Method $method(final _String _method, final Class<?>... $paramtypes) throws Exception {
			return ReflectionUtil.$method(this.$class, _method, $paramtypes);
		}

		public Method $pmethod(final _String _method, final Class<?>... $paramtypes) throws Exception {
			return ReflectionUtil.$pmethod(this.$class, _method, $paramtypes);
		}

		public @Nullable Method $$method(final _String _method, final Class<?>... $paramtypes) {
			return ReflectionUtil.$$method(this.$class, _method, $paramtypes);
		}

		public @Nullable Method $$pmethod(final _String _method, final Class<?>... $paramtypes) {
			return ReflectionUtil.$$pmethod(this.$class, _method, $paramtypes);
		}

		public Method $searchmethod(final @Nullable ModifierMatcher $matcher, final Class<?> $returntype, final Class<?>... $paramtypes) throws Exception {
			return ReflectionUtil.$searchmethod(this.$class, $matcher, $returntype, $paramtypes);
		}

		public Method $psearchmethod(final @Nullable ModifierMatcher $matcher, final Class<?> $returntype, final Class<?>... $paramtypes) throws Exception {
			return ReflectionUtil.$psearchmethod(this.$class, $matcher, $returntype, $paramtypes);
		}

		public @Nullable Method $$searchmethod(final @Nullable ModifierMatcher $matcher, final Class<?> $returntype, final Class<?>... $paramtypes) {
			return ReflectionUtil.$$searchmethod(this.$class, $matcher, $returntype, $paramtypes);
		}

		public @Nullable Method $$psearchmethod(final @Nullable ModifierMatcher $matcher, final Class<?> $returntype, final Class<?>... $paramtypes) {
			return ReflectionUtil.$$psearchmethod(this.$class, $matcher, $returntype, $paramtypes);
		}

		public Field $field(final _String _field) throws Exception {
			return ReflectionUtil.$field(this.$class, _field);
		}

		public Field $pfield(final _String _field) throws Exception {
			return ReflectionUtil.$pfield(this.$class, _field);
		}

		public @Nullable Field $$field(final _String _field) {
			return ReflectionUtil.$$field(this.$class, _field);
		}

		public @Nullable Field $$pfield(final _String _field) {
			return ReflectionUtil.$$pfield(this.$class, _field);
		}

		public Field $searchfield(final @Nullable ModifierMatcher $matcher, final Class<?> $type) throws Exception {
			return ReflectionUtil.$searchfield(this.$class, $matcher, $type);
		}

		public Field $psearchfield(final @Nullable ModifierMatcher $matcher, final Class<?> $type) throws Exception {
			return ReflectionUtil.$psearchfield(this.$class, $matcher, $type);
		}

		public @Nullable Field $$searchfield(final @Nullable ModifierMatcher $matcher, final Class<?> $type) {
			return ReflectionUtil.$$searchfield(this.$class, $matcher, $type);
		}

		public @Nullable Field $$psearchfield(final @Nullable ModifierMatcher $matcher, final Class<?> $type) {
			return ReflectionUtil.$$psearchfield(this.$class, $matcher, $type);
		}

		public _Constructor _constructor(final Class<?>... $paramtypes) throws Exception {
			return ReflectionUtil._constructor(this.$class, $paramtypes);
		}

		public _Constructor _pconstructor(final Class<?>... $paramtypes) throws Exception {
			return ReflectionUtil._pconstructor(this.$class, $paramtypes);
		}

		public @Nullable <T> _Constructor __constructor(final Class<?>... $paramtypes) {
			return ReflectionUtil.__constructor(this.$class, $paramtypes);
		}

		public @Nullable <T> _Constructor __pconstructor(final Class<?>... $paramtypes) {
			return ReflectionUtil.__pconstructor(this.$class, $paramtypes);
		}

		public _Method _method(final _String _method, final Class<?>... $paramtypes) throws Exception {
			return ReflectionUtil._method(this.$class, _method, $paramtypes);
		}

		public _Method _pmethod(final _String _method, final Class<?>... $paramtypes) throws Exception {
			return ReflectionUtil._pmethod(this.$class, _method, $paramtypes);
		}

		public @Nullable _Method __method(final _String _method, final Class<?>... $paramtypes) {
			return ReflectionUtil.__method(this.$class, _method, $paramtypes);
		}

		public @Nullable _Method __pmethod(final _String _method, final Class<?>... $paramtypes) {
			return ReflectionUtil.__pmethod(this.$class, _method, $paramtypes);
		}

		public _Method _searchmethod(final @Nullable ModifierMatcher $matcher, final Class<?> $returntype, final Class<?>... $paramtypes) throws Exception {
			return ReflectionUtil._searchmethod(this.$class, $matcher, $returntype, $paramtypes);
		}

		public _Method _psearchmethod(final @Nullable ModifierMatcher $matcher, final Class<?> $returntype, final Class<?>... $paramtypes) throws Exception {
			return ReflectionUtil._psearchmethod(this.$class, $matcher, $returntype, $paramtypes);
		}

		public @Nullable _Method __searchmethod(final @Nullable ModifierMatcher $matcher, final Class<?> $returntype, final Class<?>... $paramtypes) {
			return ReflectionUtil.__searchmethod(this.$class, $matcher, $returntype, $paramtypes);
		}

		public @Nullable _Method __psearchmethod(final @Nullable ModifierMatcher $matcher, final Class<?> $returntype, final Class<?>... $paramtypes) {
			return ReflectionUtil.__psearchmethod(this.$class, $matcher, $returntype, $paramtypes);
		}

		public _Field _field(final _String _field) throws Exception {
			return ReflectionUtil._field(this.$class, _field);
		}

		public _Field _pfield(final _String _field) throws Exception {
			return ReflectionUtil._pfield(this.$class, _field);
		}

		public @Nullable _Field __field(final _String _field) {
			return ReflectionUtil.__field(this.$class, _field);
		}

		public @Nullable _Field __pfield(final _String _field) {
			return ReflectionUtil.__pfield(this.$class, _field);
		}

		public _Field _searchfield(final @Nullable ModifierMatcher $matcher, final Class<?> $type) throws Exception {
			return ReflectionUtil._searchfield(this.$class, $matcher, $type);
		}

		public _Field _psearchfield(final @Nullable ModifierMatcher $matcher, final Class<?> $type) throws Exception {
			return ReflectionUtil._psearchfield(this.$class, $matcher, $type);
		}

		public @Nullable _Field __searchfield(final @Nullable ModifierMatcher $matcher, final Class<?> $type) {
			return ReflectionUtil.__searchfield(this.$class, $matcher, $type);
		}

		public @Nullable _Field __psearchfield(final @Nullable ModifierMatcher $matcher, final Class<?> $type) {
			return ReflectionUtil.__psearchfield(this.$class, $matcher, $type);
		}
	}

	public static class _Constructor {
		private final Constructor<?> $constructor;

		public _Constructor(final Constructor<?> $constructor) {
			this.$constructor = $constructor;
		}

		public <T> Constructor<T> $constructor() {
			return (Constructor<T>) this.$constructor;
		}

		public <T> T $new(final Object... _initargs) throws Exception {
			return (T) this.$constructor.newInstance(_initargs);
		}

		public @Nullable <T> T $$new(final Object... _initargs) {
			if (_initargs==null)
				return null;
			try {
				return $new(_initargs);
			} catch (final Exception e) {
			}
			return null;
		}
	}

	public static class _Method {
		private final Method $method;

		public _Method(final Method $method) {
			this.$method = $method;
		}

		public Method $method() {
			return this.$method;
		}

		public <T> T $invoke(@Nullable final Object _obj, final Object... _args) throws Exception {
			return (T) this.$method.invoke(_obj, _args);
		}

		public <T> T $$invoke(@Nullable final Object _obj, final Object... _args) {
			if (_args==null)
				return null;
			try {
				return $invoke(_obj, _args);
			} catch (final Exception e) {
			}
			return null;
		}
	}

	public static class _Field {
		private final Field $field;

		public _Field(final Field $field) {
			this.$field = $field;
		}

		public <T> Field $field() {
			return this.$field;
		}

		public <T> T $get(@Nullable final Object _obj) throws Exception {
			return (T) this.$field.get(_obj);
		}

		public <T> T $$get(@Nullable final Object _obj) {
			try {
				return $get(_obj);
			} catch (final Exception e) {
			}
			return null;
		}

		public void $set(@Nullable final Object _obj, final Object _value) throws Exception {
			this.$field.set(_obj, _value);
		}

		public void $$set(@Nullable final Object _obj, final Object _value) {
			try {
				$set(_obj, _value);
			} catch (final Exception e) {
			}
		}
	}
}
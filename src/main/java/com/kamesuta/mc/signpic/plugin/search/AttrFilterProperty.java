package com.kamesuta.mc.signpic.plugin.search;

import com.kamesuta.mc.signpic.attr.AttrReaders;

public interface AttrFilterProperty<E> {

	E get(AttrReaders attr);
}

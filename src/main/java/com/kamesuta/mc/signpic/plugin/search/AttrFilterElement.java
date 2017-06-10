package com.kamesuta.mc.signpic.plugin.search;

import com.kamesuta.mc.signpic.attr.AttrReaders;

public interface AttrFilterElement extends FilterElement {

	boolean filter(AttrReaders attr);
}

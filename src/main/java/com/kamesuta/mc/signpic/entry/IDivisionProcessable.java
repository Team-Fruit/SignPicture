package com.kamesuta.mc.signpic.entry;

public interface IDivisionProcessable {
	/**
	 * called multiple
	 * @return is finished
	 */
	boolean onDivisionProcess() throws Exception;
}

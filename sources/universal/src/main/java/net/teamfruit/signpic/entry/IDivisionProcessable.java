package net.teamfruit.signpic.entry;

public interface IDivisionProcessable {
	/**
	 * called multiple
	 * @return is finished
	 */
	boolean onDivisionProcess() throws Exception;
}

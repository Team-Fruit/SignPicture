package net.teamfruit.signpic.entry;

public interface IAsyncProcessable {
	/**
	 * called once, do process at once
	 */
	void onAsyncProcess() throws Exception;
}

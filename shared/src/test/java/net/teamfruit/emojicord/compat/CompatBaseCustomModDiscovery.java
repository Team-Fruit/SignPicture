package net.teamfruit.emojicord.compat;

import java.nio.file.Path;
import java.util.List;

public interface CompatBaseCustomModDiscovery {
	void registerModList(List<Path> modList);

	List<Path> getModFiles();

	void discoverMods();
}

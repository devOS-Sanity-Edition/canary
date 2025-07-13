package one.devos.nautical.canary.feature;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModDependency;
import one.devos.nautical.canary.CanaryException;

public class FabricApiCheck {
	public static void run() {
		for (ModContainer container : FabricLoader.getInstance().getAllMods()) {
			for (ModDependency dependency : container.getMetadata().getDependencies()) {
				if (dependency.getModId().equals("fabric")) {
					String name = container.getMetadata().getName();
					throw new CanaryException("Mod " + name + " depends on `fabric` and not `fabric-api`. This behaviour has been deprecated since 1.19.3.");
				}
			}
		}
	}
}

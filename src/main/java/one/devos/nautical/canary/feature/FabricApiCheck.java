package one.devos.nautical.canary.feature;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModDependency;
import one.devos.nautical.canary.CanaryException;
import one.devos.nautical.canary.Config;

public class FabricApiCheck {
	public static void checkForDeprecatedApi() {
		if (Config.INSTANCE.breakLegacyFabricDependency()) {
			for (ModContainer modContainer : FabricLoader.getInstance().getAllMods()) {
				for (ModDependency dependency : modContainer.getMetadata().getDependencies()) {
					if (dependency.getModId().equals("fabric")) {
						throw new CanaryException("Depending on `fabric` and not `fabric-api` has been deprecated since 1.19.3.");
					}
				}
			}
		}
	}
}

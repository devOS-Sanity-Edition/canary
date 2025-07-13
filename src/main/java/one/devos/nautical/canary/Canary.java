package one.devos.nautical.canary;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import one.devos.nautical.canary.feature.FabricApiCheck;

public class Canary implements ModInitializer {
	public static final String ID = "canary";

	@Override
	public void onInitialize() {
		CanaryPackets.register();

		if (Config.INSTANCE.breakLegacyFabricDependency()) {
			FabricApiCheck.run();
		}
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(ID, path);
	}
}

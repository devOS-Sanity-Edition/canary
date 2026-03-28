package one.devos.nautical.canary;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class Canary implements ModInitializer {
	public static final String ID = "canary";

	@Override
	public void onInitialize() {
		CanaryPackets.register();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(ID, path);
	}
}

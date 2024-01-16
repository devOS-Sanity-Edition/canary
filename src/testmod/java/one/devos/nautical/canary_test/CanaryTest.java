package one.devos.nautical.canary_test;

import net.fabricmc.api.ModInitializer;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;

import one.devos.nautical.canary.CanaryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CanaryTest implements ModInitializer {
	public static final String ID = "canary_test";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	@Override
	public void onInitialize() {
		try {
			EntityDataSerializers.registerSerializer(EntityDataSerializer.simpleEnum(Unit.class));
		} catch (CanaryException e) {
			LOGGER.info("Test successful!", e);
		}
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(ID, path);
	}
}

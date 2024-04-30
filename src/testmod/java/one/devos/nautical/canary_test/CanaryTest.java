package one.devos.nautical.canary_test;

import net.fabricmc.api.ModInitializer;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Creeper;

import net.minecraft.world.level.block.DoorBlock;
import one.devos.nautical.canary.CanaryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CanaryTest implements ModInitializer {
	public static final String ID = "canary_test";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Testing Canary...");
		LOGGER.info(EntityDataSerializers.class.getName());
		LOGGER.info(Creeper.class.getName());
		LOGGER.info(DoorBlock.class.getName());
		try {
			EntityDataSerializers.registerSerializer(EntityDataSerializer.forValueType(ByteBufCodecs.byteArray(3)));
			fail("new EntityDataSerializer");
		} catch (CanaryException e) {
		}
		try {
			SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.BLOCK_POS);
			fail("SynchedEntityData from mod init");
		} catch (CanaryException e) {
		}
	}

	public static void fail(String testName) {
		throw new RuntimeException("Canary test failed: " + testName);
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(ID, path);
	}
}

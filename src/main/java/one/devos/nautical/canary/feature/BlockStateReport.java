package one.devos.nautical.canary.feature;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.slf4j.Logger;

import java.util.Comparator;

public class BlockStateReport {
	private static final Logger logger = LogUtils.getLogger();

	public static void print() {
		Multimap<String, BlockState> byNamespace = HashMultimap.create();
		for (BlockState state : Block.BLOCK_STATE_REGISTRY) {
			ResourceLocation id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
			byNamespace.put(id.getNamespace(), state);
		}

		logger.info("----- Canary BlockState Report -----");
		logger.info("Total BlockStates: {}", byNamespace.values().size());

		logger.info("Contributors sorted by count:");

		byNamespace.asMap().entrySet().stream()
				.sorted(Comparator.comparingInt(entry -> -entry.getValue().size()))
				.forEach(entry -> {
					String namespace = entry.getKey();
					int count = entry.getValue().size();
					logger.info("\t- {}: {} states", namespace, count);
				});
	}
}

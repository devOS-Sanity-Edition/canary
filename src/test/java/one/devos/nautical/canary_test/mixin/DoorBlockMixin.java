package one.devos.nautical.canary_test.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import one.devos.nautical.canary.CanaryException;

import one.devos.nautical.canary_test.CanaryTest;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DoorBlock.class)
public class DoorBlockMixin {
	@Inject(
			method = "createBlockStateDefinition",
			at = @At("TAIL")
	)
	private void addWaterlogged(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
		try {
			builder.add(BlockStateProperties.WATERLOGGED);
		} catch (CanaryException e) {
			CanaryTest.LOGGER.info("Successfully caught bad BlockState modification");
		}
	}
}

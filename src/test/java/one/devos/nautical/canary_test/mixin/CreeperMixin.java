package one.devos.nautical.canary_test.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.monster.Creeper;

@Mixin(Creeper.class)
public class CreeperMixin {
	// uncomment this field to test detection of data injection via mixin.
	// the presence of a mixin-merged accessor field will trigger Canary.
	//private static net.minecraft.network.syncher.EntityDataAccessor<Integer> bad;
}

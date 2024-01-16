package one.devos.nautical.canary_test.mixin;

import one.devos.nautical.canary.CanaryException;

import one.devos.nautical.canary_test.CanaryTest;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.monster.Creeper;

// catching doesn't work some reason. But Canary does.
@Mixin(Creeper.class)
public class CreeperMixin {
//	@Unique
//	private static final EntityDataAccessor<Integer> BAD_BAD_BAD;
//
//	static {
//		// appease the compiler
//		EntityDataAccessor<Integer> temp;
//		try {
//			temp = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.INT);
//			CanaryTest.fail("SynchedEntityData from mixin");
//		} catch (CanaryException e) {
//			temp = null;
//		}
//		BAD_BAD_BAD = temp;
//	}
}

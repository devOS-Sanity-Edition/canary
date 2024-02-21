package one.devos.nautical.canary.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.StateDefinition;

import net.minecraft.world.level.block.state.properties.Property;

import one.devos.nautical.canary.Canary;
import one.devos.nautical.canary.CanaryException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;

import java.lang.reflect.Method;

@Mixin(StateDefinition.Builder.class)
public class StateDefinition$BuilderMixin {
	@Inject(method = "add", at = @At("HEAD"))
	private void onAdd(Property<?>[] properties, CallbackInfoReturnable<?> cir) {
		// check the stacktrace for calls from mixins
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		// stackTrace[0] = getStackTrace
		// stackTrace[1] = onAdd
		// stackTrace[2] = add
		// stackTrace[3] = caller
		StackTraceElement caller = stackTrace[3];
		String callerClassName = caller.getClassName();
		if (Canary.CONFIG.stateBuilderWhitelist().contains(callerClassName))
			return; // explicitly declared as safe; ex. util methods

		try {
			Class<?> clazz = Class.forName(callerClassName);
			if (!Block.class.isAssignableFrom(clazz))
				return; // not a block, safe

			// find the Method of the caller to see if it's a mixin injection
			for (Method method : getMethodsSafe(clazz)) {
				if (!method.getName().equals(caller.getMethodName()))
					continue; // not it

				MixinMerged mixinMergedMeta = method.getDeclaredAnnotation(MixinMerged.class);
				if (mixinMergedMeta != null) { // 🚨🚨🚨
					throw new CanaryException("Unsafe BlockState property addition via mixin [" + mixinMergedMeta.mixin() + "]");
				}
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Unique
	private static Method[] getMethodsSafe(Class<?> clazz) {
		try {
			return clazz.getDeclaredMethods();
		} catch (Throwable e) {
			if (e instanceof ClassNotFoundException || e instanceof NoClassDefFoundError) {
				// may load client-only classes for poorly made modded blocks.
				return new Method[0];
			} else {
				throw new RuntimeException(e);
			}
		}
	}
}

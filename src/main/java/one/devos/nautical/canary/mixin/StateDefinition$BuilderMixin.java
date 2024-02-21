package one.devos.nautical.canary.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.StateDefinition;

import net.minecraft.world.level.block.state.properties.Property;

import one.devos.nautical.canary.CanaryException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Mixin(StateDefinition.Builder.class)
public class StateDefinition$BuilderMixin {
	@Inject(method = "add", at = @At("HEAD"))
	private void onAdd(Property<?>[] properties, CallbackInfoReturnable<?> cir) {
		// walk the stack trace to find any calls from mixins
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		try {
			for (StackTraceElement element : stackTrace) {
				Class<?> clazz = Class.forName(element.getClassName());
				if (!Block.class.isAssignableFrom(clazz))
					continue; // not a block, don't care

				for (Method method : getMethodsSafe(clazz)) {
					if (!method.getName().equals(element.getMethodName()))
						continue; // only care about the method in the stack trace

					MixinMerged mixinMergedMeta = method.getDeclaredAnnotation(MixinMerged.class);
					if (mixinMergedMeta != null) { // 🚨🚨🚨
						throw new CanaryException("Unsafe BlockState property addition via mixin [" + mixinMergedMeta.mixin() + "]");
					}
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

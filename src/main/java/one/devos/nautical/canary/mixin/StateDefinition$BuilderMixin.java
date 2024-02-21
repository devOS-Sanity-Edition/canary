package one.devos.nautical.canary.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.StateDefinition;

import net.minecraft.world.level.block.state.properties.Property;

import one.devos.nautical.canary.CanaryException;

import one.devos.nautical.canary.Config;
import one.devos.nautical.canary.Utils;

import org.spongepowered.asm.mixin.Mixin;
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
		StackTraceElement caller = Utils.getCaller();
		String callerClassName = caller.getClassName();
		if (Config.INSTANCE.stateBuilderWhitelist().contains(callerClassName))
			return; // explicitly declared as safe; ex. util methods

		Class<?> clazz = Utils.getClass(callerClassName);
		if (!Block.class.isAssignableFrom(clazz))
			return; // not a block, safe

		// find the Method of the caller to see if it's a mixin injection
		for (Method method : Utils.getMethodsSafe(clazz)) {
			if (!method.getName().equals(caller.getMethodName()))
				continue; // not it

			MixinMerged mixinMergedMeta = method.getDeclaredAnnotation(MixinMerged.class);
			if (mixinMergedMeta != null) { // 🚨🚨🚨
				throw new CanaryException("Unsafe BlockState property addition via mixin [" + mixinMergedMeta.mixin() + "]");
			}
		}
	}
}

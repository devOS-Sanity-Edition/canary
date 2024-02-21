package one.devos.nautical.canary.mixin;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import one.devos.nautical.canary.CanaryException;

import one.devos.nautical.canary.Config;
import one.devos.nautical.canary.Utils;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;

@Mixin(SynchedEntityData.class)
public class SynchedEntityDataMixin {
	@Unique // use synchronized just in case, client and server thread might load different classes at the same time
	private static final Set<Class<?>> scannedEntityClasses = Collections.synchronizedSet(new HashSet<>());

	@Inject(method = "defineId", at = @At("HEAD"))
	private static <T> void onDefine(Class<? extends Entity> entityClass, EntityDataSerializer<T> serializer, CallbackInfoReturnable<EntityDataAccessor<T>> cir) {
		StackTraceElement caller = Utils.getCaller();
		String callerClassName = caller.getClassName();
		if (Config.INSTANCE.trackedDataWhitelist().contains(callerClassName))
			return; // explicitly declared as safe; ex. util methods

		// see if the caller class is not a subclass of Entity
		Class<?> callerClass = Utils.getClass(callerClassName);
		if (!Entity.class.isAssignableFrom(callerClass)) { // 🚨🚨🚨
			throw new CanaryException("Unsafe tracked data registration for [" + entityClass.getName() + "] from [" + callerClassName + "]");
		}

		// scan the entity class for mixin'd fields
		if (scannedEntityClasses.contains(entityClass))
			return; // unless we did that already

		for (Field field : Utils.getFieldsSafe(entityClass)) {
			if (!Modifier.isStatic(field.getModifiers()))
				continue;
			if (field.getType() != EntityDataAccessor.class)
				continue;
			MixinMerged mixinMergedMeta = field.getDeclaredAnnotation(MixinMerged.class);
			if (mixinMergedMeta != null) { // 🚨🚨🚨
				throw new CanaryException("Unsafe tracked data registration for [" + entityClass.getName() + "] via mixin [" + mixinMergedMeta.mixin() + "]");
			}
		}

		// all is well
		scannedEntityClasses.add(entityClass);
	}
}

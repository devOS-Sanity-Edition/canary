package one.devos.nautical.canary.mixin;

import one.devos.nautical.canary.CanaryException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

@Mixin(EntityDataSerializers.class)
public class EntityDataSerializersMixin {
	@SuppressWarnings("FieldMayBeFinal")
	@Unique
	private static boolean vanillaRegistered;

	static {
		// static blocks are injected to tail of default static block
		vanillaRegistered = true;
	}

	@Inject(method = "registerSerializer", at = @At("HEAD"))
	private static void onRegister(EntityDataSerializer<?> serializer, CallbackInfo ci) {
		if (vanillaRegistered) {
			throw new CanaryException("Tried to register a new EntityDataSerializer!");
		}
	}
}

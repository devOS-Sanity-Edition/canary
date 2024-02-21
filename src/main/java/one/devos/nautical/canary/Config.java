package one.devos.nautical.canary;

import java.util.List;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Config(List<String> trackedDataWhitelist, List<String> stateBuilderWhitelist) {
	public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.listOf()
					.optionalFieldOf("tracked_data_whitelist", List.of("com.example.mymod.Utilities"))
					.forGetter(Config::trackedDataWhitelist),
			Codec.STRING.listOf()
					.optionalFieldOf("state_builder_whitelist", List.of("net.example.examplemod.Utils"))
					.forGetter(Config::stateBuilderWhitelist)
	).apply(instance, Config::new));

	public static Config makeDefault() {
		return CODEC.parse(JsonOps.INSTANCE, new JsonObject()).result().orElseThrow();
	}
}

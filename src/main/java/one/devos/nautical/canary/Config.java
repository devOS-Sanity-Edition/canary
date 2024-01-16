package one.devos.nautical.canary;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Config(List<String> trackedDataWhitelist) {
	public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.listOf().fieldOf("tracked_data_whitelist").forGetter(Config::trackedDataWhitelist)
	).apply(instance, Config::new));

	public static Config makeDefault() {
		return new Config(List.of("com.example.mymod.Utilities"));
	}
}

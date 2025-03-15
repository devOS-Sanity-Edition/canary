package one.devos.nautical.canary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.loader.api.FabricLoader;

public record Config(boolean printBlockStateReport, List<String> trackedDataWhitelist, List<String> stateBuilderWhitelist) {
	public static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("canary.json");
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.BOOL.optionalFieldOf("print_blockstate_report", false).forGetter(Config::printBlockStateReport),
			Codec.STRING.listOf().optionalFieldOf("tracked_data_whitelist", List.of()).forGetter(Config::trackedDataWhitelist),
			Codec.STRING.listOf().optionalFieldOf("state_builder_whitelist", List.of()).forGetter(Config::stateBuilderWhitelist)
	).apply(instance, Config::new));

	public static final Config INSTANCE = load();

	public static Config load() {
		return parseOrThrow(readJson());
	}

	private static Config makeDefault() {
		return new Config(
				false,
				List.of("com.example.mymod.Utilities"),
				List.of("net.example.examplemod.Utils")
		);
	}

	private static Config parseOrThrow(JsonElement json) {
		return CODEC.parse(JsonOps.INSTANCE, json).result().orElseThrow();
	}

	private static JsonElement readJson() {
		try {
			if (!Files.exists(PATH)) {
				// write defaults
				Config defaults = makeDefault();
				JsonElement json = CODEC.encodeStart(JsonOps.INSTANCE, defaults).result().orElseThrow();
				Files.writeString(PATH, gson.toJson(json), StandardOpenOption.CREATE);
				return json;
			}

			return JsonParser.parseString(Files.readString(PATH));
		} catch (IOException e) {
			throw new RuntimeException("Failed to read Canary config!", e);
		}

	}
}

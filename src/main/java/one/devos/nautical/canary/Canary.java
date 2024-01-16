package one.devos.nautical.canary;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import com.google.gson.JsonSyntaxException;

import com.mojang.serialization.JsonOps;

import net.fabricmc.api.ModInitializer;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

import net.fabricmc.loader.api.FabricLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Canary implements ModInitializer {
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static final String ID = "canary";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static final Config CONFIG = loadConfig();

	@Override
	public void onInitialize() {
	}

	private static Config loadConfig() {
		Path configPath = FabricLoader.getInstance().getConfigDir().resolve("canary.json");
		if (Files.exists(configPath)) {
			try (BufferedReader reader = Files.newBufferedReader(configPath)) {
				JsonElement json = JsonParser.parseReader(reader);
				return Config.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(false, error -> {
					throw new JsonSyntaxException("Canary config failed to parse: " + error);
				});
			} catch (IOException | JsonSyntaxException e) {
				throw new RuntimeException("Error reading Canary config!", e);
			}
		} else {
			Config config = Config.makeDefault();
			JsonElement json = Config.CODEC.encodeStart(JsonOps.INSTANCE, config).getOrThrow(false, error -> {
				throw new RuntimeException("Failed to encode Canary config: " + error);
			});
			try {
				Files.writeString(configPath, gson.toJson(json));
				return config;
			} catch (IOException e) {
				throw new RuntimeException("Failed to save Canary config", e);
			}
		}
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(ID, path);
	}
}

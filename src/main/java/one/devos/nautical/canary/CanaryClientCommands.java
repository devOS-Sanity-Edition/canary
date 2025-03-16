package one.devos.nautical.canary;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import one.devos.nautical.canary.feature.desync.DiagnoseDesyncCommand;

public class CanaryClientCommands {
	public static final String ROOT = "canaryclient";

	public static LiteralArgumentBuilder<FabricClientCommandSource> build() {
		return ClientCommandManager.literal(ROOT)
				.then(DiagnoseDesyncCommand.build());
	}
}

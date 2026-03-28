package one.devos.nautical.canary;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import one.devos.nautical.canary.feature.desync.DiagnoseDesyncCommand;

public class CanaryClientCommands {
	public static final String ROOT = "canaryclient";

	public static LiteralArgumentBuilder<FabricClientCommandSource> build() {
		return ClientCommands.literal(ROOT)
				.then(DiagnoseDesyncCommand.build());
	}
}

package one.devos.nautical.canary.feature.desync;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.chat.Component;

public class DiagnoseDesyncCommand {
	public static LiteralArgumentBuilder<FabricClientCommandSource> build() {
		return ClientCommandManager.literal("diagnoseDesync")
				.requires(source -> source.hasPermission(4))
				.executes(ctx -> {
					FabricClientCommandSource source = ctx.getSource();
					if (source.getClient().hasSingleplayerServer()) {
						source.sendError(Component.literal("There's nothing to diagnose in singleplayer."));
						return 0;
					}

					source.sendFeedback(Component.literal(
							"Requesting diagnostics from the server..."
					));
					ResponseHandler.awaitingData = true;
					ClientPlayNetworking.send(RequestDiagnosticsPacket.INSTANCE);
					return 1;
				});
	}
}

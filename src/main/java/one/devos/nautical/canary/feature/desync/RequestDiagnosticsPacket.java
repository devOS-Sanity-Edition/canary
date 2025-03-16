package one.devos.nautical.canary.feature.desync;

import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import one.devos.nautical.canary.CanaryPackets;
import one.devos.nautical.canary.Config;

import java.util.ArrayList;
import java.util.List;

public enum RequestDiagnosticsPacket implements CustomPacketPayload {
	INSTANCE;

	public static final StreamCodec<ByteBuf, RequestDiagnosticsPacket> CODEC = StreamCodec.unit(INSTANCE);

	public static void handle(RequestDiagnosticsPacket ignored, ServerPlayNetworking.Context context) {
		ServerPlayer player = context.player();
		if (player.hasPermissions(Config.INSTANCE.diagnosticsPermissionLevel())) {
			player.sendSystemMessage(Component.literal("Diagnostics en route. Check the client log for results."));
		} else {
			player.sendSystemMessage(Component.literal("You don't have permission for that."));
			return;
		}

		PacketSender sender = context.responseSender();

		List<String> queuedStates = new ArrayList<>();
		for (BlockState state : Block.BLOCK_STATE_REGISTRY) {
			queuedStates.add(state.toString());
			if (queuedStates.size() > 50_000) {
				sender.sendPacket(new ResponseDataPacket(queuedStates));
				queuedStates = new ArrayList<>();
			}
		}

		// last one
		if (!queuedStates.isEmpty()) {
			sender.sendPacket(new ResponseDataPacket(queuedStates));
		}

		sender.sendPacket(ResponseEndPacket.INSTANCE);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return CanaryPackets.REQUEST_DIAGNOSTICS;
	}
}

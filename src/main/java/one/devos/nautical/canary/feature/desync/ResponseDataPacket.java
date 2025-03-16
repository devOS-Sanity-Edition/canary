package one.devos.nautical.canary.feature.desync;

import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import one.devos.nautical.canary.CanaryPackets;

import java.util.List;

public record ResponseDataPacket(List<String> states) implements CustomPacketPayload {
	public static final StreamCodec<ByteBuf, ResponseDataPacket> CODEC = ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()).map(
			ResponseDataPacket::new, ResponseDataPacket::states
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return CanaryPackets.RESPONSE_DATA;
	}

	@Environment(EnvType.CLIENT)
	public static void handle(ResponseDataPacket packet, ClientPlayNetworking.Context context) {
		ResponseHandler.accept(packet.states);
	}
}

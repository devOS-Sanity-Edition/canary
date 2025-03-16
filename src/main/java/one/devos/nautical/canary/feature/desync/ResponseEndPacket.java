package one.devos.nautical.canary.feature.desync;

import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import one.devos.nautical.canary.CanaryPackets;

public enum ResponseEndPacket implements CustomPacketPayload {
	INSTANCE;

	public static final StreamCodec<ByteBuf, ResponseEndPacket> CODEC = StreamCodec.unit(INSTANCE);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return CanaryPackets.RESPONSE_END;
	}

	@Environment(EnvType.CLIENT)
	public static void handle(ResponseEndPacket packet, ClientPlayNetworking.Context context) {
		ResponseHandler.finish();
	}
}

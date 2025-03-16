package one.devos.nautical.canary;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import one.devos.nautical.canary.feature.desync.ResponseDataPacket;
import one.devos.nautical.canary.feature.desync.RequestDiagnosticsPacket;
import one.devos.nautical.canary.feature.desync.ResponseEndPacket;

public class CanaryPackets {
	public static final CustomPacketPayload.Type<RequestDiagnosticsPacket> REQUEST_DIAGNOSTICS = create("request_diagnostics");
	public static final CustomPacketPayload.Type<ResponseDataPacket> RESPONSE_DATA = create("response_data");
	public static final CustomPacketPayload.Type<ResponseEndPacket> RESPONSE_END = create("response_end");

	public static void register() {
		PayloadTypeRegistry.playC2S().register(REQUEST_DIAGNOSTICS, RequestDiagnosticsPacket.CODEC);
		PayloadTypeRegistry.playS2C().register(RESPONSE_DATA, ResponseDataPacket.CODEC);
		PayloadTypeRegistry.playS2C().register(RESPONSE_END, ResponseEndPacket.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(REQUEST_DIAGNOSTICS, RequestDiagnosticsPacket::handle);
	}

	@Environment(EnvType.CLIENT)
	public static void registerClient() {
		ClientPlayNetworking.registerGlobalReceiver(RESPONSE_DATA, ResponseDataPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(RESPONSE_END, ResponseEndPacket::handle);
	}

	private static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> create(String name) {
		return new CustomPacketPayload.Type<>(Canary.id(name));
	}
}

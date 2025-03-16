package one.devos.nautical.canary;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import one.devos.nautical.canary.feature.BlockStateReport;

public class CanaryClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CanaryPackets.registerClient();

		// print the BlockState report on client init, after blocks are registered in main init
		if (Config.INSTANCE.printBlockStateReport())
			BlockStateReport.print();

		ClientCommandRegistrationCallback.EVENT.register(
				(dispatcher, registries) -> dispatcher.register(CanaryClientCommands.build())
		);
	}
}

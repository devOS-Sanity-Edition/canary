package one.devos.nautical.canary.feature.desync;

import com.mojang.logging.LogUtils;

import net.minecraft.core.IdMapper;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class ResponseHandler {
	private static final Logger logger = LogUtils.getLogger();
	private static final List<String> states = new ArrayList<>();

	static boolean awaitingData = false;

	static void accept(List<String> states) {
		if (!validateAwaiting())
			return;

		ResponseHandler.states.addAll(states);
	}

	static void finish() {
		if (!validateAwaiting())
			return;

		awaitingData = false;
		report();
		states.clear();
	}

	private static void report() {
		logger.info("Received BlockState diagnostic info. Report:");
		List<String> clientStates = StreamSupport.stream(Block.BLOCK_STATE_REGISTRY.spliterator(), false)
				.map(BlockState::toString)
				.toList();

		if (states.size() != clientStates.size()) {
			logger.info("Size mismatch! {} vs {}", states.size(), clientStates.size());

			for (String state : states) {
				if (!clientStates.contains(state)) {
					logger.info("Missing on client: {}", state);
				}
			}

			for (String state : clientStates) {
				if (!states.contains(state)) {
					logger.info("Extra on client: {}", state);
				}
			}

			return;
		}

		for (int i = 0; i < states.size(); i++) {
			String serverState = states.get(i);
			String clientState = clientStates.get(i);
			if (!serverState.equals(clientState)) {
				logger.info("Mismatch at #{}: {} vs {}", i, serverState, clientState);
			}
		}

		logger.info("End of report. If there's nothing between this and the start, all is well.");
	}

	private static boolean validateAwaiting() {
		if (!awaitingData) {
			logger.error("Discarding unexpected diagnostic packet");
			return false;
		}

		return true;
	}
}

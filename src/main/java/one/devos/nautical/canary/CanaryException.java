package one.devos.nautical.canary;

public class CanaryException extends RuntimeException {
	public CanaryException(String message) {
		super("[Canary]: " + message);
	}
}

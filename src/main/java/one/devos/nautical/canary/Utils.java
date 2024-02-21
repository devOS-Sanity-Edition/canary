package one.devos.nautical.canary;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public class Utils {
	public static StackTraceElement getCaller() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		// stackTrace[0] = getStackTrace
		// stackTrace[1] = getCaller
		// stackTrace[2] = mixin that called this
		// stackTrace[3] = mixin target
		// stackTrace[4] = actual caller
		return stackTrace[4];
	}

	public static Class<?> getClass(String name) {
		try {
			return Class.forName(name);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	// these methods exist because sometimes getting all fields/methods will end up loading
	// client-only classes on modded classes due to missing annotations.

	public static Field[] getFieldsSafe(Class<?> clazz) {
		return getSafe(clazz::getDeclaredFields, () -> new Field[0]);
	}

	public static Method[] getMethodsSafe(Class<?> clazz) {
		return getSafe(clazz::getDeclaredMethods, () -> new Method[0]);
	}

	private static <T> T getSafe(Supplier<T> supplier, Supplier<T> fallback) {
		try {
			return supplier.get();
		} catch (Throwable e) {
			if (e instanceof ClassNotFoundException || e instanceof NoClassDefFoundError) {
				return fallback.get();
			} else {
				throw new RuntimeException(e);
			}
		}
	}
}

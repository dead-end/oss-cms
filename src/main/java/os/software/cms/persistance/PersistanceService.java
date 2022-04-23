package os.software.cms.persistance;

import java.nio.file.Path;

public class PersistanceService {
	private static Persistance persistance;

	public static void init(final Path home) {
		if (persistance != null) {
			throw new IllegalStateException("Persistance is already initilized!");
		}

		persistance = new Persistance(home);
	}

	public static Persistance getService() {
		if (persistance == null) {
			throw new IllegalStateException("Persistance is not initilized!");
		}

		return persistance;
	}
}

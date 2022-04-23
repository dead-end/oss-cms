package os.software.cms.persistance;

import java.nio.file.Path;

public class PersistanceService {
	private static Persistance persistance;

	public static Persistance init(final Path home) {
		if (persistance != null) {
			throw new IllegalStateException("Service is already initilized!");
		}

		persistance = new Persistance(home);
		return persistance;
	}

	public static Persistance getService() {
		if (persistance == null) {
			throw new IllegalStateException("Service is not initilized!");
		}

		return persistance;
	}
}

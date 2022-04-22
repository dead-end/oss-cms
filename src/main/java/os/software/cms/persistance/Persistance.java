package os.software.cms.persistance;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Persistance {
	private final Path home;

	public Persistance(final Path home) {
		super();
		this.home = home;
	}

	public Reader getReader(final Path relPath) throws PersistanceException {
		try {
			final Path path = checkPath(relPath);
			return new FileReader(path.toFile(), StandardCharsets.UTF_8);
		} catch (final IOException e) {
			throw new PersistanceException("Unable to read path: " + relPath, e);
		}
	}

	public String readString(final Path relPath) throws PersistanceException {
		try {
			final Path path = checkPath(relPath);
			return Files.readString(path);
		} catch (final IOException e) {
			throw new PersistanceException("Unable to read path: " + relPath, e);
		}
	}

	public String[] getChildren(final Path relPath, final NameFilter filter) {
		final Path path = checkPath(relPath);

		final List<String> list = new ArrayList<>();
		final File[] files = path.toFile().listFiles();
		if (files == null) {
			return null;
		}
		for (int i = 0; i < files.length; i++) {
			if (filter == null || filter.accept(files[i].getName())) {
				list.add(files[i].getName());
			}
		}

		return list.toArray(new String[list.size()]);
	}

	private Path checkPath(final Path relPath) {
		final Path result = this.home.resolve(relPath).toAbsolutePath().normalize();
		if (!result.startsWith(this.home)) {
			throw new IllegalArgumentException(String.format("Paths is not valid: %s", relPath));
		}
		return result;
	}
}

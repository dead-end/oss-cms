package os.software.cms;

import java.nio.file.Path;

public class NavItem2 {
	private Path path;
	private String title;
	private String ref;

	public Path getPath() {
		return path;
	}

	public void setPath(final Path path) {
		this.path = path;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(final String ref) {
		this.ref = ref;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("NavItem [path=");
		builder.append(path);
		builder.append(", title=");
		builder.append(title);
		builder.append(", ref=");
		builder.append(ref);
		builder.append("]");
		return builder.toString();
	}
}

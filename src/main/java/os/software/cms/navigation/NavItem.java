package os.software.cms.navigation;

import java.util.ArrayList;
import java.util.List;

public class NavItem {
	private String name;
	private String title;
	private String ref;
	private NavItem parent;
	private final List<NavItem> children = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
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

	public List<NavItem> getChildren() {
		return children;
	}

	public NavItem getParent() {
		return parent;
	}

	public void setParent(final NavItem parent) {
		this.parent = parent;
	}

	public boolean isRoot() {
		return parent == null;
	}

	public String getPath() {
		if (isRoot()) {
			return "index.html";
		}

		final StringBuilder builder = new StringBuilder();
		builder.append(this.name).append(".html");
		addParents(this, builder);

		return builder.toString();
	}

	private void addParents(final NavItem item, final StringBuilder builder) {
		parent = item.getParent();
		if (parent == null || parent.isRoot()) {
			return;
		}

		builder.insert(0, "/");
		builder.insert(0, parent.name);

		addParents(parent, builder);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("NavItem [name=");
		builder.append(name);
		builder.append(", title=");
		builder.append(title);
		builder.append(", ref=");
		builder.append(ref);
		builder.append(", parent=");
		if (parent != null) {
			builder.append(parent.getName());
		}
		builder.append(", children=[");
		for (final NavItem child : this.children) {
			builder.append(child.getName());
			builder.append(",");
		}
		builder.append("]]");
		return builder.toString();
	}

}

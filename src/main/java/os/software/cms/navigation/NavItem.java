package os.software.cms.navigation;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class NavItem {
	private String name;
	private String title;
	private String ref;
	private NavItem parent;
	private final List<NavItem> children = new ArrayList<>();

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getRef() {
		return this.ref;
	}

	public void setRef(final String ref) {
		this.ref = ref;
	}

	public List<NavItem> getChildren() {
		return this.children;
	}

	public NavItem getParent() {
		return this.parent;
	}

	public void setParent(final NavItem parent) {
		this.parent = parent;
	}

	public boolean isRoot() {
		return this.parent == null;
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
		final NavItem parent = item.getParent();
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
		builder.append(this.name);
		builder.append(", title=");
		builder.append(this.title);
		builder.append(", ref=");
		builder.append(this.ref);
		builder.append(", parent=");
		if (this.parent != null) {
			builder.append(this.parent.getName());
		}
		builder.append(", children=[");
		for (final NavItem child : this.children) {
			builder.append(child.getName());
			builder.append(",");
		}
		builder.append("]]");
		return builder.toString();
	}

	public String toJson() {
		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode rootNode = mapper.createObjectNode();
		rootNode.put("name", this.name);
		rootNode.put("title", this.title);
		rootNode.put("ref", this.ref);

		return rootNode.toPrettyString();
	}
}

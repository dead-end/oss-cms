package os.software.cms.navigation;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import os.software.cms.Constants;
import os.software.cms.persistance.Persistance;
import os.software.cms.persistance.PersistanceException;

public class Navigation {
	private static final Logger logger = System.getLogger(Navigation.class.getName());

	private final Path navigation;
	private final Persistance persistance;

	private NavItem root;
	private Map<String, NavItem> map;

	public Navigation(final Persistance persistance) {
		super();
		this.persistance = persistance;
		this.navigation = Paths.get(Constants.PATH_NAVIGATION);
	}

	public void log(final NavItem navItem) {
		logger.log(Level.INFO, "nav item {0} root: {1} path: {2}", navItem, navItem.isRoot(), navItem.getPath());
		for (final NavItem child : navItem.getChildren()) {
			log(child);
		}
	}

	public NavItem getNavItem(final String pathStr) {

		if (pathStr.length() == 0) {
			return this.root;
		}

		final Path path = Paths.get(StringUtils.removeEnd(pathStr, ".html")).normalize();
		final int count = path.getNameCount();
		if (count == 0 || (count == 1 && "index".equals(path.getFileName().toString()))) {
			return this.root;
		}

		NavItem navItem = this.root;

		next: for (final Path pathCur : path) {
			for (final NavItem child : navItem.getChildren()) {
				if (pathCur.getFileName().toString().equals(child.getName())) {
					navItem = child;
					continue next;
				}
			}
			return null;
		}

		return navItem;
	}

	public void readNavTree() throws PersistanceException, IOException {
		final ObjectMapper objectMapper = new ObjectMapper();

		this.map = new HashMap<>();
		this.root = readNavTree(this.navigation, null, objectMapper);

		log(this.root);
		logger.log(Level.INFO, "-------------{0}", this.map.get("blog/blog-1"));
	}

	private NavItem readNavTree(final Path path, final NavItem parent, final ObjectMapper objectMapper)
			throws IOException, PersistanceException {

		final String[] children = this.persistance.getChildren(path, null);
		if (children == null || children.length == 0) {
			return null;
		}

		final NavItem navItem = navItemFromJson(path, children, objectMapper);
		if (parent == null) {
			navItem.setName("");
		} else {
			navItem.setName(path.getFileName().toString());
			parent.getChildren().add(navItem);
		}

		navItem.setParent(parent);

		for (final String child : children) {
			if (!Constants.FILE_NAV.equals(child)) {
				logger.log(Level.INFO, "entry: {0}", child);
				readNavTree(path.resolve(child), navItem, objectMapper);
			}
		}

		this.map.put(navItem.getRef(), navItem);

		return navItem;
	}

	private NavItem navItemFromJson(final Path parent, final String[] children, final ObjectMapper objectMapper)
			throws PersistanceException, IOException {
		for (final String child : children) {
			if (Constants.FILE_NAV.equals(child)) {
				final Path path = parent.resolve(child);
				final String json = this.persistance.readString(path);
				return objectMapper.readValue(json, NavItem.class);
			}
		}
		throw new IllegalArgumentException("No nav file found!");
	}

	public NavItem getNavRoot() {
		return this.root;
	}

	public NavItem getNavByRef(final String ref) {
		return this.map.get(ref);
	}

}

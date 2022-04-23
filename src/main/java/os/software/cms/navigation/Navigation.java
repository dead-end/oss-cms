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
import os.software.cms.persistance.PersistanceService;

public class Navigation {
	private static final Logger logger = System.getLogger(Navigation.class.getName());

	private final Path navigation;

	private NavItem root;
	private Map<String, NavItem> map;

	public Navigation() {
		super();

		this.navigation = Paths.get(Constants.PATH_NAVIGATION);
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

		this.map = new HashMap<>();
		this.root = readNavTree(this.navigation, null, new ObjectMapper());

		traverse(ni -> logger.log(Level.INFO, "nav item {0} root: {1} path: {2}", ni, ni.isRoot(), ni.getPath()));
	}

	private NavItem readNavTree(final Path path, final NavItem parent, final ObjectMapper objectMapper)
			throws IOException, PersistanceException {

		final Persistance persist = PersistanceService.getService();
		final String[] children = persist.getChildren(path, null);
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
		logger.log(Level.INFO, "################ current: {0} parent: {0}", navItem);

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

		final Persistance persist = PersistanceService.getService();

		for (final String child : children) {
			if (Constants.FILE_NAV.equals(child)) {
				final Path path = parent.resolve(child);
				final String json = persist.readString(path);
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

	public void traverse(final NavItemVisite visite) {
		traverse(this.root, visite);
	}

	private void traverse(final NavItem navItem, final NavItemVisite visite) {
		visite.visite(navItem);

		for (final NavItem child : navItem.getChildren()) {
			traverse(child, visite);
		}
	}
}

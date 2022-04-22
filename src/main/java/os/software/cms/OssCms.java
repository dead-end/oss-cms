package os.software.cms;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import os.software.cms.navigation.Navigation;
import os.software.cms.persistance.Persistance;
import os.software.cms.script.Context;
import os.software.cms.script.RenderEngine;

public class OssCms {
	private static final Logger logger = System.getLogger(OssCms.class.getName());

	public static void main(final String[] args) {

		try {

			final Path home = Paths.get(args[0]).toAbsolutePath().normalize();

			final Persistance persistance = new Persistance(home);

			final Navigation navigation = new Navigation(persistance);
			navigation.readNavTree();

			final RenderEngine renderScriptEngine = new RenderEngine(persistance);

			final Context ctx = new Context(persistance, renderScriptEngine, navigation);

			renderScriptEngine.loadDir(Paths.get(Constants.PATH_TEMPLATES));

			final Path out = Paths.get(args[1]);

			String tmp;

			tmp = ctx.render("blog/blog-1", "page", ctx.getDefaultSelector());
			Files.writeString(out.resolve("blog-1.html"), tmp);

			tmp = ctx.render("blogs/blogs", "page", ctx.getDefaultSelector());
			Files.writeString(out.resolve("blogs.html"), tmp);

			tmp = ctx.render("home/home", "page", ctx.getDefaultSelector());
			Files.writeString(out.resolve("index.html"), tmp);

			tmp = ctx.render("about/about", "page", ctx.getDefaultSelector());
			Files.writeString(out.resolve("about.html"), tmp);

			String path;
			path = "";
			logger.log(Level.INFO, "path: {0} found: {1}", path, navigation.getNavItem(path));
			path = "/";
			logger.log(Level.INFO, "path: {0} found: {1}", path, navigation.getNavItem(path));
			path = "index.html";
			logger.log(Level.INFO, "path: {0} found: {1}", path, navigation.getNavItem(path));
			path = "/index.html";
			logger.log(Level.INFO, "path: {0} found: {1}", path, navigation.getNavItem(path));

			path = "about.html";
			logger.log(Level.INFO, "path: {0} found: {1}", path, navigation.getNavItem(path));
			path = "blogs.html";
			logger.log(Level.INFO, "path: {0} found: {1}", path, navigation.getNavItem(path));
			path = "blogs/blog-1.html";
			logger.log(Level.INFO, "path: {0} found: {1}", path, navigation.getNavItem(path));

		} catch (final Exception e) {
			e.printStackTrace();
		}

	}
}

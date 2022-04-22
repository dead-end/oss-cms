package os.software.cms;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.script.ScriptException;

import os.software.cms.navigation.Navigation;
import os.software.cms.script.IRenderScriptEngine;
import os.software.cms.script.RenderScriptEngine;

public class OssCms {
	private static final Logger logger = System.getLogger(OssCms.class.getName());

	public static void main(final String[] args) {

		try {

			final Path home = Paths.get(args[0]).toAbsolutePath().normalize();

			final Navigation navigation = new Navigation(home);
			navigation.readNavTree();

			final IRenderScriptEngine renderScriptEngine = new RenderScriptEngine(home);

			final CmsContext ctx = new CmsContext(home, renderScriptEngine, navigation);

			renderScriptEngine.loadDir(Constants.PATH_TEMPLATES);

			final Path out = Paths.get("/home/senkel/Desktop/oss-cms/tmp");

			String tmp;

			tmp = ctx.render("blog/blog-1", "page", ctx.getDefaultSelector());
			Files.writeString(Paths.get(out.toString(), "blog-1.html"), tmp);

			tmp = ctx.render("blogs/blogs", "page", ctx.getDefaultSelector());
			Files.writeString(Paths.get(out.toString(), "blogs.html"), tmp);

			tmp = ctx.render("home/home", "page", ctx.getDefaultSelector());
			Files.writeString(Paths.get(out.toString(), "home.html"), tmp);

			tmp = ctx.render("about/about", "page", ctx.getDefaultSelector());
			Files.writeString(Paths.get(out.toString(), "about.html"), tmp);

			// System.out.println(ctx.render("blog/blog-1", "page",
			// ctx.getDefaultSelector()));

			// System.out.println(ctx.render("blogs/blogs", "page",
			// ctx.getDefaultSelector()));

			// System.out.println(ctx.render("home/home", "page",
			// ctx.getDefaultSelector()));

			// System.out.println(ctx.render("about/about", "page",
			// ctx.getDefaultSelector()));

			// new Navigation(home).read();

			// navigation.recursive(Paths.get(home.toString(),
			// Constants.PATH_NAVIGATION).toFile(), null);

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

		} catch (ScriptException | IOException | ReflectiveOperationException e) {
			e.printStackTrace();
		}

	}
}

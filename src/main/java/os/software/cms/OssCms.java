package os.software.cms;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;

import os.software.cms.handler.NavigationHandler;
import os.software.cms.navigation.Navigation;
import os.software.cms.navigation.NavigationService;
import os.software.cms.persistance.PersistanceService;
import os.software.cms.script.Context;
import os.software.cms.script.Renderer;

public class OssCms {
	private static final Logger logger = System.getLogger(OssCms.class.getName());

	public static void main(final String[] args) throws Exception {

		final Path home = Paths.get(args[0]).toAbsolutePath().normalize();

		PersistanceService.init(home);

		final Navigation navigation = NavigationService.init();
		navigation.readNavTree();

		final Renderer renderEngine = new Renderer();

		final Context ctx = new Context(renderEngine);

		final Path out = Paths.get(args[1]);

		navigation.traverse(ni -> {

			try {
				final String tmp = renderEngine.render(ni.getRef(), "page", null);
				final Path outPath = out.resolve(ni.getPath());
				if (!outPath.toFile().getParentFile().exists()) {
					outPath.toFile().getParentFile().mkdirs();
				}

				Files.writeString(out.resolve(ni.getPath()), tmp);
			} catch (final Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

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

		final Server server = new Server(8080);
		final Connector connector = new ServerConnector(server);
		server.addConnector(connector);

		// Create a ContextHandler with contextPath.
		final ContextHandler context = new ContextHandler();
		context.setContextPath("/nav");
		context.setHandler(new NavigationHandler(navigation, renderEngine));

		// Link the context to the server.
		server.setHandler(context);

		server.start();
	}
}

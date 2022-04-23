package os.software.cms.handler;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import os.software.cms.navigation.NavItem;
import os.software.cms.navigation.Navigation;
import os.software.cms.script.Renderer;

public class NavigationHandler extends AbstractHandler {
	private static final Logger logger = System.getLogger(NavigationHandler.class.getName());

	private final Navigation navigation;
	private final Renderer renderEngine;

	public NavigationHandler(final Navigation navigation, final Renderer renderEngine) {
		super();
		this.navigation = navigation;
		this.renderEngine = renderEngine;
	}

	@Override
	public void handle(final String target, final Request baseRequest, final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException {

		try {

			final String extension = StringUtils.substringAfterLast(target, ".");
			final String path = StringUtils.substringBeforeLast(target, ".");

			logger.log(Level.INFO, "Target: {0}", target);
			logger.log(Level.INFO, "Path: {0}", path);
			logger.log(Level.INFO, "Extension: {0}", extension);

			final NavItem navItem = this.navigation.getNavItem(path);

			if (navItem != null) {

				logger.log(Level.INFO, "NavItem: {0}", navItem);

				if ("".equals(extension) || "html".equals(extension)) {
					response.setContentType(MimeTypes.Type.TEXT_HTML_UTF_8.asString());
					response.setStatus(HttpServletResponse.SC_OK);

					final String html = this.renderEngine.render(navItem.getRef(), "page", null);
					logger.log(Level.INFO, html);

					response.getWriter().println(html);
					baseRequest.setHandled(true);
					return;
				}

				if ("json".equals(extension)) {
					response.setContentType(MimeTypes.Type.APPLICATION_JSON_UTF_8.asString());
					response.setStatus(HttpServletResponse.SC_OK);

					response.getWriter().println(navItem.toJson());
					baseRequest.setHandled(true);
					return;
				}
			}

		} catch (final Exception e) {
			logger.log(Level.ERROR, "Unable to process path: " + target, e);
			throw new ServletException("Unable to process path: " + target, e);
		}
	}
}
package os.software.cms.renderer;

import os.software.cms.navigation.NavItem;
import os.software.cms.navigation.NavigationService;

public class Context {

	private final Renderer renderScriptEngine;

	public Context(final Renderer renderScriptEngine) {
		this.renderScriptEngine = renderScriptEngine;
	}

	public String render(final String contentId, final String renderTypeStr, final String selector) throws Exception {
		return this.renderScriptEngine.render(contentId, renderTypeStr, selector);
	}

	public NavItem getNavItem(final String path) {
		return NavigationService.getService().getNavItem(path);
	}

	public NavItem getNavRoot() {
		return NavigationService.getService().getNavRoot();
	}

	public NavItem getNavByRef(final String ref) {
		return NavigationService.getService().getNavByRef(ref);
	}

	public String getDefaultSelector() {
		return null;
	}
}

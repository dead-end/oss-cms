package os.software.cms.script;

import os.software.cms.navigation.NavItem;
import os.software.cms.navigation.Navigation;

public class Context {

	private final RenderEngine renderScriptEngine;

	private final Navigation navigation;

	public Context(final RenderEngine renderScriptEngine, final Navigation navigation) {

		this.renderScriptEngine = renderScriptEngine;
		this.navigation = navigation;

		renderScriptEngine.setContext(this);
	}

	public String render(final String contentId, final String renderTypeStr, final String selector) throws Exception {
		return this.renderScriptEngine.render(contentId, renderTypeStr, selector);
		/*
		 * final RendererType renderType = RendererType.valueOf(renderTypeStr);
		 * 
		 * final Content content = new Content(contentId); final String data =
		 * PersistanceManager.getPersistance().readString(content.getJsonPath()); final
		 * String fct = content.getRendererFct(renderType, selector);
		 * 
		 * this.renderScriptEngine.loadScript(content.getRendererPath(renderType,
		 * selector));
		 * 
		 * return this.renderScriptEngine.invokeRenderFct(fct, contentId, data);
		 */
	}

	public NavItem getNavItem(final String path) {
		return this.navigation.getNavItem(path);
	}

	public NavItem getNavRoot() {
		return this.navigation.getNavRoot();
	}

	public NavItem getNavByRef(final String ref) {
		return this.navigation.getNavByRef(ref);
	}

	public String getDefaultSelector() {
		return null;
	}
}

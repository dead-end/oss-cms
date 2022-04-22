package os.software.cms;

import os.software.cms.navigation.NavItem;
import os.software.cms.navigation.Navigation;
import os.software.cms.persistance.Persistance;
import os.software.cms.script.RenderScriptEngine;

public class CmsContext {

	private final Persistance persistance;

	private final RenderScriptEngine renderScriptEngine;

	private final Navigation navigation;

	public CmsContext(final Persistance persistance, final RenderScriptEngine renderScriptEngine,
			final Navigation navigation) {
		this.persistance = persistance;
		this.renderScriptEngine = renderScriptEngine;
		this.navigation = navigation;

		renderScriptEngine.setContext(this);
	}

	private void loadRenderScript(final String contentType, final String type, final String selector) throws Exception {
		this.renderScriptEngine.loadScript(ContentType.getRendererPath(contentType, type, selector));
	}

	public String render(final String id, final String type, final String selector) throws Exception {

		final ContentType contentType = new ContentType(id);

		loadRenderScript(contentType.getContentType(), type, selector);

		final String blog = this.persistance.readString(contentType.getJsonPath());

		final String fct = contentType.getRendererFct(type, selector);

		return this.renderScriptEngine.invokeRenderFct(fct, id, blog);
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

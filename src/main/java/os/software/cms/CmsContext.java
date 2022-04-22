package os.software.cms;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.script.ScriptException;

import os.software.cms.navigation.NavItem;
import os.software.cms.navigation.Navigation;
import os.software.cms.script.IRenderScriptEngine;

public class CmsContext {

	private final Path home;

	private final IRenderScriptEngine renderScriptEngine;

	private final Navigation navigation;

	public CmsContext(final Path home, final IRenderScriptEngine renderScriptEngine, final Navigation navigation) {
		this.home = home;
		this.renderScriptEngine = renderScriptEngine;
		this.navigation = navigation;

		renderScriptEngine.setContext(this);
	}

	private void loadRenderScript(final String contentType, final String type, final String selector)
			throws IOException, ScriptException {
		this.renderScriptEngine.loadScript(ContentType.getRendererPath(contentType, type, selector).toString());
	}

	public String render(final String id, final String type, final String selector)
			throws IOException, ScriptException, NoSuchMethodException {

		final ContentType contentType = new ContentType(id);

		loadRenderScript(contentType.getContentType(), type, selector);

		final String blog = Files.readString(contentType.getJsonPath(this.home));

		final String fct = contentType.getRendererFct(type, selector);

		return this.renderScriptEngine.invokeRenderFct(fct, id, blog);
	}

	public NavItem getNavItem(final String path) {
		return this.navigation.getNavItem(path);
	}

	public NavItem getNavRoot() {
		return this.navigation.getRoot();
	}

	public NavItem getNavByRef(final String ref) {
		return this.navigation.getNavByRef(ref);
	}

	public String getDefaultSelector() {
		return null;
	}
}

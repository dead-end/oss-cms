package os.software.cms.script;

import java.io.Reader;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import javax.script.Invocable;
import javax.script.ScriptEngine;

import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;

import os.software.cms.Constants;
import os.software.cms.RendererType;
import os.software.cms.collections.Content;
import os.software.cms.persistance.PersistanceService;

public class Renderer {

	private static final Logger logger = System.getLogger(Renderer.class.getName());

	private static final NashornScriptEngineFactory factory = new NashornScriptEngineFactory();

	private final ScriptEngine engine;

	private final Set<String> loadedScripts = new HashSet<>();

	public Renderer() throws Exception {
		this.engine = factory.getScriptEngine("--language=es6");

		this.engine.put(Constants.CTX_KEY, new Context(this));

		loadDefaultTemplates();
	}

	private void loadScript(final Path path) throws Exception {
		final Path norm = path.normalize();
		if (!this.loadedScripts.contains(norm.toString())) {
			try (final Reader script = PersistanceService.getService().getReader(path)) {
				this.engine.eval(script);
			}
			this.loadedScripts.add(norm.toString());
			logger.log(Level.INFO, "Loaded: {0}", path);
		}
	}

	private void loadDefaultTemplates() throws Exception {
		final Path dir = Paths.get(Constants.PATH_TEMPLATES);
		final String[] children = PersistanceService.getService().getChildren(dir, n -> n.endsWith(Constants.EX_JS));
		for (final String child : children) {
			loadScript(dir.resolve(child));
		}
	}

	private String invokeRenderFct(final String fctStr, final String id, final String jsonStr) throws Exception {
		logger.log(Level.INFO, "Invoking function: {0} with: {1}", fctStr, id);
		final Invocable invocable = (Invocable) this.engine;

		final Object json = this.engine.eval(Constants.JS_JSON_OBJECT);

		final Object data = invocable.invokeMethod(json, Constants.JS_JSON_PARSE, jsonStr);
		return invocable.invokeFunction(fctStr, id, data).toString();
	}

	public String render(final String contentId, final String renderTypeStr, final String selector) throws Exception {
		final RendererType renderType = RendererType.valueOf(renderTypeStr);

		final Content content = new Content(contentId);
		final String data = PersistanceService.getService().readString(content.getJsonPath());
		final String fct = content.getRendererFct(renderType, selector);

		loadScript(content.getRendererPath(renderType, selector));

		return invokeRenderFct(fct, contentId, data);
	}
}

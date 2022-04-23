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
import os.software.cms.persistance.PersistanceManager;

public class RenderEngine {

	private static final Logger logger = System.getLogger(RenderEngine.class.getName());

	private static final NashornScriptEngineFactory factory = new NashornScriptEngineFactory();

	private final ScriptEngine engine;

	private final Set<String> loadedScripts = new HashSet<>();

	public RenderEngine() {
		this.engine = factory.getScriptEngine("--language=es6");
	}

	public void loadScript(final Path path) throws Exception {
		final Path norm = path.normalize();
		if (!this.loadedScripts.contains(norm.toString())) {
			try (final Reader script = PersistanceManager.getPersistance().getReader(path)) {
				this.engine.eval(script);
			}
			this.loadedScripts.add(norm.toString());
			logger.log(Level.INFO, "Loaded: {0}", path);
		}
	}

	public void loadDefaultTemplates() throws Exception {
		final Path dir = Paths.get(Constants.PATH_TEMPLATES);
		final String[] children = PersistanceManager.getPersistance().getChildren(dir, n -> n.endsWith(".js"));
		for (final String child : children) {
			loadScript(dir.resolve(child));
		}
	}

	public String invokeRenderFct(final String fctStr, final String id, final String jsonStr) throws Exception {
		logger.log(Level.INFO, "Invoking function: {0} with: {1}", fctStr, id);
		final Invocable invocable = (Invocable) this.engine;

		final Object json = this.engine.eval(Constants.JS_JSON_OBJECT);

		final Object data = invocable.invokeMethod(json, Constants.JS_JSON_PARSE, jsonStr);
		return invocable.invokeFunction(fctStr, id, data).toString();
	}

	public void setContext(final Context ctx) {
		this.engine.put(Constants.CTX_KEY, ctx);
	}
}

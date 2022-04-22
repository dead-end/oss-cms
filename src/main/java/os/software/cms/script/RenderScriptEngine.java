package os.software.cms.script;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;

import os.software.cms.CmsContext;
import os.software.cms.Constants;

public class RenderScriptEngine implements IRenderScriptEngine {

	private static final Logger logger = System.getLogger(RenderScriptEngine.class.getName());

	private static NashornScriptEngineFactory factory = new NashornScriptEngineFactory();

	private final ScriptEngine engine;

	private final Set<String> loadedScripts = new HashSet<>();

	private final Path home;

	public RenderScriptEngine(final Path home) {
		this.engine = factory.getScriptEngine("--language=es6");
		this.home = home;
	}

	@Override
	public void loadScript(final String path) throws IOException, ScriptException {
		final String abs = checkPath(path).toString();
		if (!this.loadedScripts.contains(abs.toString())) {
			try (FileReader script = new FileReader(abs.toString())) {
				this.engine.eval(script);
			}
			this.loadedScripts.add(abs.toString());
			logger.log(Level.INFO, "Loaded: {0}", abs);
		}
	}

	private Path checkPath(final String path) throws IOException {
		final Path result = Paths.get(this.home.toString(), path).toAbsolutePath().normalize();
		if (!result.startsWith(this.home)) {
			throw new IllegalArgumentException(String.format("Paths is not valid: %s", path));
		}
		return result;
	}

	@Override
	public void loadDir(final String dirName) throws IOException, ScriptException {
		final Path dirPath = Paths.get(this.home.toString(), dirName);
		final File dirFile = new File(dirPath.toString());
		final File[] jsFiles = dirFile.listFiles((final File pathname) -> pathname.getName().endsWith(".js"));

		for (final File jsFile : jsFiles) {
			loadScript(Paths.get(dirName, jsFile.getName()).toString());
		}
	}

	@Override
	public String invokeRenderFct(final String fctStr, final String id, final String jsonStr)
			throws NoSuchMethodException, ScriptException {

		final Invocable invocable = (Invocable) this.engine;

		final Object json = this.engine.eval(Constants.JS_JSON_OBJECT);
		final Object data = invocable.invokeMethod(json, Constants.JS_JSON_PARSE, jsonStr);

		return invocable.invokeFunction(fctStr, id, data).toString();
	}

	@Override
	public void setContext(final CmsContext ctx) {
		this.engine.put("ctx", ctx);
	}
}

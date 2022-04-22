package os.software.cms.script;

import java.io.IOException;

import javax.script.ScriptException;

import os.software.cms.CmsContext;

public interface IRenderScriptEngine {

	void loadScript(final String path) throws IOException, ScriptException;

	void loadDir(final String dir) throws IOException, ScriptException;

	String invokeRenderFct(final String fctStr, String id, final String jsonStr)
			throws NoSuchMethodException, ScriptException;

	void setContext(final CmsContext ctx);
}

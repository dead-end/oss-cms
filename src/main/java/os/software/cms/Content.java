package os.software.cms;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;

public class Content {

	private final String id;

	public Content(final String id) {
		this.id = id;
	}

	public String getCollection() {
		return Paths.get(this.id).getName(0).toString();
	}

	public Path getJsonPath() {
		return Paths.get(Constants.PATH_COLLECTIONS, this.id + Constants.EXT_JSON);
	}

	public Path getRendererPath(final RendererType rendererType, final String selector) {

		final StringBuilder builder = new StringBuilder();
		builder.append(Constants.PREFIX_RENDER);
		builder.append(".");
		builder.append(rendererType.toString());
		if (selector != null) {
			builder.append(".");
			builder.append(selector);
		}
		builder.append(Constants.EX_JS);

		return Paths.get(Constants.PATH_TEMPLATES, getCollection(), builder.toString());
	}

	public String getRendererFct(final RendererType rendererType, final String selector) {

		//
		// render
		//
		final StringBuilder builder = new StringBuilder();
		builder.append(Constants.PREFIX_RENDER);

		//
		// Page / Component
		//
		builder.append(StringUtils.capitalize(rendererType.toString()));

		//
		// for example: Blog
		//
		final Path path = Paths.get(this.id).getName(0);
		builder.append(StringUtils.capitalize(path.toString()));

		if (selector != null) {
			builder.append(StringUtils.capitalize(selector));
		}

		return builder.toString();
	}
}

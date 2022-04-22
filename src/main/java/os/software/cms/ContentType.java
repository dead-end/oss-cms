package os.software.cms;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;

public class ContentType {

	private final String id;

	public ContentType(final String id) {
		this.id = id;
	}

	public Path getContentTypePath() {
		return Paths.get(Constants.PATH_COLLECTIONS, this.id + ".json");
	}

	public String getContentType() {
		return Paths.get(this.id).getName(0).toString();
	}

	public Path getJsonPath() {
		return Paths.get(Constants.PATH_COLLECTIONS, this.id + ".json");
	}

	public String getRendererFct(final String type, final String selector) {
		final StringBuilder builder = new StringBuilder();
		builder.append(Constants.PREFIX_RENDER);

		builder.append(StringUtils.capitalize(type));

		final Path path = Paths.get(this.id).getName(0);
		builder.append(StringUtils.capitalize(path.toString()));

		if (selector != null) {
			builder.append(StringUtils.capitalize(selector));
		}

		return builder.toString();
	}

	public static Path getRendererPath(final String contentType, final String type, final String selector) {

		final RendererType rendererType = RendererType.valueOf(type);

		final StringBuilder builder = new StringBuilder();
		builder.append(Constants.PREFIX_RENDER);
		builder.append(".");
		builder.append(rendererType.toString());
		if (selector != null) {
			builder.append(".");
			builder.append(selector);
		}
		builder.append(".js");

		return Paths.get(Constants.PATH_TEMPLATES, contentType, builder.toString());
	}

}

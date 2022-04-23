package os.software.cms.renderer;

public class RendererService {

	public static Renderer getService() throws Exception {
		return new Renderer();
	}
}

package os.software.cms.navigation;

public class NavigationService {
	private static Navigation navigation;

	public static Navigation init() {
		if (navigation != null) {
			throw new IllegalStateException("Service is already initilized!");
		}

		navigation = new Navigation();
		return navigation;
	}

	public static Navigation getService() {
		if (navigation == null) {
			throw new IllegalStateException("Service is not initilized!");
		}

		return navigation;
	}
}

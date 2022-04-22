package os.software.cms.persistance;

public class PersistanceException extends Exception {

	private static final long serialVersionUID = 1L;

	public PersistanceException(final String str, final Exception e) {
		super(str, e);
	}
}

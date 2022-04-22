package os.software.cms.persistance;

@FunctionalInterface
public interface NameFilter {

	boolean accept(String name);
}
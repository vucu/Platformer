package platformer.builder;

// Allow objects outside this package to access the services
public class ServicePack {
	static ServiceBuilder sb;

	public static ServiceBuilder services() {
		return sb;
	}
}

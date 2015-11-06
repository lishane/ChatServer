
public class LaunchServer {

	public static void main(String[] args) {
		// Create a ChatServer and start it
		(new ChatServer(new User[0], 200)).run();
	}

}

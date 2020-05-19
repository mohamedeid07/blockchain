package blockchain;

public class MainMalicious {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {

		if (args.length < 1) {
			System.exit(1);
		}

		int port = Integer.valueOf(args[0]);
		MaliciousUser sc = new MaliciousUser(port);
		sc.mine();
	}
}

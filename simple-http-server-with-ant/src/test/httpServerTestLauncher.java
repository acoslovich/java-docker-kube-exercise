
public class httpServerTestLauncher {

	public static void main(String[] args) {
		
		try {
	
			System.out.println("Test start");
			
			httpServerTest hST = new httpServerTest();

			if 	(args.length > 0) {
				hST.setTcpPort(Integer.parseInt(args[0]));
				System.out.println("TCP Port in use: " + args[0]);
			} else {
				System.out.println("Using default TCP Port (" + hST.getTcpPort() + ")");
			}
			
			hST.testPageNotFound();
			hST.testServertime();
			
			System.out.println("Test finished");
			
		} catch (Throwable e) {
			
			e.printStackTrace();
			System.exit(1);

		}
	}

}

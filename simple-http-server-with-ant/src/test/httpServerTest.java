
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.jupiter.api.*;

class httpServerTest {

	private int tcpPort = 8500;
	private String hostName = "localhost";
	
	@BeforeAll
	static void serverStart() {
		
		// Run the server
		httpServerMain.httpServer();

	}

	@Test
	void testServertime() throws Throwable {
		
		// Prepare the request
		HttpURLConnection myConnection = getHttpURLConnection(getURL(hostName, tcpPort, "servertime/"));
		
		// Perform the request and check status code
		performRequestAndCheckResponseCode(myConnection, 200);

		// Get the response content and check it
		BufferedReader bR = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
		String inputLine;
		inputLine = bR.readLine();
		assertNotNull(inputLine, "No respose content");
		long myTimeStamp = Calendar.getInstance().getTimeInMillis();
		long serverTimeStamp = getServerTimestamp(inputLine);	
		long timeStampDelta = Math.abs(serverTimeStamp - myTimeStamp);
		// System.out.println("srv = " + serverTimeStamp);
		// System.out.println("cli = " + myTimeStamp);
		assertTrue((timeStampDelta < 3000), "Server timestamp differs from the local one for more then 3 seconds");
		
		inputLine = bR.readLine();
		assertNull(inputLine, "More then one line in the response content");

		bR.close();
		
		// Close the connection to the server
		myConnection.disconnect();
	}
	
	@Test
	void testPageNotFound() throws Throwable {
		
		// Prepare the request
		HttpURLConnection myConnection = getHttpURLConnection(getURL(hostName, tcpPort, ""));
		
		// Perform the request and check status code
		performRequestAndCheckResponseCode(myConnection, 404);
		
		// Close the connection to the server
		myConnection.disconnect();
	}
	
	private String getURL(String hostname, int tcpport, String resource) throws Throwable {
		return "http://" + hostname + ":" + tcpport + "/" + resource;
	}
	
	private long getServerTimestamp(String htmlstring) throws Throwable {
		String dateTimeFromServer = htmlstring.substring(16,39);
		// System.out.println("|" + dateTimeFromServer + "|");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		return dateFormat.parse(dateTimeFromServer).getTime();
	}

	private HttpURLConnection getHttpURLConnection(String urlstring) throws Throwable {
		URL url = new URL(urlstring);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "text/*");
		connection.setConnectTimeout(5000);
		connection.setReadTimeout(5000);
		return connection;
	}
	
	private void performRequestAndCheckResponseCode(HttpURLConnection connection, int expectedcode) throws Throwable {
		int statusCode = connection.getResponseCode();
		assertEquals(expectedcode, statusCode, "Wrong http response status code");
	}

	public int getTcpPort() {
		return tcpPort;
	}

	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

}

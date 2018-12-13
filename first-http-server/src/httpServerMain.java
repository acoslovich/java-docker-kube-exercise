import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;


public class httpServerMain {

	public static void main(String[] args) {
		HttpServer server = null;
		try {
			server = HttpServer.create(new InetSocketAddress(8500), 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		HttpContext context = server.createContext("/");
		context.setHandler(httpServerMain::handleRequest);
		HttpContext timecontext = server.createContext("/servertime/");
		timecontext.setHandler(httpServerMain::handleTimeRequest);
		server.start();

	}

	private static void handleRequest(HttpExchange exchange) throws IOException {
		String response = "<html><head><title>Not found</title></head><body><h1>Page not found!</h1><p/>The requested resource is not present on this server.</body></html>";
		System.out.println(logMessage("404", exchange.getRequestURI().toString()));
		exchange.sendResponseHeaders(404, response.getBytes().length); //response code and length
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
	
	private static void handleTimeRequest(HttpExchange exchange) throws IOException {
		String htmlPage = "<html><body><h1>" + getCurrentDateTime() + "</h1></body></html>";
		System.out.println(logMessage("200", exchange.getRequestURI().toString()));
	    exchange.sendResponseHeaders(200, htmlPage.getBytes().length); //response code and length
		OutputStream os = exchange.getResponseBody();
		os.write(htmlPage.getBytes());
		os.close();
	}
	
	private static String getCurrentDateTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").format(Calendar.getInstance().getTime());
	}
	
	private static String logMessage(String status, String URI) {
		return getCurrentDateTime() + " - " + status + " - " + URI;
	}

}

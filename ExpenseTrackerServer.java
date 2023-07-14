import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;

public class ExpenseTrackerServer extends NanoHTTPD {
    private ExpenseTracker expenseTracker;

    public ExpenseTrackerServer(int port) {
        super(port);
        expenseTracker = ExpenseTracker.loadExpenseData();
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();

        // Serve index.html
        if (uri.equals("/") || uri.equals("/index.html")) {
            return serveFile("index.html", "text/html");
        }

        // Serve styles.css
        if (uri.equals("/styles.css")) {
            return serveFile("styles.css", "text/css");
        }

        // Serve script.js
        if (uri.equals("/script.js")) {
            return serveFile("script.js", "text/javascript");
        }

        // Handle /recordExpense POST request
        if (uri.equals("/recordExpense") && session.getMethod() == Method.POST) {
            try {
                session.parseBody();
                String requestBody = session.getQueryParameterString();
                // Parse the JSON request body and process the expense data

                // Save the updated expense tracker data
                expenseTracker.saveExpenseData();

                return newFixedLengthResponse("Expense recorded successfully.");
            } catch (IOException | ResponseException e) {
                e.printStackTrace();
                return newFixedLengthResponse("Error occurred while processing the request.");
            }
        }

        // Serve other files or handle other requests as needed

        // Return a 404 Not Found response if the requested resource is not found
        return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "404 Not Found");
    }

    private Response serveFile(String filename, String mimeType) {
        try {
            return newFixedFileResponse(filename, mimeType);
        } catch (IOException e) {
            e.printStackTrace();
            return newFixedLengthResponse("Error serving the file: " + filename);
        }
    }

    public static void main(String[] args) {
        ExpenseTrackerServer server = new ExpenseTrackerServer(8080);
        try {
            server.start();
            System.out.println("Server started at http://localhost:8080/");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error starting the server: " + e.getMessage());
        }
    }
}

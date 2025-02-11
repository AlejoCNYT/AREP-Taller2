package co.edu.eci.framework;

import java.io.*;
import java.net.*;

public class HttpServer
{
    private int port;
    private static Router router = new Router();
    static Request req = new Request("");
    static Response res = new Response();
    static String responseBody = Router.handleGetRequest(req.getPath(), req, res);
    public HttpServer(int port)
    {
        this.port = port;
    }

    public static void start(int port) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);

            while (true)
            {
                Socket clientSocket = serverSocket.accept();
                handleRequest(clientSocket);
            }

    }

    private static void handleClient(Socket clientSocket) throws IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        OutputStream out = clientSocket.getOutputStream();
        PrintWriter writer = new PrintWriter(out, true);

        String line;
        String path = "";
        while  (!(line = in.readLine()).isEmpty())
        {
            if (line.startsWith("GET"))
            {
                path = line.split(" ")[1];
            }
        }

        if (path.startsWith("/static/"))
        {
            String filePath = "src/main/sources/webroot.public" + path.replace("/static", "");
            File file = new File(filePath);

            if (!file.exists())
            {
                writer.println("HTTP/1.1 200 OK");
                writer.println("Content-Type: text/html");
                writer.println();

                BufferedReader fileReader = new BufferedReader(new FileReader(file));
                String fileLine;

                while ((fileLine = fileReader.readLine()) != null)
                {
                    writer.println(fileLine);
                }
                fileReader.close();
            } else
            {
                writer.println("HTTP/1.1 404 Not Found");
                writer.println();
            }
        } else
        {
            String responseBody = Router.handleGetRequest(req.getPath(), req, res);

            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/plain");
            writer.println();
            writer.println(responseBody);
        }

    }

    private static void handleRequest(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Leer la primera línea de la solicitud HTTP
            String requestLine = in.readLine();
            if (requestLine != null) {
                System.out.println("[INFO] Request: " + requestLine);
                String[] parts = requestLine.split(" ");
                if (parts.length < 2) return;

                String method = parts[0];
                String path = parts[1];

                Request req = new Request(path);
                Response res = new Response();
                String responseBody = "";

                // Leer encabezados
                String line;
                int contentLength = 0;
                while (!(line = in.readLine()).isEmpty()) {
                    if (line.startsWith("Content-Length:")) {
                        contentLength = Integer.parseInt(line.split(":")[1].trim());
                    }
                }

                // Leer el cuerpo de la solicitud si es POST
                if ("POST".equals(method) && contentLength > 0)
                {
                    char[] buffer = new char[contentLength];
                    in.read(buffer);
                    req.setBody(new String(buffer));
                    System.out.println("[DEBUG] Handling POST for path: " + path);

                    responseBody = router.handlePostRequest(path, req, res);
                } else if (method.equals("GET")) {
                    responseBody = router.handleGetRequest(path, req, res);
                } else {
                    res.setStatus(400);
                    responseBody = "{\"status\": 400, \"error\": \"Unsupported HTTP method\"}";
                }

                // Enviar respuesta
                res.setBody(responseBody);
                out.print(res.formatResponse());
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Router getRouter()
    {
        return router;
    }
}

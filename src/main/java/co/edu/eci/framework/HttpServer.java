package co.edu.eci.framework;

import java.io.*;
import java.net.*;

public class HttpServer
{
    private int port;
    private Router router = new Router();

    public HttpServer(int port)
    {
        this.port = port;
    }

    public void start()
    {
        try (ServerSocket serverSocket = new ServerSocket(port))
        {
            System.out.println("Server running on port " + port);

            while (true)
            {
                Socket clientSocket = serverSocket.accept();
                handleRequest(clientSocket);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void handleRequest(Socket clientSocket)
    {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true))
        {

            // Leer la primera línea de la solicitud HTTP
            String requestLine = in.readLine();
            if (requestLine != null)
            {
                // System.out.println("Received request: " + requestLine);
                String[] parts = requestLine.split(" ");
                if (parts.length < 2) return;

                String method = parts[0];
                String path = parts[1];

                Request req = new Request(path);
                Response res = new Response();
                String responseBody = "";

                // Detectar y manejar POST:
                if ("POST".equals(method))
                {
                    StringBuilder payload = new StringBuilder();
                    String line;

                    // Leer encabezados
                    while (!(line = in.readLine()).isEmpty()) {
                        System.out.println("[HEADER] " + line); // Agregar logging para depuración
                        System.out.println("[DEBUG] Request Path: " + req.getPath());
                        System.out.println("[DEBUG] Request Body: " + req.getBody());

                    }

                    // Leer el cuerpo de la solicitud
                    while (in.ready()) {
                        payload.append((char) in.read());
                    }

                    req.setBody(payload.toString()); // Verifica que el metodo setBody exista en Request

                    responseBody = router.handlePostRequest(req.getPath(), req, res);
                } else
                {
                    res.setStatus(404);
                    responseBody = "{\"status\": 404, \"error\": \"Unsupported HTTP method\"}";
                }


                if ("GET".equals(method))
                {
                    responseBody = router.handleGetRequest(req.getPath(), req, res);
                } else
                {
                    res.setStatus(400);
                    responseBody = "Unsupported HTTP method.";
                }

                // Responder con un mensaje simple
                res.setBody(responseBody);
                out.print(res.formatResponse());
                out.flush();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                clientSocket.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public Router getRouter()
    {
        return router;
    }
}

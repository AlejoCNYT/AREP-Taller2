package co.edu.eci.framework;

import java.io.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class Router
{
    private Map<String, BiFunction<Request, co.edu.eci.framework.Response, String>> getRoutes = new HashMap<>();
    private String staticFilesDirectory = "";
    private Map<String, BiFunction<Request, Response, String>> postRoutes = new HashMap<>();

    // Resgistrar una ruta POST
    public void addPostRoute(String path, BiFunction<Request, Response, String> handler)
    {
        postRoutes.put(path, handler);
    }

    private String jsonErrorResponse(int statusCode, String message)
    {
        return String.format("{\"status\": %d, \"error\": \"%s\"}", statusCode, message);
    }

    // Manejar solicitudes POST
    public String handlePostRequest(String path, Request req, Response res) {
        if (postRoutes.containsKey(path)) {
            return postRoutes.get(path).apply(req, res);
        }
        res.setStatus(404);
        return "File Not Found";
    }

    // Registrar una ruta GET
    public void addGetRoute(String path, BiFunction<Request, co.edu.eci.framework.Response, String> handler)
    {
        getRoutes.put(path, handler);
    }

    // Configurar el directorio para archivos estáticos
    public void setStaticFilesDirectory(String directory)
    {
        this.staticFilesDirectory = directory;
    }

    // Servir archivos estáticos
    public void serveStaticFile(String path, Response res)
    {
        try
        {
            Path filePath = Paths.get(staticFilesDirectory, path).toAbsolutePath();
            System.out.println("Looking for file: " + filePath);

            if (Files.exists(filePath) && !Files.isDirectory(filePath))
            {
                String fileExtension = getFileExtension(filePath);
                String contentType = getContenType(fileExtension);

                res.setHeader("Content-Type", contentType);
                res.setBody(Files.readString(filePath));
                res.setStatus(200);
            } else
            {
                System.out.println("File not found: " + filePath);
                res.setStatus(404);
                res.setBody("File Not Found");
            }
        } catch (IOException e) {
            res.setStatus(500);
            res.setBody("Internal Server Error");
        }
    }

    private String getContenType(String fileExtension)
    {

        return fileExtension;
    }

    // Buscar y ejecutar el manejador para una ruta específica
    public String handleGetRequest(String path, Request req, Response res) {
        System.out.println("handling GET request for path: " + path);

        if (getRoutes.containsKey(path))
        {
            return getRoutes.get(path).apply(req, res);
        }

        // Intentar servir archivos estáticos
        if (!staticFilesDirectory.isEmpty())
        {
            serveStaticFile(path.substring(1), res);
            return res.formatResponse();
        }

        if (!getRoutes.containsKey(path))
        {
            res.setStatus(404);
            return jsonErrorResponse(404, "Resource Not Found");
        }

        res.setStatus(404);
        return "Error 404: Resource Not Found";
    }

    private String getFileExtension(Path filePath)
    {
        String fileName = filePath.getFileName().toString();
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private String getContentType(String extension)
    {
        return  switch (extension)
        {
            case "html" -> "text/html";
            case "css" -> "text/css";
            case "js" -> "application/javascript";
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            default -> "application/octet-stream";
        };
    }

}

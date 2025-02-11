package co.edu.eci.framework;

import java.io.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class Router
{
    private static final Map<String, BiFunction<Request, Response, String>> getRoutes = new HashMap<>();
    private static final Map<String, BiFunction<Request, Response, String>> postRoutes = new HashMap<>();
    private static String staticFilesDirectory = "src/main/resources/webroot.public";

    // Configurar el directorio para archivos estáticos
    public static void staticfiles(String directory)
    {
        staticFilesDirectory = directory;
    }

    // Registrar una ruta GET
    public static void get(String path, BiFunction<Request, Response, String> handler)
    {
        getRoutes.put(path, handler);
    }

    // Registrar una ruta POST
    public static void post(String path, BiFunction<Request, Response, String> handler)
    {
        postRoutes.put(path, handler);
    }

    // Manejar una solicitud GET
    public static String handleGetRequest(String path, Request req, Response res)
    {
        System.out.println("Handling GET request for path: " + path);

        if (path == null || path.isEmpty())
        {
            res.setStatus(400);
            return jsonErrorResponse(400, "Invalid request path");
        }

        // Si la ruta existe en las rutas GET, ejecutar su manejador
        if (getRoutes.containsKey(path))
        {
            return getRoutes.get(path).apply(req, res);
        }

        // Intentar servir archivos estáticos
        if (!staticFilesDirectory.isEmpty())
        {
            String filePath = path.startsWith("/") ? path.substring(1) : path;
            serveStaticFile(filePath, res);
            return res.formatResponse();
        }

        // Si no se encontró la ruta, responder con un 404
        res.setStatus(404);
        return jsonErrorResponse(404, "Resource Not Found");
    }

    // Manejar una solicitud POST
    public static String handlePostRequest(String path, Request req, Response res)
    {
        System.out.println("Handling POST request for path: " + path);

        if (postRoutes.containsKey(path))
        {
            return postRoutes.get(path).apply(req, res);
        }
        System.out.println("Route not found for POST: " + path);
        res.setStatus(404);
        return jsonErrorResponse(404, "Resource Not Found");
    }

    // Servir archivos estáticos
    public static void serveStaticFile(String path, Response res)
    {
        try
        {
            Path filePath = Paths.get(staticFilesDirectory, path).toAbsolutePath();
            System.out.println("Looking for file: " + filePath);

            if (Files.exists(filePath) && !Files.isDirectory(filePath))
            {
                String fileExtension = getFileExtension(filePath);
                String contentType = getContentType(fileExtension);

                res.setHeader("Content-Type", contentType);
                res.setBody(Files.readString(filePath));
                res.setStatus(200);
            }
            else
            {
                System.out.println("File not found: " + filePath);
                res.setStatus(404);
                res.setBody("File Not Found");
            }
        }
        catch (IOException e)
        {
            res.setStatus(500);
            res.setBody("Internal Server Error");
        }
    }

    // Obtener la extensión de un archivo
    private static String getFileExtension(Path filePath)
    {
        String fileName = filePath.getFileName().toString();
        return fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".") + 1) : "";
    }

    // Obtener el tipo de contenido basado en la extensión del archivo
    private static String getContentType(String extension)
    {
        return switch (extension)
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

    // Respuesta de error en formato JSON
    private static String jsonErrorResponse(int statusCode, String message)
    {
        return String.format("{\"status\": %d, \"error\": \"%s\"}", statusCode, message);
    }
}

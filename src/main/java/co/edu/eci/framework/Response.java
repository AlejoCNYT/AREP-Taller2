package co.edu.eci.framework;

import java.util.HashMap;
import java.util.Map;
public class Response {

    private int statusCode = 200;
    private Map<String, String> headers = new HashMap<>();
    private String body;

    public Response()
    {
        headers.put("Content-Type", "text/plain"); // Valor por defecto
    }

    public void setStatus(int statusCode)
    {
        this.statusCode = statusCode;
    }

    public void setHeader(String key, String value)
    {
        headers.put(key, value);
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public String formatResponse()
    {
        StringBuilder response = new StringBuilder();
        response.append("HTTP/1.1 ").append(statusCode).append(" ").append(getStatusMessage()).append("\r\n");

        for (Map.Entry<String, String> header : headers.entrySet())
        {
            response.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
        }

        response.append("\r\n").append(body);
        return response.toString();

    }

    private String getStatusMessage()
    {
        return switch (statusCode)
        {
            case 200 -> "Ok";
            case 400 -> "Bad Request";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            default -> "Unknown";
        };
    }

}

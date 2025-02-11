package co.edu.eci.framework;

import java.util.HashMap;
import java.util.Map;

public class Request
{
    private String path;
    private Map<String, String> queryParams = new HashMap<>();
    private String body;

    public Request(String url)
    {
        String[] parts = url.split("\\?");
        this.path = parts[0];

        if (parts.length > 1)
        {
            for ( String param : parts[1].split("&"))
            {
                String[] keyValue = parts[1].split("=");
                if (keyValue.length == 2)
                {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
    }

    private void parseUrl(String url) {
        String[] parts = url.split("\\?");
        this.path = parts[0];

        if (parts.length > 1) {
            String[] params = parts[1].split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public String getBody ()
    {
        return body;
    }

    public String getPath()
    {
        return path;
    }

    public String getValue(String key) {
        return queryParams.getOrDefault(key, null);
    }

}

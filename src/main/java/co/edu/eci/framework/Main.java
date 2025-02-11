package co.edu.eci.framework;

import java.io.IOException;
import java.util.Random;

import static co.edu.eci.framework.Router.staticfiles;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(8080);

        // Configurar archivos estáticos
        Router.staticfiles("src/main/resources/webroot.public");

        // Definir rutas GET
        server.getRouter().get("/hello", (req, res) -> "Hello " + req.getValue("name"));
        server.getRouter().get("/pi", (req, res) -> String.valueOf(Math.PI));

        server.getRouter().get("/greet", (req, res) -> {
            String name = req.getValue("name");
            if (name == null) {
                res.setStatus(400);
                return "Missing 'name' parameter";
            }
            return "Hello, " + name + "!";
        });

        server.getRouter().get("/user", (req, res) -> {
            res.setHeader("Content-Type", "application/json");
            return "{ \"id\": 1, \"name\": \"Alice\" }";
        });

        server.getRouter().get("/random", (req, res) -> {
            int randomNumber = new Random().nextInt(100) + 1;
            return "Random Number: " + randomNumber;
        });

        server.getRouter().get("/convert", (req, res) -> {
            String celsiusStr = req.getValue("celsius");
            if (celsiusStr == null) {
                res.setStatus(400);
                return "Missing 'celsius' parameter";
            }

            try {
                double celsius = Double.parseDouble(celsiusStr);
                double fahrenheit = (celsius * 9 / 5) + 32;
                return "Temperature in Fahrenheit: " + fahrenheit;
            } catch (NumberFormatException e) {
                res.setStatus(400);
                return "Invalid temperature format";
            }
        });

        // Definir rutas POST
        server.getRouter().post("/submit", (req, res) -> {
            String data = req.getBody();
            res.setHeader("Content-Type", "application/json");
            return "{\"message\": \"Data received\", \"data\": \"" + data + "\"}";
        });

        // Iniciar servidor
        HttpServer.start(8080);
    }
}

package co.edu.eci.framework;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        HttpServer server = new HttpServer(8080);

        // Configurar archivos estáticos
        server.getRouter().setStaticFilesDirectory("src/main/resources/webroot/public");

        // Definir rutas GET
        server.getRouter().addGetRoute("/hello", (req, res) -> "Hello" + req.getValue("name"));
        server.getRouter().addGetRoute("/pi", (req, res) -> String.valueOf(Math.PI));

        // System.out.println("Registered routes: " + server.getRouter().getRoutes());

        server.getRouter().addGetRoute("/greet", (req, res) -> {
            String name = req.getValue("name");
            if (name == null) {
                res.setStatus(400);
                return "Missing 'name' parameter";
            }
            return "Hello, " + name + "!";
        });

        server.getRouter().addGetRoute("/user", (req, res) -> {
            res.setHeader("Content-Type", "application/json");
            return "{ \"id\": 1, \"name\": \"Alice\" }";
        });

        server.getRouter().addGetRoute("/random", (req, res) -> {
            int randomNumber = new Random().nextInt(100) + 1;
            return "Random Number: " + randomNumber;
        });

        server.getRouter().addGetRoute("/convert", (req, res) -> {
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

        server.getRouter().addPostRoute("/submit", (req, res) -> {
            String data = req.getBody();
            return "{\"message\": \"Data received\", \"data\": \"" + data + "\"}";
        });

        // Iniciar servidor
        server.start();

    }
}

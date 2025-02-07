# Web Framework for REST Services and Static File Management

## Overview
This project enhances an existing web server by converting it into a lightweight web framework for Java. The framework allows developers to create REST services using lambda functions, manage query parameters, and serve static files easily.

## Features
1. **GET Method for REST Services**
   - Define REST services using lambda functions.
   - Example:
     ```java
     get("/hello", (req, res) -> "hello world!");
     ```

2. **Query Parameter Extraction**
   - Access query values in requests.
   - Example:
     ```java
     get("/hello", (req, res) -> "Hello " + req.getValues("name"));
     ```

3. **Static File Serving**
   - Specify the location of static files.
   - Example:
     ```java
     staticfiles("webroot/public");
     ```

4. **POST Method Support**
   - Receive data from clients.
   - Example:
     ```java
     post("/submit", (req, res) -> {
         return "Received: " + req.getBody();
     });
     ```

5. **Logging**
   - Logs each request to the console for debugging.

6. **Improved Error Handling**
   - Returns JSON-formatted error responses instead of plain text.

## Architecture
### Components
- **HttpServer:** Handles incoming HTTP requests and routes them accordingly.
- **Router:** Manages mapping of URLs to their corresponding handlers.
- **Request:** Parses HTTP requests, including query parameters and body data.
- **Response:** Manages response status codes and headers.

### How It Works
1. The `HttpServer` listens for incoming HTTP connections.
2. Requests are parsed into `Request` objects.
3. The `Router` checks if a handler exists for the request.
4. If a route matches, the corresponding lambda function executes.
5. The `Response` object sends back the appropriate HTTP response.
6. If no route matches, a `404` JSON response is returned.

## Installation & Usage
### Prerequisites
- Java 17+
- Maven

### Setup
1. **Clone the repository:**
   ```sh
   git clone <repository-url>
   cd web-framework
   ```

2. **Build the project:**
   ```sh
   mvn clean package
   ```

3. **Run the server:**
   ```sh
   java -jar target/web-framework.jar
   ```

### Example Usage
```java
public static void main(String[] args) {
    staticfiles("/webroot");
    get("/hello", (req, res) -> "Hello " + req.getValues("name"));
    get("/pi", (req, res) -> String.valueOf(Math.PI));
    post("/submit", (req, res) -> "Received: " + req.getBody());
}
```

## Testing
### Using cURL
1. **GET Request:**
   ```sh
   curl http://localhost:8080/hello?name=Pedro
   ```
2. **POST Request:**
   ```sh
   curl -X POST -d "sample data" http://localhost:8080/submit
   ```
3. **Static File Request:**
   ```sh
   curl http://localhost:8080/index.html
   ```

### Using a Web Browser
- Open `http://localhost:8080/index.html` to test static file serving.
- Try `http://localhost:8080/hello?name=Pedro` to test REST endpoints.

## Project Structure
```
web-framework/
├── src/
│   ├── main/java/com/example/framework/
│   │   ├── HttpServer.java
│   │   ├── Router.java
│   │   ├── Request.java
│   │   ├── Response.java
│   ├── resources/webroot/
│       ├── index.html
├── pom.xml
├── README.md
```

## Future Enhancements
- Support for additional HTTP methods (PUT, DELETE).
- Middleware support for authentication and logging.
- JSON parsing for structured request bodies.

## License
This project is licensed under the MIT License.


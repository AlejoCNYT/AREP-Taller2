# Web Framework for REST Services and Static File Management

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

3. **Run HttpService.class, and go to http://localhost:8080/[resource]:**
   ```sh
   mvn clean package
   ```

   - Example: http://localhost:8080/convert?celsius=25 to test /convert:
      Temperature in Fahrenheit: 77.0

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

   
      
      <!DOCTYPE html>
      <html lang="en">
      <head>
          <meta charset="UTF-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
          <title>My Java Web Framework</title>
          <link rel="stylesheet" href="styles.css">
      </head>
      <body>
      <h1>Welcome to My Web Framework</h1>
      <p>This is a simple static page served by our Java web framework.</p>
      
      <button onclick="fetchHello()">Say Hello</button>
      <p id="helloMessage"></p>
      
      <button onclick="fetchPi()">Get PI</button>
      <p id="piValue"></p>
      
      <script src="script.js"></script>
      </body>
      </html>


### Using a Web Browser
- Open `http://localhost:8080/random` to test static file serving.
      Random Number: [#]
- Try `http://localhost:8080/hello?name=Pedro` to test REST endpoints.
      HelloPedro


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
│       ├── styles.css
│       ├── script.js
│   ├── resources/webroot/images/
│       ├── background.png
│       ├── logo.png
├── pom.xml
├── README.md
```

## Daniel Alejandro Acero

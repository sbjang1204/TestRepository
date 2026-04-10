package org.example;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", new IndexHandler());
        server.createContext("/styles.css", new ResourceHandler("styles.css", "text/css; charset=UTF-8"));
        server.createContext("/script.js", new ResourceHandler("script.js", "application/javascript; charset=UTF-8"));
        server.createContext("/pig/", new PigHandler());
        server.setExecutor(null);
        server.start();

        System.out.println("Server started: http://localhost:" + port);
    }

    private static final class ResourceHandler implements HttpHandler {
        private final String resourceName;
        private final String contentType;

        private ResourceHandler(String resourceName, String contentType) {
            this.resourceName = resourceName;
            this.contentType = contentType;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return;
            }

            byte[] response = readResource(resourceName);
            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", contentType);
            exchange.sendResponseHeaders(200, response.length);

            try (OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(response);
            }
        }

        private byte[] readResource(String name) throws IOException {
            try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(name)) {
                if (inputStream == null) {
                    throw new IOException("Resource not found: " + name);
                }
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                byte[] chunk = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(chunk)) != -1) {
                    buffer.write(chunk, 0, bytesRead);
                }
                return buffer.toByteArray();
            }
        }
    }

    private static final class IndexHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (!"/".equals(path)) {
                byte[] response = "Not Found".getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(404, response.length);
                try (OutputStream outputStream = exchange.getResponseBody()) {
                    outputStream.write(response);
                }
                return;
            }

            new ResourceHandler("index.html", "text/html; charset=UTF-8").handle(exchange);
        }
    }

    private static final class PigHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return;
            }

            String path = exchange.getRequestURI().getPath();
            String resourcePath = path.startsWith("/") ? path.substring(1) : path;
            String contentType = detectContentType(resourcePath);
            new ResourceHandler(resourcePath, contentType).handle(exchange);
        }

        private String detectContentType(String resourcePath) {
            if (resourcePath.endsWith(".png")) {
                return "image/png";
            }
            if (resourcePath.endsWith(".webp")) {
                return "image/webp";
            }
            if (resourcePath.endsWith(".jfif") || resourcePath.endsWith(".jpg") || resourcePath.endsWith(".jpeg")) {
                return "image/jpeg";
            }
            return "application/octet-stream";
        }
    }
}

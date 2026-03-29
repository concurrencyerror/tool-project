package com.horace.toolbackend.service;

import com.horace.toolbackend.exception.HttpRequestException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;

@Service
public class JdkHttpClientService {
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    private final HttpClient httpClient;

    public JdkHttpClientService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String get(String url) {
        return get(url, Collections.emptyMap(), DEFAULT_TIMEOUT);
    }

    public String get(String url, Map<String, String> headers) {
        return get(url, headers, DEFAULT_TIMEOUT);
    }

    public String get(String url, Map<String, String> headers, Duration timeout) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .GET();

        applyHeaders(builder, headers);

        HttpRequest request = builder.build();
        return send(request);
    }

    public String postJson(String url, String json) {
        return postJson(url, json, Collections.emptyMap(), DEFAULT_TIMEOUT);
    }

    public String postJson(String url, String json, Map<String, String> headers) {
        return postJson(url, json, headers, DEFAULT_TIMEOUT);
    }

    public String postJson(String url, String json, Map<String, String> headers, Duration timeout) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .header("Content-Type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(json == null ? "" : json));

        applyHeaders(builder, headers);

        HttpRequest request = builder.build();
        return send(request);
    }

    public String postForm(String url, String formBody, Map<String, String> headers, Duration timeout) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(formBody == null ? "" : formBody));

        applyHeaders(builder, headers);

        HttpRequest request = builder.build();
        return send(request);
    }

    public String request(String method,
                          String url,
                          String body,
                          Map<String, String> headers,
                          Duration timeout) {

        String upperMethod = method == null ? "GET" : method.trim().toUpperCase();

        HttpRequest.BodyPublisher bodyPublisher =
                (body == null || body.isEmpty())
                        ? HttpRequest.BodyPublishers.noBody()
                        : HttpRequest.BodyPublishers.ofString(body);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout == null ? DEFAULT_TIMEOUT : timeout)
                .method(upperMethod, bodyPublisher);

        applyHeaders(builder, headers);

        HttpRequest request = builder.build();
        return send(request);
    }

    private void applyHeaders(HttpRequest.Builder builder, Map<String, String> headers) {
        if (headers == null || headers.isEmpty()) {
            return;
        }

        headers.forEach((k, v) -> {
            if (k != null && !k.isBlank() && v != null) {
                builder.header(k, v);
            }
        });
    }

    private String send(HttpRequest request) {
        try {
            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            int statusCode = response.statusCode();
            String responseBody = response.body();

            if (statusCode >= 200 && statusCode < 300) {
                return responseBody;
            }

            throw new HttpRequestException(
                    "HTTP request failed, status=" + statusCode,
                    statusCode,
                    responseBody
            );
        } catch (IOException e) {
            throw new HttpRequestException("HTTP request IO exception", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HttpRequestException("HTTP request interrupted", e);
        }
    }
}

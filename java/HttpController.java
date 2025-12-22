import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.net.http.*;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HttpController {
    private final HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
    private HttpRequest.Builder reqBuilder = HttpRequest.newBuilder();
    private HttpResponse<String> resp;

    public HttpController() {}

    public HttpResponse<String> getResponse() {
        return resp;
    }

    public void setResponse(HttpResponse<String> newResp) {
        resp = newResp;
    }

    public HttpRequest.Builder getRequestBuilder() {
        return reqBuilder;
    }

    public void setRequestBuilder(HttpRequest.Builder newReqBuilder) {
        reqBuilder = newReqBuilder;
    }

    public void resetRequestData() {
        reqBuilder = HttpRequest.newBuilder();
    }

    public void addHeader(String key, String value) {
        reqBuilder.header(key, value);
    }

    public void setHeader(String key, String value) {
        reqBuilder.setHeader(key, value);
    }

    public HttpResponse<Path> makeFileRequest(HttpRequest request, String filePath) {
        Path target = Paths.get(filePath);
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofFile(target));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public HttpResponse<String> makeStringRequest(HttpRequest request) {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void modifyRequest(String url, String method, String body) {
        reqBuilder.uri(URI.create(url));
        switch(method) {
            case "GET":
                break;
            case "HEAD":
                reqBuilder.HEAD();
                break;
            case "POST":
                if (body != null) {
                    reqBuilder.POST(HttpRequest.BodyPublishers.ofString(body));
                } else {
                    reqBuilder.POST(HttpRequest.BodyPublishers.noBody());
                }
                break;
            case "PUT":
                if (body != null) {
                    reqBuilder.PUT(HttpRequest.BodyPublishers.ofString(body));
                } else {
                    reqBuilder.PUT(HttpRequest.BodyPublishers.noBody());
                }
                break;
            case "DELETE":
                reqBuilder.DELETE();
                break;
            default:
                if (body != null) {
                    reqBuilder.method(method, HttpRequest.BodyPublishers.ofString(body));
                } else {
                    reqBuilder.method(method, HttpRequest.BodyPublishers.noBody());
                }
        }
    }

    public HttpRequest fromURL(String url, String method, String body) {
        modifyRequest(url, method, body);
        return reqBuilder.build();
    }

    public HttpRequest fromGitHub(String url, String method, String body, String auth) {
        modifyRequest(url, method, body);
        setHeader("Accept", "application/vnd.github+json");
        if (auth != null) {
            setHeader("Authorization", "Bearer " + auth);
        }
        setHeader("X-GitHub-Api-Version", "2022-11-28");
        return reqBuilder.build();
    }
}

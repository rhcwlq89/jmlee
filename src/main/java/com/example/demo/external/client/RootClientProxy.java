package com.example.demo.external.client;

import com.example.demo.ResultDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
public class RootClientProxy {
    private final String serverName;
    private final String domain;
    private final Integer timeoutSecond;
    private final ExecutorService executorService;
    private final HttpClient httpClient;
    private final ObjectMapper externalLificObjectMapper;

    public RootClientProxy(String serverName, String domain, Integer timeoutSecond, Integer maxThread) {
        this.serverName = serverName;
        this.domain = domain;
        this.timeoutSecond = timeoutSecond;
        this.executorService = Executors.newFixedThreadPool(maxThread);
        this.httpClient = HttpClient.newBuilder()
                .executor(this.executorService)
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(timeoutSecond))
                .build();

        var objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        objectMapper.registerModule(new JavaTimeModule());
        this.externalLificObjectMapper = objectMapper;
    }

	private URI getURI(String uri) {
        return URI.create(this.domain + uri);
	}

	private HttpRequest.Builder getBuilder() {
		return HttpRequest.newBuilder()
				       .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
	}

	public HttpRequest.Builder get(String uri) {
        return getBuilder()
                .GET()
                .uri(getURI(uri));
	}

	protected HttpResponse<String> sendRequest(HttpRequest req) {
		logSendRequest(req);

		try {
            return httpClient.send(
                    req,
                    HttpResponse.BodyHandlers.ofString()
            );
        } catch (InterruptedException | IOException e) {
			//서버 미응답, response 못 받았을 때 발생.
			log.error("[sendRequest] error : {}", e.getMessage());
			return null;
		}
	}

	private void logSendRequest(HttpRequest req) {
		try {
			log.debug("[{}] External 호출, url : {}", this.serverName, req.uri().toURL().toString());
		} catch (MalformedURLException e) {
			log.warn("[logSendRequest] 로깅 : {}", e.getMessage());
		}
	}

	protected ResultDto getBody(HttpRequest req, String body) {
        try {
            log.debug("[RootClientProxy {}] External 응답 body : {}", this.serverName, body);

            var httpResponse = externalLificObjectMapper.readValue(body, ResultDto.class);

            if (Objects.isNull(httpResponse)) {
                log.error("[RootClientProxy] getBody() Response 응답값 오류 URL : {} body : {}", req.uri().toURL(), body);
            }

            return httpResponse;
        } catch (JsonProcessingException | MalformedURLException e) {
            log.error("[RootClientProxy] Json error : {}", e.getMessage());
            return null;
        }
    }

    public String getResponseString(HttpRequest request) {
        var httpResponse = sendRequestAndGetStringBody(request);
        if (Objects.isNull(httpResponse)) {
            return null;
        }

        return httpResponse;
    }

    public <T> T getResponse(HttpRequest req, TypeReference<T> type) {
        var httpResponse = sendRequestAndGetBody(req);
        if (Objects.isNull(httpResponse)) {
            return null;
        }

        return externalLificObjectMapper.convertValue(httpResponse, type);
    }

    protected String sendRequestAndGetStringBody(HttpRequest request) {
        HttpResponse<String> response = sendRequest(request);

        //결과가 200이 아니면
        if (response.statusCode() != HttpStatus.OK.value()) {
            log.error("[sendRequestAndGetBody] http status code : {}", response.statusCode());
            return null;
        }

        return response.body();
    }


    protected ResultDto sendRequestAndGetBody(HttpRequest req) {
        HttpResponse<String> res = sendRequest(req);

        //결과가 200이 아니면
        if (res.statusCode() != HttpStatus.OK.value()) {
            log.error("[sendRequestAndGetBody] http status code : {}", res.statusCode());
            return null;
        }

        return getBody(req, res.body());
    }
}

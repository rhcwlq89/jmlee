package com.example.demo.external.client.naver;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface NaverClient {
    Document list(String keyword) throws IOException;

    Document next(String keyword, Long page) throws IOException;
}

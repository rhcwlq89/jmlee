package com.example.demo.external.client.naver;

import com.example.demo.ResultDto;
import com.example.demo.external.client.RootClientProxy;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;


@Component
@RequiredArgsConstructor
@Slf4j
public class NaverClientImpl implements NaverClient{

    private static final String SEARCH_NAVER_URL = "https://search.naver.com/search.naver?nso=&where=blog&query=";
    private static final String NEXT_SEARCH_QUERY = "&nx_search_query=";

    private final RootClientProxy naverRootClientProxy;

    @Override
    public Document list(String keyword) throws IOException {
        Connection connect = Jsoup.connect(SEARCH_NAVER_URL + keyword + "&sm=tab_opt");
        return connect.get();
    }

    @Override
    public Document next(String keyword, Long page) throws IOException {
        Connection connect = Jsoup.connect(SEARCH_NAVER_URL + keyword + "&start=" + page);
        return connect.get();
    }
}

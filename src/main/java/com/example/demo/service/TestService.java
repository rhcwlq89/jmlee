package com.example.demo.service;

import com.example.demo.ResultDto;
import com.example.demo.external.client.munhoyoung.MunHoYoungClient;
import com.example.demo.external.client.naver.NaverClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestService {

    private final MunHoYoungClient munHoYoungClient;
    private final NaverClient naverClient;

    private static final String NAVER_BLOG_STRING = "https://blog.naver.com/";
    private static final String TOTAL_TIT = "total_tit";
    private static final String TAG_HREF = "href";
    private static final String SPLITTER  = "/";

    public Set<String> requestNaver(String keyword) {
        try{
            Document list = naverClient.list(keyword);
            return search(list);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void requestNext(String keyword, Long start) {
        try{
            Document list = naverClient.next(keyword, start);
            search(list);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private Set<String> search(Document next) {
        Set<String> set = new HashSet<>();
        Elements total_tit = next.getElementsByClass(TOTAL_TIT);
        total_tit.forEach(element -> {
            String href = element.attr(TAG_HREF);

            if(href.startsWith(NAVER_BLOG_STRING)) {
                String id = href.split(SPLITTER)[3];
                ResultDto list = munHoYoungClient.list(id);
                set.add(list.getRow());
            }
        });

        return set;
    }


}

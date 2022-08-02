package com.example.demo.external.client.munhoyoung;


import com.example.demo.ResultDto;
import com.example.demo.external.client.RootClientProxy;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.net.http.HttpRequest;

@Component
@RequiredArgsConstructor
@Slf4j
public class MunHoYoungClientImpl implements MunHoYoungClient{

    private final RootClientProxy munHoYoungRootClientProxy;

    @Override
    public ResultDto list(String id){
        HttpRequest.Builder builder = munHoYoungRootClientProxy.get("/_ajax_blog_visit.php?blogid=" + id + "&_=1656147298020");
        ResultDto body = munHoYoungRootClientProxy.getResponse(builder.build(), new TypeReference<>() {
        });

        if (ObjectUtils.isEmpty(body)) {
            return null;
        }

        return body;
    }
}

package com.common.config;

import com.common.interceptor.IntfRestTemplateInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class IntfRestTemplateAutoConfiguration {

    private TokenRestTemplate tokenRestTemplate=new TokenRestTemplate();
    @Resource
    private IntfRestTemplateInterceptor intfRestTemplateInterceptor;

    @Bean
    public TokenRestTemplate tokenRestTemplate(){
        List<ClientHttpRequestInterceptor> interceptors = this.tokenRestTemplate.getInterceptors();
        if(CollectionUtils.isEmpty(interceptors)){
            interceptors=new ArrayList<>();
        }
        interceptors.add(intfRestTemplateInterceptor);
        this.tokenRestTemplate.setInterceptors(interceptors);

        return this.tokenRestTemplate;
    }

    @Bean
    public IntfRestTemplateInterceptor intfRestTemplateInterceptor(@Value("${intf.appCode}")String appCode,
                                                                   @Value("${intf.authCode}")String authCode,
                                                                   @Value("${intf.url}")String url){
        return new IntfRestTemplateInterceptor(appCode,authCode,url);
    }
}

//RestTemplate를 Spring이 관리하는 개체로 등록
//카카오 API 호출할 때 HTTP 요청 보내는도구
//빈(BEAN): spring이 생성하고 관리하는 객체, @Autowired나 @requiredArgsConstructor로 어디서든 가져다 쓸 수 있음
//빈 등록 = 도구를 공구함에 넣어두기
//가져다 쓰기 == 공구함에서 꺼내 쓰기

package com.maeum.maeum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

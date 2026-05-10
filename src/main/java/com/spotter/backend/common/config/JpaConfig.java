package com.spotter.backend.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// @EnableJpaAuditing을 별도 클래스로 분리 — @WebMvcTest 슬라이스 테스트에서 로드되지 않도록 함
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}

package ninjasul.config.auth;

import lombok.RequiredArgsConstructor;
import ninjasul.domain.user.Role;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @EnableWebSecurity : Spring Security 설정
 */
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            /**
             * 2. h2-console 화면을 사용하기 위한 옵션
             */
            .csrf().disable().headers().frameOptions().disable()
            .and()
            /**
             * 3. URL 별 권한 처리
             * antMatchers(getStaticResourceUrls()): 정적 리소스 파일 요청 시 무조건 허용
             * antMatchers("/api/v1/**"): /api/v1 으로 시작하는 API 호출 시 USER 권한을 가진 사용자만 이용 가능
             * anyRequest(): 설정 값 이의외 나머지 URL에 대한 처리
             */
            .authorizeRequests()
            .antMatchers(getStaticResourceUrls()).permitAll()
            .antMatchers("/api/v1/**").hasRole(Role.USER.name())
            .anyRequest().authenticated()
            /**
             * 4. 로그아웃 관련 설정
             * 로그 아웃 성공 시 "/" 경로로 이동
             */
            .and()
            .logout()
            .logoutSuccessUrl("/")
            /**
             * 5. OAuth2 로그인 설정
             * oauth2Login(): OAuth2 로그인 기능 설정 시작점
             * userService(): 소셜 로그인 성공 시 후속 조치를 진행 할 UserService 인터페이스 구현체를 등록
             */
            .and()
            .oauth2Login()
            .userInfoEndpoint()
            .userService(customOAuth2UserService);
    }

    private String[] getStaticResourceUrls() {
        return new String[]{
            "/",
            "/css/**",
            "/images/**",
            "/js/**",
            "/h2-console/**",
            "/profile"
        };
    }
}

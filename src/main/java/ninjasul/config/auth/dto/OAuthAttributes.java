package ninjasul.config.auth.dto;

import lombok.Builder;
import lombok.Getter;
import ninjasul.domain.user.Role;
import ninjasul.domain.user.User;

import java.util.Map;

/**
 * OAuthAttributes
 * - OAuth2UserService 를 통해 가져온 OAuth2User 의 attribute를 담는 클래스
 * - 이후 네이버 등 다른 소셜 로그인도 이 클래스를 사용함.
 */
@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(
        Map<String, Object> attributes,
        String nameAttributeKey,
        String name,
        String email,
        String picture
    ) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(
        String registrationId,
        String userNameAttributeName,
        Map<String, Object> attributes
    ) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(
        String userNameAttributeName,
        Map<String, Object> attributes
    ) {
        return OAuthAttributes.builder()
            .name(String.valueOf(attributes.get("name")))
            .email(String.valueOf(attributes.get("email")))
            .picture(String.valueOf(attributes.get("picture")))
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .build();
    }

    public User toEntity() {
        return User.builder()
            .name(name)
            .email(email)
            .picture(picture)
            .role(Role.GUEST)
            .build();
    }
}

package ninjasul.config.auth.dto;

import lombok.Getter;
import ninjasul.domain.user.User;

import java.io.Serializable;

/**
 *  SessionUser
 *  - 세션에 사용자 정보를 저장하기 위한 Dto 클래스
 *  - User Entity 클래스는 Serializable 되어 있지도 않아서 Session 에 저장할 수 없음.
 *    그렇다고 User 클래스에 Serializable 을 구현하여 사용하는 것도 좋지 않음.
 *  - Entity 클래스에 혹시라도 자식 Entity가 Collection이라면
 *    해당 자식 Entity 클래스들까지 모두 Serializalble 을 구현해야 하므로
 *    성능 상 손해를 볼 수도 있고 부작용이 생길 가능성이 있으므로 DTO 클래스를 별도로 생성함.
 */
@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}

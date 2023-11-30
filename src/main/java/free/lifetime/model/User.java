package free.lifetime.model;

import com.fasterxml.jackson.annotation.JsonValue;
import free.lifetime.enums.EnumValue;
import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    public static final String DEFAULT_PASSWORD = "Ultraman&Monsters";

    private Long id;

    private String username;

    private String password;

    private State state;

    private Date createTime;

    private UserDetail userDetail;

    @Getter
    @AllArgsConstructor
    public enum State {
        Root("1", "管理员"),
        Ordinary("0", "普通用户");

        @EnumValue
        @JsonValue
        private final String value;
        private final String name;
    }
}
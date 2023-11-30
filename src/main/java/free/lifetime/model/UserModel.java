package free.lifetime.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @create: 2023/11/20
 * @Description:
 * @FileName: UserModel
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private Long id;

    private String username;

    private String password;

    private User.State state;

    private Long uid;

    private String email;

    private String phone;

    private String address;

    private String avatar;
}

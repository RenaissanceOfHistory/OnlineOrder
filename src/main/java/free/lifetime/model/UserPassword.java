package free.lifetime.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @create: 2023/11/26
 * @Description:
 * @FileName: UserPassword
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPassword {
    private String oldPassword;
    private String newPassword;
}

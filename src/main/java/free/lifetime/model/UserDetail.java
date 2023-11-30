package free.lifetime.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetail {
    private Long id;

    private Long uid;

    private String email;

    private String phone;

    private String address;

    private String avatar;

    private Date createTime;
}
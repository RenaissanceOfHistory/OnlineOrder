package free.lifetime.model;

import com.fasterxml.jackson.annotation.JsonValue;
import free.lifetime.enums.EnumValue;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @create: 2023/11/20
 * @Description:
 * @FileName: Order
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private Long fid;
    private Long uid;
    private Long count;
    private BigDecimal price;
    private Status status;
    private Paid paid;
    private Date createTime;

    private User user;
    private UserDetail userDetail;
    private Food food;

    @Getter
    @AllArgsConstructor
    public enum Status {
        Undelivered("0", "未派送"),
        Deliver("1", "正派送"),
        Delivered("2", "已派送");

        @EnumValue
        @JsonValue
        private final String value;
        private final String name;
    }

    @Getter
    @AllArgsConstructor
    public enum Paid {
        Paying("0", "未支付"),
        Paid("1", "已支付");

        @JsonValue
        @EnumValue
        private final String value;
        private final String name;
    }
}

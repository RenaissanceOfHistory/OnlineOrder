package free.lifetime.model;

import com.fasterxml.jackson.annotation.JsonValue;
import free.lifetime.enums.EnumValue;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Food {
    private Long id;

    private String name;

    private Long sid;

    private BigDecimal price;

    private Integer stock;

    private String desc;

    private String image;

    private Status status;

    private Date createTime;



    @Getter
    @AllArgsConstructor
    public enum Status {
        NotListed("0", "未上架"),
        Listed("1", "已上架"),
        Removed("2", "已下架");

        @EnumValue
        @JsonValue
        private final String value;
        private final String name;
    }
}
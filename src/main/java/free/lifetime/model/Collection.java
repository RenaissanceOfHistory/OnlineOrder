package free.lifetime.model;

import com.fasterxml.jackson.annotation.JsonValue;
import free.lifetime.enums.EnumValue;
import lombok.*;

import java.util.Date;

/**
 * @create: 2023/11/29
 * @Description:
 * @FileName: Collection
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Collection {
    private Long id;
    private Long uid;
    private Long fid;
    private Collect collected;
    private Date createTime;

    @Getter
    @AllArgsConstructor
    public enum Collect {
        NotCollected("0", "未收藏"),
        Collected("1", "收藏");

        @EnumValue
        @JsonValue
        private final String value;
        private final String name;
    }
}

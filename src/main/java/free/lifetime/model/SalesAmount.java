package free.lifetime.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @create: 2023/11/23
 * @Description:
 * @FileName: SalesAmount
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesAmount {
    private String storeName;
    private Integer amount;
}

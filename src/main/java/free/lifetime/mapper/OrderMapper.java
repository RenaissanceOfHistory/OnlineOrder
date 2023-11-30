package free.lifetime.mapper;

import free.lifetime.model.SalesAmount;
import free.lifetime.model.Order;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @create: 2023/11/20
 * @Description:
 * @FileName: OrderMapper
 */
public interface OrderMapper {

    List<Order> selectList(Order order);

    int insertSelective(@Param("orderList") List<Order> orderList);

    int updateByPrimaryKeySelective(Order order);

    int deleteByPrimaryKey(Long id);

    int deleteBatch(@Param("idList") List<Long> idList);

    Order selectByPrimaryKey(Long id);

    List<SalesAmount> selectSalesAmount();

    int selectCount(Order order);

    long selectCartCount(Long uid);

    Order selectByUidAndFid(Order order);

    BigDecimal selectTotalPriceByUid(Long uid);
}

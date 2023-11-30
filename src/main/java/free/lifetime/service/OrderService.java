package free.lifetime.service;

import free.lifetime.model.Order;
import free.lifetime.model.SalesAmount;

import java.math.BigDecimal;
import java.util.List;

/**
 * @create: 2023/11/20
 * @Description:
 * @FileName: OrderService
 */
public interface OrderService {
    List<Order> list(Order order);

    int removeOrder(List<Long> idList);

    int removeOrder(Long id);

    Order queryById(Long id);

    int updateOrder(Order order);

    List<SalesAmount> querySalesAmount();

    int updateCart(Order order);

    int addOrder(Order order);

    long queryCartNum(Long uid);

    BigDecimal queryTotalPrice(Long uid);

    long updateOrder(List<Order> orderList);
}

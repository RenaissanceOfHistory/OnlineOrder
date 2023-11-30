package free.lifetime.service.impl;

import free.lifetime.mapper.OrderMapper;
import free.lifetime.model.Food;
import free.lifetime.model.Order;
import free.lifetime.model.SalesAmount;
import free.lifetime.service.FoodService;
import free.lifetime.service.OrderService;
import free.lifetime.utils.RedisTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @create: 2023/11/20
 * @Description:
 * @FileName: OrderServiceImpl
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    private static final int MAX_BATCH_SIZE = 100;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private RedisTemplateUtil redisTemplateUtil;

    @Resource
    private FoodService foodService;

    @Override
    public List<Order> list(Order order) {
        log.info("查询订单列表：{}", order);
        return orderMapper.selectList(order);
    }

    @Transactional
    @Override
    public int removeOrder(List<Long> idList) {
        idList.forEach(this::updateFoodStock);

        log.info("批量删除订单");
        int delete = 0, index = 0;
        while (idList.size() > MAX_BATCH_SIZE && index + MAX_BATCH_SIZE < idList.size()) {
            delete += orderMapper.deleteBatch(idList.subList(index, index + MAX_BATCH_SIZE));
            index += MAX_BATCH_SIZE;
        }
        delete += orderMapper.deleteBatch(idList.subList(index, idList.size()));
        return delete;
    }

    @Transactional
    @Override
    public int removeOrder(Long id) {
        updateFoodStock(id);

        log.info("删除订单：id({})", id);
        return orderMapper.deleteByPrimaryKey(id);
    }

    private int updateFoodStock(Long id) {
        int update = -1;
        Order order = orderMapper.selectByPrimaryKey(id);
        if (order.getCount() > 0) {
            log.info("修改菜品库存：id({})", id);
            Food food = foodService.queryById(order.getFid());
            food.setImage(null);
            food.setStock((int) (food.getStock() + order.getCount()));
            update = foodService.updateFood(food);
        }
        return update;
    }

    @Override
    public Order queryById(Long id) {
        log.info("查询订单：id({})", id);
        return orderMapper.selectByPrimaryKey(id);
    }

    @Transactional
    @Override
    public int updateOrder(Order order) {
        log.info("修改订单：{}", order);
        Food food = foodService.queryById(order.getFid());
        Long oldCount = orderMapper.selectByPrimaryKey(order.getId()).getCount();

        order.setPrice(food.getPrice().multiply(BigDecimal.valueOf(order.getCount())));
        orderMapper.updateByPrimaryKeySelective(order);

        food.setStock((int) (food.getStock() - Math.abs(oldCount - order.getCount())));
        food.setImage(null);
        return foodService.updateFood(food);
    }


    @Override
    public List<SalesAmount> querySalesAmount() {
        log.info("查询销售量");
        final String KEY = "salesList::amount";
        Supplier<List> supplier = () -> redisTemplateUtil.getList(KEY);
        Predicate<List> predicate = list -> Objects.isNull(list) || list.isEmpty();
        List salesAmountList = supplier.get();

        if (predicate.test(salesAmountList)) {
            synchronized (this) {
                if (predicate.test(salesAmountList = supplier.get())) {
                    salesAmountList = orderMapper.selectSalesAmount();
                    redisTemplateUtil.setList(KEY, salesAmountList, 10, TimeUnit.SECONDS);
                }
            }
        }
        //noinspection unchecked
        return salesAmountList;
    }

    @Transactional
    @Override
    public int updateCart(Order order) {
        Food food = foodService.queryStock(order.getFid());
        if (food.getStock() <= 0) return -1;

        food.setId(order.getFid());
        food.setStock(food.getStock() - 1);
        foodService.updateFood(food);

        if (!hasOrder(order)) {
            addOrder(order);
        }

        log.info("添加购物车：{}", order);
        order = orderMapper.selectByUidAndFid(order);
        order.setCount(order.getCount() + 1);
        order.setPrice(food.getPrice().multiply(BigDecimal.valueOf(order.getCount())));
        return orderMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    public int addOrder(Order order) {
        log.info("添加订单：{}", order);
        return orderMapper.insertSelective(Collections.singletonList(order));
    }

    private boolean hasOrder(Order order) {
        log.info("查询订单数量：{}", order);
        return orderMapper.selectCount(order) > 0;
    }

    @Override
    public long queryCartNum(Long uid) {
        log.info("查询购物车数量：uid({})", uid);
        return orderMapper.selectCartCount(uid);
    }

    @Override
    public BigDecimal queryTotalPrice(Long uid) {
        log.info("查询订单总价：uid({})", uid);
        return orderMapper.selectTotalPriceByUid(uid);
    }

    @Transactional
    @Override
    public long updateOrder(List<Order> orderList) {
        log.info("修改订单：size({})", orderList.size());
        long update = 0;
        for (Order order : orderList) {
            update += updateOrder(order);
        }
        return update;
    }
}


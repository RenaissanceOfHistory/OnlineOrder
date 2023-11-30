package free.lifetime.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import free.lifetime.common.Result;
import free.lifetime.constants.ServiceConstant;
import free.lifetime.model.Food;
import free.lifetime.model.Order;
import free.lifetime.model.User;
import free.lifetime.service.FoodService;
import free.lifetime.service.OrderService;
import free.lifetime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @create: 2023/11/20
 * @Description:
 * @FileName: OrderController
 */
@Controller
@RequestMapping(ServiceConstant.ORDER_SERVICE)
public class OrderController {
    private static final Predicate<Integer> predicate = i -> Objects.isNull(i) || i < 1;

    @Autowired
    private OrderService orderService;

    @Autowired
    private FoodService foodService;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public String list(@RequestParam(required = false) Integer pageNum,
                       @RequestParam(required = false) Integer pageSize,
                       Order order,
                       Model model) {
        if (predicate.test(pageNum)) pageNum = 1;
        if (predicate.test(pageSize)) pageSize = 5;

        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderService.list(order);
        PageInfo<Order> pageInfo = new PageInfo<>(orderList);

        model.addAttribute("page", pageInfo);
        return "forward:/?funcIdx=2";
    }

    @DeleteMapping("/remove/id-list")
    @ResponseBody
    public Result removeOrderBatch(@RequestBody List<Long> idList) {
        int res = orderService.removeOrder(idList);
        return Result.ok("删除成功", res);
    }

    @DeleteMapping("/remove/{id}")
    @ResponseBody
    public Result remove(@PathVariable("id") Long id) {
        int res = orderService.removeOrder(id);
        return (res < 1) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "订单删除失败") :
                Result.ok("订单删除成功");
    }

    @GetMapping("/form")
    public String orderForm(@RequestParam(required = false) Long id, Model model) {
        Order order = orderService.queryById(id);
        List<Food> foodList = foodService.foodList(null);
        List<User> userList = userService.list(null);

        model.addAttribute("order", order);
        model.addAttribute("foodList", foodList);
        model.addAttribute("userList", userList);
        return "views/order/order";
    }

    @PutMapping("/update")
    @ResponseBody
    public Result updateOrder(@RequestBody Order order) {
        Food food = foodService.queryById(order.getFid());
        if (food.getStock() < order.getCount()) {
            return Result.error(HttpStatus.NOT_ACCEPTABLE.value(), "库存不足，最大库存为：" + food.getStock());
        }

        int res = orderService.updateOrder(order);
        return (res < 1) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "订单修改失败") :
                Result.ok("订单修改成功");
    }

    @GetMapping("/status")
    public String changeStatus(Order order) {
        orderService.updateOrder(order);
        return "redirect:/orders/list";
    }

    @GetMapping("/cart/count")
    @ResponseBody
    public Result queryCartNum(HttpSession session) {
        Long uid = (Long) session.getAttribute("uid");
        long cartNum = orderService.queryCartNum(uid);
        return Result.ok("查询购物车数量成功", cartNum);
    }

    @PostMapping("/add/cart")
    @ResponseBody
    public Result addCart(@RequestBody Order order,
                          HttpSession session) {
        Long uid = (Long) session.getAttribute("uid");
        order.setUid(uid);

        int update = orderService.updateCart(order);
        return (update <= 0) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "库存不足") :
                Result.ok("添加购物车成功", update);
    }

    @GetMapping("/pay")
    @ResponseBody
    public Result payOrder(HttpSession session) {
        Long uid = (Long) session.getAttribute("uid");
        List<Order> orderList = orderService.list(Order.builder().uid(uid).build());
        orderList.forEach(order -> order.setPaid(Order.Paid.Paid));

        long update = orderService.updateOrder(orderList);
        return (update <= 0) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "支付失败") :
                Result.ok("支付成功", update);
    }
}

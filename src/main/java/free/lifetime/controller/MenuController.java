package free.lifetime.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import free.lifetime.common.Result;
import free.lifetime.constants.ServiceConstant;
import free.lifetime.model.Collection;
import free.lifetime.model.Food;
import free.lifetime.model.Order;
import free.lifetime.model.Store;
import free.lifetime.service.FoodService;
import free.lifetime.service.OrderService;
import free.lifetime.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.function.Predicate;

/**
 * @create: 2023/11/28
 * @Description:
 * @FileName: MenuController
 */
@Controller
@RequestMapping(ServiceConstant.MENU_SERVICE)
public class MenuController {

    @Resource
    private FoodService foodService;

    @Resource
    private StoreService storeService;

    @Resource
    private OrderService orderService;

    @GetMapping("/list")
    public String list(@RequestParam(required = false) Integer pageNum,
                       @RequestParam(required = false) Integer pageSize,
                       Food food,
                       HttpSession session,
                       Model model) {
        Predicate<Integer> predicate = i -> Objects.isNull(i) || i < 1;
        if (predicate.test(pageNum)) pageNum = 1;
        if (predicate.test(pageSize)) pageSize = 8;

        System.out.println("food = " + food);

        if (Objects.isNull(food)) food = Food.builder().build();
        food.setStatus(Food.Status.Listed);

        PageHelper.startPage(pageNum, pageSize);
        List<Food> foodList = foodService.foodList(food);
        PageInfo<Food> pageInfo = new PageInfo<>(foodList);

        Long uid = (Long) session.getAttribute("uid");
        Map<Long, Boolean> collectionMap = new HashMap<>(foodList.size());
        foodList.forEach(f -> collectionMap.put(f.getId(), foodService.isCollected(uid, f.getId())));

        model.addAttribute("page", pageInfo);
        model.addAttribute("searchFood", food);
        model.addAttribute("collectionMap", collectionMap);
        return "forward:/";
    }

    @GetMapping("/collection")
    public String collection(@RequestParam(required = false) Integer pageNum,
                               @RequestParam(required = false) Integer pageSize,
                               HttpSession session,
                               Model model) {
        Predicate<Integer> predicate = i -> Objects.isNull(i) || i < 1;
        if (predicate.test(pageNum)) pageNum = 1;
        if (predicate.test(pageSize)) pageSize = 8;

        Long uid = (Long) session.getAttribute("uid");
        PageHelper.startPage(pageNum, pageSize);
        List<Food> foodList = foodService.queryCollected(uid);
        PageInfo<Food> pageInfo = new PageInfo<>(foodList);

        Map<Long, String> storeMap = new HashMap<>();
        foodList.forEach(food -> {
            if (!storeMap.containsKey(food.getSid())) {
                storeMap.put(food.getSid(), storeService.queryById(food.getSid()).getName());
            }
        });

        model.addAttribute("page", pageInfo);
        model.addAttribute("storeMap", storeMap);
        return "forward:/?funcIdx=1";
    }

    @PutMapping("/collection/status")
    @ResponseBody
    public Result changeCollected(@RequestBody Collection collection,
                                  HttpSession session) {
        Long uid = (Long) session.getAttribute("uid");
        collection.setUid(uid);

        int update = foodService.updateCollection(collection);
        String info = Collection.Collect.Collected.equals(collection.getCollected()) ?
                "收藏成功" : "已取消收藏";
        return (update <= 0) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "修改失败") :
                Result.ok(info, update);
    }

    @GetMapping("/food/detail")
    public String foodDetail(Food food, Model model, HttpSession session) {
        Long uid = (Long) session.getAttribute("uid");
        food = foodService.queryById(food.getId());
        Store store = storeService.queryById(food.getSid());
        Boolean collected = foodService.isCollected(uid, food.getId());

        PageHelper.startPage(1, 4);
        List<Food> foodList = foodService.queryFoodBySid(food.getSid());
        PageInfo<Food> pageInfo = new PageInfo<>(foodList);

        model.addAttribute("food", food);
        model.addAttribute("foodList", pageInfo.getList());
        model.addAttribute("store", store);
        model.addAttribute("isRoot", session.getAttribute("isRoot"));
        model.addAttribute("collected", collected);
        return "views/menu/menu_food";
    }

    @GetMapping("/cart")
    public String toCart(HttpSession session, Model model) {
        Long uid = (Long) session.getAttribute("uid");
        List<Order> orderList = orderService.list(Order.builder().uid(uid).build());

        Map<Long, Food> foodMap = new HashMap<>(orderList.size());
        orderList.forEach(order -> foodMap.put(order.getFid(), foodService.queryById(order.getFid())));

        model.addAttribute("orderList", orderList);
        model.addAttribute("foodMap", foodMap);
        model.addAttribute("totalPrice", orderService.queryTotalPrice(uid));
        return "forward:/?funcIdx=2";
    }

    @GetMapping("/order")
    public String toOrder(HttpSession session, Model model) {
        Long uid = (Long) session.getAttribute("uid");
        List<Order> orderList = orderService.list(Order.builder().uid(uid).build());
        PageInfo<Order> pageInfo = new PageInfo<>(orderList);

        model.addAttribute("page", pageInfo);
        return "forward:/?funcIdx=3";
    }
}

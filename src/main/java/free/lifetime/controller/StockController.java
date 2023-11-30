package free.lifetime.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import free.lifetime.common.Result;
import free.lifetime.constants.ServiceConstant;
import free.lifetime.model.Food;
import free.lifetime.model.Store;
import free.lifetime.service.FoodService;
import free.lifetime.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @create: 2023/11/26
 * @Description:
 * @FileName: StockController
 */
@Slf4j
@Controller
@RequestMapping(ServiceConstant.STOCK_SERVICE)
public class StockController {

    @Resource
    private FoodService foodService;

    @Resource
    private StoreService storeService;

    @GetMapping("/list")
    public String list(@RequestParam(required = false) Integer pageNum,
                       @RequestParam(required = false) Integer pageSize,
                       Food food,
                       Model model) {
        Predicate<Integer> predicate = i -> Objects.isNull(i) || i < 1;
        if (predicate.test(pageNum)) pageNum = 1;
        if (predicate.test(pageSize)) pageSize = 5;

        PageHelper.startPage(pageNum, pageSize);
        List<Food> foodList = foodService.foodList(food);
        PageInfo<Food> pageInfo = new PageInfo<>(foodList);

        Map<Long, Store> storeMap = new HashMap<>(foodList.size());
        foodList.forEach(f -> storeMap.put(f.getSid(), storeService.queryById(f.getSid())));

        model.addAttribute("page", pageInfo);
        model.addAttribute("searchFood", food);
        model.addAttribute("storeMap", storeMap);
        return "forward:/?funcIdx=6";
    }

    @PutMapping("/add")
    @ResponseBody
    public Result addStock(@RequestBody Food food) {
        log.info("补充库存：{}", food);
        food.setStock(food.getStock() + 100);
        int update = foodService.updateFood(food);
        return (update < 1) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "补货失败") :
                Result.ok("补货成功", update);
    }
}

package free.lifetime.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import free.lifetime.common.Result;
import free.lifetime.constants.ServiceConstant;
import free.lifetime.model.Food;
import free.lifetime.model.Store;
import free.lifetime.service.FoodService;
import free.lifetime.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @create: 2023/11/15
 * @Description:
 * @FileName: StoreController
 */
@Controller
@RequestMapping(ServiceConstant.STORE_SERVICE)
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Resource
    private FoodService foodService;

    @GetMapping("/list")
    public String list(@RequestParam(required = false) Integer pageNum,
                       @RequestParam(required = false) Integer pageSize,
                       Store store,
                       Model model) {
        Predicate<Integer> predicate = i -> Objects.isNull(i) || i < 1;
        if (predicate.test(pageNum)) pageNum = 1;
        if (predicate.test(pageSize)) pageSize = 5;

        PageHelper.startPage(pageNum, pageSize);
        List<Store> storeList = storeService.storeList(store);
        PageInfo<Store> pageInfo = new PageInfo<>(storeList);

        model.addAttribute("page", pageInfo);
        model.addAttribute("searchStore", store);
        return "forward:/?funcIdx=5";
    }

    @PostMapping("/add")
    @ResponseBody
    public Result addStore(@RequestBody Store store) {
        Integer res = storeService.addStore(store);
        return (Objects.isNull(res)) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "已存在同名店铺") :
                Result.ok("店铺添加成功");
    }

    @DeleteMapping("/remove/id-list")
    @ResponseBody
    public Result removeStoreBatch(@RequestBody List<Long> idList) {
        Long res = storeService.removeStore(idList);
        return Result.ok("删除成功", res);
    }

    @DeleteMapping("/remove/{id}")
    @ResponseBody
    public Result remove(@PathVariable("id") Long id) {
        int res = storeService.removeStore(id);
        return (res < 1) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "店铺删除失败") :
                Result.ok("店铺删除成功");
    }

    @GetMapping("/form")
    public String storeForm(@RequestParam(required = false) Long id,
                           Model model) {
        model.addAttribute("store", storeService.queryById(id));
        return "views/store/store";
    }

    @PutMapping("/update")
    @ResponseBody
    public Result updateStore(@RequestBody Store store) {
        Integer res = storeService.updateStore(store);
        return (Objects.isNull(res)) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "已存在同名店铺") :
                Result.ok("菜品修改成功");
    }

    @GetMapping("/food")
    public String lookFood(@RequestParam(required = false) Integer pageNum,
                           @RequestParam(required = false) Integer pageSize,
                           Store store,
                           HttpSession session,
                           Model model) {
        Predicate<Integer> predicate = i -> Objects.isNull(i) || i < 1;
        if (predicate.test(pageNum)) pageNum = 1;
        if (predicate.test(pageSize)) pageSize = 4;

        PageHelper.startPage(pageNum, pageSize);
        List<Food> foodList = foodService.queryFoodBySid(store.getId());
        PageInfo<Food> pageInfo = new PageInfo<>(foodList);

        model.addAttribute("isRoot", session.getAttribute("isRoot"));
        model.addAttribute("page", pageInfo);
        model.addAttribute("store", store);
        return "views/store/store_food";
    }
}

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @create: 2023/11/14
 * @Description:
 * @FileName: FoodController
 */
@Slf4j
@Controller
@RequestMapping(ServiceConstant.FOOD_SERVICE)
public class FoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
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

        List<Store> storeList = storeService.storeList();
        Map<Long, String> map = storeList.stream()
                .collect(Collectors.toMap(Store::getId, Store::getName));

        model.addAttribute("page", pageInfo);
        model.addAttribute("storeMap", map);
        model.addAttribute("searchFood", food);
        return "forward:/";
    }

    @DeleteMapping("/remove/id-list")
    @ResponseBody
    public Result removeFoodBatch(@RequestBody List<Long> idList) {
        int res = foodService.removeFood(idList);
        return Result.ok("删除成功", res);
    }

    @PostMapping("/add")
    @ResponseBody
    public Result addFood(@RequestBody Food food) {
        int res = foodService.addFood(food);
        return (res < 1) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "菜品添加失败") :
                Result.ok("菜品添加成功");
    }

    @PostMapping("/image/upload")
    @ResponseBody
    public Result uploadImage(@RequestBody Food food) {
        boolean res = foodService.uploadImage(food.getImage(), food.getId());
        return !res ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "图片添加失败") :
                Result.ok("图片添加成功");
    }

    @GetMapping("/image/download/{id}")
    @ResponseBody
    public void downloadImage(@PathVariable("id") Long id,
                              HttpServletResponse response) {
        try (BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream())) {
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            output.write(foodService.downloadImage(id));
        } catch (IOException e) {
            log.error("菜品图片下载失败", e);
        }
    }

    @GetMapping("/form")
    public String foodForm(@RequestParam(required = false) Long id,
                           Model model) {
        model.addAttribute("food", foodService.queryById(id));
        model.addAttribute("storeList", storeService.storeList());
        return "views/food/food";
    }

    @PutMapping("/update")
    @ResponseBody
    public Result updateFood(@RequestBody Food food) {
        int res = foodService.updateFood(food);
        return (res < 1) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "菜品修改失败") :
                Result.ok("菜品修改成功");
    }

    @GetMapping("/status")
    public String changeStatus(Food food) {
        log.info("food = {}", food);
        foodService.updateFood(food);
        return "redirect:/foods/list";
    }

    @DeleteMapping("/remove/{id}")
    @ResponseBody
    public Result remove(@PathVariable("id") Long id) {
        int res = foodService.removeFood(id);
        return (res < 1) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "菜品删除失败") :
                Result.ok("菜品删除成功");
    }

    @GetMapping("/query/stock/{fid}")
    @ResponseBody
    public Result queryStock(@PathVariable("fid") Long fid) {
        Food food = foodService.queryById(fid);
        return Result.ok("查询菜品库存成功", food.getStock());
    }
}

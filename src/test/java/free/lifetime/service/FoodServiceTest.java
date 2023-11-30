package free.lifetime.service;

import free.lifetime.model.Food;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class FoodServiceTest {

    @Autowired
    private FoodService foodService;

    @Test
    void addFood() {
        Food food = Food.builder()
                .sid(300L)
                .name("北京烤猪")
                .price(BigDecimal.valueOf(120L))
                .status(Food.Status.Listed)
                .build();
        foodService.addFood(food);
    }

    @Test
    void addFoodBatch() {
        final int SIZE = 115;
        List<Food> foodList = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            Food food = Food.builder()
                    .sid(300L)
                    .name("北京烤鸭" + i + "号")
                    .price(BigDecimal.valueOf(120L))
                    .build();
            foodList.add(food);
        }

        int add = foodService.addFood(foodList);
        Assert.assertEquals(add, SIZE);
    }

    @Test
    void updateFood() {
        Food food = Food.builder()
                .id(13L)
                .name("天堂烤鸭")
                .price(BigDecimal.valueOf(33.33))
                .build();
        foodService.updateFood(food);
    }

    @Test
    void removeFood() {
        foodService.removeFood(14L);
    }

    @Test
    void removeFoodBatch() {
        final int SIZE = 108;
        List<Long> idList = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            idList.add((long) i);
        }
        foodService.removeFood(idList);
    }

    @Test
    void queryFoodBySid() {
        foodService.queryFoodBySid(300L);
    }

    @Test
    void removeFoodBySid() {
        foodService.removeFoodBySid(300L);
    }

    @Test
    void uploadImage() {
    }

    @Test
    void downloadImage() {
    }

    @Test
    void foodList() {
        foodService.foodList(null).forEach(System.out::println);
    }
}
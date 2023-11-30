package free.lifetime.service;

import free.lifetime.model.Collection;
import free.lifetime.model.Food;

import java.util.List;

/**
 * @create: 2023/11/14
 * @Description:
 * @FileName: FoodService
 */
public interface FoodService {

    int addFood(Food food);

    int addFood(List<Food> foodList);

    int updateFood(Food food);

    int removeFood(Long id);

    int removeFood(List<Long> idList);

    List<Food> queryFoodBySid(Long sid);

    List<Food> foodList(Food food);

    int removeFoodBySid(Long sid);

    boolean uploadImage(String base64, Long id);

    byte[] downloadImage(Long id);

    Food queryById(Long id);

    int updateCollection(Collection collection);

    boolean hasCollected(Collection collection);

    Boolean isCollected(Long uid, Long fid);

    List<Food> queryCollected(Long uid);

    Food queryStock(Long fid);
}

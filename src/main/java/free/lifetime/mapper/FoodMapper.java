package free.lifetime.mapper;

import free.lifetime.model.Food;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FoodMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Food record);

    int insertSelective(Food record);

    Food selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Food record);

    int updateByPrimaryKey(Food record);

    List<Food> selectBySid(Long sid);

    int deleteBySid(Long sid);

    String selectImage(Long id);

    int insertBatch(@Param("foodList") List<Food> foodList);

    int deleteBatch(@Param("idList") List<Long> idList);

    List<Food> selectList(Food food);

    List<Food> selectCollected(Long uid);

    Food selectStock(Long id);
}
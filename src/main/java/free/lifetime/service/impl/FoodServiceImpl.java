package free.lifetime.service.impl;

import free.lifetime.mapper.CollectionMapper;
import free.lifetime.mapper.FoodMapper;
import free.lifetime.model.Collection;
import free.lifetime.model.Food;
import free.lifetime.service.FileService;
import free.lifetime.service.FoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @create: 2023/11/14
 * @Description:
 * @FileName: FoodServiceImpl
 */
@Slf4j
@Service
public class FoodServiceImpl implements FoodService {
    private static final int MAX_BATCH_SIZE = 100;
    private static final String SAVE_PATH = "food";

    @Resource
    private FoodMapper foodMapper;

    @Resource
    private FileService fileService;

    @Resource
    private CollectionMapper collectionMapper;

    @Override
    public int addFood(Food food) {
        log.info("添加食品：{}", food);
        setImage(food);

        return foodMapper.insertSelective(food);
    }

    @Override
    public int addFood(List<Food> foodList) {
        log.info("批量添加食品：size({})", foodList.size());
        foodList.forEach(this::setImage);

        int insert = 0, index = 0;
        while (foodList.size() > MAX_BATCH_SIZE && index + MAX_BATCH_SIZE < foodList.size()) {
            insert += foodMapper.insertBatch(foodList.subList(index, index + MAX_BATCH_SIZE));
            index += MAX_BATCH_SIZE;
        }
        insert += foodMapper.insertBatch(foodList.subList(index, foodList.size()));
        return insert;
    }

    @Override
    public int updateFood(Food food) {
        setImage(food);
        log.info("修改菜品：{}", food);
        return foodMapper.updateByPrimaryKeySelective(food);
    }

    @Override
    public int removeFood(Long id) {
        log.info("删除食品：id({})", id);
        cleanupImage(id);
        return foodMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int removeFood(List<Long> idList) {
        log.info("批量删除食品");
        fileService.setSavePath(SAVE_PATH);
        idList.forEach(this::cleanupImage);

        int delete = 0, index = 0;
        while (idList.size() > MAX_BATCH_SIZE && index + MAX_BATCH_SIZE < idList.size()) {
            delete += foodMapper.deleteBatch(idList.subList(index, index + MAX_BATCH_SIZE));
            index += MAX_BATCH_SIZE;
        }
        delete += foodMapper.deleteBatch(idList.subList(index, idList.size()));
        return delete;
    }

    @Override
    public List<Food> queryFoodBySid(Long sid) {
        log.info("查询店铺食品集合：sid({})", sid);
        return foodMapper.selectBySid(sid);
    }

    @Override
    public List<Food> foodList(Food food) {
        log.info("查询食品集合");
        return foodMapper.selectList(food);
    }

    @Override
    public int removeFoodBySid(Long sid) {
        log.info("删除食品：sid({})", sid);
        return foodMapper.deleteBySid(sid);
    }

    @Override
    public boolean uploadImage(String base64, Long id) {
        cleanupImage(id);
        Food food = Food.builder()
                .id(id)
                .image(fileService.uploadImage(base64))
                .build();
        return foodMapper.updateByPrimaryKeySelective(food) > 0;
    }

    @Override
    public byte[] downloadImage(Long id) {
        log.info("请求菜品图片：id({})", id);
        String image = foodMapper.selectImage(id);
        if (StringUtils.isEmpty(image)) return new byte[0];

        fileService.setSavePath(SAVE_PATH);
        return fileService.downloadImage(image);
    }

    @Override
    public Food queryById(Long id) {
        return foodMapper.selectByPrimaryKey(id);
    }

    private void setImage(Food food) {
        if (StringUtils.isEmpty(food.getImage())) {
            food.setImage(null);
            return;
        }

        fileService.setSavePath(SAVE_PATH);
        cleanupImage(food.getId());
        food.setImage(fileService.uploadImage(food.getImage()));
    }

    private void cleanupImage(Long id) {
        String image = foodMapper.selectImage(id);
        if (StringUtils.hasText(image)) {
            fileService.setSavePath(SAVE_PATH);
            fileService.removeImage(image);
        }
    }

    @Override
    public boolean hasCollected(Collection collection) {
        log.info("查询是否已收藏：{}", collection);
        return collectionMapper.selectCount(collection) > 0;
    }

    @Override
    public int updateCollection(Collection collection) {
        log.info("修改收藏状态：{}", collection);
        return hasCollected(collection) ?
                collectionMapper.updateCollected(collection) :
                collectionMapper.insertSelective(collection);
    }

    @Override
    public Boolean isCollected(Long uid, Long fid) {
        log.info("是否已收藏：uid({}) -- fid({})", uid, fid);
        return Collection.Collect.Collected.getValue().equals(collectionMapper.selectCollected(uid, fid));
    }

    @Override
    public List<Food> queryCollected(Long uid) {
        log.info("查询收藏菜品集合");
        return foodMapper.selectCollected(uid);
    }

    @Override
    public Food queryStock(Long fid) {
        log.info("查询菜品库存：fid({})", fid);
        return foodMapper.selectStock(fid);
    }
}

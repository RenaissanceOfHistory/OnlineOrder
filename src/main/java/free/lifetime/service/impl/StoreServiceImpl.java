package free.lifetime.service.impl;

import free.lifetime.mapper.StoreMapper;
import free.lifetime.model.Store;
import free.lifetime.service.StoreService;
import free.lifetime.utils.RedisTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @create: 2023/11/14
 * @Description:
 * @FileName: StoreServiceImpl
 */
@Slf4j
@Service
public class StoreServiceImpl implements StoreService {
    private static final int MAX_BATCH_SIZE = 100;

    @Resource
    private StoreMapper storeMapper;

    @Resource
    private RedisTemplateUtil redisTemplateUtil;

    @Override
    public Integer addStore(Store store) {
        if (hasStore(store)) return null;

        log.info("添加店铺：{}", store);
        return storeMapper.insertSelective(store);
    }

    private boolean hasStore(Store store) {
        log.info("查询店铺：{}", store);
        return storeMapper.selectCount(store) > 0;
    }

    @Override
    @Transactional
    public Long addStore(List<Store> storeList) {
        log.info("批量添加店铺：size({})", storeList.size());
        int insert = 0, index = 0;
        while (storeList.size() > MAX_BATCH_SIZE && index + MAX_BATCH_SIZE < storeList.size()) {
            insert += storeMapper.insertBatch(storeList.subList(index, index + MAX_BATCH_SIZE));
            index += MAX_BATCH_SIZE;
        }
        insert += storeMapper.insertBatch(storeList.subList(index, storeList.size()));

        return (long) insert;
    }

    @Override
    public Integer updateStore(Store store) {
        if (hasStore(store)) return null;

        log.info("修改店铺：{}", store);
        return storeMapper.updateByPrimaryKeySelective(store);
    }

    @Override
    public int removeStore(Long id) {
        log.info("删除店铺：sid({})", id);
        return storeMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public Long removeStore(List<Long> idList) {
        log.info("批量删除店铺");
        int remove = 0, index = 0;
        while (idList.size() > MAX_BATCH_SIZE && index + MAX_BATCH_SIZE < idList.size()) {
            remove += storeMapper.deleteBatch(idList.subList(index, index + MAX_BATCH_SIZE));
            index += MAX_BATCH_SIZE;
        }
        remove += storeMapper.deleteBatch(idList.subList(index, idList.size()));
        return (long) remove;
    }

    @Override
    public List<Store> storeList(Store store) {
        log.info("查询店铺集合：{}", store);
        return storeMapper.selectList(store);
    }

    @Override
    public List<Store> storeList() {
        log.info("查询店铺集合");
        final String KEY = "storeList::all";
        Predicate<List> predicate = list -> Objects.isNull(list) || list.isEmpty();
        List storeList = redisTemplateUtil.getList(KEY);

        if (predicate.test(storeList)) {
            synchronized (this) {
                if (predicate.test(storeList = redisTemplateUtil.getList(KEY))) {
                    storeList = storeMapper.selectList(null);
                    redisTemplateUtil.setList(KEY, storeList, 13, TimeUnit.SECONDS);
                }
            }
        }

        //noinspection unchecked
        return storeList;
    }

    @Override
    public Store queryById(Long id) {
        log.info("查询店铺：id({})", id);

        final String KEY = "store::" + id;
        Supplier<Store> supplier = () -> (Store) redisTemplateUtil.getValue(KEY);
        Store store = supplier.get();

        if (Objects.isNull(store)) {
            synchronized (this) {
                if (Objects.isNull(store = supplier.get())) {
                    store = storeMapper.selectByPrimaryKey(id);
                    redisTemplateUtil.setValue(KEY, store, 5, TimeUnit.SECONDS);
                }
            }
        }
        return store;
    }
}

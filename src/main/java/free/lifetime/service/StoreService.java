package free.lifetime.service;

import free.lifetime.model.Store;

import java.util.List;

/**
 * @create: 2023/11/14
 * @Description:
 * @FileName: StoreService
 */
public interface StoreService {
    Integer addStore(Store store);

    Long addStore(List<Store> storeList);

    Integer updateStore(Store store);

    int removeStore(Long id);

    Long removeStore(List<Long> idList);

    List<Store> storeList(Store store);

    List<Store> storeList();

    Store queryById(Long id);
}

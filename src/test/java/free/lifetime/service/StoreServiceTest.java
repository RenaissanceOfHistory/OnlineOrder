package free.lifetime.service;

import free.lifetime.model.Store;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
class StoreServiceTest {

    @Autowired
    private StoreService storeService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String[] randDesc = {
            "食物很美味，太棒了！",
            "从没吃过这么好吃的食物。👍",
            "I'am a foreigner, but I want to say in Chinese: '太美味了🤦‍♂️'",
            "食物一般，望努力",
            "有的很好吃，有的一般，差强人意🤔",
            "我要当网红，我要来这打卡",
            "系统默认好评"
    };
    private static final SecureRandom random = new SecureRandom();

    @Test
    void addStore() {
        Store store = Store.builder()
                .name("北京烤鸭")
                .desc("想吃地道北京烤鸭，来北京鸭~😉")
                .build();

        int res = storeService.addStore(store);
        Assert.assertEquals(res, 1);
    }

    @Test
    void addStoreBatch() {
        final int SIZE = 1150;
        List<Store> storeList = new ArrayList<>(SIZE);
        for (int i = SIZE - 1; i >= 0; i--) {
            Store store = Store.builder()
                    .name("store_" + (i + 1))
                    .desc(randDesc[random.nextInt(randDesc.length)])
                    .build();
            storeList.add(store);
        }

        Long add = storeService.addStore(storeList);
    }

    @Test
    void updateStore() {
        Store store = Store.builder()
                .id(1216L)
                .desc(randDesc[random.nextInt(randDesc.length)])
                .build();
        storeService.updateStore(store);
    }

    @Test
    void removeStore() {
        storeService.removeStore(1215L);
    }

    @Test
    void removeStoreBatch() {
        final int SIZE = 100;
        List<Long> idList = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            idList.add((long) (200 + i));
        }
        storeService.removeStore(idList);
    }

    @Test
    void storeList() {
    }

    @Test
    void queryStoreByName() {
    }

    @Test
    void querySelective() {
    }
}
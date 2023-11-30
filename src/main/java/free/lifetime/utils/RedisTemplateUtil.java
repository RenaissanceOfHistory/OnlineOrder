package free.lifetime.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @create: 2023/11/24
 * @Description:
 * @FileName: RedisTemplateUtil
 */
@Slf4j
@Component
public class RedisTemplateUtil {
    private static final long LIST_START = 0;
    private static final long LIST_END = -1;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public RedisTemplateUtil setValue(final String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
        return this;
    }

    public RedisTemplateUtil setList(final String key, List value, long timeout, TimeUnit unit) {
        redisTemplate.opsForList().rightPushAll(key, value.toArray());
        expire(key, timeout, unit);
        return this;
    }

    public List getList(final String key) {
        return redisTemplate.opsForList().range(key, LIST_START, LIST_END);
    }

    public List getList(final String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    public Object getValue(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public RedisTemplateUtil expire(final String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
        return this;
    }

    public Map<String, DataType> keys() {
        Set<String> keySet = redisTemplate.keys("*");
        if (Objects.isNull(keySet)) return null;

        Map<String, DataType> map = new HashMap<>(keySet.size());
        keySet.forEach(key -> map.put(key, redisTemplate.type(key)));
        return map;
    }

    public boolean hasKey(final String key) {
        return Objects.equals(redisTemplate.hasKey(key), Boolean.TRUE);
    }
}

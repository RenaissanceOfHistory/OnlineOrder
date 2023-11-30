package free.lifetime.mapper;

import free.lifetime.model.Collection;
import org.apache.ibatis.annotations.Param;

/**
 * @create: 2023/11/29
 * @Description:
 * @FileName: CollectionMapper
 */
public interface CollectionMapper {
    int insertSelective(Collection collection);

    int updateByPrimaryKeySelective(Collection collection);

    int deleteByPrimaryKey(Long id);

    long selectCount(Collection collection);

    int updateCollected(Collection collection);

    String selectCollected(@Param("uid") Long uid,
                           @Param("fid") Long fid);
}

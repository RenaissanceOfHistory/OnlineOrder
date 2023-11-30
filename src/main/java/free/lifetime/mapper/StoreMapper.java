package free.lifetime.mapper;

import free.lifetime.model.Store;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StoreMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Store record);

    int insertSelective(Store record);

    Store selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Store record);

    int updateByPrimaryKey(Store record);

    int insertBatch(@Param("storeList") List<Store> storeList);

    int deleteBatch(@Param("idList") List<Long> idList);

    String selectNameByPrimaryKey(Long id);

    List<Store> selectList(Store store);

    int selectCount(Store store);
}
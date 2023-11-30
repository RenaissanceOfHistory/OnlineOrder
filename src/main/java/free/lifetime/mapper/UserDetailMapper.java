package free.lifetime.mapper;

import free.lifetime.model.UserDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserDetail record);

    int insertSelective(UserDetail record);

    UserDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserDetail record);

    int updateByPrimaryKey(UserDetail record);

    String selectAvatarByUid(Long uid);

    int updateAvatarByUid(@Param("uid") Long uid,
                          @Param("avatar") String avatar);

    int deleteByUid(Long uid);

    int deleteBatch(@Param("uidList") List<Long> uidList);

    UserDetail selectByUid(Long uid);
}
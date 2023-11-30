package free.lifetime.mapper;

import free.lifetime.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int selectCount(User user);

    Long selectPrimaryKeyByNameAndPwd(@Param("username") String username,
                                      @Param("password") String password);

    String selectState(Long id);

    List<User> selectList(User user);

    int deleteBatch(@Param("idList") List<Long> subList);
}
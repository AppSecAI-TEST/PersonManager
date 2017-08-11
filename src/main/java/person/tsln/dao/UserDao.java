package person.tsln.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;
import person.tsln.beans.User;

@Mapper
@Service
public interface UserDao {
    @Select("SELECT * FROM users WHERE email = #{email}")
    User getUserByEmail( String email);
}

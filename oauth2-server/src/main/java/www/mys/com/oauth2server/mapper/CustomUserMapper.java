package www.mys.com.oauth2server.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import www.mys.com.oauth2server.pojo.CustomUser;

@Repository("customUserMapper")
public interface CustomUserMapper extends JpaRepository<CustomUser, Integer> {

    public CustomUser getByMarkTypeAndMark(Integer markType, String mark);

}

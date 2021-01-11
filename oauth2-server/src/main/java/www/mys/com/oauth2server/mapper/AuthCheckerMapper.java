package www.mys.com.oauth2server.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import www.mys.com.oauth2server.pojo.AuthChecker;

@Repository("authCheckerMapper")
public interface AuthCheckerMapper extends JpaRepository<AuthChecker, Long> {

    public AuthChecker getByCheckKey(String checkKey);

}

package www.mys.com.oauth2server.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import www.mys.com.oauth2server.pojo.SMSHistory;

@Repository("smsHistoryMapper")
public interface SMSHistoryMapper extends JpaRepository<SMSHistory, Long> {

    public Page<SMSHistory> getByPhone(String phone, Pageable pageable);

    public Page<SMSHistory> getByPhoneAndDayIndex(String phone, Long dayIndex, Pageable pageable);

}

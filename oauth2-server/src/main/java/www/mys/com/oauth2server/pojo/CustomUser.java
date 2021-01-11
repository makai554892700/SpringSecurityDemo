package www.mys.com.oauth2server.pojo;

import www.mys.com.utils.TimeUtils;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(indexes = {@Index(columnList = "markType,dayIndex,bindUser", name = "mdb")}
        , uniqueConstraints = {@UniqueConstraint(columnNames = {"markType", "mark"}, name = "mm")})
public class CustomUser {

    public static class MarkType {
        public static final Integer PHONE = 0;
        public static final Integer WECHAT = 1;
        public static final Integer QQ = 2;
        public static final Integer EVERY_BIM = 3;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer markType;// 登录类型 0 phone/ 1 微信/ 2 qq /3 EveryBim...
    @Column(nullable = false, length = 50)
    private String mark;//标记(phone/openId)	String(unique)
    private Integer dayIndex = TimeUtils.getDayInt();// 注册日期标记
    private Long bindUser;//绑定的用户id
    @Column(columnDefinition = "TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Date createdAt;
    @Column(columnDefinition = "TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Date updatedAt;

    public CustomUser() {
    }

    public CustomUser(Integer markType, String mark) {
        this.markType = markType;
        this.mark = mark;
    }

    public CustomUser(Integer markType, String mark, Long bindUser) {
        this.markType = markType;
        this.mark = mark;
        this.bindUser = bindUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMarkType() {
        return markType;
    }

    public void setMarkType(Integer markType) {
        this.markType = markType;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Integer getDayIndex() {
        return dayIndex;
    }

    public void setDayIndex(Integer dayIndex) {
        this.dayIndex = dayIndex;
    }

    public Long getBindUser() {
        return bindUser;
    }

    public void setBindUser(Long bindUser) {
        this.bindUser = bindUser;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "CustomUser{" +
                "id=" + id +
                ", markType=" + markType +
                ", mark='" + mark + '\'' +
                ", dayIndex=" + dayIndex +
                ", bindUser=" + bindUser +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

package www.mys.com.oauth2server.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class SortUtils {

    public static class SortType {
        public static final int TIME_DESC = 0;//时间顺序
        public static final int TIME_ASC = 1;//时间逆序
        public static final int ID_DESC = 2;//名字顺序
        public static final int ID_ASC = 3;//名字逆序
    }

    public static Pageable getPageAble(Integer sortType, Integer page, Integer count) {
        Pageable pageable;
        if (sortType != null) {
            switch (sortType) {
                case SortType.TIME_ASC:
                    pageable = PageRequest.of(page, count, Sort.by(Sort.Direction.ASC, "updatedAt"));
                    break;
                case SortType.ID_DESC:
                    pageable = PageRequest.of(page, count, Sort.by(Sort.Direction.DESC, "id"));
                    break;
                case SortType.ID_ASC:
                    pageable = PageRequest.of(page, count, Sort.by(Sort.Direction.ASC, "id"));
                    break;
                default:
                case SortType.TIME_DESC:
                    pageable = PageRequest.of(page, count, Sort.by(Sort.Direction.DESC, "updatedAt"));
                    break;
            }
        } else {
            pageable = PageRequest.of(page, count, Sort.by(Sort.Direction.DESC, "updatedAt"));
        }
        return pageable;
    }

    public Sort getSort(Integer sortType) {
        switch (sortType) {
            case SortType.TIME_ASC:
                return Sort.by(Sort.Direction.ASC, "updatedAt");
            case SortType.ID_DESC:
                return Sort.by(Sort.Direction.DESC, "id");
            case SortType.ID_ASC:
                return Sort.by(Sort.Direction.ASC, "id");
            default:
            case SortType.TIME_DESC:
                return Sort.by(Sort.Direction.DESC, "updatedAt");
        }
    }

}

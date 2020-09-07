package www.mys.com.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {

    private static final Logger log = LoggerFactory.getLogger(LogUtils.class);

    public static void log(String data) {
        log.error(data);
        System.out.println(data);
    }


}

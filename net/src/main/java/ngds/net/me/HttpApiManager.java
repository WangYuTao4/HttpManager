package ngds.net.me;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wangyt on 2018/8/22.
 * : 管理retrofit对象
 */
public class HttpApiManager {

    private Map<String, Object> mApis;

    private HttpApiManager() {
        mApis = new ConcurrentHashMap<>(5);
    }

    public static HttpApiManager get() {
        return InstanceContainer.INSTANCE;
    }

    /**
     * 创建接口服务
     *
     * @param baseUrl
     * @param clazz
     * @param <T>
     * @return
     */
    public synchronized <T> T wrap(String baseUrl, Class<T> clazz) {
        if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }
        if (mApis.containsKey(baseUrl)) {
            return (T) mApis.get(baseUrl + clazz);
        } else {
            T t = new RetrofitClient(baseUrl).create(clazz);
            mApis.put(baseUrl + clazz, t);
            return t;
        }
    }

    public synchronized void clear() {
        if (mApis != null) {
            mApis.clear();
        }
    }

    private static class InstanceContainer {
        private final static HttpApiManager INSTANCE = new HttpApiManager();
    }
}

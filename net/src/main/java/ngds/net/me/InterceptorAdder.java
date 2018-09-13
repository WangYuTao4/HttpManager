package ngds.net.me;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;

/**
 * Created by wangyt on 2018/8/22.
 * :配置okhttp拦截器
 */
public class InterceptorAdder {

    List<Interceptor> interceptors;
    List<Interceptor> netInterceptors;

    private InterceptorAdder() {
        this.interceptors = new ArrayList<>();
        this.netInterceptors = new ArrayList<>();
    }

    public static InterceptorAdder getInstance() {
        return InstanceContainer.INSTANCE;
    }

    public void addInterceptor(Interceptor interceptor) {
        this.interceptors.add(interceptor);
    }

    public void addInterceptors(List<Interceptor> interceptorList) {
        this.interceptors.addAll(interceptorList);
    }

    public void addNetInterceptor(Interceptor interceptor) {
        this.netInterceptors.add(interceptor);
    }

    public void addNetInterceptors(List<Interceptor> interceptorList) {
        this.netInterceptors.addAll(interceptorList);
    }

    public void clearInterceptor() {
        this.interceptors.clear();
    }

    public void clearNetInterceptor() {
        this.netInterceptors.clear();
    }

    private static class InstanceContainer {
        private final static InterceptorAdder INSTANCE = new InterceptorAdder();
    }
}


package pwd.allen.filter

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.servlet.http.HttpServletRequest

class PreZuulFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(MyZuulFilter.class);

    /**
     * 配置过滤类型，有四种不同生命周期的过滤器类型
     * 1. pre：路由之前，适合做请求校验
     * 2. routing：路由之时，路由请求转发阶段
     * 3. post：routing或error过滤器之后
     * 4. error：发送错误调用
     * @return
     */
    @Override
    String filterType() {
        return "pre"
    }

    /**
     * 配置过滤的顺序，越小优先级越高
     * @return
     */
    @Override
    int filterOrder() {
        return 2000
    }

    /**
     * 该过滤器是否要执行：true/需要，false/不需要
     *
     * 用法：指定过滤器的有效范围
     *
     * @return
     */
    @Override
    boolean shouldFilter() {
        //直接返回true，表示该过滤器对所有请求都会生效
        return true
    }

    /**
     * 过滤器的具体业务代码
     *
     * @return
     */
    @Override
    Object run() {
        RequestContext context = RequestContext.getCurrentContext()
        HttpServletRequest request = context.getRequest()
        logger.info("this is a post filter：send {} request to {}", request.getMethod(), request.getRequestURL().toString())
        return null
    }
}
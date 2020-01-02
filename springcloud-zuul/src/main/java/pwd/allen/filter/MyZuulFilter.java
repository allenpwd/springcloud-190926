package pwd.allen.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Zuul 的服务过滤演示
 *
 * 步骤：
 *  继承ZuulFilter并加入到spring容器中即可
 *
 * Zuul默认实现的核心过滤器
 *  pre
 *      ServletDetectionFilter：标记处理Servlet的类型
 *      Servlet30WrapperFilter：包装HttpServletRequest请求
 *      FormBodyWrapperFilter：包装请求体
 *      DebugFilter：标记调试标记
 *      PreDecorationFilter：处理请求上下文供后续使用
 *  routing
 *      RibbonRoutingFilter：serviceId请求转发
 *      SimpleHostRoutingFilter：url请求转发
 *      SendForwardFilter：forward请求转发
 *  post
 *      SendErrorFilter：处理有错误的请求响应
 *      SendResponseFilter：处理正常处理的请求响应
 *
 * @author 门那粒沙
 * @create 2019-11-30 12:24
 **/
@Component
public class MyZuulFilter extends ZuulFilter {

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
    public String filterType() {
        return "pre";
    }

    /**
     * 配置过滤的顺序，越小优先级越高
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 该过滤器是否要执行：true/需要，false/不需要
     *
     * 用法：指定过滤器的有效范围
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        //直接返回true，表示该过滤器对所有请求都会生效
        return true;
    }

    /**
     * 过滤器的具体业务代码
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        logger.info("{} >>> {}", request.getMethod(), request.getRequestURL().toString());
        String token = request.getParameter("token");
        if (token == null) {
            //没有token则返回提示
            logger.warn("Token is empty");
            //让zuul过滤该请求，不进行路由
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(401);
            context.setResponseBody("token is empty!");
//            try {
//                context.getResponse().getWriter().write("Token is empty");
//            } catch (IOException e) {
//            }
        } else {
            logger.info(String.format("token=%s", token));
        }
        return null;
    }
}
package com.mavis.boot.common.aspect;

import com.jidian.base.annotation.NoVerify;
import com.jidian.entity.User;
import com.jidian.service.UserService;
import com.jidian.threadTask.CollectXcxFormIdTask;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 接口调用权限校验
 * @date 2018/4/20
 */
@Component
public class AccessTokenVerifyInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;
    
    @Resource(name="collecFormIdTaskExecutor")
    private ThreadPoolTaskExecutor executor;
    
    /**
     * 请求调用前处理
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
    	if(handler instanceof HandlerMethod) {
    		// 加上这个注解不校验接口权限
    		NoVerify noVerify = ((HandlerMethod) handler).getMethod().getAnnotation(NoVerify.class);
    		if (noVerify != null){
    			return true;
    		}
    		User user = userService.getLoginUser(request);
    		request.setAttribute("loginUser", user);
    		
    		// 开始收集小程序用户formId任务
    		dealCollectXcxFormIdTask(request, user);
    	}
    	return true;
    }
    
    /**
     * 收集xcx用户formId
     * @param request
     */
	private void dealCollectXcxFormIdTask(HttpServletRequest request, User user) {
		String xcxFormIds = request.getParameter("xcxFormIds");
		if(StringUtils.isNotBlank(xcxFormIds)) {
			executor.execute(new CollectXcxFormIdTask(user.getId(), xcxFormIds));
		}
	}
}

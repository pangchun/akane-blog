package github.akanemiku.akaneblog.interceptor;

import github.akanemiku.akaneblog.constant.Types;
import github.akanemiku.akaneblog.constant.WebConst;
import github.akanemiku.akaneblog.dto.MetaDTO;
import github.akanemiku.akaneblog.model.Option;
import github.akanemiku.akaneblog.service.ContentService;
import github.akanemiku.akaneblog.service.MetaService;
import github.akanemiku.akaneblog.service.OptionService;
import github.akanemiku.akaneblog.utils.Commons;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

/**
 * 自定义拦截器
 */
@Component
public class BaseInterceptor implements HandlerInterceptor {

    private static final String USER_AGENT = "User-Agent";


//    @Autowired
//    private UserService userService;
//
    @Autowired
    private OptionService optionService;

    @Autowired
    private Commons commons;

    @Autowired
    private MetaService metaService;

    @Autowired
    private ContentService contentService;
    @Autowired
    private HttpSession session;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        // 请求URL不包含域名
        String uri = request.getRequestURI();

        // 请求拦截处理
//        UserDomain user = SpecialUtil.getLoginUser(request);
//        if (null == user) {
//            Integer uid = SpecialUtil.getCookieUid(request);
//            if (null != uid) {
//                //这里还是有安全隐患,cookie是可以伪造的
//                user = userService.getUserInfoById(uid);
//                request.getSession().setAttribute(WebConst.LOGIN_SESSION_KEY, user);
//            }
//        }

//        if (uri.startsWith("/admin") && !uri.startsWith("/admin/login") && null == user
//                && !uri.startsWith("/admin/css") && !uri.startsWith("/admin/images")
//                && !uri.startsWith("/admin/js") && !uri.startsWith("/admin/plugins")
//                && !uri.startsWith("/admin/editormd")) {
//            response.sendRedirect(request.getContextPath() + "/admin/login");
//            return false;
//        }
        // 设置GET请求的token
        if (request.getMethod().equals("GET")) {
//            String csrf_token = UUID.UU64();
            // 默认存储30分钟
//            cache.hset(Types.CSRF_TOKEN.getType(), csrf_token, uri,30 * 60);
//            request.setAttribute("_csrf_token", csrf_token);
        }
        // 返回true才会执行postHandle
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView view) throws Exception {
        // TODO 移到工具类中，在controller中加载
        //获得网站备案
        Option option = optionService.getByName("site_record");
        // 分类总数
        Long categoryCount = metaService.getMetasCountByType(Types.CATEGORY.getType());
        // 标签总数
        Long tagCount = metaService.getMetasCountByType(Types.TAG.getType());
        // 获取文章总数
        Long countentCount = contentService.getContentsCount();
        // 获取友情链接
        List<MetaDTO> links = metaService.getMetaList(Types.LINK.getType(),null,WebConst.MAX_POSTS);

        session.setAttribute("categoryCount",categoryCount);
        session.setAttribute("tagCount",tagCount);
        session.setAttribute("articleCount",countentCount);
        session.setAttribute("links",links);
        request.setAttribute("commons", commons);
        request.setAttribute("option", option);
        //加载配置项
        initSiteConfig();
    }

    private void initSiteConfig() {
        if (WebConst.initConfig.isEmpty()) {
            List<Option> options = optionService.getOptions();
            Map<String, String> querys = new HashMap<>();
            options.forEach(option -> {
                querys.put(option.getName(),option.getValue());
            });
            WebConst.initConfig = querys;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}

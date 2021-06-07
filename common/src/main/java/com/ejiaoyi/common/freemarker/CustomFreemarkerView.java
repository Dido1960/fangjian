package com.ejiaoyi.common.freemarker;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Freemarker 自定义视图，该类继承Freemarker提供的默认视图类
 *
 * @author Z0001
 * @since 2020-03-17
 */
public class CustomFreemarkerView extends FreeMarkerView {

    @Override
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
        String ctx = request.getContextPath();
        String base = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + ctx;
        model.put("ctx", ctx);
        model.put("base", base);
        model.put("sid", request.getSession().getId());
        super.exposeHelpers(model, request);
    }
}

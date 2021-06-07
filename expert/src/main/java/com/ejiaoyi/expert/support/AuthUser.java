package com.ejiaoyi.expert.support;

import com.ejiaoyi.expert.constant.ExpertKey;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * spring security 安全认证用户
 *
 * @author Z0001
 * @since 2020-03-17
 */
@Data
@Builder
public class AuthUser implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户主键
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String name;

    /**
     * 区划id
     */
    private Integer regId;

    /**
     * 用户状态
     */
    private Integer enabled;

    /**
     * 当前评标标段id
     */
    private Integer bidSectionId;

    /**
     * 是否为评标组长
     */
    private String isChairman;

    /**
     * 当前专家是否签名
     * 1：签名成功
     */
    private Integer signStatus;

    /**
     * 相同专家存在多个标段的情况
     */
    private Map<Integer, Integer> sectionIdAndUserId;

    /**
     * 用户角色列表
     */
//    private List<RoleUser> roleUserList;

    /**
     * 用户菜单列表
     */
    private List<Menu> menuList;


    @Override
    public String getUsername() {
        return loginName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    /**
     * 帐户是否未过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 帐户是否未被冻结
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 帐户密码是否未过期，一般有的密码要求性高的系统会使用到，比如每隔一段时间就要求用户重置密码
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否可用
     */
    @Override
    public boolean isEnabled() {
        return this.enabled != null && (this.enabled == 1);
    }


    public Integer getBidSectionId() {
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            HttpSession session = request.getSession();
            Integer bid = (Integer) session.getAttribute(ExpertKey.BID_SECTION_ID);
            if (bid != null) {
                return bid;
            }
        }
        return bidSectionId;

    }

    public void setBidSectionId(Integer bidSectionId) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        session.setAttribute(ExpertKey.BID_SECTION_ID, bidSectionId);
    }

    public Integer getUserId() {
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            HttpSession session = request.getSession();
            Integer uid = (Integer) session.getAttribute(ExpertKey.USER_ID);
            if (uid != null) {
                return uid;
            }
        }
        return userId;

    }

    public void setUserId(Integer userId) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        session.setAttribute(ExpertKey.USER_ID, userId);
    }

    public String getIsChairman() {
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            HttpSession session = request.getSession();
            String isChairman = (String) session.getAttribute(ExpertKey.IS_CHAIRMAN);
            if (isChairman != null) {
                return isChairman;
            }
        }
        return isChairman;
    }

    public void setIsChairman(String isChairman) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        session.setAttribute(ExpertKey.IS_CHAIRMAN, isChairman);
    }

}

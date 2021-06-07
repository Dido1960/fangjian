package com.ejiaoyi.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.admin.mapper.UserStyleMapper;
import com.ejiaoyi.admin.service.IUserStyleService;
import com.ejiaoyi.common.entity.UserStyle;
import com.ejiaoyi.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户界面 服务实现类
 * </p>
 *
 * @author samzqr
 * @since 2020-05-22
 */
@Service
public class UserStyleServiceImpl extends BaseServiceImpl implements IUserStyleService {


    @Autowired
    UserStyleMapper userStyleMapper;

    @Override
    public Boolean addUserNote(Integer uid, String note) {
        QueryWrapper<UserStyle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("USER_ID", uid);
        UserStyle userStyle = userStyleMapper.selectOne(queryWrapper);
        if (userStyle != null) {
            userStyle.setNote(note);
            int i = userStyleMapper.updateById(userStyle);
            if (i == 1) {
                return true;
            }
        } else {
            UserStyle style = new UserStyle();
            style.setNote(note);
            style.setUserId(uid);
            int insert = userStyleMapper.insert(style);
            if (insert == 1) {
                return true;
            }
        }

        return false;
    }

    @Override
    public UserStyle getNoteByUid(Integer uid) {
        QueryWrapper<UserStyle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("USER_ID", uid);
        UserStyle userStyle = userStyleMapper.selectOne(queryWrapper);
        return userStyle;
    }

    @Override
    public Boolean addUserStyle(Integer uid, String theme) {
        QueryWrapper<UserStyle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("USER_ID", uid);
        UserStyle userStyle = userStyleMapper.selectOne(queryWrapper);
        if (userStyle != null) {
            userStyle.setTheme(theme);
            int i = userStyleMapper.updateById(userStyle);
            if (i == 1) {
                return true;
            }
        } else {
            UserStyle style = new UserStyle();
            style.setTheme(theme);
            style.setUserId(uid);
            int insert = userStyleMapper.insert(style);
            if (insert == 1) {
                return true;
            }
        }

        return false;
    }


}

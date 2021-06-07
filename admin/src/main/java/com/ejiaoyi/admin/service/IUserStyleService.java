package com.ejiaoyi.admin.service;

import com.ejiaoyi.common.entity.UserStyle;

/**
 * <p>
 * 用户界面 服务类
 * </p>
 *
 * @author samzqr
 * @since 2020-05-22
 */
public interface IUserStyleService {


    /**
     * 根据用户的id存储便签信息
     *
     * @param uid  用户id
     * @param note 便签内容
     * @return
     */
    Boolean addUserNote(Integer uid, String note);

    /**
     * 根据用户id获取用户的风格
     *
     * @param uid
     * @return
     */
    UserStyle getNoteByUid(Integer uid);

    /**
     * 根据用户id新增用户界面风格
     *
     * @param uid   用户id
     * @param theme 界面风格
     * @return
     */
    Boolean addUserStyle(Integer uid, String theme);

}

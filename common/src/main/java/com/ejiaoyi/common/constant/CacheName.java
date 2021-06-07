package com.ejiaoyi.common.constant;

/**
 * @author Z0001
 * @since 2020/4/30
 */
public interface CacheName {

    String WORDBOOK_REDIS_CACHE = "wordbook_redis_cache";

    String CONFIG_MAP_REDIS_CACHE = "config_map_redis_cache";

    String MENU_LIST_REDIS_CACHE = "menu_list_redis_cache";

    String MENU_ROLE_LIST = "menu_role_list_cache";

    String API_REDIS_CACHE = "api_redis_cache";

    String USER_INFO_CACHE = "user_info_cache";

    String INVOICE_CALL_RESULT_CACHE = "Invoice_Call_Result_CACHE";


    /**
     * @return
     * @author lesgod
     * @date 2020/6/5 9:49
     */
    String COMPANY_USER_CACHE = "COMPANY_USER_CACHE:";

    /**
     * @return
     * @author lesgod
     * @date 2020/6/5 9:49
     */
    String CONFIG_MAP_CACHE = "CONFIG_MAP_CACHE:";

    /**
     * 标段缓存
     *
     * @author Make
     * @date 2020/7/21
     */
    String BID_SECTION_CACHE = "BID_SECTION_CACHE:";

    /**
     * 招标文件缓存
     *
     * @author Make
     * @date 2020/7/21
     */
    String TENDER_DOC_CACHE = "TENDER_DOC_CACHE:";

    /**
     * @Description 投标人开标信息缓存
     * @Author liuguoqiang
     * @Date 2020-8-6 15:42
     */
    String BIDDER_OPEN_INFO = "BIDDER_OPEN_INFO:";

    /**
     * @Description 评标办法
     * @Author Make
     * @Date 2020-9-28 15:42
     */
    String GRADE_CACHE = "GRADE_CACHE:";

    /**
     * @Description 专家评审表信息
     * @Author Make
     * @Date 2020-8-6 15:42
     */
    String EXPERT_REVIEW = "EXPERT_REVIEW:";

    /**
     * @Description 投标人评审点
     * @Author Make
     * @Date 2020-9-28 15:42
     */
    String REVIEW_POINT = "REVIEW_POINT:";


    /**
     * @Description 回退推送
     * @Author liuguoqiang
     * @Date 2020-10-10 17:34
     */
    String BACK_PUSH = "BACK_PUSH:";

    /**
     * @Description 下载投标人文件缓存
     * @Author Make
     * @Date 2020-11-17 15:42
     */
    String DOWN_BIDDER_FILE = "DOWN_BIDDER_FILE:";

    /**
     * @Description 下载投标人文件缓存
     * @Author Make
     * @Date 2020-11-17 15:42
     */
    String LINE_STATUS = "LINE_STATUS:";

    /**
     * @Description 评标申请
     * @Author Mike
     * @Date 2020-11-30 15:42
     */
    String BID_APPLY_CACHE = "BID_APPLY_CACHE:";

    /***
     * @Description 投标人文件 缓存
     * @Author lesgod
     * @Date 2021-01-12 15:42
     * ***/
    String BIDDER_FILE_INFO = "BIDDER_FILE_INFO:";

    /***
     * @Description fsdf文件缓存 id
     * @Author lesgod
     * @Date 2021-01-12 15:42
     * ***/
    String FDFS_ID_INFO = "FDFS_ID_INFO:";
    /***
     *@Description fsdf文件缓存 mark
     * @Author lesgod
     * @Date 2021-01-12 15:42
     * ***/
    String FDFS_MARK_INFO = "FDFS_MARK_INFO:";

    /***
     * @Description 评标报告对应的转换流程进度
     * @Author lesgod
     * @Date 2021-01-12 15:42
     * ***/
    String REPORT_FLOWS_DTO = "REPORT_FLOWS_DTOS:";

    /***
     * @Description 评标报告PDF签名信息
     * @Author lesgod
     * @Date 2021-01-12 15:42
     * ***/
    String GET_REPORT_PDF_SIGN_INFO = "GET_PDF_SIGN_INFO:";

    /***
     * @Description 评标报告生成的状态值
     * @Author lesgod
     * @Date 2021-01-12 15:42
     * ****/
    String GENERATE_EVAL_REPORT_ = "GENERATE_EVAL_REPORT:";

    /***
     * @Description 当前回退的专家
     * @Author lesgod
     * @Date 2021-01-12 15:42
     * ****/
    String ROLL_BACK_NOW_EXPERT = "CURRENT_FALL_BACK_:";


    /***
     *Description 回退申请ID
     * @Author lesgod
     * @Date 2021-01-12 15:42
     */
    String BEFORE_ROLLBACK_ = "BEFORE_ROLLBACK_:";

    /**
     * 专家 消息 推送
     */
    String EXPERT_USER_MSG_ = "EXPERT_USER_MSG:";


    /***
     *Description 回退申请ID
     * @Author lesgod
     * @Date 2021-01-12 15:42
     */
    String ROLLBACK_APPLY_LOCK = "ROLLBACK_APPLY_LOCK:";

    /***
     *Description 每个专家回退申请状态list
     * @Author lesgod
     * @Date 2021-01-12 15:42
     */
    String EXPERT_USER_TEMPS = "EXPERT_USER_TEMPS_:";

    /***
     * 评标报告生成的状态值
     * ****/
    String GENERATE_EVAL_REPORT_LOCK = "GENERATE_EVAL_REPORT_LOCK:";


    /***
     * 文件上传记录
     * ****/
    String  UPLOAD_FILE = " UploadFile:";


    /**
     * TOKEN缓存
     */
    String TOKEN = "TOKEN:";

    /**
     * 菜单缓存
     **/
    String  MENU = "menu:";


    /**
     * 手机扫码登录缓存
     **/
    String PHONE_LOGIN = "PHONE_LOGIN:";

}


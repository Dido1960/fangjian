package com.ejiaoyi.common.constant;
/**
 * 支付宝身份认证
 */
public class AlipayIdentityAuthConstant {

    /**
     * 身份信息参数类型
     */
    public static final String IDENTITY_TYPE = "CERT_INFO";

    /**
     * 证件类型
     */
    public static final String CERT_TYPE = "IDENTITY_CARD";

    /**
     * 需要回跳的目标地址，一般指定为商户业务页面
     */
    public static final String RETURN_URL = "http://www.ejiaoyi.xin";

    /**
     * 成功编码
     */
    public static final String SUCCESS_CODE = "10000";

    /**
     * 认证成功
     */
    public static final String VERIFY_PASSED_T = "T";

    /**
     * 支付宝身份认证常量
     */
    public interface AlipayConstants {
        /**
         * 创建应用后生成的APPID
         */
        String APP_ID = "2021002128673658";

        /**
         * 开发者私钥
         */
        String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCay5rlo/r8TOGOp+/Cc16Q6oKRnUqSjJcstbdGa/vWH41u8zcX+2GFjdJ+Aete/nEtssW+viyOFzDhTVl05suVqamsOLFxBCDdUHTrijuzUwP1QraJVy2PUhT5M0tt0122wJF8i27oz3yvRixTjFptHpRFC+iM82V9ZHJXRCONCSW/0dHxBedULLuwYy+ciYc6ysW3tIcvNZ9qHwTotS4Lxf04VNvQ51PnZYm3XSdF3iwBMWrg61h2WU/lKHE6WtODBwJPKtkzOuKT3mi6T7DTz3XDM5eySedu4d/Gdm08LVbZkM7AdtDzw3gVMK0adLhnCMXkcNKVY1xFRo7njsdNAgMBAAECggEAOmnY8w2VWf49I9M1w+A7cF4+lWqQyIqrRaDxH28CYsQXYCYBxVgEbhydsRVSV8+WsBMFUjx7ncccl/jTaCzr6HdU4vul4isXp8yDwpqwerQ6CyR4s40lUS89YOi9bx1TAPLcM+5+FPava4kEkyakvQbMFt4ujHoQ82ejSlEZRU/3IdoSpHDpRKSdxokVcge2K4o5IirSnjzMffBfmRRvQIQva+iK1XNpreH158+3mMsghfwoU2ckfVu1WeLVuGwnAV+QVo1eFSDnNOU4x9ZriTPJGwP/l9Pa0SJ4Txx7wFE4FwqB1rladouKyzkEJqSfAwCjF21WVBl30xDBlULUoQKBgQDJJgXltu32bFJeo7hH+s3bb7wb+V0vt2FKMTE2h16ukRP02s9stnUmmborSKvD3OSP95MDiC+lb6Gcw4lFU5kwh1cDQ9xjWetu0Djvk08HOISIsMkOcKGcNj2a4/Sx34iTgp2YnQISbytEFp3ZGzXxVnjITgY9J/TPF9DjwK1hVQKBgQDFAbZDNvDjZthMmX0+vMo7v0tGFjRDWCkFNXu6eEa1a7uw78QSp7q9If2FBlLoM+AUaaGRaKi8mJj4MIlC/h7rOvhNu/IMCCvUYtifbl+PY/YSll/eMPyulZLKlmp5R95ijTIJBliQ2UTPEiZkvnKsMkfR3Ne7L04R0zMvbpcuGQKBgQCebYZDkbsHVfXieIzSlKXC6dR9LADLbmIAb5JVYnWehMH1Hso009idmILbSJeQ4tBa3CI8q+zrHmtp71CU++0yHcbY2YAh+FbIptPRTnL2OvuYNbWF3AxN3yLSdizqVCdcF4pNA4z4O/sV28Jjl1z7QqJLEbk0Pd5Bpn5ug/mLKQKBgHULB1X3ljYELA2WQyq6zwugIwCIwJQiFHzbwZ6PRrMSRnhgeoT60Z78Srp1f1wlNcPq3eqd22Hw5zDM5T+/6m6mok7MkolDcYbFEx3lXTC7mr6ASIShZf3zLzrdEnTrldvHNyrVcljcPF0p01WXMFnNpY2xPxoeiKteZtKPSZJBAoGADKvD7DLh0BEsnUoit4IsD8P09PPjYeAsXAIUdrCOyhWZsi9q+1vWrV/Pmj2i0/1P5HBQ0nayYez1zPC2fYzYXt5QoqxKeys8KM7fkNLTPGHJ8NZ8nIZmcABL48EjzH8EPgKMnGRPv+m6jRx5jnhny4wXPF//VAzMFJVZobnIrIA=";

        /**
         * 支付宝公钥
         */
        String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAki1V9t5XNuw1yHeNLfRXKhmfXFmGaOoiFuSw8L/yMJY7tNy7vk0ayf0270mpk44xeXI9g+GrbvvOCstFE0BQiRjnIR/6OJF1/kq872jiOd93L1UqQfLhrXBSnUZ2nmrT/K+DPBVkFoi6Ee/zNsIZukiQLfygY/PwFjbetSN/ymmTCe1g4lZpV7Av0vJZEj69m4MKryA7osfTAbmkf51SlLbUw7zc8eUpDxaH2y6eH1rSkZujRR1IBwT9tVh+RUzJgVNXVLI485IwHIACPrmAs+U5acXkiOP5Wk0jOYQ1/hhNxfR7Gp+Ji7JsSIVeTzCDTutwda/DJKApiJ2zGuwEWQIDAQAB" ;

    }

    /**
     * 认证场景码
     */
    public interface BizCodeConstants {
        /**
         * 多因子人脸认证
         */
        String FACE = "FACE";
        /**
         * 多因子证照认证
         */
        String CERT_PHOTO = "CERT_PHOTO";
        /**
         * 多因子证照和人脸认证
         */
        String CERT_PHOTO_FACE = "CERT_PHOTO_FACE";
        /**
         * 多因子快捷认证
         */
        String SMART_FACE = "SMART_FACE";
    }

    /**
     * 支付宝接口常量
     */
    public interface ApiInterfaceConstants {
        /**
         * 身份认证初始化服务（固定）
         */
        String INITIALIZE = "alipay.user.certify.open.initialize";
        /**
         * 身份认证开始认证（固定）
         */
        String CERTIFY = "alipay.user.certify.open.certify";
        /**
         * 身份认证记录查询（固定）
         */
        String QUERY = "alipay.user.certify.open.query";
    }

    /**
     * 支付宝请求接口常量
     */
    public static class AliHttpInterface {
        /**
         * 支付宝网关（固定）
         */
        public static final String URL = "https://openapi.alipay.com/gateway.do";

    }
}

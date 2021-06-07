package com.ejiaoyi.common.crypto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SM2 国密编码器
 *
 * @author Z0001
 * @since 2020-5-15
 */
public class SM2PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        String encrypt = null;
        try {
            encrypt = SM2Util.encrypt(rawPassword.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encrypt;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword != null && encodedPassword.length() != 0) {
            try {
                String decrypt = SM2Util.decrypt(encodedPassword);

                return StringUtils.equals(decrypt, rawPassword.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
}

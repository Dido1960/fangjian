package com.ejiaoyi.common.util;

import jp.sourceforge.qrcode.data.QRCodeImage;

import java.awt.image.BufferedImage;

/**
 * @author fengjunhong
 * @since 2020/5/26
 */
public class TwoDimensionCodeImage implements QRCodeImage {
    BufferedImage bufImg;

    public TwoDimensionCodeImage(BufferedImage bufImg) {
        this.bufImg = bufImg;
    }

    @Override
    public int getHeight() {
        return bufImg.getHeight();
    }

    @Override
    public int getPixel(int x, int y) {
        return bufImg.getRGB(x, y);
    }

    @Override
    public int getWidth() {
        return bufImg.getWidth();
    }
}

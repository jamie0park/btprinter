package com.yefeng.night.btprinter.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by yefeng on 6/15/15.
 * github:yefengfreedom
 */
public class ImageUtils {

    /**
     * Zoom the image by the specified width
     *
     * @param bitmap Original Image
     * @param width  Specify the width
     * @return Zoom the picture
     */
    public static Bitmap zoomBitmapByWidth(Bitmap bitmap, int width) {
        if (width <= 0 || null == bitmap) {
            return bitmap;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float scale = (float) width / (float) w;
        if (scale <= 0) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); //Long and wide zoom ratio
        try {
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return bitmap;
        }
        return bitmap;
    }

}

package com.yefeng.night.btprinter.print;

/**
 * Created by yefeng on 6/2/15.
 * github:yefengfreedom
 * <p/>
 * printer command
 */
public class GPrinterCommand {

    public static final byte[] left = new byte[]{0x1b, 0x61, 0x00};// Left
    public static final byte[] center = new byte[]{0x1b, 0x61, 0x01};// Center
    public static final byte[] right = new byte[]{0x1b, 0x61, 0x02};// Right
    public static final byte[] bold = new byte[]{0x1b, 0x45, 0x01};// Choose bold mode
    public static final byte[] bold_cancel = new byte[]{0x1b, 0x45, 0x00};// Cancel bold mode
    public static final byte[] text_normal_size = new byte[]{0x1d, 0x21, 0x00};// The font does not zoom in
    public static final byte[] text_big_height = new byte[]{0x1b, 0x21, 0x10};// High doubled
    public static final byte[] text_big_size = new byte[]{0x1d, 0x21, 0x11};// Width and height doubled
    public static final byte[] reset = new byte[]{0x1b, 0x40};//Reset the printer
    public static final byte[] print = new byte[]{0x0a};//Print and wrap
    public static final byte[] under_line = new byte[]{0x1b, 0x2d, 2};//Underline
    public static final byte[] under_line_cancel = new byte[]{0x1b, 0x2d, 0};//Cancel underline

    /**
     * Take paper
     *
     * @param n Rows
     * @return command
     */
    public static byte[] walkPaper(byte n) {
        return new byte[]{0x1b, 0x64, n};
    }

    /**
     * Set the horizontal and vertical movement units
     *
     * @param x Move horizontally
     * @param y Move vertically
     * @return command
     */
    public static byte[] move(byte x, byte y) {
        return new byte[]{0x1d, 0x50, x, y};
    }

}

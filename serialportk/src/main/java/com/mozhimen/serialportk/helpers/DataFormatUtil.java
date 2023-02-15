package com.mozhimen.serialportk.helpers;

import android.util.Log;

/**
 * 西安地铁格式转换格式验证工具类
 */
public class DataFormatUtil {

    // 0x02   报文正文长度   报文正文	   crc16	0x03
    // 1Byte	 4Byte	     nByte	   4Byte	1Byte

    private static int HEAD = 0x02;
    private static int END = 0x03;
    private static String TAG = "DataFormatUtil>>>>>";

    public static boolean isValid(byte[] data) {
        if (data.length <= 10) {
            return false;
        } // 最小长度初查

        if (data[0] != HEAD || data[data.length - 1] != END) {
            return false;
        } // 头尾校验

        int length = (ascii2int(data[1]) & 0x000F) << 12 | (ascii2int(data[2]) & 0x000F) << 8
                | (ascii2int(data[3]) & 0x000F) << 4 | (ascii2int(data[4]) & 0x000F);

        if (data.length != length + 10) {
            return false;
        } // 长度校验

        int crc16 = (ascii2int(data[data.length - 5]) & 0x000F) << 12
                | (ascii2int(data[data.length - 4]) & 0x000F) << 8
                | (ascii2int(data[data.length - 3]) & 0x000F) << 4 | (
                ascii2int(data[data.length - 2]) & 0x000F);
        byte[] body = new byte[length];
        System.arraycopy(data, 5, body, 0, length);
        int calCrc16 = calCrc16(body);

        if (crc16 != calCrc16) {
            return false;
        } // crc校验
        return true;
    }

    public static boolean isValidCRC16(byte[] data) {
        if (data.length <= 5) {
            return false;
        } // 最小长度初查

        if (data[0] != HEAD || data[data.length - 1] != END) {
            return false;
        } // 头尾校验

        int length = data[2];

        if (data.length != length) {
            return false;
        } // 长度校验

        int crc16 = (data[data.length - 3] & 0xFF00) >> 8 | (data[data.length - 2] & 0x00FF) << 8;
        byte[] body = new byte[length - 3];
        System.arraycopy(data, 0, body, 0, length - 3);
        int calCrc16 = CRC16Util.getCrc16ToInt(body);

        if (crc16 != calCrc16) {
            return false;
        } // crc校验
        return true;
    }

    /**
     * 校验跳过CRC
     *
     * @param data
     * @return
     */
    public static boolean isValidNoCRC(byte[] data) {
        if (data.length <= 10) {
            return false;
        } // 最小长度初查

        if (data[0] != HEAD || data[data.length - 1] != END) {
            return false;
        } // 头尾校验

        int length = (ascii2int(data[1]) & 0x000F) << 12 | (ascii2int(data[2]) & 0x000F) << 8
                | (ascii2int(data[3]) & 0x000F) << 4 | (ascii2int(data[4]) & 0x000F);

        if (data.length != length + 10) {
            Log.i(TAG, "data.length" + data.length);
            Log.i(TAG, "length" + length);
            return false;
        } // 长度校验

        return true;
    }

    public static byte[] getBody(byte[] data) {
        if (!isValid(data)) {
            return null;
        }

        int length = (ascii2int(data[1]) & 0x000F) << 12 | (ascii2int(data[2]) & 0x000F) << 8
                | (ascii2int(data[3]) & 0x000F) << 4 | (ascii2int(data[4]) & 0x000F);

        byte[] body = new byte[length];
        System.arraycopy(data, 5, body, 0, length);

        return body;
    }

    private static int ascii2int(int ascii) { // '5' ascci 是 53。 输入 int 53，输出 int 5
        int value = Character.getNumericValue(ascii);
        return value;
    }

    private static int calCrc16(byte[] buf) {
        return CRCKermit.crc_kermit(buf);
    }
}

package com.piles.common.util;

import com.google.common.primitives.Bytes;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Stack;

public class BytesUtil {
    /**
     * 将int数值转换为占两个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。
     */
    public static byte[] intToBytes(int value) {
        //limit 传入2
        return intToBytes(value, 2);
    }

    public static byte[] intToBytes(int value, int limit) {
        byte[] src = new byte[limit];
        for (int i = 0; i < limit; i++) {
            int x = 8 * (limit - i - 1);
            if (x == 0) {
                src[i] = (byte) (value & 0xFF);
            } else {
                src[i] = (byte) ((value >> x) & 0xFF);
            }
        }
        return src;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。
     */
    public static int bytesToInt(byte[] src, int offset) {

        if (src == null) {
            return 0;
        }
        while (src.length > 0 && src[0] == 0x00) {
            src = BytesUtil.copyBytes(src, 1, src.length - 1);
        }
        if (src.length == 0) {
            return 0;
        }
        int len = src.length;
        if (len == 0) {
            return 0;
        }
        int value = 0;
        for (int i = 0; i < len; i++) {
            if (i == (len - 1)) {
                value = value | ((src[i] & 0xFF));
            }
            value = value | ((src[i] & 0xFF) << (8 * (len - i - 1)));
        }
//        value = (int) (  ((src[offset] & 0xFF)<<8)
//                |(src[offset+1] & 0xFF));
//        value = (int) ( ((src[offset] & 0xFF)<<24)
//                |((src[offset+1] & 0xFF)<<16)
//                |((src[offset+2] & 0xFF)<<8)
//                |(src[offset+3] & 0xFF));
        return value;
    }

    /**
     * long转字节，大端模式
     *
     * @param number
     * @return
     */
    public static byte[] long2Byte(long number) {
        long temp = number;
        byte[] b = new byte[8];
        for (int i = (b.length - 1); i >= 0; i--) {
            b[i] = new Long(temp & 0xff).byteValue();//
            //将最低位保存在最低位
            temp = temp >> 8;// 向右移8位
        }
        return b;
    }

    /**
     * 字节转long 大端模式
     *
     * @param b
     * @return
     */
    public static long byte2Long(byte[] b) {
        long s = 0;
        long s0 = b[7] & 0xff;
        long s1 = b[6] & 0xff;
        long s2 = b[5] & 0xff;
        long s3 = b[4] & 0xff;
        long s4 = b[3] & 0xff;
        long s5 = b[2] & 0xff;
        long s6 = b[1] & 0xff;
        long s7 = b[0] & 0xff;

        // s0不变
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return s;
    }

    /**
     * 从一个byte数组中拷贝一部分出来
     *
     * @param oriBytes
     * @param startIndex
     * @param length
     * @return
     */
    public static byte[] copyBytes(byte[] oriBytes, int startIndex, int length) {
        int endIndex = startIndex + length;

        byte[] bts = new byte[length];

        int index = 0;
        for (int i = 0; i < oriBytes.length; i++) {
            if (i >= startIndex && i < endIndex) {
                bts[index] = oriBytes[i];
                index++;
            }
        }

        return bts;
    }


    /**
     * 将byte[]转为各种进制的字符串
     *
     * @param bytes byte[]
     * @param radix 基数可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix) {
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }
    /** */
    /**
     * @函数功能: BCD码转为10进制串(阿拉伯数据)
     * @输入参数: BCD码
     * @输出结果: 10进制串
     */
    public static String bcd2Str(byte[] bytes) {
        StringBuffer temp = new StringBuffer(bytes.length * 2);

        for (int i = 0; i < bytes.length; i++) {
            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
            temp.append((byte) (bytes[i] & 0x0f));
        }
        return temp.toString();
    }
    /** */
    /**
     * @函数功能: BCD码转为10进制串(阿拉伯数据) 小端模式
     * @输入参数: BCD码
     * @输出结果: 10进制串
     */
    public static String bcd2StrLittle(byte[] bytes) {
        Stack<String> strings = new Stack<>();
        String temp = bcd2Str(bytes);
        for (int i = 0; i < temp.length(); i = i + 2) {
            strings.push(temp.substring(i, i + 2));

        }
        StringBuilder stringBuilder = new StringBuilder();
        while (!strings.isEmpty()) {
            stringBuilder.append(strings.pop());
        }
        return stringBuilder.toString();
    }

    /**
     * @函数功能: 10进制串转为BCD码
     * @输入参数: 10进制串
     * @输出结果: BCD码
     */
    public static byte[] str2Bcd(String asc) {
        if (asc == null) {
            asc = "";
        }
        int len = asc.length();
        int mod = len % 2;

        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }

        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }

        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;

        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }

            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }

            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    /**
     * @函数功能: 10进制串转为BCD码
     * @输入参数: 10进制串
     * @输出结果: BCD码
     */
    public static byte[] str2BcdLittle(String asc) {
        byte[] temp = str2Bcd(asc);
        return revert(temp);
    }

    /**
     * 将int转为byte 小端模式
     *
     * @param value int值
     * @return
     */
    public static byte[] intToBytesLittle(int value) {
        //limit 传入2
        return intToBytes(value, 2);
    }

    /**
     * 将int转换byte 小端模式
     *
     * @param value int值
     * @param limit 保留长度
     * @return
     */
    public static byte[] intToBytesLittle(int value, int limit) {
        byte[] src = new byte[limit];
        for (int i = 0; i < limit; i++) {
            int x = 8 * i;
            if (x == 0) {
                src[i] = (byte) (value & 0xFF);
            } else {
                src[i] = (byte) ((value >> x) & 0xFF);
            }
        }
        return src;

    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后 )的顺序。小端模式
     */
    public static int bytesToIntLittle(byte[] src) {

        if (src == null) {
            return 0;
        }
        int len = src.length;
        if (len == 0) {
            return 0;
        }
        int value = 0;
        for (int i = 0; i < len; i++) {
            value = value | ((src[i] & 0xFF) << (8 * i));
        }
        return value;
    }

    /**
     * long转字节，小端模式
     *
     * @param number
     * @param limit  保留字节位
     * @return
     */
    public static byte[] long2ByteLittle(long number, int limit) {
        long temp = number;
        byte[] b = new byte[limit];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Long(temp & 0xff).byteValue();//
            //将最低位保存在最前面
            temp = temp >> 8;// 向右移8位
        }
        return b;
    }


    /**
     * 字节转long 小端模式
     *
     * @param src
     * @return
     */
    public static long byte2LongLittle(byte[] src) {
        long s = 0;
        for (int i = 0; i < src.length; i++) {
            //防止转为int
            long si = src[i] & 0xFF;
            si = si << (8 * i);
            s = s | si;
        }
        return s;
    }

    /**
     * 使用字节数组替换目标数组从指定位置开始替换字节
     *
     * @param target     被替换的数组
     * @param startIndex 开始位置
     * @param replace    用于替换的数组
     */
    public static void replaceBytes(byte[] target, int startIndex, byte[] replace) {
        //TODO 暂时由外界保证不会出数组越界的异常
        for (int i = 0; i < replace.length; i++) {
            int targetIndex = startIndex + i;
            target[targetIndex] = replace[i];
        }
    }

    /**
     * 创建数组
     *
     * @param bytes 用于替换的数组
     */
    public static byte[] createByteArray(byte... bytes) {
        byte[] temp = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            temp[i] = bytes[i];
        }
        return temp;

    }

    public static byte[] rightPadBytes(byte[] target, int len, byte b) {
        int length = target.length;
        if (len <= length) {
            return target;
        }
        int addedLen = len - length;
        byte[] added = new byte[addedLen];
        for (int i = 0; i < addedLen; i++) {
            added[i] = b;
        }
        return Bytes.concat(target, added);
    }

    public static byte[] rightPadBytes(String targetStr, int len, byte b) {
        if (targetStr == null) {
            targetStr = "";
        }
        byte[] target = targetStr.getBytes();
        return rightPadBytes(target, len, b);
    }

    /**
     * 获取循道默认控制域
     *
     * @return
     */
    public static byte[] xundaoControl() {

        return createByteArray((byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01);
    }

    /**
     * 获取循道默认控制域
     *
     * @return
     */
    public static byte[] xundaoControlInt2Byte(int serial) {

        return Bytes.concat(intToBytes(serial), intToBytes(serial));
    }

    /**
     * 根据整体控制域获得对应int值
     *
     * @return
     */
    public static int xundaoControlByte2Int(byte[] control) {


        return bytesToInt(copyBytes(control, 0, 2), 0);
    }

    public static String ascii2StrLittle(byte[] ascs) {
        byte[] data = revert(ascs);
        String asciiStr = null;
        try {
            asciiStr = new String(data, "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return asciiStr;
    }

    public static String ascii2Str(byte[] ascs) {
        byte[] data = ascs;
        String asciiStr = null;
        try {
            asciiStr = new String(data, "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return asciiStr;
    }

    public static byte[] str2AscLittle(String str) {
        return revert(str2Asc(str));
    }

    public static byte[] str2Asc(String str) {
        byte[] bytes = null;
        try {
            bytes = str.getBytes("ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static byte[] revert(byte[] temp) {
        byte[] ret = new byte[temp.length];
        for (int i = 0; i < temp.length; i++) {
            ret[temp.length - i - 1] = temp[i];
        }
        return ret;
    }

    /**
     * cp56time2a 格式转date格式
     */
    public static Date byteCp2Date(byte[] bytes) {
        if (bytes == null || bytes.length != 7) {
            return null;
        }
        int ms = bytesToIntLittle(copyBytes(bytes, 0, 2));
        int min = bytesToIntLittle(copyBytes(bytes, 2, 1));
        int hour = bytesToIntLittle(copyBytes(bytes, 3, 1));
        int day = bytesToIntLittle(copyBytes(bytes, 4, 1));
        int month = bytesToIntLittle(copyBytes(bytes, 5, 1));
        int year = bytesToIntLittle(copyBytes(bytes, 6, 1));
        if (month == 0 || day == 0 || year == 0) {
            return null;
        }

        LocalDateTime localDateTime = LocalDateTime.of(year + 2000, month, day, hour, min, ms / 1000);
        Date date = JdkDateUtil.localDateTime2Date(localDateTime);
        return date;
    }

    public static void main(String[] args) {

        byte[] concat = Bytes.concat(intToBytesLittle(20000, 2), intToBytesLittle(25, 1), intToBytesLittle(12, 1), intToBytesLittle(3, 1), intToBytesLittle(11, 1), intToBytesLittle(18, 1));
        Date date = byteCp2Date(concat);
        System.out.println(date);
        byte[] bytes = new byte[]{0x04, 0x28, 0x00, 0x00, 0x00, 0x30, 0x40, 0x31};

        System.out.println(bcd2StrLittle(bytes));
        for (byte b : str2BcdLittle("3140300000002804")) {
            System.out.println(Integer.toHexString((int) b));
        }
//        int i = bytesToInt("f".getBytes(), 0);
//        System.out.println(i);
//        System.out.println(3 << 1);
//        byte[] bytes = intToBytes(3,4);
//        byte[] bytes1 = intToBytesLittle(232424244,4);
//        System.out.println(bytesToIntLittle(bytes1));
//        long x = 5L;
//        System.out.println(Long.MAX_VALUE);
//        byte[] bytes2 = long2Byte(x);
//        System.out.println(binary(bytes2,2));
//        System.out.println(byte2LongLittle(bytes2));

////        byte[] bytes = intToBytes(30000,2);
//        int i = bytesToInt(bytes,0);
//        System.out.println(i);
//        System.out.println(BigDecimal.valueOf(BytesUtil.bytesToInt(bytes, 0)).divide(new BigDecimal(1000),3,BigDecimal.ROUND_HALF_UP));

//        String xx = "ffdf";
//        byte[] bytes = xx.getBytes();
//        System.out.println(bytes);


//        System.out.println(i);
//        System.out.println(s);
//        byte[] temp=str2Bcd("1000025484561835");
//        System.out.println(bcd2Str(new byte[]{temp[7]}));
//        System.out.println(Integer.toHexString(Byte.toUnsignedInt(temp[1])).toUpperCase());
//        byte[] bytes = intToBytes(123);
//        int i = bytesToInt(bytes, 0);
//        long i = 1l;
//        byte[] bytes = long2Byte(i);
//        System.out.println(byte2Long(bytes));

//        System.out.println(i);
//        byte[] bytes = BytesUtil.str2Bcd("171108");
//        String s = BytesUtil.bcd2Str(bytes);
//        System.out.println(s);
//        System.out.println(String.valueOf(0x53));
    }
}

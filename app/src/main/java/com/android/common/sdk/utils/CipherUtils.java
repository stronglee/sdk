/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.common.sdk.utils;

import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 加密与解密的工具类<br>
 *
 * @version 1.1
 */
public final class CipherUtils {
    /**
     * MD5加密
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 返回可逆算法DES的密钥
     *
     * @param key 前8字节将被用来生成密钥。
     * @return 生成的密钥
     * @throws Exception
     */
    public static Key getDESKey(byte[] key) throws Exception {
        DESKeySpec des = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        return keyFactory.generateSecret(des);
    }

    /**
     * 根据指定的密钥及算法，将字符串进行解密。
     *
     * @param data      要进行解密的数据，它是由原来的byte[]数组转化为字符串的结果。
     * @param key       密钥。
     * @param algorithm 算法。
     * @return 解密后的结果。它由解密后的byte[]重新创建为String对象。如果解密失败，将返回null。
     * @throws Exception
     */
    public static String decrypt(String data, Key key, String algorithm)
            throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        String result = new String(cipher.doFinal(StringUtils
                .hexStringToByteArray(data)), "utf8");
        return result;
    }

    /**
     * 根据指定的密钥及算法对指定字符串进行可逆加密。
     *
     * @param data      要进行加密的字符串。
     * @param key       密钥。
     * @param algorithm 算法。
     * @return 加密后的结果将由byte[]数组转换为16进制表示的数组。如果加密过程失败，将返回null。
     */
    public static String encrypt(String data, Key key, String algorithm)
            throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return StringUtils.byteArrayToHexString(cipher.doFinal(data
                .getBytes("utf8")));
    }

    /**
     * 将签名字符串转换成需要的32位签名
     */
    public static String hexdigest(byte[] paramArrayOfByte) {
        final char[] hexDigits = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97,
                98, 99, 100, 101, 102};
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            for (int i = 0, j = 0; ; i++, j++) {
                if (i >= 16) {
                    return new String(arrayOfChar);
                }
                int k = arrayOfByte[i];
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                arrayOfChar[++j] = hexDigits[(k & 0xF)];
            }
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * Created by zhy on 15/11/6.
     */
    public static class ImageUtils
    {
        /**
         * 根据InputStream获取图片实际的宽度和高度
         *
         * @param imageStream
         * @return
         */
        public static ImageSize getImageSize(InputStream imageStream)
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(imageStream, null, options);
            return new ImageSize(options.outWidth, options.outHeight);
        }

        public static class ImageSize
        {
            int width;
            int height;

            public ImageSize()
            {
            }

            public ImageSize(int width, int height)
            {
                this.width = width;
                this.height = height;
            }

            @Override
            public String toString()
            {
                return "ImageSize{" +
                        "width=" + width +
                        ", height=" + height +
                        '}';
            }
        }

        public static int calculateInSampleSize(ImageSize srcSize, ImageSize targetSize)
        {
            // 源图片的宽度
            int width = srcSize.width;
            int height = srcSize.height;
            int inSampleSize = 1;

            int reqWidth = targetSize.width;
            int reqHeight = targetSize.height;

            if (width > reqWidth && height > reqHeight)
            {
                // 计算出实际宽度和目标宽度的比率
                int widthRatio = Math.round((float) width / (float) reqWidth);
                int heightRatio = Math.round((float) height / (float) reqHeight);
                inSampleSize = Math.max(widthRatio, heightRatio);
            }
            return inSampleSize;
        }

        /**
         * 根据ImageView获适当的压缩的宽和高
         *
         * @param view
         * @return
         */
        public static ImageSize getImageViewSize(View view)
        {

            ImageSize imageSize = new ImageSize();

            imageSize.width = getExpectWidth(view);
            imageSize.height = getExpectHeight(view);

            return imageSize;
        }

        /**
         * 根据view获得期望的高度
         *
         * @param view
         * @return
         */
        private static int getExpectHeight(View view)
        {

            int height = 0;
            if (view == null) return 0;

            final ViewGroup.LayoutParams params = view.getLayoutParams();
            //如果是WRAP_CONTENT，此时图片还没加载，getWidth根本无效
            if (params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT)
            {
                height = view.getWidth(); // 获得实际的宽度
            }
            if (height <= 0 && params != null)
            {
                height = params.height; // 获得布局文件中的声明的宽度
            }

            if (height <= 0)
            {
                height = getImageViewFieldValue(view, "mMaxHeight");// 获得设置的最大的宽度
            }

            //如果宽度还是没有获取到，憋大招，使用屏幕的宽度
            if (height <= 0)
            {
                DisplayMetrics displayMetrics = view.getContext().getResources()
                        .getDisplayMetrics();
                height = displayMetrics.heightPixels;
            }

            return height;
        }

        /**
         * 根据view获得期望的宽度
         *
         * @param view
         * @return
         */
        private static int getExpectWidth(View view)
        {
            int width = 0;
            if (view == null) return 0;

            final ViewGroup.LayoutParams params = view.getLayoutParams();
            //如果是WRAP_CONTENT，此时图片还没加载，getWidth根本无效
            if (params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT)
            {
                width = view.getWidth(); // 获得实际的宽度
            }
            if (width <= 0 && params != null)
            {
                width = params.width; // 获得布局文件中的声明的宽度
            }

            if (width <= 0)

            {
                width = getImageViewFieldValue(view, "mMaxWidth");// 获得设置的最大的宽度
            }
            //如果宽度还是没有获取到，憋大招，使用屏幕的宽度
            if (width <= 0)

            {
                DisplayMetrics displayMetrics = view.getContext().getResources()
                        .getDisplayMetrics();
                width = displayMetrics.widthPixels;
            }

            return width;
        }

        /**
         * 通过反射获取imageview的某个属性值
         *
         * @param object
         * @param fieldName
         * @return
         */
        private static int getImageViewFieldValue(Object object, String fieldName)
        {
            int value = 0;
            try
            {
                Field field = ImageView.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                int fieldValue = field.getInt(object);
                if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE)
                {
                    value = fieldValue;
                }
            } catch (Exception e)
            {
            }
            return value;

        }
    }
}

/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package qian.xin.library.util;

import android.util.Log;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*通用字符串(String)相关类,为null时返回""
 * @author Lemon
 * @use StringUtil.xxxMethod(...);
 */
public class StringUtil {
    private static final String TAG = "StringUtil";


    private static String currentString = "";

    /*获取刚传入处理后的string
     * @must 上个影响currentString的方法 和 这个方法都应该在同一线程中，否则返回值可能不对
     * @return
     */
    public static String getCurrentString() {
        return currentString == null ? "" : currentString;
    }


    //获取string,为null时返回"" <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /*获取string,为null则返回""
     * @param tv
     * @return
     */
    public static String get(TextView tv) {
        if (tv == null || tv.getText() == null) {
            return "";
        }
        return tv.getText().toString();
    }

    /*获取string,为null则返回""
     * @param object
     * @return
     */
    public static String get(Object object) {
        return object == null ? "" : object.toString();
    }

    /*获取string,为null则返回""
     * @param cs
     * @return
     */
    public static String get(CharSequence cs) {
        return cs == null ? "" : cs.toString();
    }

    /*获取string,为null则返回""
     * @param s
     * @return
     */
    public static String get(String s) {
        return s == null ? "" : s;
    }


    /*deprecated 用get代替，这个保留到17.0
     */
    public static String getString(TextView tv) {
        return get(tv);
    }

    /*deprecated 用get代替，这个保留到17.0
     */
    public static String getString(Object object) {
        return get(object);
    }

    /*deprecated 用get代替，这个保留到17.0
     */
    public static String getString(CharSequence cs) {
        return get(cs);
    }

    /*deprecated 用get代替，这个保留到17.0
     */
    public static String getString(String s) {
        return get(s);
    }

    //获取string,为null时返回"" >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //获取去掉前后空格后的string<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /*获取去掉前后空格后的string,为null则返回""
     * @param tv
     * @return
     */
    public static String trim(TextView tv) {
        return trim(get(tv));
    }

    /*获取去掉前后空格后的string,为null则返回""
     * @param object
     * @return
     */
    public static String trim(Object object) {
        return trim(get(object));
    }

    /*获取去掉前后空格后的string,为null则返回""
     * @param s
     * @return
     */
    public static String trim(String s) {
        return s == null ? "" : s.trim();
    }


    /*deprecated 用trim代替，这个保留到17.0
     */
    public static String getTrimedString(TextView tv) {
        return trim(tv);
    }

    /*deprecated 用trim代替，这个保留到17.0
     */
    public static String getTrimedString(Object object) {
        return trim(object);
    }

    /*deprecated 用trim代替，这个保留到17.0
     */
    public static String getTrimedString(CharSequence cs) {
        return trim(get(cs));
    }

    /*deprecated 用trim代替，这个保留到17.0
     */
    public static String getTrimedString(String s) {
        return trim(s);
    }

    //获取去掉前后空格后的string>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //获取去掉所有空格后的string <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /*获取去掉所有空格后的string,为null则返回""
     * @param s
     * @return
     */
    public static String noBlank(String s) {
        return get(s).replaceAll(" ", "");
    }


    /*deprecated 用noBlank代替，这个保留到17.0
     */
    public static String getNoBlankString(TextView tv) {
        return noBlank(get(tv));
    }

    /*deprecated 用noBlank代替，这个保留到17.0
     */
    public static String getNoBlankString(String s) {
        return noBlank(s);
    }

    //获取去掉所有空格后的string >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //获取string的长度<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /*获取string的长度,为null则返回0
     * @param tv
     * @return
     */
    public static int length(TextView tv) {
        return length(get(tv));
    }

    /*获取string的长度,为null则返回0
     * @param object
     * @return
     */
    public static int length(Object object) {
        return length(get(object));
    }

    /*获取string的长度,为null则返回0
     * @param cs
     * @return
     */
    public static int length(CharSequence cs) {
        return length(get(cs));
    }

    /*获取string的长度,为null则返回0
     * @param s
     * @return
     */
    public static int length(String s) {
        return get(s).length();
    }


    /*deprecated 用length代替，这个保留到17.0
     */
    public static int getLength(TextView tv, boolean trim) {
        return getLength(get(tv), trim);
    }

    /*deprecated 用length代替，这个保留到17.0
     */
    public static int getLength(Object object, boolean trim) {
        return getLength(get(object), trim);
    }

    /*deprecated 用length代替，这个保留到17.0
     */
    public static int getLength(CharSequence cs, boolean trim) {
        return getLength(get(cs), trim);
    }

    /*deprecated 用length代替，这个保留到17.0
     */
    public static int getLength(String s, boolean trim) {
        s = trim ? getTrimedString(s) : s;
        return length(s);
    }

    //获取string的长度>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //判断字符是否为空 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /*判断字符是否为空
     * trim = true
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        return isEmpty(s, true);
    }

    /*判断字符是否为空
     * @param s
     * @param trim
     * @return
     */
    public static boolean isEmpty(String s, boolean trim) {
        //		Log.i(TAG, "isEmpty   s = " + s);
        if (s == null) {
            return true;
        }
        if (trim) {
            s = s.trim();
        }
        if (s.length() <= 0) {
            return true;
        }

        currentString = s;

        return false;
    }
    //判断字符是否为空 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //判断字符是否非空 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /*判断字符是否非空
     * @param tv
     * @param trim
     * @return
     */
    public static boolean isNotEmpty(TextView tv, boolean trim) {
        return isNotEmpty(get(tv), trim);
    }

    /*判断字符是否非空
     * @param object
     * @param trim
     * @return
     */
    public static boolean isNotEmpty(Object object, boolean trim) {
        return isNotEmpty(get(object), trim);
    }

    /*判断字符是否非空
     * @param cs
     * @param trim
     * @return
     */
    public static boolean isNotEmpty(CharSequence cs, boolean trim) {
        return isNotEmpty(get(cs), trim);
    }

    /*判断字符是否非空
     * @param s
     * @param trim
     * @return
     */
    public static boolean isNotEmpty(String s, boolean trim) {
        return !isEmpty(s, trim);
    }

    //判断字符是否非空 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //判断字符类型 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    //判断手机格式是否正确
    public static boolean isPhone(String phone) {
        if (isEmpty(phone, true)) {
            return false;
        }

        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-2,5-9])|(17[0-9]))\\d{8}$");

        currentString = phone;

        return p.matcher(phone).matches();
    }

    //判断email格式是否正确
    public static boolean isEmail(String email) {
        if (isEmpty(email, true)) {
            return false;
        }

        String str = "^([a-zA-Z0-9_\\-.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$";
        Pattern p = Pattern.compile(str);

        currentString = email;

        return p.matcher(email).matches();
    }

    /*deprecated，保留到17.0
     */
    public static boolean isNumer(String number) {
        return isNumber(number);
    }

    //判断是否全是数字
    public static boolean isNumber(String number) {
        if (isEmpty(number, true)) {
            return false;
        }

        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(number);
        if (!isNum.matches()) {
            return false;
        } else {
            currentString = number;

            return true;
        }

    }

    /*判断字符类型是否是号码或字母
     * @param s
     * @return
     */
    public static boolean isNumberOrAlpha(String s) {
        if (s == null) {
            Log.e(TAG, "isNumberOrAlpha  s == null >> return false;");
            return false;
        }
        Pattern pNumber = Pattern.compile("[0-9]*");
        Matcher mNumber;
        Pattern pAlpha = Pattern.compile("[a-zA-Z]");
        Matcher mAlpha;
        for (int i = 0; i < s.length(); i++) {
            mNumber = pNumber.matcher(s.substring(i, i + 1));
            mAlpha = pAlpha.matcher(s.substring(i, i + 1));
            if (!mNumber.matches() && !mAlpha.matches()) {
                return false;
            }
        }

        currentString = s;
        return true;
    }

    public static final String URL_PREFIX = "http://";
    public static final String URL_PREFIXs = "https://";

    /*判断字符类型是否是网址
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        if (isEmpty(url, true)) {
            return false;
        } else if (!url.startsWith(URL_PREFIX) && !url.startsWith(URL_PREFIXs)) {
            return false;
        }

        currentString = url;
        return true;
    }

    /*判断字符类型是否是路径
     * @param path
     * @return
     */
    public static boolean isFilePath(String path) {
        if (isEmpty(path, true)) {
            return false;
        }

        if (!path.contains(".") || path.endsWith(".")) {
            return false;
        }

        currentString = path;

        return true;
    }

    //判断字符类型 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //提取特殊字符<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /*去掉string内所有非数字类型字符
     * @param s
     * @return
     */
    public static String getNumber(String s) {
        if (isEmpty(s, true)) {
            return "";
        }

        StringBuilder numberString = new StringBuilder();
        String single;
        for (int i = 0; i < s.length(); i++) {
            single = s.substring(i, i + 1);
            if (isNumer(single)) numberString.append(single);
        }

        return numberString.toString();
    }

    //提取特殊字符>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //校正（自动补全等）字符串<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /*获取网址，自动补全
     * @param url
     * @return
     */
    public static String getCorrectUrl(String url) {
        Log.i(TAG, "getCorrectUrl : \n" + url);
        if (isEmpty(url, true)) {
            return "";
        }

//		if (! url.endsWith("/") && ! url.endsWith(".html")) {
//			url = url + "/";
//		}

        return isUrl(url) ? url : URL_PREFIX + url;
    }

    /*获取去掉所有 空格 、"-" 、"+86" 后的phone
     * @param tv
     * @return
     */
    public static String getCorrectPhone(TextView tv) {
        return getCorrectPhone(get(tv));
    }

    /*获取去掉所有 空格 、"-" 、"+86" 后的phone
     * @param phone
     * @return
     */
    public static String getCorrectPhone(String phone) {
        if (isEmpty(phone, true)) {
            return "";
        }

        phone = noBlank(phone);
        phone = phone.replaceAll("-", "");
        if (phone.startsWith("+86")) {
            phone = phone.substring(3);
        }
        return phone;
    }


    /*获取邮箱，自动补全
     * @param tv
     * @return
     */
    public static String getCorrectEmail(TextView tv) {
        return getCorrectEmail(get(tv));
    }

    /*获取邮箱，自动补全
     * @param email
     * @return
     */
    public static String getCorrectEmail(String email) {
        if (isEmpty(email, true)) {
            return "";
        }

        email = noBlank(email);
        if (!isEmail(email) && !email.endsWith(".com")) {
            email += ".com";
        }

        return email;
    }


    public static final int PRICE_FORMAT_DEFAULT = 0;
    public static final int PRICE_FORMAT_PREFIX = 1;
    public static final int PRICE_FORMAT_SUFFIX = 2;
    public static final int PRICE_FORMAT_PREFIX_WITH_BLANK = 3;
    public static final int PRICE_FORMAT_SUFFIX_WITH_BLANK = 4;
    public static final String[] PRICE_FORMATS = {
            "", "￥", "元", "￥ ", " 元"
    };

    /*获取价格，保留两位小数
     * @param price
     * @return
     */
    public static String getPrice(String price) {
        return getPrice(price, PRICE_FORMAT_DEFAULT);
    }

    /*获取价格，保留两位小数
     * @param price
     * @param formatType 添加单位（元）
     * @return
     */
    public static String getPrice(String price, int formatType) {
        if (isEmpty(price, true)) {
            return getPrice(0, formatType);
        }

        //单独写到getCorrectPrice? <<<<<<<<<<<<<<<<<<<<<<
        StringBuilder correctPrice = new StringBuilder();
        String s;
        for (int i = 0; i < price.length(); i++) {
            s = price.substring(i, i + 1);
            if (".".equals(s) || isNumer(s)) {
                correctPrice.append(s);
            }
        }
        //单独写到getCorrectPrice? >>>>>>>>>>>>>>>>>>>>>>

        Log.i(TAG, "getPrice  <<<<<<<<<<<<<<<<<< correctPrice =  " + correctPrice);
        if (correctPrice.toString().contains(".")) {
//			if (correctPrice.startsWith(".")) {
//				correctPrice = 0 + correctPrice;
//			}
            if (correctPrice.toString().endsWith(".")) {
                correctPrice = new StringBuilder(correctPrice.toString().replaceAll(".", ""));
            }
        }

        Log.i(TAG, "getPrice correctPrice =  " + correctPrice + " >>>>>>>>>>>>>>>>");
        return isEmpty(correctPrice.toString(), true) ? getPrice(0, formatType) : getPrice(new BigDecimal(0 + correctPrice.toString()), formatType);
    }

    /*获取价格，保留两位小数
     * @param price
     * @param formatType 添加单位（元）
     * @return
     */
    public static String getPrice(BigDecimal price, int formatType) {
        return getPrice(price == null ? 0 : price.doubleValue(), formatType);
    }

    /*获取价格，保留两位小数
     * @param price
     * @param formatType 添加单位（元）
     * @return
     */
    public static String getPrice(double price, int formatType) {
        String s = new DecimalFormat("#########0.00").format(price);
        switch (formatType) {
            case PRICE_FORMAT_PREFIX:
                return PRICE_FORMATS[PRICE_FORMAT_PREFIX] + s;
            case PRICE_FORMAT_SUFFIX:
                return s + PRICE_FORMATS[PRICE_FORMAT_SUFFIX];
            case PRICE_FORMAT_PREFIX_WITH_BLANK:
                return PRICE_FORMATS[PRICE_FORMAT_PREFIX_WITH_BLANK] + s;
            case PRICE_FORMAT_SUFFIX_WITH_BLANK:
                return s + PRICE_FORMATS[PRICE_FORMAT_SUFFIX_WITH_BLANK];
            default:
                return s;
        }
    }


}

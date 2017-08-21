package com.zhiyicx.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zhiyicx.common.config.ConstantConfig.REGEX_DATE;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_EMAIL;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_ID_CARD15;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_ID_CARD18;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_IP;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_MOBILE_EXACT;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_MOBILE_SIMPLE;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_NOT_NUMBER_START;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_TEL;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_URL;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_USERNAME;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_ZH;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_ZH_;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/8/2
 *     desc  : 正则相关工具类
 * </pre>
 */
public class RegexUtils {

    private RegexUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * If u want more please visit http://toutiao.com/i6231678548520731137/
     */

    /**
     * 验证手机号（简单）
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileSimple(CharSequence input) {
        return isMatch(REGEX_MOBILE_SIMPLE, input);
    }

    /**
     * 验证手机号（精确）
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileExact(CharSequence input) {
        return isMatch(REGEX_MOBILE_EXACT, input);
    }

    /**
     * 验证电话号码
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isTel(CharSequence input) {
        return isMatch(REGEX_TEL, input);
    }

    /**
     * 验证身份证号码15位
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIDCard15(CharSequence input) {
        return isMatch(REGEX_ID_CARD15, input);
    }

    /**
     * 验证身份证号码18位
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIDCard18(CharSequence input) {
        return isMatch(REGEX_ID_CARD18, input);
    }

    /**
     * 验证邮箱
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isEmail(CharSequence input) {
        return isMatch(REGEX_EMAIL, input);
    }

    /**
     * 验证URL
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isURL(CharSequence input) {
        return isMatch(REGEX_URL, input);
    }

    /**
     * 验证汉字
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isZh(CharSequence input) {
        return isMatch(REGEX_ZH, input);
    }

    /**
     * 验证是否包含中文
     *
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile(REGEX_ZH_);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static int getChineseCouns(String str) {
        String regEx = REGEX_ZH_;
        String term = str.replaceAll(regEx, "aa");
        return term.length() - str.length();
    }

    /**
     * 验证用户名
     * <p>不能以数字开通</p>
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isUsernameNoNumberStart(CharSequence input) {
        return isMatch(REGEX_NOT_NUMBER_START, input);
    }

    /**
     * 用户名至少为 length 个英文字符,用户名至少为 length/3 个中文字符 ,至少length个字节
     * 至少两个文字 一个中文一个英文 , 用户名至少为 4 个英文字符 ，用户名至少为 2 个中文字符
     * <p>不能以数字开通</p>
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isUsernameLength(CharSequence input, int minLength, int maxLength) {
        int chineseCount = getChineseCouns(input.toString());
        int charLength = "帅".getBytes().length;
        int currentChineseByteLenght = chineseCount * charLength;
        int length = input.toString().getBytes().length;
        if (currentChineseByteLenght > 0) {// 有中文
            return length >= (currentChineseByteLenght + (minLength - chineseCount)) && (length
                    <= currentChineseByteLenght + (maxLength - chineseCount));
        } else {
            return length >= 4 && length <= maxLength;
        }
    }

    /**
     * 验证用户名
     * <p>不能以数字开通</p>
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isUsername(CharSequence input) {
        return isMatch(REGEX_USERNAME, input);
    }

    /**
     * 验证yyyy-MM-dd格式的日期校验，已考虑平闰年
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isDate(CharSequence input) {
        return isMatch(REGEX_DATE, input);
    }

    /**
     * 验证IP地址
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIP(CharSequence input) {
        return isMatch(REGEX_IP, input);
    }

    /**
     * 判断是否匹配正则
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMatch(String regex, CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

    /**
     * 获取正则匹配的部分
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return 正则匹配的部分
     */
    public static List<String> getMatches(String regex, CharSequence input) {
        if (input == null) return null;
        List<String> matches = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    /**
     * 获取正则匹配分组
     *
     * @param input 要分组的字符串
     * @param regex 正则表达式
     * @return 正则匹配分组
     */
    public static String[] getSplits(String input, String regex) {
        if (input == null) return null;
        return input.split(regex);
    }

    /**
     * 替换正则匹配的第一部分
     *
     * @param input       要替换的字符串
     * @param regex       正则表达式
     * @param replacement 代替者
     * @return 替换正则匹配的第一部分
     */
    public static String getReplaceFirst(String input, String regex, String replacement) {
        if (input == null) return null;
        return Pattern.compile(regex).matcher(input).replaceFirst(replacement);
    }

    /**
     * 替换所有正则匹配的部分
     *
     * @param input       要替换的字符串
     * @param regex       正则表达式
     * @param replacement 代替者
     * @return 替换所有正则匹配的部分
     */
    public static String getReplaceAll(String input, String regex, String replacement) {
        if (input == null) return null;
        return Pattern.compile(regex).matcher(input).replaceAll(replacement);
    }

    public static int getImageIdFromMarkDown(String regex, String input) {
        if (regex == null || input == null) return -1;
        Matcher matcher = Pattern.compile(regex).matcher(input);
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(2));
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    public static String replaceImageId(String regex, String input) {
        Matcher matcher = Pattern.compile(regex).matcher(input);
        try {
            return matcher.replaceAll("");
        } catch (Exception e) {
            return input;
        }
    }

    public static List<String> cutStringByImgTag(String targetStr) {
        List<String> splitTextList = new ArrayList<>();
        String reg = "@!\\[.*]\\((\\d+)\\)";
//        String targetStr = "xxx@![image](123)ssss@![image](456)";
        Pattern pattern = Pattern.compile("@!\\[.*?]\\((\\d+)\\)");
        Matcher matcher1 = pattern.matcher(targetStr);
        int lastIndex = 0;
        while (matcher1.find()) {

            if (matcher1.start() > lastIndex) {
                String result1 = targetStr.substring(lastIndex, matcher1.start());// 文字
                System.out.println("result 1 :: " + result1);
                splitTextList.add(result1);
            }
            String result2 = targetStr.substring(matcher1.start(), matcher1.end());// 图片
            splitTextList.add(result2);

            Matcher matcher2 = Pattern.compile(reg).matcher(result2);
            System.out.println("result 2 :: " + result2);
            if (matcher2.find()){
                System.out.println("matcher 2 :: " + matcher2.group(0));
                System.out.println("matcher 2 :: " + matcher2.group(1));
            }
            lastIndex = matcher1.end();
        }
        if (lastIndex != targetStr.length()) {
            System.out.println("result 3 :: " + targetStr.substring(lastIndex, targetStr.length()));
        }
        if (splitTextList.size()>0){
            String last=splitTextList.get(splitTextList.size()-1);
            splitTextList.set(splitTextList.size()-1,last+"tym_last");
        }
        return splitTextList;
    }

    public static int getImageId(String input){
        String reg = "@!\\[.*]\\((\\d+)\\)";
        Matcher matcher2 = Pattern.compile(reg).matcher(input);
        System.out.println("result 2 :: " + input);
        if (matcher2.find()){
            System.out.println("matcher 2 :: " + matcher2.group(0));
            System.out.println("matcher 2 :: " + matcher2.group(1));
        }
        try {
            return Integer.parseInt(matcher2.group(1));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
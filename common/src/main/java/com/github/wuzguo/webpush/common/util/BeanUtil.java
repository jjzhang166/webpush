package com.github.wuzguo.webpush.common.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * bean util
 *
 * @author wuzguo
 * @date 2016年12月18日 下午3:38:45
 */
public class BeanUtil {

    /**
     * 搜索方法
     *
     * @param clazz           搜索对象class
     * @param first           开头关键字
     * @param nameIsLowerCase 名字首字母是否转小写
     * @return
     */
    public static List<String> findMethod(final Class clazz, final String first, final boolean nameIsLowerCase) {
        List<String> fields = new ArrayList();
        int beginIndex = first.length();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().indexOf(first) == 0) {
                String methodName = method.getName().substring(beginIndex);
                fields.add(nameIsLowerCase ? (Character.toLowerCase(methodName.charAt(0)) + methodName.substring(1)) : methodName);
            }
        }
        return fields;
    }
}

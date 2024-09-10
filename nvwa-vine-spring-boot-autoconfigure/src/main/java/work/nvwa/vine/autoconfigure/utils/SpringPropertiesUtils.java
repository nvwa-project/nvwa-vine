package work.nvwa.vine.autoconfigure.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Geng Rong
 */
public class SpringPropertiesUtils {

    public static void copyProperties(Object target, Map<String, Object> properties) throws InvocationTargetException, IllegalAccessException {
        if (CollectionUtils.isEmpty(properties)) {
            return;
        }
        Map<String, Object> newProperties = new HashMap<>();
        properties.forEach((key, value) -> newProperties.put(convertToCamelCase(key), value));
        BeanUtils.copyProperties(target, newProperties);
    }


    private static String convertToCamelCase(String key) {
        StringBuilder sb = new StringBuilder();
        boolean convertNext = false;

        for (char ch : key.toCharArray()) {
            if (ch == '-') {
                convertNext = true;
            } else {
                if (convertNext) {
                    sb.append(Character.toUpperCase(ch));
                    convertNext = false;
                } else {
                    sb.append(ch);
                }
            }
        }

        return sb.toString();
    }
}

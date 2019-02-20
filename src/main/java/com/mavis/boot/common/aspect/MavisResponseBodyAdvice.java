package com.mavis.boot.common.aspect;

import com.mavis.boot.common.annotation.FieldFilter;
import com.mavis.boot.common.annotation.FieldFilters;
import com.mavis.boot.common.annotation.NoReflection;
import com.mavis.boot.common.response.R;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 过滤返回值中的字段
 */
@Slf4j
@ControllerAdvice
// todo 梳理处理逻辑，简化流程
public class MavisResponseBodyAdvice implements ResponseBodyAdvice {

    /**
     * 包含项
     */
    private Map<Class<?>, FieldFilter> fieldFilterMap = new HashMap<>();

    /**
     * 包含项
     */
    private List<String> includes = new ArrayList<>();
    /**
     * 排除项
     */
    private List<String> excludes = new ArrayList<>();

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // 如果有自定义的 FieldFilter | FieldFilters 注解 ,就进行过滤
        boolean hasFieldFilterAnno = returnType.getMethodAnnotation(FieldFilter.class) != null
            || returnType.getMethodAnnotation(FieldFilters.class) != null;
        return hasFieldFilterAnno;
    }

    @Override
    public Object beforeBodyWrite(Object obj, MethodParameter methodParameter,
        MediaType selectedContentType,
        Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 解析注解，获取过滤条件
        Annotation[] annos = methodParameter.getMethodAnnotations();
        Arrays.asList(annos).forEach(anno -> {
            if (anno instanceof FieldFilter) {
                FieldFilter fieldFilter = (FieldFilter) anno;
                fieldFilterMap.put(fieldFilter.type(), fieldFilter);
            } else if (anno instanceof FieldFilters) {
                // 使用多重注解时，返回 @FieldFilters注解
                FieldFilters fieldFilters = (FieldFilters) anno;
                Arrays.asList(fieldFilters.value()).forEach(fieldFilter -> {
                    fieldFilterMap.put(fieldFilter.type(), fieldFilter);
                });
            }
        });

        // 没有属性过滤，直接返回
        if (fieldFilterMap.size() == 0) {
            return obj;
        }

        Object tmp;
        R r = null;

        // 如果返回的对象是ResultBean则特殊处理，只过滤ResultBean内的Data对象中的属性
        if (obj instanceof R) {
            r = (R) obj;
            tmp = r.getData();
            if (tmp == null) {
                return r;
            }
        } else {
            tmp = obj;
        }

        if (tmp == null) {
            return null;
        }

        Object retObj = null;
        // 判断返回的对象是单个对象，还是list，还是map
        try {
            if (tmp instanceof List) {
                // List
                List<?> list = (List<?>) tmp;
                retObj = handleList(list);
            } else if (tmp instanceof Enum) {
                // Enum
                retObj = tmp;
            } else {
                // Single Object
                retObj = handleSingleObject(tmp);
            }
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        // 重新组装返回值
        if (obj instanceof R) {
            r.setData(retObj);
            return r;
        }
        return retObj;
    }

    /**
     * 处理返回值是对象
     */
    private Map<String, Object> handleSingleObject(Object obj) throws IllegalAccessException {
        Map<String, Object> returnMap = new HashMap<>();
        if (obj instanceof Map) {
            returnMap = this.handleMap(obj);
        } else {
            FieldFilter fieldFilter = fieldFilterMap.get(obj.getClass());
            returnMap = this.processSingleObject(obj, fieldFilter);
        }
        return returnMap;
    }

    /**
     * 处理返回值是list
     */
    private List<Map<String, Object>> handleList(List list) throws IllegalAccessException {
        List<Map<String, Object>> retList = new ArrayList<>();
        for (Object obj : list) {
            Map<String, Object> map = (Map<String, Object>) handleSingleObject(obj);
            retList.add(map);
        }
        return retList;
    }

    /**
     * 处理返回值为map
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> handleMap(Object obj) {
        // 封装处理后的返回值
        Map<String, Object> returnMap = new HashMap<>();
        Map objMap = (Map) obj;
        // 属性过滤
        objMap.forEach((key, value) -> {
            // 如果有注解
            FieldFilter fieldFilter = fieldFilterMap.get(value.getClass());
            if (fieldFilter != null) {
                Map<String, Object> tempMap = null;
                try {
                    tempMap = this.processSingleObject(value, fieldFilter);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (tempMap != null) {
                    returnMap.put(key.toString(), tempMap);
                }
            } else {
                returnMap.put(key.toString(), value);
            }
        });
        return returnMap;
    }

    /**
     * 处理map中的单个对象
     */
    private Map<String, Object> processSingleObject(Object obj, FieldFilter fieldFilter)
        throws IllegalAccessException {
        Map<String, Object> returnMap = new HashMap<>();
        // 获取过滤的属性
        if (fieldFilter != null) {
            includes = new ArrayList<>(Arrays.asList(fieldFilter.includes()));
            excludes = new ArrayList<>(Arrays.asList(fieldFilter.excludes()));
        }
        // 获取对象属性列表
        List<Field> fieldList = new ArrayList<>();
        Class<?> tmpClass = obj.getClass();
        String objClassName = "java.lang.Object";
        // 获取所有字段，包括父类的，除了Object父类
        while (tmpClass != null && !objClassName.equalsIgnoreCase(tmpClass.getName())) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(tmpClass.getDeclaredFields())));
            tmpClass = tmpClass.getSuperclass();
        }
        for (Field field : fieldList) {
            // 设置可以访问private修饰的字段
            field.setAccessible(true);
            // 静态字段去掉
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            // 加了这个注解的不序列化
            NoReflection nr = field.getAnnotation(NoReflection.class);
            if (nr != null) {
                continue;
            }
            // 判断属性类型
            Object fieldValue = field.get(obj);
            if (fieldValue != null) {
                // 属性为list
                if (fieldValue instanceof List) {
                    List<Map<String, Object>> fieldValueList = this.handleList((List) fieldValue);
                    returnMap.put(field.getName(), fieldValueList);
                }
                // 属性为map
                else if (fieldValue instanceof Map) {
                    Map<String, Object> fieldValueMap = this.handleMap(fieldValue);
                    returnMap.put(field.getName(), fieldValueMap);
                } else {
                    // 属性为普通对象
                    FieldFilter hasFieldFilter = fieldFilterMap.get(fieldValue.getClass());
                    if (hasFieldFilter != null) {
                        Map<String, Object> fieldValueMap = this
                            .processSingleObject(fieldValue, hasFieldFilter);
                        returnMap.put(field.getName(), fieldValueMap);
                    } else {
                        // 属性过滤
                        if (includes.size() > 0 && includes.contains(field.getName())) {
                            returnMap.put(field.getName(), fieldValue);
                        } else if (excludes.size() > 0 && !excludes.contains(field.getName())) {
                            returnMap.put(field.getName(), fieldValue);
                        }
                    }
                }
            } else {
                returnMap.put(field.getName(), fieldValue);
            }
        }
        return returnMap;
    }

}

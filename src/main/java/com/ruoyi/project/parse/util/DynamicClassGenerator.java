package com.ruoyi.project.parse.util;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenl
 */
public class DynamicClassGenerator {

    private static final Map<String, Class<?>> classCache = new ConcurrentHashMap<>();

    public static Class<?> getOrCreateClass(String className, List<Map<String, Object>> headers) throws Exception {
        return classCache.computeIfAbsent(className, key -> {
            try {
                return DynamicClassGenerator.generateClass(key, headers);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }


    public static List<Object> obtainDynamicObject(List<List<Object>> dataList, Class<?> dynamicClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
        // 实例化对象并填充数据
        List<Object> entityList = new ArrayList<>();
        for (List<Object> row : dataList) {
            Object instance = dynamicClass.getDeclaredConstructor().newInstance();
            for (int i = 0; i < row.size(); i++) {
                String fieldName = "field" + i;
                String methodName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

                Field field = dynamicClass.getDeclaredField(fieldName);
                Class<?> paramType = field.getType();
                Method method = dynamicClass.getMethod(methodName, paramType);

                Object value = row.get(i);
                if (!paramType.isInstance(value)) {
                    if (paramType == Integer.class || paramType == int.class) {
                        value = ((Number) value).intValue();
                    } else if (paramType == String.class) {
                        value = String.valueOf(value);
                    }
                }

                method.invoke(instance, value);
            }
            entityList.add(instance);
        }
        List<Object> typedEntityList = new ArrayList<>();
        for (Object obj : entityList) {
            if (!dynamicClass.isInstance(obj)) {
                throw new IllegalArgumentException("List contains elements not of type " + dynamicClass.getName());
            }
            typedEntityList.add(obj);
        }
        return typedEntityList;
    }

    /**
     * 动态生成包含 @Excel 注解的类。
     *
     * @param className 类名
     * @param headers   表头信息，包括字段名和注解属性
     * @return CtClass 动态生成的类
     */
    public static Class<?> generateClass(String className, List<Map<String, Object>> headers) throws Exception {
        ClassPool pool = new ClassPool(true); // 设置为非严格模式
        CtClass ctClass = pool.makeClass(className);
        pool.appendSystemPath(); // 添加此行

        for (int i = 0; i < headers.size(); i++) {
            Map<String, Object> header = headers.get(i);
            String fieldName = "field" + i;
            String typeName = (String) header.get("type");
            String excelName = (String) header.get("name");

            CtClass fieldType = pool.get(typeName);
            CtField field = new CtField(fieldType, fieldName, ctClass);
            field.setModifiers(Modifier.PRIVATE);
            ctClass.addField(field);

            // 添加 getter 和 setter 方法
            ctClass.addMethod(CtNewMethod.setter("set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1), field));
            ctClass.addMethod(CtNewMethod.getter("get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1), field));

            // 创建 @Excel 注解并设置其成员值
            AnnotationsAttribute visibleAttr = new AnnotationsAttribute(ctClass.getClassFile().getConstPool(), AnnotationsAttribute.visibleTag);
            Annotation excelAnnotation = new Annotation("com.ruoyi.framework.aspectj.lang.annotation.Excel", ctClass.getClassFile().getConstPool());
            excelAnnotation.addMemberValue("name", new StringMemberValue(excelName, ctClass.getClassFile().getConstPool()));

            // 将注解添加到可见属性中
            visibleAttr.setAnnotation(excelAnnotation);

            // 将属性添加到字段信息中
            field.getFieldInfo().addAttribute(visibleAttr);
        }

        return ctClass.toClass();
    }
}
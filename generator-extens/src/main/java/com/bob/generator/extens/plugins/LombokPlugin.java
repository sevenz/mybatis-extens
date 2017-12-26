package com.bob.generator.extens.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;

import java.util.*;

/**
 * Lombok插件, 生成带Data注解的Model，有父类，有些字段不会生成，因为父类已经有了
 * <p>
 * Created by wangxiang on 17/7/4.
 */
public class LombokPlugin extends PluginAdapterEnhancement {

    private final Collection<Annotations> annotations;

    /**
     * 小波特殊需求，这几列在父类中已经包含，故在modelField生成时排除掉
     */
    private List<String> baseColumns = new ArrayList<>();

    public LombokPlugin() {
        annotations = new LinkedHashSet<>(Annotations.values().length);
        baseColumns.add("id");
        baseColumns.add("creator");
        baseColumns.add("gmtCreated");
        baseColumns.add("modifier");
        baseColumns.add("gmtModified");
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        addDataAnnotation(topLevelClass);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        addDataAnnotation(topLevelClass);
        return true;
    }

    private void addDataAnnotation(TopLevelClass topLevelClass) {
        for (Annotations annotation : annotations) {
            topLevelClass.addImportedType(annotation.javaType);
            topLevelClass.addAnnotation(annotation.name);
        }
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);

        //@Data is default annotation
        annotations.add(Annotations.DATA);

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            boolean isEnable = Boolean.parseBoolean(entry.getValue().toString());

            if (isEnable) {
                String paramName = entry.getKey().toString().trim();
                Annotations annotation = Annotations.getValueOf(paramName);
                if (annotation != null) {
                    annotations.add(annotation);
                    annotations.addAll(Annotations.getDependencies(annotation));
                }
            }
        }
    }

    @Override
    public boolean clientGenerated(Interface interfaze,
                                   TopLevelClass topLevelClass,
                                   IntrospectedTable introspectedTable) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
        interfaze.addAnnotation("@Mapper");
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass,
                                       IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable,
                                       ModelClassType modelClassType) {
        String rootClass = context.getJavaModelGeneratorConfiguration().getProperty("rootClass");
        if (rootClass == null || rootClass.trim().length() == 0) {
            return true;
        }

        Class<?> clasz;
        try {
            clasz = Class.forName(rootClass);
        } catch (ClassNotFoundException ce) {
            logger.error("can't load " + rootClass, ce);
            clasz = null;
        }
        if (clasz != null) {
            baseColumns.clear();
            java.lang.reflect.Field[] fields = clasz.getFields();
            for (java.lang.reflect.Field temp : fields) {
                baseColumns.add(temp.getName());
            }
        }

        final String fieldName = field.getName();
        Boolean found = false;
        for (String item : baseColumns) {
            if (item.contentEquals(fieldName)) {
                found = true;
                break;
            }
        }
        return !found;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass,
                                              IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass,
                                              IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        return false;
    }

    private enum Annotations {
        DATA("data", "@Data", "lombok.Data"),
        BUILDER("builder", "@Builder", "lombok.Builder"),
        ALL_ARGS_CONSTRUCTOR("allArgsConstructor", "@AllArgsConstructor", "lombok.AllArgsConstructor"),
        NO_ARGS_CONSTRUCTOR("noArgsConstructor", "@NoArgsConstructor", "lombok.NoArgsConstructor"),
        TO_STRING("toString", "@ToString", "lombok.ToString"),
        EqualsAndHashCode("equalsAndHashCode", "@EqualsAndHashCode(callSuper = true)", "lombok.EqualsAndHashCode");

        private final String paramName;
        private final String name;
        private final FullyQualifiedJavaType javaType;

        Annotations(String paramName, String name, String className) {
            this.paramName = paramName;
            this.name = name;
            this.javaType = new FullyQualifiedJavaType(className);
        }

        private static Annotations getValueOf(String paramName) {
            for (Annotations annotation : Annotations.values()) {
                if (String.CASE_INSENSITIVE_ORDER.compare(paramName, annotation.paramName) == 0) {
                    return annotation;
                }
            }
            return null;
        }

        private static Collection<Annotations> getDependencies(Annotations annotation) {
            if (annotation == ALL_ARGS_CONSTRUCTOR) {
                return Collections.singleton(NO_ARGS_CONSTRUCTOR);
            } else {
                return Collections.emptyList();
            }
        }
    }
}
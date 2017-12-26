package com.bob.generator.extens.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;

import java.util.*;

import static com.bob.utils.Reflect.on;

/**
 * 只能使用于XML环境
 * <p>
 * Created by wangxiang on 17/6/28.
 */
public class CleanMapperPlugin extends PluginAdapterEnhancement {

    private Config config;

    @Override
    public boolean validate(List<String> list) {
        if (this.config == null)
            this.config = new Config(getProperties());
        return super.validate(list);
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        String idType = introspectedTable.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType().getShortName();

        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(config.className.trim() + "<"
                + introspectedTable.getBaseRecordType() + ","
                + introspectedTable.getExampleType() + ","
                + idType + ">");
        FullyQualifiedJavaType imp = new FullyQualifiedJavaType(config.packageName + "." + config.className);
        interfaze.addSuperInterface(fqjt);
        interfaze.addImportedType(imp);

        // 保留方法名中含有blob的，其余的均去掉，因为父类中有
        List<Method> methods = new ArrayList<>();
        for (Method m : interfaze.getMethods()) {
            if (m.getName().toLowerCase().contains("blob")) {
                methods.add(m);
            }
        }
        interfaze.getMethods().clear();
        interfaze.getMethods().addAll(methods);

        Set<FullyQualifiedJavaType> imports = interfaze.getImportedTypes();
        final Set<FullyQualifiedJavaType> newImport = new HashSet<>();
        for (FullyQualifiedJavaType c : imports) {
            String a = c.getFullyQualifiedName();
            if (a.equalsIgnoreCase("java.util.List") ||
                    a.equalsIgnoreCase("org.apache.ibatis.annotations.Param")) {
            } else {
                newImport.add(c);
            }
        }

        on(interfaze).set("importedTypes", newImport);
        return true;
    }

    private static final class Config extends PluginConfig {

        private static final String packagenameproperty = "packagename";
        private static final String classnameproperty = "classname";

        private String packageName;
        private String className;

        protected Config(Properties props) {
            super(props);
            this.packageName = props.getProperty(packagenameproperty, "");
            this.className = props.getProperty(classnameproperty, "");
        }
    }
}
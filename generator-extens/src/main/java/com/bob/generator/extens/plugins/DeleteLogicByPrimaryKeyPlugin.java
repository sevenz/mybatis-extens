package com.bob.generator.extens.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;
import java.util.Properties;

/**
 * Created by wangxiang on 17/6/23.
 */
public class DeleteLogicByPrimaryKeyPlugin extends PluginAdapterEnhancement {

    private Config config;
    private boolean should_generator_delete_method = false;

    private static final class Config extends PluginConfig {

        private static final String defaultMethodToGenerate = "deleteLogicByPrimaryKey";
        private static final String defaultLogicColumn = "is_delete";
        private static final String methodToGenerateKey = "methodToGenerate";

        private String methodToGenerate;
        private String logicDeleteColumn;

        protected Config(Properties props) {
            super(props);
            this.methodToGenerate = props.getProperty(methodToGenerateKey, defaultMethodToGenerate);
            this.logicDeleteColumn = props.getProperty("logicDeleteColumn", defaultLogicColumn);
        }
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        if (should_generator_delete_method) {
            String tableName = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();
            XmlElement deleteLogicByIdsElement = new XmlElement("update");
            deleteLogicByIdsElement.addAttribute(new Attribute("id", config.methodToGenerate));
            TextElement textElement = new TextElement("update " + tableName + " set "
                    + config.logicDeleteColumn + " = 1 where "
                    + introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName()
                    + " = #{id}; "
            );
            deleteLogicByIdsElement.addElement(textElement);
            document.getRootElement().addElement(deleteLogicByIdsElement);
        }
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    @Override
    public boolean clientDeleteByExampleMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        if (!config.shouldExclude(interfaze.getType())) {
            Method temp = generateDeleteLogicByPrimaryKey(method, introspectedTable);
            if (temp != null) {
                interfaze.addMethod(temp);
            }
        }
        addDeleteMethodJavaDoc(method, "根据查询条件进行物理删除");
        return super.clientDeleteByExampleMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientDeleteByExampleMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (!config.shouldExclude(topLevelClass.getType())) {
            Method temp = generateDeleteLogicByPrimaryKey(method, introspectedTable);
            if (temp != null) {
                topLevelClass.addMethod(temp);
            }
        }
        addDeleteMethodJavaDoc(method, "根据查询条件进行物理删除");
        return super.clientDeleteByExampleMethodGenerated(method, topLevelClass, introspectedTable);
    }

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        if (!config.shouldExclude(interfaze.getType())) {
            Method temp = generateDeleteLogicByPrimaryKey(method, introspectedTable);
            if (temp != null) {
                interfaze.addMethod(temp);
            }
        }
        addDeleteMethodJavaDoc(method, "根据主键进行物理删除");
        return super.clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (!config.shouldExclude(topLevelClass.getType())) {
            Method temp = generateDeleteLogicByPrimaryKey(method, introspectedTable);
            if (temp != null) {
                topLevelClass.addMethod(temp);
            }
        }
        addDeleteMethodJavaDoc(method, "根据主键进行物理删除");
        return super.clientDeleteByPrimaryKeyMethodGenerated(method, topLevelClass, introspectedTable);
    }

    private void addDeleteMethodJavaDoc(Method method, String javaDocLine2) {
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * " + javaDocLine2);
        method.addJavaDocLine(" */");
    }

    @Override
    public boolean validate(List<String> list) {
        if (this.config == null)
            this.config = new Config(getProperties());
        return super.validate(list);
    }

    private Method generateDeleteLogicByPrimaryKey(Method method, IntrospectedTable introspectedTable) {

        if (!should_generator_delete_method) {
            should_generator_delete_method = true;
            Method m = new Method(config.methodToGenerate);
            m.setVisibility(method.getVisibility());
            m.setReturnType(FullyQualifiedJavaType.getIntInstance());
            String id = introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName();
            m.addParameter(new Parameter(PrimitiveTypeWrapper.getLongInstance(), id, "@Param(\"" + id + "\")"));
            List<String> annotations = method.getAnnotations();
            if (annotations.size() > 0) {
                // 简单由此来做为判断当前是注解形式生成SQL
                String tableName = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();
                String sql = "@Update(\"update " + tableName + " set "
                        + config.logicDeleteColumn + " = 1 where "
                        + introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName()
                        + " = #{id};\")";
                m.addAnnotation(sql);
            }

            m.addJavaDocLine("/**");
            m.addJavaDocLine(" * 逻辑删除");
            m.addJavaDocLine(" */");
            context.getCommentGenerator().addGeneralMethodComment(m, introspectedTable);
            return m;
        } else {
            return null;
        }
    }
}
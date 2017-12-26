package com.bob.generator.extens.plugins;

import com.bob.utils.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.sql.Types;
import java.util.List;
import java.util.Properties;

/**
 * 逻辑删除, 其实可以不用, 自己用update***替换
 *
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
            this.logicDeleteColumn = props.getProperty("logicDeleteColumn", defaultLogicColumn).toLowerCase();
        }
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        if (should_generator_delete_method) {

            String ddd = introspectedTable.getTableConfigurationProperty("logicDeleteColumn");
            if (!StringUtils.isNullOrEmpty(ddd)) {
                config.logicDeleteColumn = ddd.toLowerCase();
            }

            String tableName = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();
            XmlElement deleteLogicByIdsElement = new XmlElement("update");
            deleteLogicByIdsElement.addAttribute(new Attribute("id", config.methodToGenerate));
            List<IntrospectedColumn> columns = introspectedTable.getBaseColumns();
            for (IntrospectedColumn column : columns) {
                if (column.getActualColumnName().toLowerCase().equalsIgnoreCase(config.logicDeleteColumn)) {
                    TextElement textElement = null;
                    switch (column.getJdbcType()) {
                        case Types.BIT:
                            textElement = new TextElement("update " + tableName + " set "
                                    + config.logicDeleteColumn + " = 1 where "
                                    + introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName()
                                    + " = #{id}; "
                            );
                            break;
                        case Types.CHAR:
                            String vvv = introspectedTable.getTableConfigurationProperty("logicDeleteValue");
                            String emp = introspectedTable.getTableConfigurationProperty("logicOther");
                            String sql;
                            if (StringUtils.isNullOrEmpty(emp)) {
                                sql = " = " + vvv + " where ";
                            } else {
                                sql = " = " + vvv + emp + " where ";
                            }

                            textElement = new TextElement("update " + tableName + " set "
                                    + config.logicDeleteColumn + sql
                                    + introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName()
                                    + " = #{id}; "
                            );
                            break;
                        default:
                            break;
                    }

                    if (textElement != null) {
                        deleteLogicByIdsElement.addElement(textElement);
                        document.getRootElement().addElement(deleteLogicByIdsElement);
                    }
                }
            }
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

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        for (InnerClass innerClass : topLevelClass.getInnerClasses()) {
            if ("GeneratedCriteria".equals(innerClass.getType().getShortName())) { //$NON-NLS-1$

            }
            if ("Criteria".equals(innerClass.getType().getShortName())) { //$NON-NLS-1$

            }
            if ("Criterion".equals(innerClass.getType().getShortName())) { //$NON-NLS-1$

            }
        }

        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        List<Method> methods = interfaze.getMethods();
        for (Method m : methods) {
            System.out.println(m.getName());
            if (m.getName().equalsIgnoreCase("deleteByPrimaryKey")) {
                Method newM = new Method(config.methodToGenerate);
                newM.setVisibility(m.getVisibility());
                newM.setReturnType(FullyQualifiedJavaType.getIntInstance());

                String keyParam = m.getParameters().get(0).getName();
                keyParam = keyParam + ",jdbcType="
                        + m.getParameters().get(0).getType().getShortName().toUpperCase();

                List<String> annotations = m.getAnnotations();
                if (annotations.size() > 0) {
                    // 简单由此来做为判断当前是注解形式生成SQL

                    String ddd = introspectedTable.getTableConfigurationProperty("logicDeleteColumn");
                    if (!StringUtils.isNullOrEmpty(ddd)) {
                        config.logicDeleteColumn = ddd.toLowerCase();
                    }
                    List<IntrospectedColumn> columns = introspectedTable.getBaseColumns();
                    String tableName = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();
                    String ssql = "";
                    for (IntrospectedColumn column : columns) {
                        if (column.getActualColumnName().toLowerCase().equalsIgnoreCase(config.logicDeleteColumn)) {
                            switch (column.getJdbcType()) {
                                case Types.BIT:
                                    ssql = "update " + tableName + " set "
                                            + config.logicDeleteColumn + " = 1 where "
                                            + introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName()
                                            + " = #{" + keyParam + "};\"";
                                    break;
                                case Types.CHAR:
                                    String vvv = introspectedTable.getTableConfigurationProperty("logicDeleteValue");
                                    String emp = introspectedTable.getTableConfigurationProperty("logicOther");
                                    String sql;
                                    if (StringUtils.isNullOrEmpty(emp)) {
                                        sql = " = " + vvv + " where ";
                                    } else {
                                        sql = " = " + vvv + emp + " where ";
                                    }
                                    ssql = "update " + tableName + " set "
                                            + config.logicDeleteColumn + sql
                                            + introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName()
                                            + " = #{" + keyParam + "};\"";
                                    break;
                                default:
                                    break;
                            }
                        }
                    }

                    if (!StringUtils.isNullOrEmpty(ssql)) {
                        String sql = "@Update(\"" + ssql + ")";
                        newM.addAnnotation(sql);
                    }
                }

                List<Parameter> params = m.getParameters();
                for (Parameter p : params) {
                    newM.addParameter(p);
                }

                newM.addJavaDocLine("/**");
                newM.addJavaDocLine(" * 逻辑删除");
                newM.addJavaDocLine(" */");
                interfaze.getMethods().add(newM);
                break;
            }
        }
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    private Method generateDeleteLogicByPrimaryKey(Method method, IntrospectedTable introspectedTable) {

        should_generator_delete_method = true;

//        if (!should_generator_delete_method) {
//            should_generator_delete_method = true;
//            Method m = new Method(config.methodToGenerate);
//            m.setVisibility(method.getVisibility());
//            m.setReturnType(FullyQualifiedJavaType.getIntInstance());
//            String id = introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName();
//            String idType = introspectedTable.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType().getShortName();
//            m.addParameter(new Parameter(new FullyQualifiedJavaType(idType), id, "@Param(\"" + id + "\")"));
//            List<String> annotations = method.getAnnotations();
//            if (annotations.size() > 0) {
//                // 简单由此来做为判断当前是注解形式生成SQL
//                String tableName = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();
//                String sql = "@Update(\"update " + tableName + " set "
//                        + config.logicDeleteColumn + " = 1 where "
//                        + introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName()
//                        + " = #{id};\")";
//                m.addAnnotation(sql);
//            }
//
//            m.addJavaDocLine("/**");
//            m.addJavaDocLine(" * 逻辑删除");
//            m.addJavaDocLine(" */");
//            context.getCommentGenerator().addGeneralMethodComment(m, introspectedTable);
//            return m;
//        } else {
//            return null;
//        }

        return null;
    }
}
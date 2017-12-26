package com.bob.generator.extens.plugins;

import com.bob.utils.JavaElementGeneratorTools;
import com.bob.utils.PluginTools;
import com.bob.utils.XmlElementGeneratorTools;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * 批量插入
 * <p>
 * Created by wangxiang on 17/6/30.
 */
public class BatchInsertPlugin extends PluginAdapterEnhancement {

    public static final String METHOD_BATCH_INSERT = "batchInsert";  // 方法名
    public static final String METHOD_BATCH_INSERT_SELECTIVE = "batchInsertSelective";  // 方法名

    private Config config;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(List<String> warnings) {

        // 插件使用前提是数据库为MySQL或者SQLserver，因为返回主键使用了JDBC的getGenereatedKeys方法获取主键
        if ("com.mysql.jdbc.Driver".equalsIgnoreCase(this.getContext().getJdbcConnectionConfiguration().getDriverClass()) == false
                && "com.microsoft.jdbc.sqlserver.SQLServer".equalsIgnoreCase(this.getContext().getJdbcConnectionConfiguration().getDriverClass()) == false
                && "com.microsoft.sqlserver.jdbc.SQLServerDriver".equalsIgnoreCase(this.getContext().getJdbcConnectionConfiguration().getDriverClass()) == false) {
            warnings.add("bob:插件" + this.getClass().getSimpleName() + "插件使用前提是数据库为MySQL或者SQLserver，因为返回主键使用了JDBC的getGenereatedKeys方法获取主键！");
            return false;
        }

        if (this.config == null)
            this.config = new Config(getProperties());

        if (config.shouldOrNot.equalsIgnoreCase("true")) {
            // 插件使用前提是使用了ModelColumnPlugin插件
            if (!PluginTools.checkDependencyPlugin(getContext(), ModelColumnPlugin.class)) {
                warnings.add("bob:插件" + this.getClass().getSimpleName() + "插件需配合com.bob.mybatis.generator.plugins.ModelColumnPlugin插件使用！");
                return false;
            }
        }

        return super.validate(warnings);
    }


    /**
     * Java Client Methods 生成
     * 具体执行顺序 http://www.mybatis.org/generator/reference/pluggingIn.html
     *
     * @param interfaze
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        // 1. batchInsert
        FullyQualifiedJavaType listType = FullyQualifiedJavaType.getNewListInstance();
        listType.addTypeArgument(introspectedTable.getRules().calculateAllFieldsClass());
        Method mBatchInsert = JavaElementGeneratorTools.generateMethod(
                METHOD_BATCH_INSERT,
                JavaVisibility.DEFAULT,
                FullyQualifiedJavaType.getIntInstance(),
                new Parameter(listType, "list", "@Param(\"list\")")
        );
        commentGenerator.addGeneralMethodComment(mBatchInsert, introspectedTable);
        // interface 增加方法
        interfaze.addMethod(mBatchInsert);
        logger.debug("bob(批量插入插件):" + interfaze.getType().getShortName() + "增加batchInsert方法。");

        if (config.shouldOrNot.equalsIgnoreCase("true")) {
            // 2. batchInsertSelective
            FullyQualifiedJavaType selectiveType = new FullyQualifiedJavaType(introspectedTable.getRules().calculateAllFieldsClass().getShortName() + "." + ModelColumnPlugin.ENUM_NAME);
            Method mBatchInsertSelective = JavaElementGeneratorTools.generateMethod(
                    METHOD_BATCH_INSERT_SELECTIVE,
                    JavaVisibility.DEFAULT,
                    FullyQualifiedJavaType.getIntInstance(),
                    new Parameter(listType, "list", "@Param(\"list\")"),
                    new Parameter(selectiveType, "selective", "@Param(\"selective\")", true)
            );
            mBatchInsertSelective.addJavaDocLine("/**");
            mBatchInsertSelective.addJavaDocLine(" * 集合中每项都需要具有后面枚举所列出来的参数值");
            mBatchInsertSelective.addJavaDocLine(" */");
            commentGenerator.addGeneralMethodComment(mBatchInsertSelective, introspectedTable);
            // interface 增加方法
            interfaze.addMethod(mBatchInsertSelective);
            logger.debug("bob(批量插入插件):" + interfaze.getType().getShortName() + "增加batchInsertSelective方法。");
        }

        return true;
    }

    /**
     * batchInsertSelective#another way to generator batch insert selective in mapper
     *
     * @param interfaze
     * @param introspectedTable
     */
    private void addBatchInsertSelective(Interface interfaze, IntrospectedTable introspectedTable) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
        importedTypes.add(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));

        Method ibsmethod = new Method();
        // 1.设置方法可见性
        ibsmethod.setVisibility(JavaVisibility.PUBLIC);
        // 2.设置返回值类型
        FullyQualifiedJavaType ibsreturnType = FullyQualifiedJavaType.getIntInstance();// int型
        ibsmethod.setReturnType(ibsreturnType);
        // 3.设置方法名
        ibsmethod.setName("batchInsertSelective");
        // 4.设置参数列表
        FullyQualifiedJavaType paramType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType paramListType = JavaElementGeneratorTools
                .getModelTypeWithoutBLOBs(introspectedTable);
        paramType.addTypeArgument(paramListType);
        ibsmethod.addParameter(new Parameter(paramType, "list", "@Param(\"list\")"));
        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(ibsmethod);
    }

    /**
     * SQL Map Methods 生成
     * 具体执行顺序 http://www.mybatis.org/generator/reference/pluggingIn.html
     *
     * @param document
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {

        // 1. batchInsert
        XmlElement batchInsertEle = new XmlElement("insert");
        batchInsertEle.addAttribute(new Attribute("id", METHOD_BATCH_INSERT));
        // 参数类型
        batchInsertEle.addAttribute(new Attribute("parameterType", "java.util.List"));
        // 添加注释(!!!必须添加注释，overwrite覆盖生成时，@see XmlFileMergerJaxp.isGeneratedNode会去判断注释中是否存在OLD_ELEMENT_TAGS中的一点，例子：@mbg.generated)
        commentGenerator.addComment(batchInsertEle);

        // 使用JDBC的getGenereatedKeys方法获取主键并赋值到keyProperty设置的领域模型属性中。所以只支持MYSQL和SQLServer
        XmlElementGeneratorTools.useGeneratedKeys(batchInsertEle, introspectedTable);

        batchInsertEle.addElement(new TextElement("insert into " + introspectedTable.getFullyQualifiedTableNameAtRuntime()));
        for (Element element : XmlElementGeneratorTools.generateKeys(ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns()))) {
            batchInsertEle.addElement(element);
        }

        // 添加foreach节点
        XmlElement foreachElement = new XmlElement("foreach");
        foreachElement.addAttribute(new Attribute("collection", "list"));
        foreachElement.addAttribute(new Attribute("item", "item"));
        foreachElement.addAttribute(new Attribute("separator", ","));

        for (Element element : XmlElementGeneratorTools.generateValues(ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns()), "item.")) {
            foreachElement.addElement(element);
        }

        // values 构建
        batchInsertEle.addElement(new TextElement("values"));
        batchInsertEle.addElement(foreachElement);
        if (context.getPlugins().sqlMapInsertElementGenerated(batchInsertEle, introspectedTable)) {
            document.getRootElement().addElement(batchInsertEle);
            logger.debug("bob(批量插入插件):" + introspectedTable.getMyBatis3XmlMapperFileName() + "增加batchInsert实现方法。");
        }

        if (config.shouldOrNot.equalsIgnoreCase("true")) {

            // 2. batchInsertSelective
            XmlElement element = new XmlElement("insert");
            element.addAttribute(new Attribute("id", METHOD_BATCH_INSERT_SELECTIVE));
            // 参数类型
            element.addAttribute(new Attribute("parameterType", "java.util.List"));
            // 添加注释(!!!必须添加注释，overwrite覆盖生成时，@see XmlFileMergerJaxp.isGeneratedNode会去判断注释中是否存在OLD_ELEMENT_TAGS中的一点，例子：@mbg.generated)
            commentGenerator.addComment(element);

            // 使用JDBC的getGenereatedKeys方法获取主键并赋值到keyProperty设置的领域模型属性中。所以只支持MYSQL和SQLServer
            XmlElementGeneratorTools.useGeneratedKeys(element, introspectedTable);

            element.addElement(new TextElement("insert into " + introspectedTable.getFullyQualifiedTableNameAtRuntime() + " ("));

            XmlElement foreachInsertColumns = new XmlElement("foreach");
            foreachInsertColumns.addAttribute(new Attribute("collection", "selective"));
            foreachInsertColumns.addAttribute(new Attribute("item", "column"));
            foreachInsertColumns.addAttribute(new Attribute("separator", ","));
            foreachInsertColumns.addElement(new TextElement("${column.value}"));

            element.addElement(foreachInsertColumns);

            element.addElement(new TextElement(")"));

            // values
            element.addElement(new TextElement("values"));

            // foreach values
            XmlElement foreachValues = new XmlElement("foreach");
            foreachValues.addAttribute(new Attribute("collection", "list"));
            foreachValues.addAttribute(new Attribute("item", "item"));
            foreachValues.addAttribute(new Attribute("separator", ","));

            foreachValues.addElement(new TextElement("("));

            // foreach 所有插入的列，比较是否存在
            XmlElement foreachInsertColumnsCheck = new XmlElement("foreach");
            foreachInsertColumnsCheck.addAttribute(new Attribute("collection", "selective"));
            foreachInsertColumnsCheck.addAttribute(new Attribute("item", "column"));
            foreachInsertColumnsCheck.addAttribute(new Attribute("separator", ","));

            // 所有表字段
            List<IntrospectedColumn> columns = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns());
            List<IntrospectedColumn> columns1 = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns());
            for (int i = 0; i < columns1.size(); i++) {
                IntrospectedColumn introspectedColumn = columns.get(i);
                XmlElement check = new XmlElement("if");
                check.addAttribute(new Attribute("test", "'" + introspectedColumn.getActualColumnName() + "' == column.value"));
                check.addElement(new TextElement(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "item.")));

                foreachInsertColumnsCheck.addElement(check);
            }
            foreachValues.addElement(foreachInsertColumnsCheck);

            foreachValues.addElement(new TextElement(")"));

            element.addElement(foreachValues);

            if (context.getPlugins().sqlMapInsertElementGenerated(element, introspectedTable)) {
                document.getRootElement().addElement(element);
                logger.debug("bob(批量插入插件):" + introspectedTable.getMyBatis3XmlMapperFileName() + "增加batchInsertSelective实现方法。");
            }
        }

        return true;
    }

    /**
     * batchInsertSelective#another way to generator batch insert selective in xml
     *
     * @param document
     * @param introspectedTable
     */
    @SuppressWarnings("unused")
    private void addBatchInsertSelectiveXml(Document document, IntrospectedTable introspectedTable) {

        List<IntrospectedColumn> columns = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns());
        XmlElement insertBatchElement = new XmlElement("insert");
        insertBatchElement.addAttribute(new Attribute("id", "batchInsertSelective"));
        insertBatchElement.addAttribute(new Attribute("parameterType", "java.util.List"));

        XmlElement foreachElement = new XmlElement("foreach");
        foreachElement.addAttribute(new Attribute("collection", "list"));
        foreachElement.addAttribute(new Attribute("index", "index"));
        foreachElement.addAttribute(new Attribute("item", "item"));
        foreachElement.addAttribute(new Attribute("separator", ";"));
        foreachElement.addElement(new TextElement("insert into " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));

        XmlElement trim1Element = new XmlElement("trim");
        trim1Element.addAttribute(new Attribute("prefix", "("));
        trim1Element.addAttribute(new Attribute("suffix", ")"));
        trim1Element.addAttribute(new Attribute("suffixOverrides", ","));

        XmlElement javaPropertyAndDbType = new XmlElement("trim");
        javaPropertyAndDbType.addAttribute(new Attribute("prefix", " ("));
        javaPropertyAndDbType.addAttribute(new Attribute("suffix", ")"));
        javaPropertyAndDbType.addAttribute(new Attribute("suffixOverrides", ","));

        XmlElementGeneratorTools.useGeneratedKeys(insertBatchElement, introspectedTable);

        for (IntrospectedColumn introspectedColumn : columns) {

            XmlElement iftest = new XmlElement("if");
            iftest.addAttribute(new Attribute("test", "item." + introspectedColumn.getJavaProperty() + "!=null"));
            iftest.addElement(new TextElement(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn) + ","));
            trim1Element.addElement(iftest);

            XmlElement trimiftest = new XmlElement("if");
            trimiftest.addAttribute(new Attribute("test", "item." + introspectedColumn.getJavaProperty() + "!=null"));
            trimiftest.addElement(new TextElement("#{item." + introspectedColumn.getJavaProperty() + ",jdbcType=" + introspectedColumn.getJdbcTypeName() + "},"));
            javaPropertyAndDbType.addElement(trimiftest);

        }

        foreachElement.addElement(trim1Element);
        foreachElement.addElement(new TextElement(" values "));
        foreachElement.addElement(javaPropertyAndDbType);

        insertBatchElement.addElement(foreachElement);

        document.getRootElement().addElement(insertBatchElement);
    }

    private static final class Config extends PluginConfig {

        private static final String defaultMethodToGenerate = "false";
        private static final String methodToGenerateKey = "shouldGeneratorBatchInsertSelective";

        private String shouldOrNot;

        protected Config(Properties props) {
            super(props);
            this.shouldOrNot = props.getProperty(methodToGenerateKey, defaultMethodToGenerate);
        }

    }
}
package com.bob.generator.extens.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.PrimitiveTypeWrapper;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * 在模型及辅助类中添加一个tableName的属性，用于分表处理
 */
public class SimpleSplitTablePlugin extends PluginAdapterEnhancement {

    /**
     * 用于从模型中读取属性值，模型/辅助类中都会有此tableName属性
     */
    private final String tableName = " ${tableName} ";

    /**
     * 用于保留原始表名，做为生成的模型类跟Example类中tableName属性的默认值
     */
    private String original_tableName = "";

    public SimpleSplitTablePlugin() {
        super();
        logger.debug("simple split table plugin initialized");
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        original_tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();

        /*//将blob对象统一当成字符串处理
        if (introspectedTable.hasBLOBColumns()) {
            List<IntrospectedColumn> blobColumns = introspectedTable.getBLOBColumns();
            for (IntrospectedColumn column : blobColumns) {
                column.setJdbcTypeName("String");
                column.setFullyQualifiedJavaType(PrimitiveTypeWrapper.getStringInstance());
                introspectedTable.getBaseColumns().add(column);
            }
            blobColumns.clear();
        }*/

        introspectedTable.getTableConfiguration().setTableName(tableName);
        introspectedTable.setSqlMapFullyQualifiedRuntimeTableName(tableName);
        introspectedTable.setSqlMapAliasedFullyQualifiedRuntimeTableName(tableName);
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addTableNameProperty(topLevelClass, introspectedTable);
        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }

    /**
     * 添加一个tableName的属性
     *
     * @param topLevelClass
     * @param introspectedTable
     */
    private void addTableNameProperty(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        Field tableName = new Field("tableName", PrimitiveTypeWrapper.getStringInstance());
        tableName.setInitializationString("\"" + original_tableName + "\"");
        tableName.setVisibility(JavaVisibility.PRIVATE);
        addField(topLevelClass, introspectedTable, tableName);
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addTableNameProperty(topLevelClass, introspectedTable);
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }
}
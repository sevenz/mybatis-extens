package com.bob.generator.extens.plugins;

import com.bob.utils.JavaElementGeneratorTools;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;


import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 批量更新
 * Created by wangxiang on 17/7/6.
 */
public class BatchUpdatePlugin extends PluginAdapterEnhancement {

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addBatchUpdateMethod(interfaze, introspectedTable);
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        addBatchUpdateXml(document, introspectedTable);
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    private void addBatchUpdateMethod(Interface interfaze, IntrospectedTable introspectedTable) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
        importedTypes.add(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));

        Method ibsmethod = new Method();
        ibsmethod.setVisibility(JavaVisibility.PUBLIC);
        ibsmethod.setReturnType(FullyQualifiedJavaType.getIntInstance());
        ibsmethod.setName("updateBatchByPrimaryKeySelective");
        FullyQualifiedJavaType paramType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType paramListType = JavaElementGeneratorTools
                .getModelTypeWithoutBLOBs(introspectedTable);
        paramType.addTypeArgument(paramListType);
        ibsmethod.addJavaDocLine("/**");
        ibsmethod.addJavaDocLine(" * 业务处理时请控制下列表大小");
        ibsmethod.addJavaDocLine(" */");
        ibsmethod.addParameter(new Parameter(paramType, "list", "@Param(\"list\")"));

        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(ibsmethod);
    }

    public void addBatchUpdateXml(Document document, IntrospectedTable introspectedTable) {

        List<IntrospectedColumn> columns = ListUtilities
                .removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns());

        XmlElement insertBatchElement = new XmlElement("update");
        insertBatchElement.addAttribute(new Attribute("id", "updateBatchByPrimaryKeySelective"));
        insertBatchElement.addAttribute(new Attribute("parameterType", "java.util.List"));

        insertBatchElement.addElement(new TextElement("update " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));

        XmlElement trim1Element = new XmlElement("trim");
        trim1Element.addAttribute(new Attribute("prefix", "set"));
        trim1Element.addAttribute(new Attribute("suffixOverrides", ","));
        for (IntrospectedColumn introspectedColumn : columns) {

            // data类型数据有默认值刚不做任何操作
//            if (introspectedColumn.getFullyQualifiedJavaType().equals(FullyQualifiedJavaType.getDateInstance())) {
//                String d = introspectedColumn.getDefaultValue();
//                if (d != null && d.trim().length() > 0) {
//                    continue;
//                }
//            }

            String columnName = MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn);
            XmlElement ifColumnXml = new XmlElement("trim");
            ifColumnXml.addAttribute(new Attribute("prefix", columnName + " = case"));
            ifColumnXml.addAttribute(new Attribute("suffix", "end,"));

            XmlElement foreach = new XmlElement("foreach");
            foreach.addAttribute(new Attribute("collection", "list"));
            foreach.addAttribute(new Attribute("item", "item"));
            foreach.addAttribute(new Attribute("index", "index"));

            XmlElement ifxml = new XmlElement("if");
            ifxml.addAttribute(new Attribute("test", "item." + introspectedColumn.getJavaProperty() + "!=null"));
            ifxml.addElement(new TextElement("when id=#{item.id} then #{item." + introspectedColumn.getJavaProperty() + "}"));
            foreach.addElement(ifxml);

            ifColumnXml.addElement(foreach);

            trim1Element.addElement(ifColumnXml);
        }
        insertBatchElement.addElement(trim1Element);

        insertBatchElement.addElement(new TextElement("where id in ("));
        XmlElement foreach = new XmlElement("foreach");
        foreach.addAttribute(new Attribute("collection", "list"));
        foreach.addAttribute(new Attribute("separator", ","));
        foreach.addAttribute(new Attribute("item", "item"));
        foreach.addAttribute(new Attribute("index", "index"));
        foreach.addElement(new TextElement("#{item.id}"));
        insertBatchElement.addElement(foreach);
        insertBatchElement.addElement(new TextElement(" )"));

        document.getRootElement().addElement(insertBatchElement);
    }

    /**
     * 常规做法 - update ** set ** where **; update ** set ** where **;
     *
     * @param introspectedTable
     * @param columns
     * @return
     */
    private XmlElement oneXmlElement(IntrospectedTable introspectedTable, List<IntrospectedColumn> columns) {
        XmlElement insertBatchElement = new XmlElement("update");
        insertBatchElement.addAttribute(new Attribute("id", "updateBatchByPrimaryKeySelective"));
        insertBatchElement.addAttribute(new Attribute("parameterType", "java.util.List"));

        XmlElement foreach = new XmlElement("foreach");
        foreach.addAttribute(new Attribute("collection", "list"));
        foreach.addAttribute(new Attribute("item", "item"));
        foreach.addAttribute(new Attribute("index", "index"));
        foreach.addAttribute(new Attribute("separator", ";"));

        foreach.addElement(new TextElement("update " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));

        XmlElement trim1Element = new XmlElement("set");
        for (IntrospectedColumn introspectedColumn : columns) {
            String columnName = MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn);
            XmlElement ifxml = new XmlElement("if");
            ifxml.addAttribute(new Attribute("test", "item." + introspectedColumn.getJavaProperty() + "!=null"));
            ifxml.addElement(new TextElement(columnName + "=#{item." + introspectedColumn.getJavaProperty() + ",jdbcType=" + introspectedColumn.getJdbcTypeName() + "},"));
            trim1Element.addElement(ifxml);
        }
        foreach.addElement(trim1Element);

        foreach.addElement(new TextElement("where "));
        int index = 0;
        for (IntrospectedColumn i : introspectedTable.getPrimaryKeyColumns()) {
            foreach.addElement(new TextElement((index > 0 ? " AND " : "")
                    + MyBatis3FormattingUtilities.getAliasedEscapedColumnName(i)
                    + " = #{item." + i.getJavaProperty()
                    + ",jdbcType=" + i.getJdbcTypeName() + "}"));
            index = index + 1;
        }

        insertBatchElement.addElement(foreach);
        return insertBatchElement;
    }
}
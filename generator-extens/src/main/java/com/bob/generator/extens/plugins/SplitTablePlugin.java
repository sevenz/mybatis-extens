package com.bob.generator.extens.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.PrimitiveTypeWrapper;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

/**
 * 需要注意的是，如果同时使用了RowBoundsPlugin插件，需要把此插件写在它的前面，
 * 因为RowBoundsPlugin插件会拷呗生成一个后缀是WithRowbounds的方法，而此时from table还没改掉
 * 此插件只支持XMLMAPPER，因为是通过更改生成的xml节点来做到的，建议用SimpleSplitTablePlugin代替
 */
@Deprecated
public class SplitTablePlugin extends PluginAdapterEnhancement {

    private final String tableName = "tableName";

    public SplitTablePlugin() {
        logger.debug("split table plugin initialized");
    }

    /**
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        Field tableName = new Field("tableName", PrimitiveTypeWrapper.getStringInstance());
        tableName.setInitializationString("\"" + introspectedTable.getTableConfiguration().getTableName() + "\"");
        tableName.setVisibility(JavaVisibility.PRIVATE);
        addField(topLevelClass, introspectedTable, tableName);

        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        Field tableName = new Field("tableName", PrimitiveTypeWrapper.getStringInstance());
        tableName.setInitializationString("\"" + introspectedTable.getTableConfiguration().getTableName() + "\"");
        tableName.setVisibility(JavaVisibility.PRIVATE);
        addField(topLevelClass, introspectedTable, tableName);

        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        /*List<Element> elements = element.getElements();
        String content = elements.get(0).getFormattedContent(0);
        System.out.println(content);
        TextElement subSentence = new TextElement("delete from ${" + tableName + "}");
        elements.set(0, subSentence);*/
        return false;
    }

    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        /*List<Element> elements = element.getElements();
        String content = elements.get(2).getFormattedContent(0);
        System.out.println(content);
        TextElement subSentence = new TextElement("from ${" + tableName + "}");
        elements.set(2, subSentence);*/
        return false;
    }

    @Override
    public boolean sqlMapCountByExampleElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        resetCountByExample(element);
        return super.sqlMapCountByExampleElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        resetSelectXmlElementTableName(element);
        return super.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapUpdateByExampleSelectiveElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {

        resetUpdateXmlElementTableName(element);
        return super.sqlMapUpdateByExampleWithBLOBsElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        resetUpdateXmlElementTableName(element);
        return super.sqlMapUpdateByExampleWithBLOBsElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        resetUpdateXmlElementTableNameNotMapType(element);
        return super.sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        resetUpdateXmlElementTableNameNotMapType(element);
        return super.sqlMapUpdateByPrimaryKeySelectiveElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapInsertElementGenerated(XmlElement element,
                                                IntrospectedTable introspectedTable) {
        resetInsertXmlElementTableName(element);
        return super.sqlMapInsertElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element,
                                                         IntrospectedTable introspectedTable) {
        resetInsertXmlElementTableName(element);
        return super.sqlMapInsertSelectiveElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapDeleteByExampleElementGenerated(XmlElement element,
                                                         IntrospectedTable introspectedTable) {
        resetDeleteXmlElementTableName(element);
        return super.sqlMapDeleteByExampleElementGenerated(element, introspectedTable);
    }

    private void resetSelectXmlElementTableName(XmlElement element) {
        List<Element> elements = element.getElements();
        TextElement subSentence = new TextElement("from ${" + tableName + "}");
        elements.set(3, subSentence);
    }

    private void resetInsertXmlElementTableName(XmlElement element) {
        resetDIXmlElementTableName(element);
    }

    private void resetDeleteXmlElementTableName(XmlElement element) {
        resetDIXmlElementTableName(element);
    }

    private void resetDIXmlElementTableName(XmlElement element) {
        List<Element> elements = element.getElements();
        String content = elements.get(0).getFormattedContent(0);
        String[] data = content.split(" ");
        data[2] = "${" + tableName + "}";
        TextElement subSentence = new TextElement(SplitTablePlugin.join(" ", data));
        elements.set(0, subSentence);
    }

    private void resetUpdateXmlElementTableName(XmlElement element) {
        List<Element> elements = element.getElements();
        TextElement subSentence = new TextElement("update ${record." + tableName + "}");
        elements.set(0, subSentence);
    }

    private void resetUpdateXmlElementTableNameNotMapType(XmlElement element) {
        List<Element> elements = element.getElements();
        TextElement subSentence = new TextElement("update ${" + tableName + "}");
        elements.set(0, subSentence);
    }

    private void resetCountByExample(XmlElement element) {
        List<Element> elements = element.getElements();
        String content = elements.get(0).getFormattedContent(0);
        String[] data = content.split(" ");
        data[3] = "${" + tableName + "}";
        TextElement subSentence = new TextElement(SplitTablePlugin.join(" ", data));
        elements.set(0, subSentence);
    }

    public static String join(String join, String[] strAry) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strAry.length; i++) {
            if (i == (strAry.length - 1)) {
                sb.append(strAry[i]);
            } else {
                sb.append(strAry[i]).append(join);
            }
        }
        return new String(sb);
    }
}

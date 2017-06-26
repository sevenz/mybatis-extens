package com.bob.generator.extens.plugins;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

/**
 * 通过limit和offset 或只通过limit可以实现分页功能。
 * 假设 numberperpage 表示每页要显示的条数，pagenumber表示页码，那么 返回第pagenumber页，每页条数为numberperpage的sql语句：
 * select * from table limit (pagenumber-1)*numberperpage,numberperpage
 * select * from table limit numberperpage offset (pagenumber-1)*numberperpage
 * Created by wangxiang on 17/6/23.
 */
public class MySqlLimitPlugin extends PluginAdapterEnhancement {

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
                                              IntrospectedTable introspectedTable) {

        PrimitiveTypeWrapper integerWrapper = FullyQualifiedJavaType.getIntInstance().getPrimitiveTypeWrapper();
        CommentGenerator commentGenerator = context.getCommentGenerator();
        StringBuilder sb = new StringBuilder();

        Field field = new Field();
        field.setName("pageSize");
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(integerWrapper);
        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);
        String fieldName = field.getName();
        // 生成Set方法
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(generateSetMethodName(fieldName));
        method.addParameter(new Parameter(field.getType(), fieldName));
        sb.append("assert " + fieldName + " >= 0 : \"" + fieldName + " should be bigger than zero\";");
        sb.append(" this." + fieldName + "=" + fieldName + ";");
        method.addBodyLine(sb.toString());
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
        // 生成Get方法
        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(field.getType());
        method.setName(generateGetMethodName(fieldName));
        method.addBodyLine("return " + fieldName + ";");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);


        field = new Field();
        field.setName("pageNo");
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(integerWrapper);
        fieldName = field.getName();
        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);
        sb.setLength(0);
        // 生成Set方法
        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(generateSetMethodName(fieldName));
        method.addParameter(new Parameter(field.getType(), fieldName));
        sb.append("assert " + fieldName + " > 0 : \"" + fieldName + " should be bigger than zero\";");
        sb.append(" this." + fieldName + "=" + fieldName + ";");
        method.addBodyLine(sb.toString());
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
        // 生成Get方法
        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(field.getType());
        method.setName(generateGetMethodName(fieldName));
        method.addBodyLine("return " + fieldName + ";");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(integerWrapper);
        method.setName(generateGetMethodName("offset"));
        sb.setLength(0);
        sb.append(" if (pageSize != null && pageNo != null) {");
        sb.append(" return (pageNo - 1) * pageSize; ");
        sb.append(" } else {");
        sb.append(" return null;");
        sb.append(" }");
        method.addBodyLine(sb.toString());
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }

    /**
     * XML生成SQL时使用
     *
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        XmlElement ifLimitNotNullElement = new XmlElement("if");
        ifLimitNotNullElement.addAttribute(new Attribute("test", "pageSize != null"));

        XmlElement ifOffsetNotNullElement = new XmlElement("if");
        ifOffsetNotNullElement.addAttribute(new Attribute("test", "offset != null"));
        ifOffsetNotNullElement.addElement(new TextElement("limit ${offset}, ${pageSize}"));
        ifLimitNotNullElement.addElement(ifOffsetNotNullElement);

        XmlElement ifOffsetNullElement = new XmlElement("if");
        ifOffsetNullElement.addAttribute(new Attribute("test", "offset == null"));
        ifOffsetNullElement.addElement(new TextElement("limit ${pageSize}"));
        ifLimitNotNullElement.addElement(ifOffsetNullElement);

        element.addElement(ifLimitNotNullElement);
        return super.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
    }

    /**
     * 注解生成SQL时使用
     *
     * @param method
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean providerSelectByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        List<String> bodyLines = method.getBodyLines();
        // delete the line of "return sql.toString();"
        bodyLines.remove(bodyLines.size() - 1);

        bodyLines.add("// add pagination for mysql with limit clause ");
        bodyLines.add("StringBuilder sqlBuilder = new StringBuilder(sql.toString());");
        bodyLines.add("if( example != null && example.getPageSize() != null ) { ");
        bodyLines.add("     sqlBuilder.append(\" limit \");");
        bodyLines.add("     if( example.getOffset() != null ) { ");
        bodyLines.add("         sqlBuilder.append(example.getOffset()).append(\",\").append(example.getPageSize());");
        bodyLines.add("     } else {");
        bodyLines.add("         sqlBuilder.append(example.getPageSize()); ");
        bodyLines.add("     }");
        bodyLines.add("}");
        bodyLines.add("return sqlBuilder.toString();");

        return super.providerSelectByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
    }


}
package com.bob.generator.extens;

import com.bob.utils.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.DefaultCommentGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static com.bob.utils.Reflect.on;

/**
 * Created by bob on 16/6/27.
 */
public class DDefaultCommentGenerator extends DefaultCommentGenerator {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Properties pproperties = null;

    public DDefaultCommentGenerator() {
        super();
        if (pproperties == null) {
            pproperties = on(this).get("properties");
        }
    }

    @Override
    public void addJavaFileComment(CompilationUnit compilationUnit) {
        if (pproperties.getProperty("fileComment", "false").equalsIgnoreCase("true")) {
            compilationUnit.addFileCommentLine("/**");
            compilationUnit.addFileCommentLine(" * " + compilationUnit.getType().getShortName() + ".java");
            compilationUnit.addFileCommentLine(" * Copyright(C) com.weidai.*** Company");
            compilationUnit.addFileCommentLine(" * All rights reserved.");
            compilationUnit.addFileCommentLine(" * " + sdf.format(new Date()) + " was created");
            compilationUnit.addFileCommentLine(" */");
        }
    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        if (pproperties.getProperty("addWDParm", "false").equalsIgnoreCase("true")) {
            topLevelClass.addImportedType(new FullyQualifiedJavaType("com.weidai.apiExtractor.annotation.ApiParam"));
        }
        super.addModelClassComment(topLevelClass, introspectedTable);
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {

        final String remark = introspectedColumn.getRemarks();
        if (isNotEmpty(remark)) {
            if (pproperties.getProperty("addWDParm", "false").equalsIgnoreCase("true")) {
                field.addAnnotation("@ApiParam(description = \"" + remark + "\")");
            }
        }

        if (introspectedColumn.isIdentity()) {
            field.addJavaDocLine("/**");
            if (introspectedColumn.isAutoIncrement()) {
                field.addJavaDocLine(" * " + "数据库自增长物理主键");
            } else {
                field.addJavaDocLine(" * " + "数据库物理主键");
            }
            field.addJavaDocLine(" */");
        } else {

            String fieldType = field.getType().getShortName().toLowerCase();
            if (fieldType.equalsIgnoreCase("boolean")) {
                field.setInitializationString("false");
            }

            field.addJavaDocLine("/**");
            field.addJavaDocLine(" * " + (!introspectedColumn.isNullable() ? "此字段不许为空" : "此字段允许为空"));
            String defaultValue = "此字段数据库没有设置默认值";
            if (isNotEmpty(introspectedColumn.getDefaultValue())) {
                if (fieldType.equalsIgnoreCase("boolean")) {
                    if (introspectedColumn.getDefaultValue().equalsIgnoreCase("0")) {
                        defaultValue = "此字段数据库默认值为: false";
                    } else {
                        defaultValue = "此字段数据库默认值为: true";
                    }
                } else {
                    defaultValue = "此字段数据库默认值为: " + introspectedColumn.getDefaultValue();
                }
            } else {
                if (fieldType.equalsIgnoreCase("string")) {
                    defaultValue = "此字段数据库设置默认值为空";
                }
            }
            field.addJavaDocLine(" * " + defaultValue);

            if (isNotEmpty(remark)) {
                field.addJavaDocLine(" * " + "字段描述信息: " + remark);
            }
            if (fieldType.equalsIgnoreCase("string")) {
                field.addJavaDocLine(" * " + "数据库字段定义长度为: " + introspectedColumn.getLength());
            }
            if (introspectedColumn.getScale() > 0) {
                field.addJavaDocLine(" * " + "数据库字段保留小数点后 " + introspectedColumn.getScale() + " 位");
            }
            field.addJavaDocLine(" */");
        }
    }

    private boolean isNotEmpty(String input) {
        return !StringUtils.isNullOrEmpty(input);
    }
}
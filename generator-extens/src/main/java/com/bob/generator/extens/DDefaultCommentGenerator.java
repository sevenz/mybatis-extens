package com.bob.generator.extens;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.internal.DefaultCommentGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bob on 16/6/27.
 */
public class DDefaultCommentGenerator extends DefaultCommentGenerator {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void addJavaFileComment(CompilationUnit compilationUnit) {

        compilationUnit.addFileCommentLine("/**");
        compilationUnit.addFileCommentLine(" * " + compilationUnit.getType().getShortName() + ".java");
        compilationUnit.addFileCommentLine(" * Copyright(C) com.bob.*** Company");
        compilationUnit.addFileCommentLine(" * All rights reserved.");
        compilationUnit.addFileCommentLine(" * " + sdf.format(new Date()) + " was created");
        compilationUnit.addFileCommentLine(" */");
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        final String remark = introspectedColumn.getRemarks();
        if (remark != null && !"".equals(remark)) {
            field.addJavaDocLine("/**");
            field.addJavaDocLine(remark);
            field.addJavaDocLine(" */");
        }
    }
}
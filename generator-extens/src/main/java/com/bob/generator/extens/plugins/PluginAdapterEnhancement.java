package com.bob.generator.extens.plugins;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.logging.JdkLoggingImpl;
import org.mybatis.generator.logging.Log;

import java.util.List;

/**
 * Created by bob on 16/6/27.
 */
public abstract class PluginAdapterEnhancement extends PluginAdapter {

    protected Log logger = new JdkLoggingImpl(this.getClass());

    /**
     * 注释工具
     */
    protected CommentGenerator commentGenerator;

    /**
     * 取消验证
     *
     * @param list
     * @return
     */
    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        commentGenerator = context.getCommentGenerator();
    }

    /**
     * 获取系统分隔符
     *
     * @return
     */
    protected String getSeparator() {
        return System.getProperty("line.separator");
    }

    protected static String generateGetMethodName(String fieldName) {
        return "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }

    protected static String generateSetMethodName(String fieldName) {
        return "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }

    /**
     * 添加字段，同时也添加get,set方法
     *
     * @param topLevelClass
     * @param introspectedTable
     * @param field
     */
    protected void addField(TopLevelClass topLevelClass, IntrospectedTable introspectedTable, Field field) {

        CommentGenerator commentGenerator = context.getCommentGenerator();

        // 添加Java字段
        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);

        String fieldName = field.getName();

        // 生成Set方法
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(generateSetMethodName(fieldName));
        method.addParameter(new Parameter(field.getType(), fieldName));
        method.addBodyLine("this." + fieldName + "=" + fieldName + ";");
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
    }
}
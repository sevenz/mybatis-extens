package com.bob.generator.extens.plugins;

import com.bob.utils.PluginTools;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by wangxiang on 17/6/23.
 */
public class SelectOneByExamplePlugin extends PluginAdapterEnhancement {

    private boolean should_generator_one_method = false;
    private Config config;

    @Override
    public boolean validate(List<String> list) {
        if (this.config == null)
            this.config = new Config(getProperties());

        return PluginTools.shouldAfterPlugins(context, getClass(), new ArrayList<String>(), MySqlLimitPlugin.class);
    }

    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        if (!config.shouldExclude(interfaze.getType())) {
            Method temp = generateSelectOneByExample(method, introspectedTable);
            if (temp != null) {
                interfaze.addMethod(temp);
            }
        }
        return super.clientSelectByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (!config.shouldExclude(topLevelClass.getType())) {
            Method temp = generateSelectOneByExample(method, introspectedTable);
            if (temp != null) {
                topLevelClass.addMethod(temp);
            }
        }
        return super.clientSelectByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
    }

    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        if (!config.shouldExclude(interfaze.getType())) {
            Method temp = generateSelectOneByExample(method, introspectedTable);
            if (temp != null) {
                interfaze.addMethod(temp);
            }
        }
        return super.clientSelectByExampleWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (!config.shouldExclude(topLevelClass.getType())) {
            Method temp = generateSelectOneByExample(method, introspectedTable);
            if (temp != null) {
                topLevelClass.addMethod(temp);
            }
        }
        return super.clientSelectByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        List<Method> methods = interfaze.getMethods();
        for (Method m : methods) {
            System.out.println(m.getName());
            if (m.getName().equalsIgnoreCase("selectByExample")) {
                Method newM = new Method(config.methodToGenerate);
                newM.setVisibility(m.getVisibility());
                FullyQualifiedJavaType returnType = introspectedTable.getRules().calculateAllFieldsClass();
                newM.setReturnType(returnType);

                List<String> annotations = m.getAnnotations();
                for (String a : annotations) {
                    newM.addAnnotation(a);
                }

                List<Parameter> params = m.getParameters();
                for (Parameter p : params) {
                    newM.addParameter(p);
                }

                newM.addJavaDocLine("/**");
                newM.addJavaDocLine(" * 根据查询条件返回一条，找不到则返回null，找到多余一条数据则拋错");
                newM.addJavaDocLine(" */");
                newM.addException(new FullyQualifiedJavaType("org.apache.ibatis.exceptions.TooManyResultsException"));
                interfaze.getMethods().add(newM);
                break;
            }
        }
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {

        if (should_generator_one_method) {
            /**
             * 在生成的***MapperXML文件中添加一个id为selectOnyByExample的方法
             */
            XmlElement parentElement = document.getRootElement();
            for (Element item : parentElement.getElements()) {
                XmlElement temp = (XmlElement) item;
                Attribute attribute = null;
                for (Attribute itemA : temp.getAttributes()) {
                    if (itemA.getValue().contentEquals("selectByExample")) {
                        attribute = itemA;
                        break;
                    }
                }
                if (attribute != null) {
                    XmlElement select = new XmlElement(temp);
                    Attribute idFound = null;
                    for (Attribute itemB : select.getAttributes()) {
                        if (itemB.getName().contentEquals("id")) {
                            idFound = itemB;
                            break;
                        }
                    }
                    if (idFound != null) {
                        select.getAttributes().remove(idFound);
                    }

                    select.getAttributes().add(new Attribute("id", config.methodToGenerate));
                    int i = select.getElements().size();
                    if (i > 1) {
                        // 最后一个就是为了分页而添加的limit，不过最好判断出来有没有使用分页插件
                        select.getElements().remove(i - 1);
                    }
//                    select.addElement(new TextElement(" limit 1"));
                    parentElement.addElement(select);
                    break;
                }
            }
        }
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    /**
     * 在XXXMapper中添加一个selectOnyByExample方法
     *
     * @param method
     * @param introspectedTable
     * @return
     */
    private Method generateSelectOneByExample(Method method, IntrospectedTable introspectedTable) {

        should_generator_one_method = true;
//        if (!should_generator_one_method) {
//            should_generator_one_method = true;
//            Method m = new Method(config.methodToGenerate);
//            m.setVisibility(method.getVisibility());
//            FullyQualifiedJavaType returnType = introspectedTable.getRules().calculateAllFieldsClass();
//            m.setReturnType(returnType);
//
//            List<String> annotations = method.getAnnotations();
//            for (String a : annotations) {
//                m.addAnnotation(a);
//            }
//
//            List<Parameter> params = method.getParameters();
//            for (Parameter p : params) {
//                m.addParameter(p);
//            }
//
//            m.addJavaDocLine("/**");
//            m.addJavaDocLine(" * 根据查询条件返回一条，找不到则返回null，找到多余一条数据则拋错");
//            m.addJavaDocLine(" */");
//            m.addException(new FullyQualifiedJavaType("org.apache.ibatis.exceptions.TooManyResultsException"));
//            context.getCommentGenerator().addGeneralMethodComment(m, introspectedTable);
//            return m;
//        } else {
//            return null;
//        }

        return null;
    }

    private static final class Config extends PluginConfig {

        private static final String defaultMethodToGenerate = "selectOneByExample";
        private static final String methodToGenerateKey = "methodToGenerate";

        private String methodToGenerate;

        protected Config(Properties props) {
            super(props);
            this.methodToGenerate = props.getProperty(methodToGenerateKey, defaultMethodToGenerate);
        }

    }
}
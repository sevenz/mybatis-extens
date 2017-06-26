package com.bob.generator.extens.plugins;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.XmlElement;

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
        return super.validate(list);
    }

    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        if (!config.shouldExclude(interfaze.getType())) {
            Method temp = generateSelectOneByExample(method, introspectedTable);
            if(temp != null) {
                interfaze.addMethod(temp);
            }
        }
        return super.clientSelectByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (!config.shouldExclude(topLevelClass.getType())) {
            Method temp = generateSelectOneByExample(method, introspectedTable);
            if(temp != null) {
                topLevelClass.addMethod(temp);
            }
        }
        return super.clientSelectByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
    }

    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        if (!config.shouldExclude(interfaze.getType())) {
            Method temp = generateSelectOneByExample(method, introspectedTable);
            if(temp != null) {
                interfaze.addMethod(temp);
            }
        }
        return super.clientSelectByExampleWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (!config.shouldExclude(topLevelClass.getType())) {
            Method temp = generateSelectOneByExample(method, introspectedTable);
            if(temp != null) {
                topLevelClass.addMethod(temp);
            }
        }
        return super.clientSelectByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
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
                Optional<Attribute> attribute = Iterables.tryFind(temp.getAttributes(), x -> {
                    if (x.getValue().equalsIgnoreCase("selectByExample")) {
                        return true;
                    } else {
                        return false;
                    }
                });
                if (attribute.isPresent()) {
                    XmlElement select = new XmlElement(temp);
                    select.getAttributes().removeIf(x -> {
                        if (x.getName().equalsIgnoreCase("id")) {
                            return true;
                        } else {
                            return false;
                        }
                    });
                    select.getAttributes().add(new Attribute("id", config.methodToGenerate));
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
        if (!should_generator_one_method) {
            should_generator_one_method = true;
            Method m = new Method(config.methodToGenerate);
            m.setVisibility(method.getVisibility());
            FullyQualifiedJavaType returnType = introspectedTable.getRules().calculateAllFieldsClass();
            m.setReturnType(returnType);

            List<String> annotations = method.getAnnotations();
            for (String a : annotations) {
                m.addAnnotation(a);
            }

            List<Parameter> params = method.getParameters();
            for (Parameter p : params) {
                m.addParameter(p);
            }

            m.addJavaDocLine("/**");
            m.addJavaDocLine(" * 根据查询条件返回一条，找不到则返回null，找到多余一条数据则拋错");
            m.addJavaDocLine(" */");
            m.addException(new FullyQualifiedJavaType("org.apache.ibatis.exceptions.TooManyResultsException"));
            context.getCommentGenerator().addGeneralMethodComment(m, introspectedTable);
            return m;
        } else {
            return null;
        }
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
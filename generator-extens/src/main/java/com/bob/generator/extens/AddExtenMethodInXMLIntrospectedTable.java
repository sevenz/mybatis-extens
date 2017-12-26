package com.bob.generator.extens;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxiang on 17/6/23.
 */
public class AddExtenMethodInXMLIntrospectedTable extends IntrospectedTableMyBatis3Impl {

    public AddExtenMethodInXMLIntrospectedTable() {
        super();
    }

    /**
     * https://my.oschina.net/u/140938/blog/220006
     * 解决生成xml文件时默认是追加而不是覆盖
     *
     * @return
     */
    @Override
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>();

        if (xmlMapperGenerator != null) {
            String tmp = context.getProperty("mergeable");
            boolean mergeable = false;
            if ("true".equalsIgnoreCase(tmp)) {
                mergeable = true;
            }

            Document document = xmlMapperGenerator.getDocument();
            GeneratedXmlFile gxf = new GeneratedXmlFile(document,
                    getMyBatis3XmlMapperFileName(), getMyBatis3XmlMapperPackage(),
                    context.getSqlMapGeneratorConfiguration().getTargetProject(),
                    mergeable, context.getXmlFormatter());
            if (context.getPlugins().sqlMapGenerated(gxf, this)) {
                answer.add(gxf);
            }
        }
        return answer;
    }
}
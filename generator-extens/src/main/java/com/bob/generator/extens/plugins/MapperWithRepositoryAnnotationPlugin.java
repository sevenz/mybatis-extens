package com.bob.generator.extens.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * 在生成的Mapper类中添加Spring的Repository注解
 */
public class MapperWithRepositoryAnnotationPlugin extends PluginAdapterEnhancement {

    /**
     * Spring's Repository Annotation
     */
    private final String REPOSITORY_ANNOTATION = "@Repository";

    /**
     * The Repositiory Keyword Package Path In Spring
     */
    private final String REPOSITORY_PACKAGE = "org.springframework.stereotype.Repository";

    public MapperWithRepositoryAnnotationPlugin() {
        logger.debug("mapper with repository plugin initialized");
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interfaze.addAnnotation(REPOSITORY_ANNOTATION);
        interfaze.addImportedType(new FullyQualifiedJavaType(REPOSITORY_PACKAGE));
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }
}
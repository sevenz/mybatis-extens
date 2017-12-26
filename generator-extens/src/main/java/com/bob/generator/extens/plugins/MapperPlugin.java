package com.bob.generator.extens.plugins;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.bob.utils.Reflect.on;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * 用于生成一些额外文件
 * <p>
 * Created by wangxiang on 17/6/26.
 */
public class MapperPlugin extends PluginAdapterEnhancement {

    private static final String DEFAULT_DAO_SUPER_CLASS = "com.dfz.base.BaseMapper";
    private static final String DEFAULT_EXPAND_DAO_SUPER_CLASS = "com.bob.mybatis.demos.bbtotal.mapper";
    private String daoTargetDir;
    private String daoTargetPackage;

    private String daoSuperClass;

    // 扩展
    private String expandDaoTargetPackage;
    private String expandDaoSuperClass;

    private ShellCallback shellCallback = null;

    public MapperPlugin() {
        shellCallback = new DefaultShellCallback(false);
    }

    @Override
    public boolean validate(List<String> list) {

        daoTargetDir = properties.getProperty("targetProject");
        boolean valid = stringHasValue(daoTargetDir);

        daoTargetPackage = properties.getProperty("targetPackage");
        boolean valid2 = stringHasValue(daoTargetPackage);

        daoSuperClass = properties.getProperty("daoSuperClass");
        if (!stringHasValue(daoSuperClass)) {
            daoSuperClass = DEFAULT_DAO_SUPER_CLASS;
        }

        expandDaoTargetPackage = properties.getProperty("expandTargetPackage");
        expandDaoSuperClass = properties.getProperty("expandDaoSuperClass");
        if (!stringHasValue(expandDaoSuperClass)) {
            expandDaoSuperClass = DEFAULT_EXPAND_DAO_SUPER_CLASS;
        }
        if (!stringHasValue(expandDaoTargetPackage)) {
            expandDaoTargetPackage = DEFAULT_EXPAND_DAO_SUPER_CLASS;
        }
        return valid && valid2;
    }

    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(
            IntrospectedTable introspectedTable) {
        List<GeneratedXmlFile> mapperXmlFiles = new ArrayList<>();
        GeneratedXmlFile mapperXmlfile;
        for (GeneratedXmlFile xmlFile : introspectedTable.getGeneratedXmlFiles()) {
            System.out.println(xmlFile.getFileName());
            Document document = on(xmlFile).get("document");
            System.out.println(document);
            document.getRootElement().getElements().clear();
            XmlElement xmlElement = document.getRootElement();
            System.out.println(xmlElement);
            String fileName = xmlFile.getFileName().replace("Mapper", "ExpandMapper");
            mapperXmlfile = new GeneratedXmlFile(document,
                    fileName, xmlFile.getTargetPackage(), xmlFile.getTargetProject(), false, context.getXmlFormatter());
            try {
                File mapperFile = generator(shellCallback.getDirectory(daoTargetDir, daoTargetPackage), fileName);
                // 文件不存在
                if (!mapperFile.exists()) {
                    mapperXmlFiles.add(mapperXmlfile);
                }
            } catch (ShellException e) {
                e.printStackTrace();
            }
        }
        mapperXmlFiles.clear();
        return mapperXmlFiles;
    }

    /**
     * 配合CleanMapperPlugin使用，这样才可以在Mapper中添加自定义方法。如果文件已存在，则不重新生成，
     * 默认情况这个mapper已经被CleanMapperPlugin搞的很干净了，特殊要求在里面添加手写方法，所以得判断是不是已经存在
     *
     * @param interfaze
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        try {
            File mapperDir = shellCallback.getDirectory(context.getJavaClientGeneratorConfiguration().getTargetProject(),
                    context.getJavaClientGeneratorConfiguration().getTargetPackage());
            String fileName = interfaze.getType().getShortName() + ".java";
            File mapperFile = generator(mapperDir, fileName);
            if (mapperFile.exists()) {
                return false;
            }
            System.out.println(mapperDir);
        } catch (ShellException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        JavaFormatter javaFormatter = context.getJavaFormatter();
        List<GeneratedJavaFile> mapperJavaFiles = new ArrayList<>();
        List<GeneratedJavaFile> generatedJavaFiles = introspectedTable.getGeneratedJavaFiles();
        for (GeneratedJavaFile javaFile : generatedJavaFiles) {
            /**
             * javaFile是如下三个文件
             * ***Entity
             * ***EntityExample
             * ***EntityMapper
             */

            CompilationUnit unit = javaFile.getCompilationUnit();
            FullyQualifiedJavaType baseModelJavaType = unit.getType();

            String shortName = baseModelJavaType.getShortName();
            GeneratedJavaFile mapperJavafile;

            if (shortName.endsWith("Mapper")) { // 扩展Mapper
                if (stringHasValue(expandDaoTargetPackage)) {
                    Interface mapperInterface = new Interface(
                            expandDaoTargetPackage + "." + shortName.replace("Mapper", "ExpandMapper"));
                    mapperInterface.setVisibility(JavaVisibility.PUBLIC);
                    mapperInterface.addJavaDocLine("/**");
                    mapperInterface.addJavaDocLine(" * " + shortName + "扩展");
                    mapperInterface.addJavaDocLine(" */");

                    mapperJavafile = new GeneratedJavaFile(mapperInterface, daoTargetDir, javaFormatter);
                    try {
                        File mapperDir = shellCallback.getDirectory(daoTargetDir, daoTargetPackage);
                        File mapperFile = generator(mapperDir, mapperJavafile.getFileName());
                        // 文件不存在
                        if (!mapperFile.exists()) {
                            mapperJavaFiles.add(mapperJavafile);
                        }
                    } catch (ShellException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        mapperJavaFiles.clear();
        return mapperJavaFiles;
    }

    private File generator(File dir, String name) {
        return new File(dir, name);
    }
}
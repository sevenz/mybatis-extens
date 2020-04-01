package com.bob.generator.extens.plugins;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * 〈〉
 *
 * @author wangxiang
 * @create 2019-01-24
 */
public class JsonPlugin extends PluginAdapterEnhancement {

  private ShellCallback shellCallback;

  public JsonPlugin() {
    shellCallback = new DefaultShellCallback(false);
  }

  @Override
  public boolean sqlMapResultMapWithoutBLOBsElementGenerated(XmlElement element,
      IntrospectedTable introspectedTable) {

    System.out.println(element.getName());

    return super.sqlMapResultMapWithoutBLOBsElementGenerated(element, introspectedTable);
  }

  @Override
  public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass,
      IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable,
      ModelClassType modelClassType) {

    System.out.println(field.getName());

    return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable,
        modelClassType);
  }

  @Override
  public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
      IntrospectedTable introspectedTable) {

    //Mapper文件的注释
    interfaze.addJavaDocLine("/**");
    interfaze.addJavaDocLine("* Created by Mybatis Generator on " + new Date());
    interfaze.addJavaDocLine("*/");
    interfaze.addAnnotation("@org.apache.ibatis.annotations.Mapper");
    return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
  }

  /**
   * 额外生成一个 repository
   */
  @Override
  public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(
      IntrospectedTable introspectedTable) {

    JavaFormatter javaFormatter = context.getJavaFormatter();
    List<GeneratedJavaFile> mapperJavaFiles = new ArrayList<>();

    List<GeneratedJavaFile> generatedJavaFiles = introspectedTable.getGeneratedJavaFiles();
    for (GeneratedJavaFile javaFile : generatedJavaFiles) {
      String shortName = javaFile.getCompilationUnit().getType().getShortName();
      if (shortName.endsWith("Mapper")) {

        TopLevelClass repositoryClass = new TopLevelClass(
            javaFile.getTargetPackage().replace("mapper", "repository")
                + "."
                + shortName.replace("Mapper", "Repository"));

        repositoryClass.setVisibility(JavaVisibility.PUBLIC);
        repositoryClass.addJavaDocLine("/**");
        repositoryClass
            .addJavaDocLine(" * " + shortName.replace("Mapper", "Repository") + new Date());
        repositoryClass.addJavaDocLine(" */");
        repositoryClass.addAnnotation("@Repository");
        repositoryClass.addImportedType(
            new FullyQualifiedJavaType("org.springframework.stereotype.Repository"));
        repositoryClass.addImportedType("com.bob.mybatis.demos.bbtotal.repository.AbstractRepository");
        repositoryClass.addImportedType(introspectedTable.getBaseRecordType());
        repositoryClass.addImportedType(javaFile.getTargetPackage() + "." + shortName);
        repositoryClass.setSuperClass(
            new FullyQualifiedJavaType(
                "AbstractRepository<" + shortName + ", "
                    + shortName.replace("Mapper", "")
                    + " >"));
        Method method = new Method("whenBuild");
        method.setReturnType(new FullyQualifiedJavaType("String"));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine(
            "return \"" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + "\";");
        repositoryClass.addMethod(method);

        GeneratedJavaFile repositoryJavafile = new GeneratedJavaFile(repositoryClass,
            javaFile.getTargetProject(),
            context.getProperty("javaFileEncoding"),
            javaFormatter);
        try {
          File mapperDir = shellCallback
              .getDirectory(javaFile.getTargetProject(),
                  repositoryJavafile.getTargetPackage());
          File mapperFile = generator(mapperDir, repositoryJavafile.getFileName());
          // 文件不存在
          if (!mapperFile.exists()) {
            mapperJavaFiles.add(repositoryJavafile);
          }
        } catch (ShellException e) {
          e.printStackTrace();
        }
      }
    }

    return mapperJavaFiles;
  }

  private File generator(File dir, String name) {
    return new File(dir, name);
  }
}
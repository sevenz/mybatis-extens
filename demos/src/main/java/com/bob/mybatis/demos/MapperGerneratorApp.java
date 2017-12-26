package com.bob.mybatis.demos;

import org.mybatis.generator.api.ShellRunner;

public class MapperGerneratorApp {

    public static void main(String[] args) {

        String fileName = "/generator-mybatis-localhotel.xml";
        System.out.println(fileName);
        String path = System.class.getResource(fileName).getPath();
        System.out.println(path);
        String[] args2 = {"-configfile", path, "-overwrite", "-verbose"};
        ShellRunner.main(args2);
    }
}
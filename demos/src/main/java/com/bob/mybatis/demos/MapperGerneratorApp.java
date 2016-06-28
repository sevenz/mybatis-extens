package com.bob.mybatis.demos;

import org.mybatis.generator.api.ShellRunner;

public class MapperGerneratorApp {

    public static void main(String[] args) {

        String path = System.class.getResource("/generator-mybatis.xml").getPath();
        System.out.println(path);
        String[] args2 = {"-configfile", path, "-overwrite", "-verbose"};
        ShellRunner.main(args2);
    }
}
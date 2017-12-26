package com.bob.generator.extens.plugins;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Created by wangxiang on 17/6/23.
 */
public abstract class PluginConfig {

    private static final String excludeClassNamesRegexpKey = "excludeClassNamesRegexp";

    private Pattern excludeClassNamesRegexp;

    protected PluginConfig(Properties props) {
        String regexp = props.getProperty(excludeClassNamesRegexpKey, null);
        if (regexp != null)
            this.excludeClassNamesRegexp = Pattern.compile(regexp);
    }

    boolean shouldExclude(FullyQualifiedJavaType type) {
        return this.shouldExclude(type.getFullyQualifiedName());
    }

    boolean shouldExclude(String className) {
        return excludeClassNamesRegexp != null && excludeClassNamesRegexp.matcher(className).matches();
    }
}
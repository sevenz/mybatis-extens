package com.bob.generator.extens;

import org.mybatis.generator.api.JavaTypeResolver;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import java.sql.Types;

/**
 * Created by wangxiang on 17/6/27.
 */
public class JavaTypeResolverCustomImpl extends JavaTypeResolverDefaultImpl implements JavaTypeResolver {

    public JavaTypeResolverCustomImpl() {
        super();
        typeMap.put(Types.TINYINT, new JdbcTypeInformation("TINYINT",
                new FullyQualifiedJavaType(Integer.class.getName())));
    }
}
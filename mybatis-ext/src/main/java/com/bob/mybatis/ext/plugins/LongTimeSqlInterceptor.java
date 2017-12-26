package com.bob.mybatis.ext.plugins;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.util.*;

/**
 * 打印出执行时间过长的SQL语句
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class LongTimeSqlInterceptor implements Interceptor {

    private Logger logger = LoggerFactory.getLogger(LongTimeSqlInterceptor.class);

    private Properties properties;

    static int MAPPED_STATEMENT_INDEX = 0;
    static int PARAMETER_INDEX = 1;
    static int ROWBOUNDS_INDEX = 2;
    static int RESULT_HANDLER_INDEX = 3;

    static DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);

    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[MAPPED_STATEMENT_INDEX];
//        Object parameter = invocation.getArgs().length > 1 ? invocation.getArgs()[PARAMETER_INDEX] : null;
//        BoundSql bsql = mappedStatement.getBoundSql(parameter);
//        Configuration configuration = mappedStatement.getConfiguration();
        long start = System.currentTimeMillis();
        Object returnValue = invocation.proceed();
        long end = System.currentTimeMillis();
        long time = (end - start);
        if (time > 1) {
            String sqlId = mappedStatement.getId();
            logger.info(sqlId + " cost: " + time + " ms");
        }
        return returnValue;

        /*Object o0 = invocation.getArgs()[MAPPED_STATEMENT_INDEX];
        if (o0 instanceof MappedStatement) {
            MappedStatement ms = (MappedStatement) o0;
            if (ms.getSqlCommandType() == SqlCommandType.SELECT) {
                BoundSql boundSql = ms.getBoundSql(invocation.getArgs()[PARAMETER_INDEX]);
                String sql = boundSql.getSql().trim();
                BoundSql newBoundSql = new BoundSql(ms.getConfiguration(),
                        getCountSQL(sql), boundSql.getParameterMappings(),
                        boundSql.getParameterObject());
                for (ParameterMapping mapping : boundSql.getParameterMappings()) {
                    String prop = mapping.getProperty();
                    if (boundSql.hasAdditionalParameter(prop)) {
                        newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
                    }
                }
                System.out.println(newBoundSql.getSql().trim());
                MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
                invocation.getArgs()[MAPPED_STATEMENT_INDEX] = newMs;
                invocation.proceed();
                invocation.getArgs()[MAPPED_STATEMENT_INDEX] = o0;
            } else if (ms.getSqlCommandType() == SqlCommandType.INSERT) {

            }
        }*/
    }

    @Override
    public Object plugin(Object target) {
        // 通过此处判断需不需要wrap，如果没有wrap，则不会进入intercept方法
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @SuppressWarnings("unused")
    private final String getSql(Configuration configuration, BoundSql boundSql, String sqlId, long time) {
        String sql = showSql(configuration, boundSql);
        StringBuilder str = new StringBuilder(100);
        str.append(sqlId);
        str.append(":");
        str.append(sql);
        str.append(":");
        str.append(time);
        str.append("ms");
        return str.toString();
    }

    private final String getParameterValue(Object obj) {
        String value;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }

    private final String showSql(final Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));

            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }
        return sql;
    }


    // see: MapperBuilderAssistant
    @SuppressWarnings({"unchecked", "rawtypes"})
    private MappedStatement copyFromMappedStatement(MappedStatement ms,
                                                    SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms
                .getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        List<ResultMap> resultMaps = new ArrayList<>();
        String id = "-inline";
        if (ms.getResultMaps() != null) {
            id = ms.getResultMaps().get(0).getId() + "-inline";
        }
        ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), id, Long.class,
                new ArrayList()).build();
        resultMaps.add(resultMap);
        builder.resultMaps(resultMaps);
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

}
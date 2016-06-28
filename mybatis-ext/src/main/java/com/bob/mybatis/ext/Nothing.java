package com.bob.mybatis.ext;

public class Nothing {

    /**
     * mybatis支持对Executor,StatementHandler,PameterHandler,ResultSetHandler的四种拦截
     * 可以看到在Configuration中有newParameterHandler,newResultSetHandler,newStatementHandler,newExecutor四个方法
     * 拦截ResultSetHandler(handleResultSets,handleOutputParameters)修改接口返回类型
     * 拦截StatementHandler(prepare,parameterize,batch,update,query)修改mybatis框架分页机制
     * 拦截Executor(update,query,flushStatements,commit,rollback,getTransaction,close,isClosed)查看mybatis的sql执行过程
     */

    /**
     * 如果采用java code的方式配置，同时采用非注解即xml形式的sql，此时mappper类跟mapper.xml要么在一个文件夹下，如果不是，那么需要他们包空间一样
     */
}
package com.bob.mybatis.demos;


import com.bob.mybatis.demos.bbtotal.entity.Mybatisbb;
import com.bob.mybatis.demos.bbtotal.entity.MybatisbbExample;
import com.bob.mybatis.demos.bbtotal.entity.Mybatisbb_01;
import com.bob.mybatis.demos.bbtotal.entity.Mybatisbb_01Example;
import com.bob.mybatis.demos.bbtotal.mapper.MybatisbbMapper;
import com.bob.mybatis.demos.bbtotal.mapper.Mybatisbb_01Mapper;
import com.bob.mybatis.ext.plugins.LogPlugin;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import java.util.List;

public class MyBatisApp {

    public static void main(String[] args) {

        MyBatisApp app = new MyBatisApp();
        SqlSessionFactory sqlSessionFactory = app.sqlSessionFactory();
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            MybatisbbMapper mybatisbbMapper = session.getMapper(MybatisbbMapper.class);

            Mybatisbb mybatisbbone = new Mybatisbb();
            mybatisbbone.setName("one");
            mybatisbbone.setTableName("mybatis_bb_00");
            mybatisbbone.setUserId(100l);
            mybatisbbMapper.insert(mybatisbbone);
            System.out.println(mybatisbbone.getAutoId());

            Mybatisbb mybatisbbtwo = new Mybatisbb();
            mybatisbbtwo.setName("two");
            mybatisbbtwo.setTableName("mybatis_bb_01");
            mybatisbbtwo.setUserId(101l);
            mybatisbbMapper.insert(mybatisbbtwo);
            System.out.println(mybatisbbtwo.getAutoId());

            Mybatisbb mybatisbbthr = new Mybatisbb();
            mybatisbbthr.setName("thr");
            mybatisbbthr.setTableName("mybatis_bb_02");
            mybatisbbthr.setUserId(102l);
            mybatisbbMapper.insert(mybatisbbthr);
            System.out.println(mybatisbbthr.getAutoId());

            Mybatisbb_01Mapper mybatisbb_01Mapper = session.getMapper(Mybatisbb_01Mapper.class);
            Mybatisbb_01Example mybatisbbExample_01 = new Mybatisbb_01Example();
            mybatisbbExample_01.createCriteria().andNameEqualTo("two");
            List<Mybatisbb_01> results = mybatisbb_01Mapper.selectByExample(mybatisbbExample_01);
            results.stream().forEach(x -> System.out.println(x));



            MybatisbbExample mybatisbbExample = new MybatisbbExample();
            mybatisbbExample.setTableName("mybatis_bb_01");
            mybatisbbMapper.deleteByExample(mybatisbbExample);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    private SqlSessionFactory sqlSessionFactory() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/bbtotal");

        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("dev", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(MybatisbbMapper.class);
        configuration.addMapper(Mybatisbb_01Mapper.class);
        configuration.addInterceptor(new LogPlugin());
        configuration.setLogPrefix("dao.");

        return new SqlSessionFactoryBuilder().build(configuration);
    }
}

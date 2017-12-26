package com.bob.mybatis.demos;

import com.bob.mybatis.demos.bbtotal.entity.HelpCenterEntity;
import com.bob.mybatis.demos.bbtotal.entity.HelpCenterEntityExample;
import com.bob.mybatis.demos.bbtotal.mapper.HelpCenterEntityMapper;
import org.apache.ibatis.session.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyBatisApp {

    public static void main(String[] args) {

        MyBatisApp myBatisApp = new MyBatisApp();
        SqlSessionFactory sqlSessionFactory = myBatisApp.sqlSessionFactory();

        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            HelpCenterEntityMapper helpCenterEntityMapper = session.getMapper(HelpCenterEntityMapper.class);

            HelpCenterEntity helpCenterEntity = new HelpCenterEntity();
            helpCenterEntity.setAnswer("A");
            helpCenterEntity.setQuestion("d");
            helpCenterEntity.setOtherstatus(1);
            helpCenterEntity.setIsdelete(true);
            helpCenterEntityMapper.insertSelective(helpCenterEntity);

            HelpCenterEntityExample entityExample = new HelpCenterEntityExample();
            entityExample.createCriteria().andIdEqualTo(helpCenterEntity.getId() + 3);
            helpCenterEntity = helpCenterEntityMapper.selectOneByExample(entityExample);
            if (helpCenterEntity != null) {
                System.out.println(helpCenterEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    private SqlSessionFactory sqlSessionFactory() {

//        HikariDataSource dataSource = new HikariDataSource();
//        dataSource.setUsername("root");
//        dataSource.setPassword("root");
//        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/hotel");
//        TransactionFactory transactionFactory = new JdbcTransactionFactory();
//        Environment environment = new Environment("dev", transactionFactory, dataSource);
//        Configuration configuration = new Configuration(environment);
//        configuration.addMapper(MybatisbbMapper.class);
//        configuration.addMapper(Mybatisbb_01Mapper.class);
//        configuration.addMapper(HelpCenterEntityMapper.class);
//        configuration.addInterceptor(new LogPlugin());
//        configuration.setLogPrefix("dao.");
//        return new SqlSessionFactoryBuilder().build(configuration);


//        String resource = "mybatis-config.xml";
//        InputStream inputStream = null;
//        try {
//            inputStream = Resources.getResourceAsStream(resource);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
//        SqlSessionFactory factory = builder.build(inputStream);
//        return factory;


        ApplicationContext application = new
                ClassPathXmlApplicationContext("classpath:spring-beans.xml");
        return application.getBean(SqlSessionFactory.class);
    }
}
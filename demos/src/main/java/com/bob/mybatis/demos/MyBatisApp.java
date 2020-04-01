package com.bob.mybatis.demos;

import com.bob.mybatis.demos.bbtotal.entity.Stock;
import com.bob.mybatis.demos.bbtotal.repository.StockRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyBatisApp {

  public static void main(String[] args) {

    ObjectMapper objectMapper = new ObjectMapper();


    MyBatisApp myBatisApp = new MyBatisApp();
    ApplicationContext applicationContext = myBatisApp.applicationContext();
    StockRepository stockRepository = applicationContext.getBean(StockRepository.class);
    Stock stock = new Stock();
    stock.setDate("adadfa");
    stock.setId(1122l);
    stock.setLdtime(111111l);
    stock.setTime("1");
    Integer i = stockRepository.insert(stock);
    System.out.println(i.toString());

    SqlSessionFactory sqlSessionFactory = myBatisApp.sqlSessionFactory();
    try (SqlSession session = sqlSessionFactory.openSession(true)) {

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

    ApplicationContext application = applicationContext();
    return application.getBean(SqlSessionFactory.class);
  }

  private ApplicationContext applicationContext() {

    ApplicationContext application = new
        ClassPathXmlApplicationContext("classpath:spring-beans.xml");

    return application;
  }
}
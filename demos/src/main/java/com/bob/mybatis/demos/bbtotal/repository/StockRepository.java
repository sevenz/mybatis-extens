package com.bob.mybatis.demos.bbtotal.repository;

import com.bob.mybatis.demos.bbtotal.entity.Stock;
import com.bob.mybatis.demos.bbtotal.mapper.StockMapper;
import org.springframework.stereotype.Repository;

/**
 * StockRepositoryWed Apr 01 14:19:27 CST 2020
 */
@Repository
public class StockRepository extends AbstractRepository<StockMapper, Stock> {

    public String whenBuild() {
        return "2020-04-01 02:19:27";
    }
}
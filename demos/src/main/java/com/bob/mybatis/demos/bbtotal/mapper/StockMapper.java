package com.bob.mybatis.demos.bbtotal.mapper;

import com.bob.mybatis.demos.bbtotal.entity.Stock;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
* Created by Mybatis Generator on Wed Apr 01 14:19:27 CST 2020
*/
@org.apache.ibatis.annotations.Mapper
public interface StockMapper extends Mapper<Stock>, InsertListMapper<Stock> {
}
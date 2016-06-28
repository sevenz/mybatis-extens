/*
 * Mybatisbb_01Mapper.java
 * Copyright(C) com.bob.*** Company
 * All rights reserved.
 * 2016-06-28 was created
 */
package com.bob.mybatis.demos.bbtotal.mapper;

import com.bob.mybatis.demos.bbtotal.entity.Mybatisbb_01;
import com.bob.mybatis.demos.bbtotal.entity.Mybatisbb_01Example;
import java.util.List;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.JdbcType;

public interface Mybatisbb_01Mapper {
    @SelectProvider(type=Mybatisbb_01SqlProvider.class, method="countByExample")
    int countByExample(Mybatisbb_01Example example);

    @DeleteProvider(type=Mybatisbb_01SqlProvider.class, method="deleteByExample")
    int deleteByExample(Mybatisbb_01Example example);

    @Insert({
        "insert into mybatis_bb_01 (name, userId)",
        "values (#{name,jdbcType=VARCHAR}, #{userId,jdbcType=BIGINT})"
    })
    @Options(useGeneratedKeys=true,keyProperty="autoId")
    int insert(Mybatisbb_01 record);

    @InsertProvider(type=Mybatisbb_01SqlProvider.class, method="insertSelective")
    @Options(useGeneratedKeys=true,keyProperty="autoId")
    int insertSelective(Mybatisbb_01 record);

    @SelectProvider(type=Mybatisbb_01SqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="autoId", property="autoId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="userId", property="userId", jdbcType=JdbcType.BIGINT)
    })
    List<Mybatisbb_01> selectByExampleWithRowbounds(Mybatisbb_01Example example, RowBounds rowBounds);

    @SelectProvider(type=Mybatisbb_01SqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="autoId", property="autoId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="userId", property="userId", jdbcType=JdbcType.BIGINT)
    })
    List<Mybatisbb_01> selectByExample(Mybatisbb_01Example example);

    @UpdateProvider(type=Mybatisbb_01SqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") Mybatisbb_01 record, @Param("example") Mybatisbb_01Example example);

    @UpdateProvider(type=Mybatisbb_01SqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") Mybatisbb_01 record, @Param("example") Mybatisbb_01Example example);

    @UpdateProvider(type=Mybatisbb_01SqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Mybatisbb_01 record);

    @Update({
        "update mybatis_bb_01",
        "set name = #{name,jdbcType=VARCHAR},",
          "userId = #{userId,jdbcType=BIGINT}",
        "where autoId = #{autoId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Mybatisbb_01 record);
}
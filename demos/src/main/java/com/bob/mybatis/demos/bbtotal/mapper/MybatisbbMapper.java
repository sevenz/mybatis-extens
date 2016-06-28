/*
 * MybatisbbMapper.java
 * Copyright(C) com.bob.*** Company
 * All rights reserved.
 * 2016-06-28 was created
 */
package com.bob.mybatis.demos.bbtotal.mapper;

import com.bob.mybatis.demos.bbtotal.entity.Mybatisbb;
import com.bob.mybatis.demos.bbtotal.entity.MybatisbbExample;
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

public interface MybatisbbMapper {
    @SelectProvider(type=MybatisbbSqlProvider.class, method="countByExample")
    int countByExample(MybatisbbExample example);

    @DeleteProvider(type=MybatisbbSqlProvider.class, method="deleteByExample")
    int deleteByExample(MybatisbbExample example);

    @Insert({
        "insert into  ${tableName}  (name, userId)",
        "values (#{name,jdbcType=VARCHAR}, #{userId,jdbcType=BIGINT})"
    })
    @Options(useGeneratedKeys=true,keyProperty="autoId")
    int insert(Mybatisbb record);

    @InsertProvider(type=MybatisbbSqlProvider.class, method="insertSelective")
    @Options(useGeneratedKeys=true,keyProperty="autoId")
    int insertSelective(Mybatisbb record);

    @SelectProvider(type=MybatisbbSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="autoId", property="autoId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="userId", property="userId", jdbcType=JdbcType.BIGINT)
    })
    List<Mybatisbb> selectByExampleWithRowbounds(MybatisbbExample example, RowBounds rowBounds);

    @SelectProvider(type=MybatisbbSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="autoId", property="autoId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="userId", property="userId", jdbcType=JdbcType.BIGINT)
    })
    List<Mybatisbb> selectByExample(MybatisbbExample example);

    @UpdateProvider(type=MybatisbbSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") Mybatisbb record, @Param("example") MybatisbbExample example);

    @UpdateProvider(type=MybatisbbSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") Mybatisbb record, @Param("example") MybatisbbExample example);

    @UpdateProvider(type=MybatisbbSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Mybatisbb record);

    @Update({
        "update  ${tableName} ",
        "set name = #{name,jdbcType=VARCHAR},",
          "userId = #{userId,jdbcType=BIGINT}",
        "where autoId = #{autoId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Mybatisbb record);
}
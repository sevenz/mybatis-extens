package com.bob.mybatis.demos.bbtotal.mapper;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangxiang on 17/6/28.
 */
public interface BaseMapper<T, E, PK extends Serializable> {

    long countByExample(E example);

    /**
     * 根据查询条件进行物理删除
     * @param example
     * @return
     */
    int deleteByExample(E example);

    /**
     * 根据主键进行物理删除
     * @param pk
     * @return
     */
    int deleteByPrimaryKey(PK pk);

    /**
     * 逻辑删除
     */
    int deleteLogicByPrimaryKey(PK pk);

    int insert(T record);

    int insertSelective(T record);

    List<T> selectByExample(E example);

    /**
     * 根据查询条件返回一条，找不到则返回null，找到多余一条数据则拋错
     */
    T selectOneByExample(E example) throws org.apache.ibatis.exceptions.TooManyResultsException;

    T selectByPrimaryKey(PK pk);

    int updateByExampleSelective(@Param("record") T record, @Param("example") E example);

    int updateByExample(@Param("record") T record, @Param("example") E example);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);

    int batchInsert(@Param("list") List<T> list);
}
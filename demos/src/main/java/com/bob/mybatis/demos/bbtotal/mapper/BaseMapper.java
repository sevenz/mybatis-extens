package com.bob.mybatis.demos.bbtotal.mapper;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangxiang on 17/6/28.
 */
public interface BaseMapper<T, E, PK extends Serializable> {

    /**
     * 获取总记录数
     *
     * @param example
     * @return
     */
    long countByExample(E example);

    /**
     * 根据查询条件进行物理删除
     *
     * @param example
     * @return
     */
    int deleteByExample(E example);

    /**
     * 根据主键进行物理删除
     *
     * @param pk
     * @return
     */
    int deleteByPrimaryKey(PK pk);

    int insert(T record);

    int insertSelective(T record);

    /**
     * 批量插入, 返回影响记录数
     *
     * @param list
     * @return
     */
    int batchInsert(@Param("list") List<T> list);

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

    /**
     * 业务处理时请控制下列表大小
     */
    int updateBatchByPrimaryKeySelective(@Param("list") List<T> list);
}
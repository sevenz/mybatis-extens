package com.bob.mybatis.demos.bbtotal.repository;

import java.util.List;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

/**
 * 〈〉
 *
 * @author wangxiang
 * @create 2020-04-01
 */
public abstract class AbstractRepository<M extends Mapper<T>, T> {

  @Autowired
  private M mapper;

  protected M getMapper() {
    return this.mapper;
  }

  public int insert(T entity) {
    return this.mapper.insert(entity);
  }

  public int insertSelective(T entity) {
    return this.mapper.insertSelective(entity);
  }

  public int delete(T entity) {
    return this.mapper.delete(entity);
  }

  public int deleteByExample(Object o) {
    return this.mapper.deleteByExample(o);
  }

  public int deleteByPrimaryKey(Object o) {
    return this.mapper.deleteByPrimaryKey(o);
  }

  public int updateByPrimaryKey(T entity) {
    return this.mapper.updateByPrimaryKey(entity);
  }

  public int updateByPrimaryKeySelective(T entity) {
    return this.mapper.updateByPrimaryKeySelective(entity);
  }

  public int updateByExample(T entity, Object o) {
    return this.mapper.updateByExample(entity, o);
  }

  public int updateByExampleSelective(T entity, Object o) {
    return this.mapper.updateByExampleSelective(entity, o);
  }

  public List<T> select(T entity) {
    return this.mapper.select(entity);
  }

  public List<T> selectAll() {
    return this.mapper.selectAll();
  }

  public List<T> selectByExample(Object o) {
    return this.mapper.selectByExample(o);
  }

  public int selectCount(T entity) {
    return this.mapper.selectCount(entity);
  }

  public int selectCountByExample(Object o) {

    return this.mapper.selectCountByExample(o);
  }

  public T selectOne(T entity) {
    return this.mapper.selectOne(entity);
  }

  public T selectOneByExample(Object o) {
    return this.mapper.selectOneByExample(o);
  }

  public List<T> selectByRowBounds(T entity, RowBounds rowBounds) {
    return this.mapper.selectByRowBounds(entity, rowBounds);
  }

  public List<T> selectByExampleAndRowBounds(Object o, RowBounds rowBounds) {
    return this.mapper.selectByExampleAndRowBounds(o, rowBounds);
  }

  public T selectByPrimaryKey(Object o) {
    return this.mapper.selectByPrimaryKey(o);
  }
}

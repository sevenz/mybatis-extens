/*
 * Mybatisbb_01SqlProvider.java
 * Copyright(C) com.bob.*** Company
 * All rights reserved.
 * 2016-06-28 was created
 */
package com.bob.mybatis.demos.bbtotal.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.DELETE_FROM;
import static org.apache.ibatis.jdbc.SqlBuilder.FROM;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.ORDER_BY;
import static org.apache.ibatis.jdbc.SqlBuilder.SELECT;
import static org.apache.ibatis.jdbc.SqlBuilder.SELECT_DISTINCT;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import com.bob.mybatis.demos.bbtotal.entity.Mybatisbb_01;
import com.bob.mybatis.demos.bbtotal.entity.Mybatisbb_01Example.Criteria;
import com.bob.mybatis.demos.bbtotal.entity.Mybatisbb_01Example.Criterion;
import com.bob.mybatis.demos.bbtotal.entity.Mybatisbb_01Example;
import java.util.List;
import java.util.Map;

public class Mybatisbb_01SqlProvider {

    public String countByExample(Mybatisbb_01Example example) {
        BEGIN();
        SELECT("count(*)");
        FROM("mybatis_bb_01");
        applyWhere(example, false);
        return SQL();
    }

    public String deleteByExample(Mybatisbb_01Example example) {
        BEGIN();
        DELETE_FROM("mybatis_bb_01");
        applyWhere(example, false);
        return SQL();
    }

    public String insertSelective(Mybatisbb_01 record) {
        BEGIN();
        INSERT_INTO("mybatis_bb_01");
        
        if (record.getName() != null) {
            VALUES("name", "#{name,jdbcType=VARCHAR}");
        }
        
        if (record.getUserId() != null) {
            VALUES("userId", "#{userId,jdbcType=BIGINT}");
        }
        
        return SQL();
    }

    public String selectByExample(Mybatisbb_01Example example) {
        BEGIN();
        if (example != null && example.isDistinct()) {
            SELECT_DISTINCT("autoId");
        } else {
            SELECT("autoId");
        }
        SELECT("name");
        SELECT("userId");
        FROM("mybatis_bb_01");
        applyWhere(example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            ORDER_BY(example.getOrderByClause());
        }
        
        return SQL();
    }

    public String updateByExampleSelective(Map<String, Object> parameter) {
        Mybatisbb_01 record = (Mybatisbb_01) parameter.get("record");
        Mybatisbb_01Example example = (Mybatisbb_01Example) parameter.get("example");
        
        BEGIN();
        UPDATE("mybatis_bb_01");
        
        if (record.getAutoId() != null) {
            SET("autoId = #{record.autoId,jdbcType=BIGINT}");
        }
        
        if (record.getName() != null) {
            SET("name = #{record.name,jdbcType=VARCHAR}");
        }
        
        if (record.getUserId() != null) {
            SET("userId = #{record.userId,jdbcType=BIGINT}");
        }
        
        applyWhere(example, true);
        return SQL();
    }

    public String updateByExample(Map<String, Object> parameter) {
        BEGIN();
        UPDATE("mybatis_bb_01");
        
        SET("autoId = #{record.autoId,jdbcType=BIGINT}");
        SET("name = #{record.name,jdbcType=VARCHAR}");
        SET("userId = #{record.userId,jdbcType=BIGINT}");
        
        Mybatisbb_01Example example = (Mybatisbb_01Example) parameter.get("example");
        applyWhere(example, true);
        return SQL();
    }

    public String updateByPrimaryKeySelective(Mybatisbb_01 record) {
        BEGIN();
        UPDATE("mybatis_bb_01");
        
        if (record.getName() != null) {
            SET("name = #{name,jdbcType=VARCHAR}");
        }
        
        if (record.getUserId() != null) {
            SET("userId = #{userId,jdbcType=BIGINT}");
        }
        
        WHERE("autoId = #{autoId,jdbcType=BIGINT}");
        
        return SQL();
    }

    protected void applyWhere(Mybatisbb_01Example example, boolean includeExamplePhrase) {
        if (example == null) {
            return;
        }
        
        String parmPhrase1;
        String parmPhrase1_th;
        String parmPhrase2;
        String parmPhrase2_th;
        String parmPhrase3;
        String parmPhrase3_th;
        if (includeExamplePhrase) {
            parmPhrase1 = "%s #{example.oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{example.oredCriteria[%d].allCriteria[%d].value} and #{example.oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{example.oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{example.oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{example.oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        } else {
            parmPhrase1 = "%s #{oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{oredCriteria[%d].allCriteria[%d].value} and #{oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        }
        
        StringBuilder sb = new StringBuilder();
        List<Criteria> oredCriteria = example.getOredCriteria();
        boolean firstCriteria = true;
        for (int i = 0; i < oredCriteria.size(); i++) {
            Criteria criteria = oredCriteria.get(i);
            if (criteria.isValid()) {
                if (firstCriteria) {
                    firstCriteria = false;
                } else {
                    sb.append(" or ");
                }
                
                sb.append('(');
                List<Criterion> criterions = criteria.getAllCriteria();
                boolean firstCriterion = true;
                for (int j = 0; j < criterions.size(); j++) {
                    Criterion criterion = criterions.get(j);
                    if (firstCriterion) {
                        firstCriterion = false;
                    } else {
                        sb.append(" and ");
                    }
                    
                    if (criterion.isNoValue()) {
                        sb.append(criterion.getCondition());
                    } else if (criterion.isSingleValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase1, criterion.getCondition(), i, j));
                        } else {
                            sb.append(String.format(parmPhrase1_th, criterion.getCondition(), i, j,criterion.getTypeHandler()));
                        }
                    } else if (criterion.isBetweenValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase2, criterion.getCondition(), i, j, i, j));
                        } else {
                            sb.append(String.format(parmPhrase2_th, criterion.getCondition(), i, j, criterion.getTypeHandler(), i, j, criterion.getTypeHandler()));
                        }
                    } else if (criterion.isListValue()) {
                        sb.append(criterion.getCondition());
                        sb.append(" (");
                        List<?> listItems = (List<?>) criterion.getValue();
                        boolean comma = false;
                        for (int k = 0; k < listItems.size(); k++) {
                            if (comma) {
                                sb.append(", ");
                            } else {
                                comma = true;
                            }
                            if (criterion.getTypeHandler() == null) {
                                sb.append(String.format(parmPhrase3, i, j, k));
                            } else {
                                sb.append(String.format(parmPhrase3_th, i, j, k, criterion.getTypeHandler()));
                            }
                        }
                        sb.append(')');
                    }
                }
                sb.append(')');
            }
        }
        
        if (sb.length() > 0) {
            WHERE(sb.toString());
        }
    }
}
/*
 * Mybatisbb_01.java
 * Copyright(C) com.bob.*** Company
 * All rights reserved.
 * 2016-06-28 was created
 */
package com.bob.mybatis.demos.bbtotal.entity;

public class Mybatisbb_01 {
    /** 主键自增长 */
    private Long autoId;

    /** 姓名 */
    private String name;

    /** 用户分表标识 */
    private Long userId;

    public Long getAutoId() {
        return autoId;
    }

    public void setAutoId(Long autoId) {
        this.autoId = autoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", autoId=").append(autoId);
        sb.append(", name=").append(name);
        sb.append(", userId=").append(userId);
        sb.append("]");
        return sb.toString();
    }
}
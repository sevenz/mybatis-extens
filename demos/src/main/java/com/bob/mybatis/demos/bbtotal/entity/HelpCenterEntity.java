package com.bob.mybatis.demos.bbtotal.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class HelpCenterEntity implements Serializable {
    /**
     * 数据库自增长物理主键
     */
    private Long id;

    /**
     * 此字段允许为空
     * 此字段数据库设置默认值为空
     * 字段描述信息: 问题
     * 数据库字段定义长度为: 250
     */
    private String question;

    /**
     * 此字段允许为空
     * 此字段数据库设置默认值为空
     * 字段描述信息: 答案
     * 数据库字段定义长度为: 250
     */
    private String answer;

    /**
     * 此字段不许为空
     * 此字段数据库默认值为: CURRENT_TIMESTAMP
     * 字段描述信息: 创建时间
     */
    private Date createtime;

    /**
     * 此字段不许为空
     * 此字段数据库默认值为: CURRENT_TIMESTAMP
     * 字段描述信息: 修改时间
     */
    private Date modifytime;

    /**
     * 此字段允许为空
     * 此字段数据库默认值为: false
     */
    private Boolean isdelete = false;

    /**
     * 此字段允许为空
     * 此字段数据库没有设置默认值
     */
    private Integer otherstatus;

    /**
     * 此字段允许为空
     * 此字段数据库默认值为: 0.00
     * 数据库字段保留小数点后 2 位
     */
    private BigDecimal money;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public HelpCenterEntity withId(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public HelpCenterEntity withQuestion(String question) {
        this.setQuestion(question);
        return this;
    }

    public void setQuestion(String question) {
        this.question = question == null ? null : question.trim();
    }

    public String getAnswer() {
        return answer;
    }

    public HelpCenterEntity withAnswer(String answer) {
        this.setAnswer(answer);
        return this;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public HelpCenterEntity withCreatetime(Date createtime) {
        this.setCreatetime(createtime);
        return this;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public HelpCenterEntity withModifytime(Date modifytime) {
        this.setModifytime(modifytime);
        return this;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public Boolean getIsdelete() {
        return isdelete;
    }

    public HelpCenterEntity withIsdelete(Boolean isdelete) {
        this.setIsdelete(isdelete);
        return this;
    }

    public void setIsdelete(Boolean isdelete) {
        this.isdelete = isdelete;
    }

    public Integer getOtherstatus() {
        return otherstatus;
    }

    public HelpCenterEntity withOtherstatus(Integer otherstatus) {
        this.setOtherstatus(otherstatus);
        return this;
    }

    public void setOtherstatus(Integer otherstatus) {
        this.otherstatus = otherstatus;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public HelpCenterEntity withMoney(BigDecimal money) {
        this.setMoney(money);
        return this;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", question=").append(question);
        sb.append(", answer=").append(answer);
        sb.append(", createtime=").append(createtime);
        sb.append(", modifytime=").append(modifytime);
        sb.append(", isdelete=").append(isdelete);
        sb.append(", otherstatus=").append(otherstatus);
        sb.append(", money=").append(money);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        HelpCenterEntity other = (HelpCenterEntity) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getQuestion() == null ? other.getQuestion() == null : this.getQuestion().equals(other.getQuestion()))
            && (this.getAnswer() == null ? other.getAnswer() == null : this.getAnswer().equals(other.getAnswer()))
            && (this.getCreatetime() == null ? other.getCreatetime() == null : this.getCreatetime().equals(other.getCreatetime()))
            && (this.getModifytime() == null ? other.getModifytime() == null : this.getModifytime().equals(other.getModifytime()))
            && (this.getIsdelete() == null ? other.getIsdelete() == null : this.getIsdelete().equals(other.getIsdelete()))
            && (this.getOtherstatus() == null ? other.getOtherstatus() == null : this.getOtherstatus().equals(other.getOtherstatus()))
            && (this.getMoney() == null ? other.getMoney() == null : this.getMoney().equals(other.getMoney()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getQuestion() == null) ? 0 : getQuestion().hashCode());
        result = prime * result + ((getAnswer() == null) ? 0 : getAnswer().hashCode());
        result = prime * result + ((getCreatetime() == null) ? 0 : getCreatetime().hashCode());
        result = prime * result + ((getModifytime() == null) ? 0 : getModifytime().hashCode());
        result = prime * result + ((getIsdelete() == null) ? 0 : getIsdelete().hashCode());
        result = prime * result + ((getOtherstatus() == null) ? 0 : getOtherstatus().hashCode());
        result = prime * result + ((getMoney() == null) ? 0 : getMoney().hashCode());
        return result;
    }

    public enum Column {
        id("id"),
        question("question"),
        answer("answer"),
        createtime("createtime"),
        modifytime("modifytime"),
        isdelete("isdelete"),
        otherstatus("otherstatus"),
        money("money");

        private final String column;

        public String getValue() {
            return this.column;
        }

        Column(String column) {
            this.column = column;
        }

        public String desc() {
            return this.column + " DESC";
        }

        public String asc() {
            return this.column + " ASC";
        }
    }
}
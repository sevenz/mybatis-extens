package com.bob.mybatis.demos.bbtotal.entity;

import javax.persistence.*;

@Table(name = "`stock`")
public class Stock {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`date`")
    private String date;

    @Column(name = "`time`")
    private String time;

    @Column(name = "`ldtime`")
    private Long ldtime;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date
     */
    public void setDate(String date) {
        this.date = date == null ? null : date.trim();
    }

    /**
     * @return time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time
     */
    public void setTime(String time) {
        this.time = time == null ? null : time.trim();
    }

    /**
     * @return ldtime
     */
    public Long getLdtime() {
        return ldtime;
    }

    /**
     * @param ldtime
     */
    public void setLdtime(Long ldtime) {
        this.ldtime = ldtime;
    }
}
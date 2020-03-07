package com.howie.easyexcelmethodencapsulation.vo;

/**
 * @desc: people
 * @author: luodengxiong
 * @date: 2019/12/30 18:46:09
 **/
public class People {

    String name;
    String month;
    Integer times;
    Integer hours;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"name\":\"")
                .append(name).append('\"');
        sb.append(",\"month\":\"")
                .append(month).append('\"');
        sb.append(",\"times\":")
                .append(times);
        sb.append(",\"hours\":")
                .append(hours);
        sb.append('}');
        return sb.toString();
    }
}

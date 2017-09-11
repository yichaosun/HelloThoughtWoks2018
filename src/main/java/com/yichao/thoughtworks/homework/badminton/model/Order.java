package com.yichao.thoughtworks.homework.badminton.model;

import java.util.Date;

public class Order implements Comparable<Order> {

    private String userName;
    private Date date;
    private int startClock;
    private int endClock;
    private String fieldName;
    private double sum;
    // 订单状态 1成功 0违约
    private int state;

    public Order() {
    }

    public Order(String userName, Date date, int startClock, int endClock, String fieldName){
        this.userName = userName;
        this.date = date;
        this.startClock = startClock;
        this.endClock = endClock;
        this.fieldName = fieldName;
    }

    /**
     * 指定订单排序方式
     * fixme:同一人相同预定信息反复预定无法保留信息
     * 因为查询以订单信息为关键字，因此订单没有唯一编号
     * 以Set为容器，可能造成订单不唯一无法完整记录重复信息
     *
     * @param order
     * @return
     */
    public int compareTo(Order order) {
        int ans = this.date.compareTo(order.getDate());
        if (0 == ans) {
            ans = this.startClock - order.startClock;
            if (ans == 0) {
                ans = this.userName.compareTo(order.userName);
            }
        }
        return ans;
    }

    /**
     * 指定订单hashCode计算方式
     *
     * @return
     */
    @Override
    public int hashCode() {
        int result = userName.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + startClock;
        result = 31 * result + endClock;
        result = 31 * result + fieldName.hashCode();
        return result;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        if (startClock != order.startClock) return false;
        if (endClock != order.endClock) return false;
        if (!userName.equals(order.userName)) return false;
        if (!date.equals(order.date)) return false;
        return fieldName.equals(order.fieldName);
    }

    @Override
    public String toString() {
        return "{" + userName + "}" +
                "{" +
                date + " " +
                startClock + ":00~" +
                endClock + ":00 " +
                fieldName + " " +
                sum + " " +
                state +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStartClock() {
        return startClock;
    }

    public void setStartClock(int startClock) {
        this.startClock = startClock;
    }

    public int getEndClock() {
        return endClock;
    }

    public void setEndClock(int endClock) {
        this.endClock = endClock;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}

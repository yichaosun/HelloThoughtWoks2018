package com.yichao.thoughtworks.homework.badminton.model;

import com.yichao.thoughtworks.homework.badminton.common.Constants;

import java.text.ParseException;
import java.util.*;


public class BadmintonField {
    private String name;
    // 每天已预定时间点详情
    private Map<Date, HashSet<Integer>> orderedDayDetail;
    // 预定记录，包括预定+取消记录
    private Set<Order> orderHistories;
    // 成功订单记录
    private Set<Order> orderSucceedList;
    // 该场地总价
    private double total;
    // 定价配置参数
//    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private int[] dayMatrix;
    private int[][] dayTimeMatrix;
    private double[][] dayUnitCostMatrix;
    private double[] dayCancelCostMatrix;

    public BadmintonField() {
        initMemory();
    }

    public BadmintonField(String name, int[] dayMatrix, int[][] dayTimeMatrix, double[][] dayUnitCostMatrix, double[] dayCancelCostMatrix) {
        this.name = name;
        this.dayMatrix = dayMatrix;
        this.dayTimeMatrix = dayTimeMatrix;
        this.dayUnitCostMatrix = dayUnitCostMatrix;
        this.dayCancelCostMatrix = dayCancelCostMatrix;
        initMemory();
    }

    private void initMemory() {
        // HashMap 快速检索预定状态
        orderedDayDetail = new HashMap<Date, HashSet<Integer>>();
        // TreeSet 指定排序
        orderHistories = new TreeSet<Order>();
        // HashSet 快速检索
        orderSucceedList = new HashSet<Order>();
    }

    /**
     * 订单预定
     *
     * @param userName   用户名
     * @param parseDate  预定时间 YYYY-MM-DD
     * @param beginClock 开始时间点
     * @param endClock   结束时间点
     * @return
     */
    public int order(String userName, Date parseDate, int beginClock, int endClock) {
//        Date parseDate = simpleDateFormat.parse(date);
        int result;
        if (orderAvailable(parseDate, beginClock, endClock)) {
            //计算总价
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parseDate);
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            double sum = dayCost(week, beginClock, endClock);
            //生成预定订单
            Order order = new Order(userName, parseDate, beginClock, endClock, this.name);
            order.setSum(sum);
            order.setState(1);
            //保存成功订单
            orderHistories.add(order);
            //保存订单历史
            orderSucceedList.add(order);
            //更改可预定状态
            addDayDetails(parseDate, beginClock, endClock);
            total += sum;
            result = Constants.ResponseCode.BOOK_ACCEPTED;
        } else {
            result = Constants.ResponseCode.BOOK_CONFLICTS;
        }
        return result;
    }

    /**
     * 取消用户订单
     *
     * @param userName   用户名
     * @param parseDate  预定时间 YYYY-MM-DD
     * @param beginClock 开始时间点
     * @param endClock   结束时间点
     * @return
     * @throws ParseException 预定日期格式错误
     */
    public int cancel(String userName, Date parseDate, int beginClock, int endClock) {
        int result;
//        Date parseDate = simpleDateFormat.parse(date);
        Order order = new Order(userName, parseDate, beginClock, endClock, this.name);
        //查找订单
        boolean contains = orderSucceedList.contains(order);
        if (contains) {
            //移除已成功订单
            orderSucceedList.remove(order);
            //移除已记录订单记录
            orderHistories.remove(order);
            //计算违约金
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parseDate);
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            double cost = dayCost(week, beginClock, endClock);
            double punishment = cancelCost(1, cost);
            //更改订单状态
            order.setSum(punishment);
            order.setState(0);
            // 保存更改历史
            orderHistories.add(order);
            //释放场地预定状态
            removeDayDetails(parseDate, beginClock, endClock);
            total = total - cost + punishment;
            result = Constants.ResponseCode.BOOK_ACCEPTED;
        } else {
            result = Constants.ResponseCode.BOOK_CANCELLED_DOES_NOT_EXIT;
        }
        return result;
    }

    public Set<Order> getOrderHistories() {
        return orderHistories;
    }

    public double getTotal() {
        return total;
    }

    /**
     * 订单时间点是否可预订
     *
     * @param date
     * @param beginClock
     * @param endClock
     * @return
     */
    private boolean orderAvailable(Date date, int beginClock, int endClock) {
        HashSet<Integer> integers = orderedDayDetail.get(date);
        if (integers != null) {
            // 有当天记录
            for (int i = beginClock; i < endClock; i++)
                if (integers.contains(i))
                    return false;
        }
        return true;
    }

    /**
     * 更改场地预定状态为不可预定
     *
     * @param date
     * @param beginClock
     * @param endClock
     */
    private void addDayDetails(Date date, int beginClock, int endClock) {
        HashSet<Integer> dayDetails = orderedDayDetail.get(date);
        if (null == dayDetails)
            dayDetails = new HashSet<Integer>();
        for (int i = beginClock; i < endClock; i++) {
            dayDetails.add(i);
        }
        orderedDayDetail.put(date, dayDetails);
    }

    /**
     * @param date
     * @param beginClock
     * @param endClock
     */
    private void removeDayDetails(Date date, int beginClock, int endClock) {
        HashSet<Integer> integers = orderedDayDetail.get(date);
        if (null != integers) {
            for (int i = beginClock; i < endClock; i++) {
                integers.remove(i);
            }
        }
    }

    /**
     * 某日预定总价
     *
     * @param day
     * @param beginTime
     * @param endTime
     * @return
     */
    private double dayCost(int day, int beginTime, int endTime) {
        int strategy = findStrategy(dayMatrix, day);
        return itemsCost(dayTimeMatrix[strategy], dayUnitCostMatrix[strategy], beginTime, endTime);
    }

    /**
     * 取消订单违约金
     *
     * @param day
     * @param sum
     * @return
     */
    private double cancelCost(int day, double sum) {
        int strategy = findStrategy(dayMatrix, day);
        return (int) (sum * dayCancelCostMatrix[strategy]);
    }

    /**
     * 某日，时间段定价决策
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    private double itemsCost(int[] time, double[] cost, int beginTime, int endTime) {
        double sum = 0;
        do {
            sum += singleItemCost(time, cost, beginTime);
            beginTime += 1;
        } while (beginTime < endTime);

        return sum;
    }

    /**
     * 某日，单个时间单位定价决策
     *
     * @param time
     * @param cost
     * @param beginTime
     * @return
     */
    private double singleItemCost(int[] time, double[] cost, int beginTime) {
        return cost[findStrategy(time, beginTime)];
    }

    /**
     * @param array
     * @param value
     * @return
     */
    private int findStrategy(int[] array, int value) {
        int l = 0, r = array.length - 1;
        while (l <= r) {
            int mid = (l + r) / 2;
            if (value < array[mid]) {
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return l - 1;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getDayMatrix() {
        return dayMatrix;
    }

    public void setDayMatrix(int[] dayMatrix) {
        this.dayMatrix = dayMatrix;
    }

    public int[][] getDayTimeMatrix() {
        return dayTimeMatrix;
    }

    public void setDayTimeMatrix(int[][] dayTimeMatrix) {
        this.dayTimeMatrix = dayTimeMatrix;
    }

    public double[][] getDayUnitCostMatrix() {
        return dayUnitCostMatrix;
    }

    public void setDayUnitCostMatrix(double[][] dayUnitCostMatrix) {
        this.dayUnitCostMatrix = dayUnitCostMatrix;
    }

    public double[] getDayCancelCostMatrix() {
        return dayCancelCostMatrix;
    }

    public void setDayCancelCostMatrix(double[] dayCancelCostMatrix) {
        this.dayCancelCostMatrix = dayCancelCostMatrix;
    }
}

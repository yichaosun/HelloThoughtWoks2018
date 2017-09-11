package com.yichao.thoughtworks.homework.badminton.controller;


import com.yichao.thoughtworks.homework.badminton.common.Constants;
import com.yichao.thoughtworks.homework.badminton.model.BadmintonField;
import com.yichao.thoughtworks.homework.badminton.model.BadmintonFieldFactory;
import com.yichao.thoughtworks.homework.badminton.model.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class BadmintonController {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void handleRequest(String input) {
        if ("".equals(input)) {
            System.out.println("收入汇总\r\n---");
            Set<Map.Entry<String, BadmintonField>> allFields = BadmintonFieldFactory.getAllFields();
            int income = 0;
            for (Map.Entry<String, BadmintonField> field : allFields
                    ) {
                System.out.println("场地:" + field.getKey());
                Set<Order> orderHistories = field.getValue().getOrderHistories();
                printOrderHistories(orderHistories);
                int singleIncome = (int) field.getValue().getTotal();
                System.out.println("小计：" + singleIncome + "元\r\n");
                income += singleIncome;
            }
            System.out.println("---\r\n总计：" + income + "元");
        } else {
            int ans = Constants.ResponseCode.BOOK_INVALID;
            Order order = parseOrder(input);
            if (null != order) {
                BadmintonField badmintonField = BadmintonFieldFactory.getBadmintonField(order.getFieldName());
                if (null != badmintonField) {
                    if (1 == order.getState()) {
                        ans = badmintonField.order(order.getUserName(), order.getDate(), order.getStartClock(), order.getEndClock());
                    } else {
                        ans = badmintonField.cancel(order.getUserName(), order.getDate(), order.getStartClock(), order.getEndClock());
                    }
                }
            }
            handleResponse(ans);
        }
    }

    private void printOrderHistories(Set<Order> orderHistories) {
        for (Order order : orderHistories
                ) {
            System.out.println(
                    simpleDateFormat.format(order.getDate()) + " "
                            + (order.getStartClock() < 10 ? "0" + order.getStartClock() : order.getStartClock()) + ":00~"
                            + (order.getEndClock() < 10 ? "0" + order.getEndClock() : order.getEndClock()) + ":00 "
                            + (order.getState() == 0 ? "违约金 " : "") + (int)order.getSum() + "元"
            );
        }
    }

    private void handleResponse(int ans) {
        switch (ans) {
            case Constants.ResponseCode.BOOK_ACCEPTED:
                System.out.println("Success: the booking is accepted!");
                break;
            case Constants.ResponseCode.BOOK_CANCELLED_DOES_NOT_EXIT:
                System.out.println("Error: the booking being cancelled does not exist!");
                break;
            case Constants.ResponseCode.BOOK_INVALID:
                System.out.println("Error: the booking is invalid!");
                break;
            case Constants.ResponseCode.BOOK_CONFLICTS:
                System.out.println("Error: the booking conflicts with existing bookings!");
                break;
        }
    }

    //{用户ID} {预订⽇日期 yyyy-MM-dd} {预订时间段 HH:mm~HH:mm} {场地} {C:退订}
    private Order parseOrder(String input) {
        Order order = new Order();
        String[] params = input.split(" ");
        if (params.length != 4 && params.length != 5) {
            return null;
        }
        order.setUserName(params[0]);
        Date date = null;
        try {
            date = simpleDateFormat.parse(params[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (null == date) {
            return null;
        }
        order.setDate(date);
        String[] hours = params[2].split("~");
        String[] beginClock = hours[0].split(":");
        String[] endClock = hours[1].split(":");
        if ((0 != Integer.parseInt(beginClock[1])) || (0 != Integer.parseInt(endClock[1]))) {
            return null;
        }
        int begin = Integer.parseInt(beginClock[0]);
        int end = Integer.parseInt(endClock[0]);
        if (0 > begin || 24 < begin || 0 > end || 24 < end || begin >= end) {
            return null;
        }
        order.setStartClock(begin);
        order.setEndClock(end);
        order.setFieldName(params[3]);
        order.setState(1);
        if (5 == params.length) {
            if ("C".equals(params[4]))
                order.setState(0);
            else return null;
        }
        return order;
    }
}

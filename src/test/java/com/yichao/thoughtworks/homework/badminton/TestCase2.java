package com.yichao.thoughtworks.homework.badminton;

import com.yichao.thoughtworks.homework.badminton.controller.BadmintonController;
import org.junit.Test;

public class TestCase2 {

    @Test
    public void TestCase2() throws Exception {
        System.out.println("\r\n----------------测试用例2--------------------");
        BadmintonController badmintonController = new BadmintonController();
        String[] inputs = new String[]{
                "U002 2017-08-01 19:00~22:00 A",
                "U003 2017-08-01 18:00~20:00 A",
                "U002 2017-08-01 19:00~22:00 A C",
                "U002 2017-08-01 19:00~22:00 A C",
                "U003 2017-08-01 18:00~20:00 A",
                "U003 2017-08-02 13:00~17:00 B"};
        for (String input : inputs
                ) {
            badmintonController.handleRequest(input);
        }
        badmintonController.handleRequest("");
    }
}

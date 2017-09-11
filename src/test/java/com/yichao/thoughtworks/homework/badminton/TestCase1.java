package com.yichao.thoughtworks.homework.badminton;

import com.yichao.thoughtworks.homework.badminton.controller.BadmintonController;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestCase1 {
    @Test
    public void TestCase1() throws Exception {
        System.out.println("\r\n----------------测试用例1--------------------");
        BadmintonController badmintonController = new BadmintonController();
        String[] inputs = new String[]{
                "adfakfkajfsd9f8sf987fa98f7d98asdf79",
                "U001 2016-06-02 22:00~22:00 A",
                "U002 2017-08-01 19:00~22:00 A",
                "U003 2017-08-02 13:00~17:00 B",
                "U004 2017-08-03 15:00~16:00 C",
                "U005 2016-08-05 09:00~11:00 D"};
        for (String input : inputs
                ) {
            badmintonController.handleRequest(input);
        }
        badmintonController.handleRequest("");
    }


}
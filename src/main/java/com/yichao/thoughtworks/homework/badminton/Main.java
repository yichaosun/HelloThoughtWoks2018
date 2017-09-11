package com.yichao.thoughtworks.homework.badminton;

import com.yichao.thoughtworks.homework.badminton.controller.BadmintonController;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        BadmintonController badmintonController = new BadmintonController();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            badmintonController.handleRequest(input);
        }
    }
}

package com.yichao.thoughtworks.homework.badminton.model;

import com.yichao.thoughtworks.homework.badminton.common.Constants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;

import static org.junit.Assert.*;

public class BadmintonFieldTest {
    private SimpleDateFormat simpleDateFormat;
    private BadmintonField badmintonField;
    private int[] dayMatrix;
    private int[][] dayTimeMatrix;
    private double[][] dayUnitCostMatrix;
    private double[] dayCancelCostMatrix;

    @Before
    public void setUp() throws Exception {
        dayMatrix = new int[]{1, 6};
        dayTimeMatrix = new int[][]{{9, 12, 18, 20, 22}, {9, 12, 18, 22}};
        dayUnitCostMatrix = new double[][]{{30, 50, 80, 60}, {40, 50, 60}};
        dayCancelCostMatrix = new double[]{0.5, 0.25};
        badmintonField = new BadmintonField("A", dayMatrix, dayTimeMatrix, dayUnitCostMatrix, dayCancelCostMatrix);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Test
    public void order() throws Exception {
        Assert.assertEquals(Constants.ResponseCode.BOOK_ACCEPTED,badmintonField.order("yichao",simpleDateFormat.parse("2017-08-01"),10,12));
        Assert.assertEquals(Constants.ResponseCode.BOOK_CONFLICTS,badmintonField.order("yichao",simpleDateFormat.parse("2017-08-01"),10,12));
    }

    @Test
    public void cancel() throws Exception {
        Assert.assertEquals(Constants.ResponseCode.BOOK_ACCEPTED,badmintonField.order("yichao",simpleDateFormat.parse("2017-08-01"),10,12));
        Assert.assertEquals(Constants.ResponseCode.BOOK_ACCEPTED,badmintonField.cancel("yichao",simpleDateFormat.parse("2017-08-01"),10,12));
        Assert.assertEquals(Constants.ResponseCode.BOOK_CANCELLED_DOES_NOT_EXIT,badmintonField.cancel("yichao",simpleDateFormat.parse("2017-08-02"),10,12));

    }

}
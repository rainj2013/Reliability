package edu.gdut.test;

import Jama.Matrix;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by rainj2013 on 16-11-18.
 */
public class MatrixTest {
    @Test
    public void test() {
        double[][] array0 = {{1},{0},{-0}};
        double[][] array1 = {{0.25},{0.15},{-0.25}};
        double[][] array2 = {
                {1,  0,  0.5},
                {0,  1,  0.5},
                {0.5,0.5, 1}
        };

        Matrix m1 = new Matrix(array0);
        Matrix m2 = new Matrix(array1);
        Matrix m3 = m1.minus(m2);
        Matrix D = new Matrix(array2);
        double temp = m3.transpose().times(D).times(m3).get(0,0);
        temp = Math.sqrt(0.5*temp);
        System.out.println(temp);
    }
}

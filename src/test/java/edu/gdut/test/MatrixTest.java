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
        double[][] array0 = {{0.15},{0.05},{-0.2}};
        double[][] array1 = {
                {1,  0,  0.5},
                {0,  1,  0.5},
                {0.5,0.5, 1}
        };

        Matrix matrix0 = new Matrix(array0);
        Matrix matrix1 = new Matrix(array1);
        double temp = matrix0.transpose().times(matrix1).times(matrix0).get(0,0);
        temp = Math.sqrt(0.5*temp);
        System.out.println(temp);
    }
}

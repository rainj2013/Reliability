package edu.gdut.test;

import Jama.Matrix;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by rainj2013 on 16-11-18.
 */
public class MatrixTest {
    @Test
    public void test(){
        double[][] array={
            {1d,2d,3d},
            {4d,5d,6d},
            {7d,8d,9d}
        };

        Matrix matrix = new Matrix(array);
        System.out.println(Arrays.deepToString(matrix.plus(matrix).getArray()));
        System.out.println(Arrays.deepToString(matrix.times(matrix).getArray()));
    }
}

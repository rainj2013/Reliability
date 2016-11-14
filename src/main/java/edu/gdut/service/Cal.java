package edu.gdut.service;

import java.util.List;
import java.util.Map;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-15 上午1:38
 */
public interface Cal {
    Map<String, Double[]> cal(Map<String, List<Double[]>> trainingData, List<Integer> label,
                              Map<String, List<Double[]>> testData);
}

package edu.gdut.service.GA;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-8
 */
public interface FitnessCal {
    /**
     * @Description 获取适应度
     * @param individual 染色体
     * @return 适应度
     */
    double getFitness(Individual individual);
}

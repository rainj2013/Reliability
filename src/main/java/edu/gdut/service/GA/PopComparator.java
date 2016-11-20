package edu.gdut.service.GA;

import java.util.Comparator;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-20 下午10:48
 * @Description 一个种群比较器
 */
public class PopComparator implements Comparator<Population>{
    private boolean desc;

    public PopComparator(boolean desc) {
        this.desc = desc;
    }

    @Override
    public int compare(Population o1, Population o2) {

        if (o1.getFittest().getFitness() > o2.getFittest().getFitness()){
            if (desc)
                return -1;
            else
                return 1;
        }else if(o1.getFittest().getFitness() < o2.getFittest().getFitness()){
            if (desc)
                return 1;
            else
                return -1;
        }else{
            return 0;
        }
    }
}

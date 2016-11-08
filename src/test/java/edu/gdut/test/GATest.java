package edu.gdut.test;

import edu.gdut.service.GA.FitnessCal;
import edu.gdut.service.GA.GA;
import edu.gdut.service.GA.Individual;
import edu.gdut.service.GA.Population;
import org.junit.Test;

import java.util.*;

public class GATest {
    @Test
    public void test() {
        //设置基因个数
        Individual.setDefaultGeneLength(5);
        //设置适应度计算器
        Individual.setFitnessCal(new FitnessCal() {
            @Override
            public double getFitness(Individual individual) {
                int value = Integer.parseInt(individual.toString(), 2);// 十进制数值
                return Math.pow(value, 3) - 60 * Math.pow(value, 2) + 900 * value + 100;
            }
        });
        // 初始化一个种群
        //Population myPop = new Population(5,true);
        Population myPop = new Population(Arrays.asList(
                new byte[]{1, 0, 0, 1, 1},
                new byte[]{1, 0, 0, 1, 1},
                new byte[]{1, 0, 0, 1, 1},
                new byte[]{1, 0, 0, 1, 1},
                new byte[]{1, 0, 0, 1, 1},
                new byte[]{1, 0, 0, 1, 1}));

        // 不断迭代，进行进化操作，直到达到指定的代数，打印最优的基因和对应的最优适应度
        int generationCount = 0;
        int maxGenerationCount = 50;
        List<Population> pList = new ArrayList<>();
        while (generationCount < maxGenerationCount) {
            generationCount++;
            System.out.println("Generation: " + generationCount + " Fittest: "
                    + myPop.getFittest().getFitness());
            myPop = GA.evolvePopulation(myPop);
            pList.add(myPop);
        }
        //倒序排
        Collections.sort(pList, new Comparator<Population>() {
            @Override
            public int compare(Population o1, Population o2) {
                if (o1.getFittest().getFitness() > o2.getFittest().getFitness())
                    return -1;
                return 1;
            }
        });

        System.out.println("代数: " + generationCount);
        System.out.println("最优基因:");
        System.out.println(pList.get(0).getFittest());
        System.out.println("最优适应度:");
        System.out.println(pList.get(0).getFittest().getFitness());

    }

    @Test
    public void otherTest(){
        byte[] arr = new byte[]{1,2,3,4,5,6,7,8,9,0};
        System.out.println(Arrays.toString(Arrays.copyOfRange(arr,0+3,3+3)));
    }
}

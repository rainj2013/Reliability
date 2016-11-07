package edu.gdut.service.GA;

import java.util.List;

public class Population {
    Individual[] individuals;

    // 创建一个随机基因的种群
    public Population(int populationSize, boolean initialise) {
        individuals = new Individual[populationSize];
        // 初始化种群
        if (initialise) {
            for (int i = 0; i < size(); i++) {
                Individual newIndividual = new Individual();
                saveIndividual(i, newIndividual);
            }
        }
    }

    // 创建一个指定基因的种群
    public Population(List<byte[]> genesList) {
        individuals = new Individual[genesList.size()];
        // 初始化种群
        for (int i = 0; i < size(); i++) {
            Individual newIndividual = new Individual(genesList.get(i));
            saveIndividual(i, newIndividual);
        }
    }

    //种群中最优个体
    public Individual getFittest() {
        Individual fittest = individuals[0];
        // Loop through individuals to find fittest
        for (int i = 0; i < size(); i++) {
            if (fittest.getFitness() <= getIndividual(i).getFitness()) {
                fittest = getIndividual(i);
            }
        }
        return fittest;
    }

    public int size() {
        return individuals.length;
    }

    public Individual getIndividual(int index) {
        return individuals[index];
    }

    public void saveIndividual(int index, Individual indiv) {
        individuals[index] = indiv;
    }
}
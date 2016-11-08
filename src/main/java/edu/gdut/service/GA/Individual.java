package edu.gdut.service.GA;

public class Individual {
    //默认基因个数
    static int defaultGeneLength = 64;
    // 基因序列
    private byte[] genes = new byte[defaultGeneLength];
    // 个体的适应度
    private double fitness = 0;
    //适应度计算类
    private static FitnessCal fitnessCal;

    public static void setFitnessCal(FitnessCal fitnessCal) {
        Individual.fitnessCal = fitnessCal;
    }
    // 创建一个随机基因的个体
    public Individual(){
        for (int i = 0; i < size(); i++) {
            byte gene = (byte) Math.round(Math.random());
            genes[i] = gene;
        }
    }

    // 创建一个指定基因的个体
    public Individual(byte[] genes){
        this.genes = genes;
    }

    //手动设置基因个数
    public static void setDefaultGeneLength(int length) {
        defaultGeneLength = length;
    }

    public byte getGene(int index) {
        return genes[index];
    }

    public void setGene(int index, byte value) {
        genes[index] = value;
        fitness = 0;
    }
    public byte[] getGenes() {
        return genes;
    }

    public int size() {
        return genes.length;
    }

    //适应度
    public double getFitness() {
        if (fitness == 0) {
            fitness = fitnessCal.getFitness(this);
        }
        return fitness;
    }

    @Override
    public String toString() {
        String geneString = "";
        for (int i = 0; i < size(); i++) {
            geneString += getGene(i);
        }
        return geneString;
    }
}
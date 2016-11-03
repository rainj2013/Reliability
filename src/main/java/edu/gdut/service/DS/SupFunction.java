package edu.gdut.service.DS;


/**
 * 相似度函数
 *
 * @author 景敏
 * @version V1.0
 * @date 2015年5月7日 上午1:39:09
 */
public class SupFunction {

    private static SimDistance sim = new SimDistance();
    private Evidences es;
    //距离矩阵
    //private RealMatrix d;

    public SupFunction(Evidences es) {
        super();
        this.es = es;
    }

    public double sup(Evidence e) {
        double value = 0;
        for (Evidence ee : es) {
            if (!ee.equals(e)) {
                value += sim.distance(e, ee);
            }
        }
        return value;
    }

    public Evidences getEs() {
        return es;
    }


}

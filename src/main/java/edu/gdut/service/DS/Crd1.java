package edu.gdut.service.DS;

import java.util.HashMap;
import java.util.Map;

/**
 * 证据的可信度1-邓勇
 *
 * @author 景敏
 * @version V1.0
 */
public class Crd1 {

    private SupFunction sup;

    private Map<Evidence, Double> cache = new HashMap<Evidence, Double>();

    private double sum = 0;

    public Crd1(SupFunction sup) {
        this.sup = sup;
        for (Evidence ee : sup.getEs()) {
            double s = sup.sup(ee);
            sum += s;
            cache.put(ee, s);
        }
    }

    public Crd1(Evidences es) {
        this(new SupFunction(es));
    }

    public double crd(Evidence e) {
        if (!cache.containsKey(e)) {
            throw new IllegalArgumentException("此证据不在当前计算中!!");
        }
        return cache.get(e) / sum;
    }

}

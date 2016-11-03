package edu.gdut.service.DS;

import java.util.HashMap;
import java.util.Map;

/**
 * 证据的可信度2-李文立
 * @author 景敏
 * @version V1.0
 */
public class Crd2 {

	private SupFunction sup;

	private Map<Evidence, Double> cache = new HashMap<Evidence, Double>();

	private double supMax = 0;
	//private double sum = 0;
	
	public Crd2(Evidences es){
		this(new SupFunction(es));
	}
	
	public Crd2(SupFunction sup) {
		this.sup = sup;
		for (Evidence ee : sup.getEs()) {
			double s = sup.sup(ee);
			//sum+=s;
			cache.put(ee, s);
			if(supMax < s) supMax = s;
		}
	}
	
	public double crd(Evidence e) {
		return crdR(e);
	}
	//绝对可信度
	public double crdA(Evidence e) {
		if (!cache.containsKey(e)) {
			throw new IllegalArgumentException("此证据不在当前计算中!!");
		}
		return cache.get(e) / supMax;
	}
	private double crdSum = 0;
	//相对可信度
	public double crdR(Evidence e) {
		if (!cache.containsKey(e)) {
			throw new IllegalArgumentException("此证据不在当前计算中!!");
		}
		if(crdSum == 0){
			for(Evidence ee : cache.keySet()){
				crdSum+=crd(ee);
			}
		}
		return cache.get(e) / crdSum;
	}
}

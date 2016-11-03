package edu.gdut.service.DS;


/**
 * 证据的支持度 sim(m1,m2)
 * @author 景敏
 * @date 2015年5月7日 上午1:36:22
 * @version V1.0
 */
public class SimDistance extends JousselmeDistance{

	@Override
	public double distance(Evidence e1, Evidence e2) {
		return 1-super.distance(e1, e2);
	}
	
	@Override
	public String getName() {
		return "jousselme_sim";
	}
}

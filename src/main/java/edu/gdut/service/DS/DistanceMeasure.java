package edu.gdut.service.DS;

/**
 * 距离度量
 *
 * @author 景敏
 * @version V1.0
 * @date 2015年4月14日 下午4:34:55
 */
public interface DistanceMeasure extends Measure {

    public double distance(Evidence e1, Evidence e2);

    public double distance(Evidences evidences);

    /**
     * 是否支持多个同时度量
     *
     * @return
     */
    public boolean canMore();
}

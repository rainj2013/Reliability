package edu.gdut.service.DS;

import java.util.Arrays;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * Jousselme证据距离d(m1,m2)
 *
 * @author JIMLIANG
 */
public class JousselmeDistance implements DistanceMeasure {

    private static final Log log = Logs.get();
    //简化ds证据距离的计算
    private static final RealMatrix D = new Array2DRowRealMatrix(
            new double[][]{{1, 0, 1d / 2}, {0, 1, 1d / 2},
                    {1d / 2, 1d / 2, 1}});

    public JousselmeDistance() {

    }

    public double distance(Evidence e1, Evidence e2) {
        // m = m1-m2
        RealMatrix m = e1.toCompletedMatrix().subtract(e2.toCompletedMatrix());
        // mm = m * ( d * mT )
        RealMatrix mm = m.multiply(getD(e1, e2).multiply(m.transpose()));
        // sqrt(mm/2)
        double result = Math.sqrt(mm.getEntry(0, 0) / 2);
        return result;
    }

    protected RealMatrix getD(Evidence e1, Evidence e2) {
        if ((e1 instanceof EvidenceDS) && (e2 instanceof EvidenceDS)) {
            return D;
        }
        ElementHead head = e1.getElements().getCompletedHead();
        //对称矩阵
        RealMatrix d = new Array2DRowRealMatrix(head.size(), head.size());
        for (int i = 0; i < head.size(); i++)
            for (int j = 0; j < i + 1; j++) {
                Element ele1 = head.get(i);
                Element ele2 = head.get(j);
                double value = 0;
                int down = ele1.union(ele2).size();
                if (down != 0) {
                    value = ele1.intersect(ele2).size() * 1d / down;
                }
                d.setEntry(i, j, value);
                d.setEntry(j, i, value);
            }
        return d;
    }

    public static void main(String[] args) {
        ElementHead elements = ElementHead.factor(new String[][]{
                {"fraud"}, {"unfraud"}, {"fraud", "unfraud"}
        });
        Evidence e1 = new Evidence(elements, Arrays.asList(.72, .08, .2));
        Evidence e2 = new Evidence(elements, Arrays.asList(.57, .03, .45));
        log.info("result: " + new JousselmeDistance().distance(e1, e2)); // 0.1118
    }

    @Override
    public String getName() {
        return "jousselme_d";
    }

    @Override
    public double distance(Evidences evidences) {
        if (evidences.size() == 2)
            return distance(evidences.getEvidences().get(0), evidences.getEvidences().get(1));
        return 0;
    }

    @Override
    public boolean canMore() {
        return false;
    }
}

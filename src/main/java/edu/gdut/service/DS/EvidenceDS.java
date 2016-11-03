package edu.gdut.service.DS;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;

import org.nutz.lang.Stopwatch;
import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * 由ds证据推理出来的结果
 *
 * @author jimliang
 * @version 1.0, 2014年8月1日 下午10:11:30
 */
public class EvidenceDS extends Evidence {

    private static final Log log = Logs.get();
    private static final DecimalFormat FORMATER = new DecimalFormat("0.00");
    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final int ROUND_DIVIDE = 9;//不整除时小数保留位数
    public static ElementHead elements = ElementHead.factor(new String[][]{
            {"fraud"}, {"unfraud"}, {"fraud", "unfraud"}
    });
    private String evidence;

    private String entityId;

    private String type;

    // ---------

    private Object value;

    private BigDecimal f;
    private BigDecimal uf;
    private BigDecimal uc;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public EvidenceDS(double fraud, double unfraud) {
        this(new BigDecimal(fraud), new BigDecimal(unfraud));
    }

    public EvidenceDS(BigDecimal fraud, BigDecimal unfraud) {
        this(fraud, unfraud, BigDecimal.ONE.subtract(fraud.add(unfraud)));
    }

    private EvidenceDS(BigDecimal fraud, BigDecimal unfraud, BigDecimal unc) {
        super(elements, Arrays.asList(fraud.doubleValue(), unfraud.doubleValue(), unc.doubleValue()));
        this.f = fraud;
        this.uf = unfraud;
        this.uc = unc;
        //assertCheck();
    }

    public static EvidenceDS newBean(double value, double fraud, double unfraud) {
        //稍微超过1的调整一下
        if (fraud > 1) {
            if (fraud > 1.01)
                log.warnf("EvidenceDS 数据有误 [%s,%s]", fraud, unfraud);
            fraud = 1;
        }
        if (unfraud > 1) {
            if (unfraud > 1.01)
                log.warnf("EvidenceDS 数据有误 [%s,%s]", fraud, unfraud);
            unfraud = 1;
        }
        EvidenceDS e = new EvidenceDS(fraud, unfraud);
        e.value = value;
        return e;
    }

    public static EvidenceDS newBean(double value, BigDecimal fraud, BigDecimal unfraud) {
        EvidenceDS e = new EvidenceDS(fraud, unfraud);
        e.value = value;
        return e;
    }


    private static EvidenceDS newBean(BigDecimal fraud, BigDecimal unfraud) {
        return new EvidenceDS(fraud, unfraud);
    }

    public double getFraud() {
        return f.doubleValue();
    }

    public void setFraud(double fraud) {
        this.f = new BigDecimal(fraud);
    }

    public double getUnfraud() {
        return uf.doubleValue();
    }

    public double getUncertainty() {
        return uc.doubleValue();
    }

    public void setUncertainty(double uncertainty) {
        this.uc = new BigDecimal(uncertainty);
    }

    public EvidenceDS add(EvidenceDS bean) {
        // fraud全为零
        if (ZERO.equals(f) && ZERO.equals(bean.f)) {
            //return newBean(0, unfraud+bean.unfraud-unfraud*bean.unfraud);
            return newBean(ZERO, uf.add(bean.uf).subtract(uf.multiply(bean.uf)));
        }
        // unfraud全为零
        else if (ZERO.equals(uf) && ZERO.equals(bean.uf)) {
            return newBean(f.add(bean.f).subtract(f.multiply(bean.f)), ZERO);
            // fraud,bean.unfrand = 0
        } else if (ZERO.equals(f) && ZERO.equals(bean.uf)) {
            //k = 1 - uf*f
            BigDecimal k = BigDecimal.ONE.subtract(uf.multiply(bean.f));
            return newBean(bean.f.subtract(uf.multiply(bean.f)).divide(k, ROUND_DIVIDE, BigDecimal.ROUND_HALF_EVEN), uf
                    .subtract(uf.multiply(bean.f)).divide(k, ROUND_DIVIDE, BigDecimal.ROUND_HALF_EVEN));
        } else if (ZERO.equals(bean.f) && ZERO.equals(uf)) {
            return bean.add(this);
        } else {
            // k = 1- (fr*b.ufr+ufr*b.fr)
            BigDecimal k = BigDecimal.ONE.subtract(f.multiply(bean.uf).add(
                    uf.multiply(bean.f)));
            BigDecimal fr = f.multiply(bean.f).add(f.multiply(bean.uc)).add(bean.f.multiply(uc)).divide(k, ROUND_DIVIDE, BigDecimal.ROUND_HALF_EVEN);
            BigDecimal ufr = uf.multiply(bean.uf).add(uf.multiply(bean.uc)).add(uc.multiply(bean.uf)).divide(k, ROUND_DIVIDE, BigDecimal.ROUND_HALF_EVEN);
            return newBean(fr, ufr);
        }

    }

    @Override
    public String toString() {
        return "EvidenceDS [{fraud}=" + FORMATER.format(f) + ", {~fraud}=" + FORMATER.format(uf)
                + ", Θ{fraud,~fraud}=" + FORMATER.format(uc) + "]";
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }


    public BigDecimal getF() {
        return f;
    }

    public BigDecimal getUf() {
        return uf;
    }

    public BigDecimal getUc() {
        return uc;
    }

    public static void main(String[] args) {

        System.out.println(Stopwatch.run(new Runnable() {

            @Override
            public void run() {
                EvidenceDS e1 = new EvidenceDS(0.5, 0.3);
                EvidenceDS e2 = new EvidenceDS(0.6, 0.3);

                System.out.println(e1.add(e2));
                System.out.println(e2.add(e1));

            }
        }));
    }
}

package edu.gdut.service.DS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.gdut.util.Assert;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Lang;
import org.nutz.lang.util.Context;

/**
 * 证据模型
 *
 * @author JIMLIANG
 */
public class Evidence implements Serializable {

    String name;

    //焦元
    private Set<String> focals;
    Map<Element, Double> valMap = new HashMap<Element, Double>();

    private ElementHead headCache;
    private Evidences evidences;
    private Context params;

    public Evidence() {
        super();
    }

    public Evidence(Set<String> focals) {
        super();
        this.focals = focals;
    }


    public Evidence(String name, ElementHead elements, List<Double> values) {
        this(elements.getAllElements());
        Assert.isTrue(elements.size() == values.size());
        this.name = name;
        init(elements, values);
    }

    public Evidence(ElementHead elements, List<Double> values) {
        this(elements.getAllElements());
        if (elements.size() != values.size()) {
            throw new IllegalArgumentException("识别空间和信任函数不对称：" + elements + "," + values);
        }
        init(elements, values);
    }

    public Context params() {
        if (params == null) {
            params = Lang.context();
        }
        return params;
    }

    public void set(Element ele, double value) {
        if (value <= 0) {
            valMap.remove(ele);
        } else {
            valMap.put(ele, value);
        }
        headCache = null;
    }

    protected void init(ElementHead elements, List<Double> values) {
        int i = 0;
        for (Element e : elements) {
            set(e, values.get(i));
            i++;
        }
    }

    public List<Element> getContainElements(String key) {
        return getElements().getContainElements(key);
    }

    public double getValue(Element ele) {
        if (valMap.containsKey(ele)) {
            return valMap.get(ele);
        }
        return 0;
    }

    public RealMatrix toMatrix() {
        ElementHead h = getElements();
        double[][] m = new double[1][h.size()];
        int i = 0;
        for (Element e : h) {
            m[0][i] = getValue(e);
            i++;
        }
        return new Array2DRowRealMatrix(m);
    }

    public RealMatrix toCompletedMatrix() {
        ElementHead h = getElements().getCompletedHead();
        double[][] m = new double[1][h.size()];
        int i = 0;
        for (Element e : h) {
            m[0][i] = getValue(e);
            i++;
        }
        return new Array2DRowRealMatrix(m);
    }

    public ElementHead getElements() {
        if (headCache == null) {
            headCache = new ElementHead(new ArrayList<Element>(valMap.keySet()), focals);
        }
        return headCache;
    }


    @Override
    public String toString() {
        return "Evidence " + Json.toJson(valMap, JsonFormat.compact());
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Evidences getEvidences() {
        if (evidences == null) {
            evidences = new Evidences(Arrays.asList(this));
        }
        return evidences;
    }


    public void setEvidences(Evidences evidences) {
        this.evidences = evidences;
    }

    public double sup() {
        return getEvidences().sup(this);
    }

    public double crd1() {
        return getEvidences().crd1(this);
    }

    public double crd2() {
        return getEvidences().crd2(this);
    }


    public Set<String> getFocals() {
        return focals;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((valMap == null) ? 0 : valMap.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Evidence other = (Evidence) obj;
        if (valMap == null) {
            if (other.valMap != null)
                return false;
        } else if (!valMap.equals(other.valMap))
            return false;
        return true;
    }

}

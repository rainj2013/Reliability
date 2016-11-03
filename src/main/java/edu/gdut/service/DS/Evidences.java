package edu.gdut.service.DS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import edu.gdut.util.Arrange;
import org.nutz.lang.Lang;
import org.nutz.lang.util.Context;

public class Evidences implements Iterable<Evidence> {

    private List<Evidence> evidences = new ArrayList<Evidence>();
    private Context params;

    public Evidences() {
        super();
    }

    public Evidences(List<Evidence> evidences) {
        super();
        for (Evidence e : evidences) {
            this.evidences.add(e);
            e.setEvidences(this);
        }
    }

    public Context getParams() {
        if (params == null)
            params = Lang.context();
        return params;
    }

    @Override
    public Iterator<Evidence> iterator() {
        return evidences.iterator();
    }

    private ElementHead head = null;

    public ElementHead getHead() {
        if (head == null && !evidences.isEmpty()) {
            List<Element> elements = new ArrayList<Element>();
            for (Evidence e : evidences) {
                for (Element ele : e.getElements()) {
                    if (!elements.contains(ele)) {
                        elements.add(ele);
                    }
                }
            }
            Collections.sort(elements);
            head = new ElementHead(elements);
        }
        return head;
    }

    public int size() {
        return evidences.size();
    }

    public List<Evidence> getEvidences() {
        return evidences;
    }

    private SupFunction supFunction;
    private Crd1 crd1;
    private Crd2 crd2;

    private SupFunction getSupFunction() {
        if (supFunction == null) {
            supFunction = new SupFunction(this);
        }
        return supFunction;
    }

    public double sup(Evidence e) {
        return getSupFunction().sup(e);
    }

    public double crd1(Evidence e) {
        if (crd1 == null) {
            crd1 = new Crd1(getSupFunction());
        }
        return crd1.crd(e);
    }

    public double crd2(Evidence e) {
        if (crd2 == null) {
            crd2 = new Crd2(getSupFunction());
        }
        return crd2.crd(e);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        Iterator<Evidence> it = evidences.iterator();
        while (it.hasNext()) {
            sb.append(it.next().getName());
            if (it.hasNext())
                sb.append(",");
        }
        return sb.toString();
    }

    Arrange<Element> _arrange = null;// new Arrange<Element>(evSize, evidences

    // .getHead().getElements(), true);

    /**
     *
     */
    public double intersectEqual(Element ele) {
        int size = getEvidences().size();
        if (_arrange == null) {
            _arrange = new Arrange<Element>(size, getHead().getElements(), true);
        }
        double sum = 0;
        for (List<Element> e : _arrange.getResult()) {
            if (EvidenceUtil.intersect(e).equals(ele)) {
                double m = 1;
                for (int i = 0; i < size; i++) {
                    m *= evidences.get(i).getValue(e.get(i));
                }
                sum += m;
            }
        }
        return sum;
    }
}

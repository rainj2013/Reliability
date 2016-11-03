package edu.gdut.service.DS;

import java.util.Collection;
import java.util.Set;

public class EvidenceUtil {

    public static Element intersect(Collection<Element> es) {
        Element e = null;
        for (Element ee : es) {
            if (e == null)
                e = ee;
            else
                e = e.intersect(ee);
        }
        return e;
    }

    public static boolean equal(Evidence e1, Evidence e2) {
        Set<Element> set = e1.valMap.keySet();
        Set<Element> set1 = e2.valMap.keySet();
        for (Element e : set) {
            if (Math.abs(e1.getValue(e) - e2.getValue(e)) > .001) {
                return false;
            }
            set1.remove(e);
        }
        if (!set1.isEmpty()) {
            for (Element e : set1) {
                if (Math.abs(e1.getValue(e) - e2.getValue(e)) > .001) {
                    return false;
                }
            }
        }
        return true;
    }

}

package edu.gdut.service.DS;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import edu.gdut.util.Combination;
import org.nutz.lang.Strings;

public class CompositeUtil {

    public static ElementHead compositeHead(ElementHead head) {
        List<Element> newHead = new ArrayList<Element>();
        newHead.add(Element.EMPTY);
        Set<String> set = head.getAllElements();
        for (int i = 1; i <= set.size(); i++) {
            Combination<String> c = new Combination<String>(i, set);
            for (List<String> s : c.getResult()) {
                newHead.add(new Element(new TreeSet<String>(s)));
            }
        }
        return new ElementHead(newHead);
    }

    public static boolean equal(Element set, String value) {
        return set.size() == 1 && set.contains(value);
    }

    public static double intersectEqual(Evidence e1, Evidence e2, Element ele) {
        double value = 0;
        for (Element ele1 : e1.getElements())
            for (Element ele2 : e2.getElements()) {
                if (ele1.intersect(ele2).equals(ele)) {
                    value += e1.getValue(ele1) * e2.getValue(ele2);
                }
            }
        return value;
    }

    public static void print(Evidence... evidences) {
        //  x | e1 | e2
        // m1 |  0 | .2
        // m2 |  0 | .2

        //print head
        ElementHead head = evidences[0].getElements();
        List<String> list = new ArrayList<String>();
        list.add("xx");
        for (Element ele : head)
            list.add(ele.toString());
        System.out.println(Strings.join(" | ", list.toArray()));
        int index = 0;
        for (Evidence e : evidences) {
            List<String> list2 = new ArrayList<String>();
            list2.add("m" + (++index));
            for (Element ele : head)
                list2.add(e.getValue(ele) + "");
            System.out.println(Strings.join(" | ", list2.toArray()));
            for (String key : e.params().keys()) {
                System.out.println("params:  " + key + "," + e.params().getDouble(key));
            }
        }
    }
}

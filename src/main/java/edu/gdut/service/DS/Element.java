package edu.gdut.service.DS;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import edu.gdut.util.Assert;
import org.nutz.lang.ContinueLoop;
import org.nutz.lang.Each;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.Lang;
import org.nutz.lang.LoopException;


/**
 * 证据的认识框架
 *
 * @author JIMLIANG
 */
public class Element implements Iterable<String>, Comparable<Element> {

    public static final Element EMPTY = new Element();

    Set<String> element;// 焦元

    public Element() {
        super();
        element = new TreeSet<String>();
    }

    public Element(Set<String> element) {
        super();
        this.element = element;
    }

    public int size() {
        return element.size();
    }

    public boolean contains(String key) {
        return element.contains(key);
    }

    // 交集 ∩
    public Element intersect(Element ele) {
        Set<String> s = new TreeSet<String>();
        for (String t : ele.element)
            if (element.contains(t))
                s.add(t);
        return new Element(s);
    }

    // 并集 ∪
    public Element union(Element ele) {
        Set<String> s = new TreeSet<String>(element);
        s.addAll(ele.element);
        return new Element(s);
    }

    @Override
    public String toString() {
        if (element.isEmpty()) return "φ";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        Iterator<String> it = element.iterator();
        if (it.hasNext())
            sb.append(it.next());
        while (it.hasNext())
            sb.append("," + it.next());
        return sb.append("}").toString();
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((element == null) ? 0 : element.hashCode());
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
        Element other = (Element) obj;
        if (element == null) {
            if (other.element != null)
                return false;
        } else if (other.size() != element.size())
            return false;
        else if (intersect(other).size() != element.size())
            return false;
        return true;
    }

    public boolean isEmpty() {
        return element.isEmpty();
    }

    @Override
    public Iterator<String> iterator() {
        return element.iterator();
    }

    public Element subElement(final int x, final int j) {
        Assert.isTrue(x >= 0);
        Assert.isTrue(j <= size(), "Error,subElement:" + size() + ": " + x + "-" + j);
        final Set<String> e = new LinkedHashSet<String>();
        Lang.each(element, new Each<String>() {

            @Override
            public void invoke(int index, String ele, int length)
                    throws ExitLoop, ContinueLoop, LoopException {
                if (x <= index && j > index)
                    e.add(ele);
            }
        });
        return new Element(e);
    }

    public static Element factor(String... strings) {
        if (strings.length == 0) return Element.EMPTY;
        Set<String> set = new LinkedHashSet<String>();
        set.addAll(Arrays.asList(strings));
        return new Element(set);
    }

    @Override
    public int compareTo(Element o) {
        if (isEmpty()) return -1;
        else if (o.isEmpty()) return 1;
        if (element.size() == o.element.size()) {
            return element.hashCode() - o.element.hashCode();
        }
        return element.size() - o.element.size();
    }

}

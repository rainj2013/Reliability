package edu.gdut.service.DS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author JIMLIANG
 */
public class ElementHead implements Iterable<Element>, Cloneable {

    List<Element> elements;//认识框架
    Set<String> focals;


    public ElementHead(List<Element> elements, Set<String> focals) {
        super();
        this.elements = elements;
        this.focals = focals;
    }


    public ElementHead(List<Element> elements) {
        super();
        this.elements = elements;
    }


    public int size() {
        return elements.size();
    }


    public Element get(int index) {
        return elements.get(index);
    }

    public Set<String> getAllElements() {
        if (focals == null) {
            focals = new TreeSet<String>();
            for (Element e : elements) {
                focals.addAll(e.element);
            }
        }
        return focals;
    }

    //全集
    public Element getFull() {
        return new Element(getAllElements());
    }

    public List<Element> getContainElements(String key) {
        List<Element> list = new ArrayList<Element>();
        for (Element e : elements)
            if (e.contains(key))
                list.add(e);
        return list;
    }

    @Override
    public Iterator<Element> iterator() {
        return elements.iterator();
    }

    public static ElementHead factor(String[][] array) {
        List<Element> list = new ArrayList<Element>();
        for (String[] a : array) {
            if (a.length == 0)
                list.add(Element.EMPTY);
            else
                list.add(new Element(new HashSet<String>(Arrays.asList(a))));

        }
        return new ElementHead(list);
    }

    @Override
    public ElementHead clone() {
        return new ElementHead(new ArrayList<Element>(elements));
    }

    @Override
    public String toString() {
        return elements.toString();
    }

    public List<Element> getElements() {
        return new ArrayList<Element>(elements);
    }

    ElementHead c = null;

    public ElementHead getCompletedHead() {
        if (c == null) {
            c = CompositeUtil.compositeHead(this);
        }
        return c;
    }
}

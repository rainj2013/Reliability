package edu.gdut.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 组合
 *
 * @param <T>
 * @author 景敏
 * @version V1.0
 * @date 2015年5月3日 上午2:44:21
 */
public class Combination<T> {

    int num;
    Collection<T> datas;
    List<List<T>> result;

    public Combination(int num, Collection<T> datas) {
        super();
        this.num = num;
        this.datas = datas;
    }

    public List<List<T>> getResult() {
        if (result == null) {
            result = new ArrayList<List<T>>();
            arrange(num, new ArrayList<T>(datas), new ArrayList<T>());
        }
        return result;
    }

    private void arrange(int num, List<T> t, List<T> set) {
        if (num == 0)
            result.add(set);
        else {
            List<T> dest = new ArrayList<T>(t);
            Iterator<T> it = dest.iterator();
            while (it.hasNext()) {
                List<T> newSet = new ArrayList<T>(set);
                newSet.add(it.next());
                it.remove();
                arrange(num - 1, dest, newSet);
            }
        }
    }
}

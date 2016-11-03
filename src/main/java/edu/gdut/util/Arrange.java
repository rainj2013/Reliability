package edu.gdut.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 排列
 *
 * @param <T>
 * @author 景敏
 * @version V1.0
 * @date 2015年5月3日 上午2:45:07
 */
public class Arrange<T> {

    int num;
    List<T> datas;
    List<List<T>> result;
    private boolean repeat = false;

    public Arrange(int num, List<T> datas) {
        super();
        this.num = num;
        this.datas = datas;
    }

    public Arrange(int num, List<T> datas, boolean repeat) {
        super();
        this.num = num;
        this.datas = datas;
        setRepeat(repeat);
    }

    public List<List<T>> getResult() {
        if (result == null) {
            result = new ArrayList<List<T>>();
            arrange(num, datas, new ArrayList<T>());
        }
        return result;
    }

    private void arrange(int num, List<T> t, List<T> set) {
        if (num == 0)
            result.add(set);
        else {
            for (int i = 0; i < t.size(); i++) {
                if (repeat) {
                    List<T> newSet = new ArrayList<T>(set);
                    newSet.add(t.get(i));
                    arrange(num - 1, t, newSet);
                } else {
                    List<T> dest = new ArrayList<T>(t);
                    List<T> newSet = new ArrayList<T>(set);
                    newSet.add(dest.remove(i));
                    arrange(num - 1, dest, newSet);
                }
            }
        }
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        if (this.repeat != repeat) {
            result = null;
        }
        this.repeat = repeat;
    }

}

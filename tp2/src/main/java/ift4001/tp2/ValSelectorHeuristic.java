package ift4001.tp2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.chocosolver.solver.search.strategy.selectors.IntValueSelector;
import org.chocosolver.solver.variables.IntVar;

public final class ValSelectorHeuristic implements IntValueSelector {

    private Map<Integer, Double> map = new HashMap<>();
    public List<Integer> bestLettersIndexes = new ArrayList<Integer>();

    public ValSelectorHeuristic(double[] frequencies) {

        for (int i = 0; i < 26; i++) {
            map.put(i, frequencies[i]);
        }
        Map<Integer, Double> sortedMap = sortByValue(map);

        for (Integer key : sortedMap.keySet()) {
            bestLettersIndexes.add(key);
        }
    }

    @Override
    public int selectValue(IntVar var) {
        for (int index : bestLettersIndexes) {
            if (var.contains(index)) {
                return index;
            }
        }
        return -1;
    }

    private Map<Integer, Double> sortByValue(Map<Integer, Double> map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        Map result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

}

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

import org.chocosolver.solver.search.strategy.selectors.VariableSelector;
import org.chocosolver.solver.variables.IntVar;

public class VarSelectorHeuristic implements VariableSelector<IntVar> {

    private Map<String, Integer> map = new HashMap<>();
    public List<Integer> bestIndexes = new ArrayList<Integer>();

    public VarSelectorHeuristic(String text, int keyLength) {
        char[] textArray = text.toCharArray();
        List<String> texts = new ArrayList<String>();

        for (int startIndex = 0; startIndex < keyLength; startIndex++) {
            texts.add("");
            for (int i = startIndex; i < textArray.length; i += keyLength) {
                Character c = textArray[i];
                String key = c + String.valueOf(startIndex);
                Integer count = map.get(key);
                map.put(key, count != null ? count + 1 : 1);
                texts.set(startIndex, texts.get(startIndex) + c);
            }
        }

        Map<String, Integer> sortedMap = sortByValue(map);

        for (String key : sortedMap.keySet()) {
            int startIndex = Integer.parseInt(key.substring(1));
            char character = key.charAt(0);
            bestIndexes.add(texts.get(startIndex).indexOf(character) * keyLength + startIndex);
        }
    }

    @Override
    public IntVar getVariable(IntVar[] vars) {
        for (Integer index : bestIndexes) {
            if (!vars[index].isInstantiated()) {
                return vars[index];
            }
        }

        for (int i = 0; i < vars.length; i++) {
            if (!vars[i].isInstantiated()) {
                return vars[i];
            }
        }
        return null;
    }

    private Map<String, Integer> sortByValue(Map<String, Integer> map) {
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
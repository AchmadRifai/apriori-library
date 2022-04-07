/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package achmad.rifai.apriori.library.util;

import achmad.rifai.apriori.library.model.ItemJual;
import achmad.rifai.apriori.library.model.Jual;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author acmadrifai
 */
public class DataUtil {
    public static Map<Jual, List<ItemJual>> grouping(List<Jual> juals, List<ItemJual> itemJuals) {
        Map<String, List<ItemJual>> m1 = itemJuals.parallelStream()
                .collect(Collectors.groupingBy(ItemJual::getNota, Collectors.toList()));
        Map<Jual, List<ItemJual>> map = new HashMap<>();
        juals.stream().filter(j->m1.containsKey(j.getNota())).forEach(j->map.put(j, m1.get(j.getNota())));
        return map;
    }

    public static Map<String, Long> countProductSold(List<ItemJual> itemJuals) {
        Map<String, Long> map = new HashMap<>();
        itemJuals.parallelStream()
                .filter(Objects::nonNull)
                .map(ItemJual::getBarang)
                .filter(StringUtils::nonBlank)
                .distinct()
                .forEach(s->map.put(s, itemJuals.stream()
                        .filter(Objects::nonNull)
                        .filter(i->s.equals(i.getBarang()))
                        .count()));
        return map;
    }

    public static Map<String, Long> countProductTrans(List<ItemJual> itemJuals) {
        Map<String, Long> map = new HashMap<>();
        long all = itemJuals.parallelStream()
                .filter(Objects::nonNull)
                .map(ItemJual::getNota)
                .filter(StringUtils::nonBlank)
                .distinct()
                .count();
        itemJuals.parallelStream()
                .filter(Objects::nonNull)
                .map(ItemJual::getBarang)
                .filter(StringUtils::nonBlank)
                .distinct()
                .forEach(s->map.put(s, all));
        return map;
    }

    public static Map<String, BigDecimal> support(Map<String, Long> sold, Map<String, Long> trans) {
        Map<String, BigDecimal> map = new HashMap<>();
        sold.keySet()
                .parallelStream()
                .filter(StringUtils::nonBlank)
                .filter(trans::containsKey)
                .forEach(s->map.put(s, BigDecimal.valueOf(sold.get(s)).divide(BigDecimal.valueOf(trans.get(s)), MathContext.DECIMAL128)));
        return map;
    }

    public static Map<String, BigDecimal> treshold(Map<String, BigDecimal> support) {
        Map<String, BigDecimal> map = new HashMap<>();
        support.keySet()
                .parallelStream()
                .filter(StringUtils::nonBlank)
                .filter(support::containsKey)
                .filter(s->-1 == BigDecimal.valueOf(0.1).compareTo(support.get(s)))
                .forEach(s->map.put(s, support.get(s)));
        return map;
    }

}

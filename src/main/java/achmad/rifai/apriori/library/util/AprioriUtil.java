/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package achmad.rifai.apriori.library.util;

import achmad.rifai.apriori.library.model.ItemJual;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 *
 * @author acmadrifai
 */
@AllArgsConstructor
@Builder
public class AprioriUtil {

    private final List<ItemJual> itemJuals;
    private List<String> barangs;
    private long all;

    public List<AprioryBigDecimalSupport> apriory() {
        createBarang();
        return IntStream.range(0, barangs.size())
                .boxed()
                .parallel()
                .map(this::treshold)
                .filter(m->!m.isEmpty())
                .sequential()
                .reduce(new ArrayList<>(), (l1, l2)->{
                    l1.addAll(l2);
                    return l1;
                });
    }

    private void createBarang() {
        barangs = itemJuals.parallelStream()
                .filter(Objects::nonNull)
                .map(ItemJual::getBarang)
                .filter(StringUtils::nonBlank)
                .distinct()
                .collect(Collectors.toList());
        all = itemJuals.parallelStream()
                .filter(Objects::nonNull)
                .map(ItemJual::getNota)
                .filter(StringUtils::nonBlank)
                .distinct()
                .count();
    }

    private List<AprioryBigDecimalSupport> treshold(int i) {
        final List<String> alle = combining(i);
        return alle.parallelStream()
                .filter(StringUtils::nonBlank)
                .map(s->AprioryLongSupport.builder()
                        .name(s)
                        .value(counting(s))
                        .build())
                .map(a->AprioryBigDecimalSupport.builder()
                        .name(a.getName())
                        .value(BigDecimal.valueOf(a.getValue()).divide(BigDecimal.valueOf(all), MathContext.DECIMAL128))
                        .build())
                .filter(a->-1 == BigDecimal.valueOf(0.1).compareTo(a.getValue()))
                .collect(Collectors.toList());
    }

    private List<String> combining(int i) {
        long size = barangs.size();
        for (int j = 1; j < i; j++) size *= barangs.size();
        return LongStream.range(0L, size)
                .boxed()
                .parallel()
                .map(l->{
                    final int index = (int) (l % barangs.size());
                    return IntStream.range(0, i)
                            .boxed()
                            .parallel()
                            .map(j->barangs.get((index + j) % barangs.size()))
                            .distinct()
                            .sorted()
                            .collect(Collectors.joining("&"));
                })
                .filter(StringUtils::nonBlank)
                .distinct()
                .collect(Collectors.toList());
    }

    private long counting(String s) {
        Map<String, List<ItemJual>> map = itemJuals.parallelStream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(ItemJual::getNota, Collectors.toList()));
        return map.keySet()
                .parallelStream()
                .filter(StringUtils::nonBlank)
                .filter(ss->{
                    Map<String, List<ItemJual>> map2 = map.get(ss)
                            .parallelStream()
                            .filter(Objects::nonNull)
                            .collect(Collectors.groupingBy(ItemJual::getBarang, Collectors.toList()));
                    return Stream.of(s.split("&")).allMatch(sss->map2.containsKey(sss));
                })
                .count();
    }

    private static final Logger log = Logger.getLogger(AprioriUtil.class.getName());

}

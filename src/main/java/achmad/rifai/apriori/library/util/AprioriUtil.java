/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package achmad.rifai.apriori.library.util;

import achmad.rifai.apriori.library.model.ItemJual;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
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
        log.info("Starting apriory");
    	createBarang();
        List<AprioryBigDecimalSupport> l = new CopyOnWriteArrayList<>();
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(20);
        for (int i = 1; i <= barangs.size(); i++) {
        	final List<AprioryBigDecimalSupport> l2 = treshold(i);
        	if (l2.isEmpty()) 
        		break;
        	exec.execute(()->l.addAll(l2));
        	ThreadUtils.onMax(exec);
        }
        ThreadUtils.blocking(exec);
        log.info("Apriory done");
        return l;
    }

    private void createBarang() {
        Stream<ItemJual> si = itemJuals.parallelStream();
        Stream<ItemJual> si2 = itemJuals.parallelStream();
    	try {
    		barangs = si.filter(Objects::nonNull)
                    .map(ItemJual::getBarang)
                    .filter(StringUtils::nonBlank)
                    .distinct()
                    .collect(Collectors.toList());
            all = si2.filter(Objects::nonNull)
                    .map(ItemJual::getNota)
                    .filter(StringUtils::nonBlank)
                    .distinct()
                    .count();
    	} finally {
			si.close();
			si2.close();
		}
    }

    private List<AprioryBigDecimalSupport> treshold(int i) {
    	long size = barangs.size();
        for (int j = 1; j < i; j++) size *= barangs.size();
        LongStream ls = LongStream.range(0L, size);
    	try {
    		return ls.boxed()
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
                    }).filter(StringUtils::nonBlank)
                    .distinct()
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
    	} finally {
			ls.close();
		}
    }

    private long counting(String s) {
        Stream<ItemJual> si = itemJuals.parallelStream();
        Stream<String> ss = null;
    	try {
    		final Map<String, List<ItemJual>> map = si.filter(Objects::nonNull)
                    .collect(Collectors.groupingBy(ItemJual::getNota, Collectors.toList()));
        	ss = map.keySet().parallelStream();
            return ss.filter(StringUtils::nonBlank)
                    .filter(ss1->{
                    	Stream<ItemJual> si2 = map.get(ss1).parallelStream();
                    	Stream<String> ss2 = null;
                        try {
                        	final Map<String, List<ItemJual>> map2 = si2.filter(Objects::nonNull)
                                    .collect(Collectors.groupingBy(ItemJual::getBarang, Collectors.toList()));
                            ss2 = Stream.of(s.split("&"));
                            return ss2.allMatch(sss->map2.containsKey(sss));
                        } finally {
							si2.close();
							if (ss2 != null) ss2.close();
						}
                    }).count();
    	} finally {
			si.close();
			if (ss != null) ss.close();
		}
    }

    private static final Logger log = Logger.getLogger(AprioriUtil.class.getName());

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package achmad.rifai.apriori.library.constants;

import achmad.rifai.apriori.library.model.Barang;
import achmad.rifai.apriori.library.model.ItemJual;
import achmad.rifai.apriori.library.model.Jual;
import achmad.rifai.apriori.library.model.Pembeli;
import achmad.rifai.apriori.library.util.RandomUtil;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 *
 * @author acmadrifai
 */
public class DataConstant {
    public static final List<Pembeli> PEMBELIS = Arrays.asList(
            Pembeli.builder()
                    .nomor(1)
                    .kode("rifai")
                    .nama("Achmad Rifa'i")
                    .build(),
            Pembeli.builder()
                    .nomor(2)
                    .nama("Tri")
                    .kode("tri")
                    .build(),
            Pembeli.builder()
                    .nomor(3)
                    .kode("jefri")
                    .nama("Jefri")
                    .build(),
            Pembeli.builder()
                    .nomor(4)
                    .kode("iqbal")
                    .nama("Iqbal Yusuf")
                    .build(),
            Pembeli.builder()
                    .nomor(5)
                    .kode("akbar")
                    .nama("Akbar")
                    .build()
    );

    public static List<Jual> genJuals(List<Pembeli> pembelis) {
        IntStream is = IntStream.range(0, 36000);
    	try {
    		return is.boxed()
                    .parallel()
                    .map(i->{
                        int index = RandomUtil.randomInt(0, pembelis.size() - 1);
                        Timestamp now = Timestamp.valueOf(LocalDateTime.now()), prev = Timestamp.valueOf(LocalDateTime.now().minusYears(1));
                        long waktuUtc = RandomUtil.randomLong(prev.getTime(), now.getTime());
                        Pembeli p = pembelis.get(index);
                        Timestamp waktu = new Timestamp(waktuUtc);
                        return Jual.builder()
                                .nota(String.format("%s-%s", p.getKode(), waktuUtc))
                                .waktu(waktu)
                                .pembeli(p.getKode())
                                .build();
                    }).collect(Collectors.toList());
    	} finally {
			is.close();
		}
    }

    public static List<ItemJual> genItemsJual(List<Jual> juals, List<Barang> barangs) {
        final long jual = juals.size();
        LongStream ls = LongStream.range(0, jual * barangs.size());
        Stream<String> ss = null;
        try {
        	final Map<String, List<ItemJual>> map = ls.boxed()
                    .parallel()
                    .map(i->ItemJual.builder()
                            .qty(RandomUtil.randomInt(5, 30))
                            .barang(barangs.get(RandomUtil.randomInt(0, barangs.size() - 1)).getKode())
                            .nota(juals.get(RandomUtil.randomInt(0, juals.size() - 1)).getNota())
                            .build())
                    .collect(Collectors.groupingBy(ItemJual::getNota, Collectors.toList()));
            ss = map.keySet().parallelStream();
            return ss.map(map::get)
                    .map(list->list.stream().collect(Collectors.groupingBy(ItemJual::getBarang, Collectors.toList())))
                    .map(m->m.keySet().parallelStream().map(m::get).filter(list->!list.isEmpty()).map(list2->list2.get(0)).collect(Collectors.toList()))
                    .sequential()
                    .reduce(new ArrayList<>(), (l1, l2)->{
                        l2.forEach(l1::add);
                        return l1;
                    });
        } finally {
        	ls.close();
			if (ss != null) ss.close();
		}
    }
}

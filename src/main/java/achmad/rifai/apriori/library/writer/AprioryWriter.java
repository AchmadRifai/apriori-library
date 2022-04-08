/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package achmad.rifai.apriori.library.writer;

import achmad.rifai.apriori.library.model.Barang;
import achmad.rifai.apriori.library.util.AprioryBigDecimalSupport;
import achmad.rifai.apriori.library.util.StringUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 *
 * @author acmadrifai
 */
@AllArgsConstructor
@Builder
public class AprioryWriter {
    private final List<AprioryBigDecimalSupport> apriory;
    private final List<Barang> barangs;

    public void write(String path) {
    	log.log(Level.INFO, "writing in {0}", path);
        File f = new File(path);
        if (!f.getParentFile().exists()) f.getParentFile().mkdirs();
        if (f.exists()) f.delete();
        Stream<AprioryBigDecimalSupport> sa = apriory.stream()
        		.onClose(()->{
        			log.log(Level.INFO, "wrote in {0}", path);
        			System.gc();
        			System.exit(0);
        		});
        try (PrintWriter p = new PrintWriter(f)) {
            p.println("kombinasi,treshold");
            sa.filter(Objects::nonNull)
                    .map(a->new String[]{a.getName(), a.getValue().toString()})
                    .map(this::toName)
                    .map(StringUtils::combineToString)
                    .forEach(p::println);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AprioryWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
			sa.close();
		}
    }

    private String[] toName(String[] sa) {
        String[] s2 = sa;
        Stream<String> ss = Stream.of(s2[0].split("&"));
        try {
        	s2[0] = ss.map(s->{
                    	Stream<Barang> sb = barangs.stream();
                    	try {
                    		return sb.filter(Objects::nonNull).filter(b->Objects.deepEquals(b.getKode(), s)).findFirst();
                    	} finally {
							sb.close();
						}
                    })
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(Barang::getNama)
                    .collect(Collectors.joining(" dan "));
            return s2;
        } finally {
			ss.close();
		}
    }

    private static final Logger log = Logger.getLogger(AprioryWriter.class.getName());

}

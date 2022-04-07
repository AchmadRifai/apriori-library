/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package achmad.rifai.apriori.library.writer;

import achmad.rifai.apriori.library.model.Barang;
import achmad.rifai.apriori.library.util.AprioryBigDecimalSupport;
import achmad.rifai.apriori.library.util.DirsUtil;
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
        DirsUtil.mkdirs(path);
        File f = new File(path);
        if (f.exists()) f.delete();
        try (PrintWriter p = new PrintWriter(f)) {
            p.println("kombinasi,treshold");
            apriory.parallelStream()
                    .filter(Objects::nonNull)
                    .map(a->new String[]{a.getName(), a.getValue().toString()})
                    .map(this::toName)
                    .map(StringUtils::combineToString)
                    .forEach(p::println);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AprioryWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String[] toName(String[] sa) {
        String[] s2 = sa;
        s2[0] = Stream.of(s2[0].split("&"))
                .parallel()
                .map(s->barangs.parallelStream().filter(b->Objects.deepEquals(b.getKode(), s)).findFirst())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Barang::getNama)
                .collect(Collectors.joining(" dan "));
        return s2;
    }

}

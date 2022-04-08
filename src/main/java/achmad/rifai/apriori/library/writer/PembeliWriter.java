/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package achmad.rifai.apriori.library.writer;

import achmad.rifai.apriori.library.model.Pembeli;
import achmad.rifai.apriori.library.util.StringUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 *
 * @author acmadrifai
 */
@AllArgsConstructor
@Builder
public class PembeliWriter {

    private final List<Pembeli> pembelis;

    public void write(String path) {
        log.log(Level.INFO, "writing on {0}", path);
    	File f = new File(path);
        if (!f.getParentFile().exists()) f.getParentFile().mkdirs();
        if (f.exists()) f.delete();
        Stream<Pembeli> sp = pembelis.parallelStream()
        		.onClose(()->log.log(Level.INFO, "wrote on {0}", path));
        try (PrintWriter p = new PrintWriter(f)) {
            p.println("nomor,kode,nama");
            sp.filter(Objects::nonNull)
                    .map(this::convertToArrayString)
                    .map(StringUtils::combineToString)
                    .forEach(p::println);
        } catch (FileNotFoundException ex) {
            log.log(Level.SEVERE, null, ex);
        } finally {
			sp.close();
		}
    }

    private static final Logger log = Logger.getLogger(PembeliWriter.class.getName());

    private String[] convertToArrayString(Pembeli p) {
        return new String[]{Integer.toString(p.getNomor()), p.getKode(), p.getNama()};
    }

}

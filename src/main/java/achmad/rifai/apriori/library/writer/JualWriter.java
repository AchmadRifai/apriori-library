/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package achmad.rifai.apriori.library.writer;

import achmad.rifai.apriori.library.model.Jual;
import achmad.rifai.apriori.library.util.DirsUtil;
import achmad.rifai.apriori.library.util.StringUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 *
 * @author acmadrifai
 */
@AllArgsConstructor
@Builder
public class JualWriter {
    private final List<Jual> juals;

    public void write(String path) {
        DirsUtil.mkdirs(path);
        File f = new File(path);
        if (f.exists()) f.delete();
        try (PrintWriter p = new PrintWriter(f)) {
            p.println("nota,waktu,pembeli");
            juals.parallelStream()
                    .filter(Objects::nonNull)
                    .map(this::convertToArrayString)
                    .map(StringUtils::combineToString)
                    .forEach(p::println);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JualWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String[] convertToArrayString(Jual j) {
        return new String[]{j.getNota(), j.getWaktu().toString(), j.getPembeli()};
    }

}

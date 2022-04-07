/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package achmad.rifai.apriori.library.readers;

import achmad.rifai.apriori.library.constants.CsvContants;
import achmad.rifai.apriori.library.model.Pembeli;
import achmad.rifai.apriori.library.util.StringUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
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
public class PembeliReader {
    private final String path;

    public List<Pembeli> read() {
        try {
            return Files.readAllLines(new File(path).toPath())
                    .parallelStream()
                    .filter(StringUtils::nonBlank)
                    .filter(this::validLine)
                    .map(this::toPembeli)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        return Collections.emptyList();
    }

    private Pembeli toPembeli(String s) {
        List<String> strings = Stream.of(s.split(CsvContants.CSV_DELIMITER))
                .map(StringUtils::cleanUpCsv)
                .collect(Collectors.toList());
        return Pembeli.builder()
                .nomor(Integer.parseInt(strings.get(0)))
                .kode(strings.get(1))
                .nama(strings.get(2))
                .build();
    }

    private boolean validLine(String s) {
        return !s.startsWith("nomor") && s.split(CsvContants.CSV_DELIMITER).length == 3;
    }

    private static final Logger log = Logger.getLogger(PembeliReader.class.getName());
}

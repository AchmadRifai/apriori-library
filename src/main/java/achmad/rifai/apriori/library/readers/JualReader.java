/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package achmad.rifai.apriori.library.readers;

import achmad.rifai.apriori.library.constants.CsvContants;
import achmad.rifai.apriori.library.model.Jual;
import achmad.rifai.apriori.library.util.StringUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
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
public class JualReader {
    private final String path;

    private static final Logger log = Logger.getLogger(JualReader.class.getName());

    public List<Jual> read() {
        try {
            return Files.readAllLines(new File(path).toPath())
                    .parallelStream()
                    .filter(StringUtils::nonBlank)
                    .filter(this::toValid)
                    .map(this::toJual)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        return Collections.emptyList();
    }

    private boolean toValid(String s) {
        return !s.startsWith("nota") && s.split(CsvContants.CSV_DELIMITER).length == 3;
    }

    private Jual toJual(String s) {
        List<String> ss = Stream.of(s.split(CsvContants.CSV_DELIMITER))
                .map(StringUtils::cleanUpCsv)
                .collect(Collectors.toList());
        return Jual.builder()
                .nota(ss.get(0))
                .pembeli(ss.get(2))
                .waktu(Timestamp.valueOf(ss.get(1)))
                .build();
    }
}

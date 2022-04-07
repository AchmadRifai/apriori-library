/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package achmad.rifai.apriori.library.readers;

import achmad.rifai.apriori.library.constants.CsvContants;
import achmad.rifai.apriori.library.model.ItemJual;
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
public class ItemJualReader {

    private static final Logger log = Logger.getLogger(ItemJualReader.class.getName());

    private final String path;

    public List<ItemJual> read() {
        try {
            return Files.readAllLines(new File(path).toPath())
                .parallelStream()
                .filter(StringUtils::nonBlank)
                .filter(this::toValid)
                .map(this::toItemJual)
                .collect(Collectors.toList());
        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        return Collections.emptyList();
    }

    private boolean toValid(String s) {
        return !s.startsWith("nota") && s.split(CsvContants.CSV_DELIMITER).length == 3;
    }

    private ItemJual toItemJual(String s) {
        List<String> strings = Stream.of(s.split(CsvContants.CSV_DELIMITER))
                .map(StringUtils::cleanUpCsv)
                .collect(Collectors.toList());
        return ItemJual.builder()
                .nota(strings.get(0))
                .barang(strings.get(1))
                .qty(Integer.parseInt(strings.get(2)))
                .build();
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package achmad.rifai.apriori.library;

import achmad.rifai.apriori.library.constants.CsvContants;
import achmad.rifai.apriori.library.constants.DataConstant;
import achmad.rifai.apriori.library.model.ItemJual;
import achmad.rifai.apriori.library.readers.BarangReader;
import achmad.rifai.apriori.library.readers.ItemJualReader;
import achmad.rifai.apriori.library.readers.JualReader;
import achmad.rifai.apriori.library.util.AprioriUtil;
import achmad.rifai.apriori.library.util.AprioryBigDecimalSupport;
import achmad.rifai.apriori.library.writer.AprioryWriter;
import achmad.rifai.apriori.library.writer.ItemJualWriter;
import achmad.rifai.apriori.library.writer.JualWriter;
import achmad.rifai.apriori.library.writer.PembeliWriter;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author acmadrifai
 */
public class AprioriLibrary {

    public static void main(String[] args) {
        initData();
        ItemJualReader r4 = ItemJualReader.builder().path(CsvContants.ITEM_JUAL_PATH).build();
        List<ItemJual> itemJuals = r4.read();
        AprioriUtil util = AprioriUtil.builder().itemJuals(itemJuals).build();
        List<AprioryBigDecimalSupport> apriory = util.apriory();
        BarangReader r2 = BarangReader.builder().path(CsvContants.BARANG_PATH).build();
        AprioryWriter.builder().apriory(apriory).barangs(r2.reading()).build().write(CsvContants.OUT_PATH);
    }

    private static final Logger log = Logger.getLogger(AprioriLibrary.class.getName());

    public static void initData() {
        PembeliWriter w = PembeliWriter.builder().pembelis(DataConstant.PEMBELIS).build();
        w.write(CsvContants.PEMBELI_PATH);
        JualWriter jualWriter = new JualWriter(DataConstant.genJuals(DataConstant.PEMBELIS));
        jualWriter.write(CsvContants.JUAL_PATH);
        BarangReader r2 = BarangReader.builder().path(CsvContants.BARANG_PATH).build();
        JualReader r3 = JualReader.builder().path(CsvContants.JUAL_PATH).build();
        ItemJualWriter w2 = ItemJualWriter.builder()
                .itemJuals(DataConstant.genItemsJual(r3.read(), r2.reading()))
                .build();
        w2.write(CsvContants.ITEM_JUAL_PATH);
    }
}

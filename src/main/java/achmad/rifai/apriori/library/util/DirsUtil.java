/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package achmad.rifai.apriori.library.util;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author acmadrifai
 */
public class DirsUtil {
    public static void mkdirs(String path) {
        File f = new File(path);
        log.log(Level.INFO, "creating folder : {0}", f.getParentFile().getAbsolutePath());
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
    }

    private static final Logger log = Logger.getLogger(DirsUtil.class.getName());
}

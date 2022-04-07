/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package achmad.rifai.apriori.library.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author acmadrifai
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class Barang {
    private String kode;
    private String nama;
    private BigDecimal harga;
}

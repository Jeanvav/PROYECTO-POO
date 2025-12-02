package com.proyectopoo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
// ELIMINAR O COMENTAR: import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Medicamento {
    private int id;
    private int usuarioId;
    private String nombre;
    private String dosis;
    private int frecuenciaHoras;
    private int stock;
    private LocalDateTime proximaToma;

    /**
     * Constructor requerido por MedicamentoDAO y DialogoMedicamento (7 argumentos).
     */
    public Medicamento(int id, int usuarioId, String nombre, String dosis, int frecuenciaHoras, int stock, LocalDateTime proximaToma) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.dosis = dosis;
        this.frecuenciaHoras = frecuenciaHoras;
        this.stock = stock;
        this.proximaToma = proximaToma;
    }
}
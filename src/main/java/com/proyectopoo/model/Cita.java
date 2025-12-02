package com.proyectopoo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Cita {
    private int id;
    private int usuarioId;
    private String especialidad;
    private String medico;

    private String centroMedico; // <--- NUEVO CAMPO

    private LocalDateTime fechaHora;

    /**
     * Constructor actualizado para incluir el campo 'centroMedico' (6 argumentos).
     */
    public Cita(int id, int usuarioId, String especialidad, String medico, String centroMedico, LocalDateTime fechaHora) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.especialidad = especialidad;
        this.medico = medico;
        this.centroMedico = centroMedico;
        this.fechaHora = fechaHora;
    }
}
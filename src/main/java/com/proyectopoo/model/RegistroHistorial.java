package com.proyectopoo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroHistorial {

    private int id;
    private int usuarioId;
    private String tipoEvento; // Ejemplo: TOMA_MEDICAMENTO, CITA_CANCELADA, TRATAMIENTO_CANCELADO
    private String descripcion; // Descripción del evento
    private LocalDateTime fechaHora;
}
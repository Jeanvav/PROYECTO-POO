package com.proyectopoo.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recordatorio {
    private int id;
    private int usuarioId;
    private String tipo; // Ej: "Toma realizada", "Dosis perdida", "Alerta farmacia", "Recordatorio cita"
    private String descripcion; // Detalle del evento (Ej: "Ibuprofeno 400mg")
    private LocalDateTime fechaRegistro; // Cuándo ocurrió o se registró el evento
}
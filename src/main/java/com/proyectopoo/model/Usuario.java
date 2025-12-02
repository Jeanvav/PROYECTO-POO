package com.proyectopoo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String password;

    // 📢 ACTUALIZADO: Preferencia de alerta (HU-08)
    private String modoAlerta;
}
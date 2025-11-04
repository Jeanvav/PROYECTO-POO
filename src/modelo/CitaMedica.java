package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CitaMedica {
    private String especialidad;
    private String medico;
    private LocalDateTime fechaHora;

    public CitaMedica(String especialidad, String medico, LocalDateTime fechaHora) {
        this.especialidad = especialidad;
        this.medico = medico;
        this.fechaHora = fechaHora;
    }

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Cita médica: " + especialidad + " con " + medico + " el " + fechaHora.format(formato);
    }
}

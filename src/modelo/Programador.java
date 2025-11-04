package modelo;

import java.util.Timer;
import java.util.TimerTask;

public class Programador {
    private Timer temporizador = new Timer();

    public void programarRecordatorio(Recordatorio recordatorio, ServicioNotificaciones servicio, int segundos) {
        temporizador.schedule(new TimerTask() {
            @Override
            public void run() {
                servicio.mostrarNotificacion(recordatorio.getMensaje());
            }
        }, segundos * 1000L); // segundos a milisegundos
    }
}

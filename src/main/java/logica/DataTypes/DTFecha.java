package logica.DataTypes;

public class DTFecha {
    private int anio;
    private int mes;
    private int dia;

    public DTFecha(int anio, int mes, int dia) {
        this.anio = anio;
        this.mes = mes;
        this.dia = dia;
    }

    public int getAnio() {
        return anio;
    }

    public int getMes() {
        return mes;
    }

    public int getDia() {
        return dia;
    }

    @Override
    public String toString() {
        return dia + "-" + mes + "-" + anio;
    }
}
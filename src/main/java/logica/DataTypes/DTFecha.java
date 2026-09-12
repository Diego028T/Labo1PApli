package logica.DataTypes;

import java.util.Objects;

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

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (!(objeto instanceof DTFecha otraFecha)) {
            return false;
        }
        return anio == otraFecha.anio
                && mes == otraFecha.mes
                && dia == otraFecha.dia;
    }

    @Override
    public int hashCode() {
        return Objects.hash(anio, mes, dia);
    }
}
package logica;

import logica.Clases.Evento;
import logica.sistema01.ISistema;
import logica.sistema01.Sistema;

import java.util.ArrayList;
import java.util.List;

public class PruebaSistema {

    public static void main(String[] args) {
        ISistema sistema = Sistema.getInstancia();

        System.out.println("Instituciones iniciales:");
        System.out.println(sistema.listarNombresInstituciones());

        try {
            sistema.altaInstitucion(
                    "Ceibal",
                    "Centro Ceibal",
                    "https://ceibal.edu.uy"
            );

            System.out.println("Alta correcta:");
            System.out.println(sistema.listarNombresInstituciones());

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            sistema.altaInstitucion(
                    "utec",
                    "Institución repetida",
                    "https://ejemplo.com"
            );

        } catch (IllegalArgumentException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }

    }
}
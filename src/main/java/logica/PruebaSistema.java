package logica;

import logica.sistema01.ISistema;
import logica.sistema01.Sistema;

public class PruebaSistema {

    public static void main(String[] args) {
        ISistema sistema = Sistema.getInstancia();

        System.out.println("Instituciones iniciales:");
        System.out.println(sistema.listarNombresInstituciones());

        System.out.println("Eventos iniciales:");
        System.out.println(sistema.listarEventos());
        System.out.println("Organizadores iniciales:");
        System.out.println(sistema.listarOrganizadores());

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

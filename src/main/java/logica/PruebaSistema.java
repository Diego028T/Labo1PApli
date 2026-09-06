package logica;

import logica.sistema01.ISistema;
import logica.sistema01.Sistema;
import logica.DataTypes.DTFecha;
import java.util.List;

public class PruebaSistema {

    public static void main(String[] args) {
        ISistema sistema = Sistema.getInstancia();

        System.out.println("Instituciones iniciales:");
        System.out.println(sistema.listarNombresInstituciones());

        System.out.println("Eventos iniciales:");
        System.out.println(sistema.listarEventos());
        System.out.println("Organizadores iniciales:");
        System.out.println(sistema.listarOrganizadores());
        System.out.println("Categorías disponibles:");
        System.out.println(sistema.listarNombresCategorias());

        sistema.altaEvento(
                "Expo IA 2026",
                "Evento sobre inteligencia artificial",
                "IA26",
                new DTFecha(2026, 9, 5),
                List.of("Tecnología", "Negocios")
        );

        System.out.println("Eventos luego del alta:");
        System.out.println(sistema.listarEventos());

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

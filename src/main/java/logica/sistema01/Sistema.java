package logica.sistema01;

import logica.Clases.Institucion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sistema implements ISistema {

    private static final Sistema instancia = new Sistema();

    private final Map<String, Institucion> instituciones;

    private Sistema() {
        instituciones = new HashMap<>();

        cargarDatosIniciales();
    }

    public static Sistema getInstancia() {
        return instancia;
    }

    @Override
    public void altaInstitucion(
            String nombre,
            String descripcion,
            String sitioWeb) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la institución es obligatorio."
            );
        }

        String clave = nombre.trim().toLowerCase();

        if (instituciones.containsKey(clave)) {
            throw new IllegalArgumentException(
                    "Ya existe una institución con ese nombre."
            );
        }

        Institucion nuevaInstitucion = new Institucion(
                nombre.trim(),
                descripcion,
                sitioWeb
        );

        instituciones.put(clave, nuevaInstitucion);
    }

    @Override
    public List<String> listarNombresInstituciones() {
        List<String> nombres = new ArrayList<>();

        for (Institucion institucion : instituciones.values()) {
            nombres.add(institucion.getNombre());
        }

        return nombres;
    }

    private void cargarDatosIniciales() {
        altaInstitucion(
                "UTEC",
                "Universidad Tecnológica del Uruguay",
                "https://utec.edu.uy"
        );

        altaInstitucion(
                "ANTEL",
                "Empresa nacional de telecomunicaciones",
                "https://www.antel.com.uy"
        );
    }
}
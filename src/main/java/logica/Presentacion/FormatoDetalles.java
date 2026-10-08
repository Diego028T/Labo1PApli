package logica.Presentacion;

import logica.DataTypes.DTEdicion;
import logica.DataTypes.DTPatrocinio;
import logica.DataTypes.DTTipoRegistro;

public final class FormatoDetalles {

    private FormatoDetalles() {
    }

    public static String edicion(DTEdicion datos) {
        return "Nombre: " + datos.getNombre()
                + "\nSigla: " + datos.getSigla()
                + "\nFecha de inicio: " + datos.getFechaInicio()
                + "\nFecha de fin: " + datos.getFechaFin()
                + "\nFecha de alta: " + datos.getFechaAlta()
                + "\nCiudad: " + datos.getCiudad()
                + "\nPais: " + datos.getPais()
                + "\nOrganizador: " + datos.getNombreOrganizador();
    }

    public static String tipoRegistro(DTTipoRegistro datos) {
        return "Nombre: " + datos.getNombre()
                + "\nDescripcion: " + datos.getDescripcion()
                + "\nCosto: " + datos.getCosto()
                + "\nCupo: " + datos.getCupo();
    }

    public static String patrocinio(DTPatrocinio datos) {
        return "Codigo: " + datos.getCodigo()
                + "\nFecha de alta: " + datos.getFecha()
                + "\nInstitucion: " + datos.getNombreInstitucion()
                + "\nEdicion: " + datos.getNombreEdicion()
                + "\nTipo de registro: " + datos.getNombreTipoRegistro()
                + "\nNivel: " + datos.getNivelPatrocinio()
                + "\nMonto aportado: " + datos.getMontoAportado()
                + "\nRegistros gratuitos: " + datos.getCantRegistros();
    }
}
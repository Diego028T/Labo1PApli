package logica.DataTypes;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import logica.DataTypes.DTFecha;

import java.time.LocalDate;

@Converter(autoApply = true)
public class DTFechaConverter implements AttributeConverter<DTFecha, LocalDate> {

    @Override
    public LocalDate convertToDatabaseColumn(DTFecha attribute) {
        if (attribute == null) {
            return null;
        }
        return LocalDate.of(attribute.getAnio(), attribute.getMes(), attribute.getDia());
    }

    @Override
    public DTFecha convertToEntityAttribute(LocalDate dbData) {
        if (dbData == null) {
            return null;
        }
        return new DTFecha(dbData.getYear(), dbData.getMonthValue(), dbData.getDayOfMonth());
    }
}

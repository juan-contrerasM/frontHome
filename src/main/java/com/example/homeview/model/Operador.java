package com.example.homeview.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.sql.Date;

@Data
public class Operador {

    private String cedula;
    private String nombre;
    private String apellido;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fecha_nacimiento;
    private String indicador_aislamiento;

}

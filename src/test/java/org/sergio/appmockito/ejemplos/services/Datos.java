package org.sergio.appmockito.ejemplos.services;

import org.sergio.appmockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.List;

public class Datos {
    public final static  List<Examen> EXAMENES= Arrays.asList(
            new Examen(5L,"Matemáticas"),
            new Examen(6L,"Lenguage"),
            new Examen(7L,"Historia")
    );
    public final static  List<String> PREGUNTAS= Arrays.asList(
           "Aritméticas",
           "Integrales",
           "Derivadas",
            "Trigonometrías",
            "Geometría"
    );
    public final static  Examen EXAMEN=  new Examen(null,"Física");
}

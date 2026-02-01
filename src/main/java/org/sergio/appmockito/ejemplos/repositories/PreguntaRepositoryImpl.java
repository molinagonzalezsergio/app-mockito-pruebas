package org.sergio.appmockito.ejemplos.repositories;

import org.sergio.appmockito.ejemplos.Datos;

import java.util.List;

public class PreguntaRepositoryImpl implements PreguntaRepository {
    @Override
    public List<String> findPreguntasPorExamenId(Long id) {
        System.out.println("<<<<<<< PregunasRepositoryImpl.findPreguntasPorExamenId >>>>>>>");
        return Datos.PREGUNTAS;
    }

    @Override
    public void guardarVarias(List<String> preguntas) {
        System.out.println("<<<<<<< PregunasRepositoryImpl.guardarVarias <<<<<<< PregunasRepositoryImpl.findPreguntasPorExamenId >>>>>>>");

    }
}

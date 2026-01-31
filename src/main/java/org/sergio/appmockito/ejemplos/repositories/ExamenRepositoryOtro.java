package org.sergio.appmockito.ejemplos.repositories;

import org.sergio.appmockito.ejemplos.models.Examen;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamenRepositoryOtro implements ExamenRepository{
    @Override
    public List<Examen> findAll() {
        try {
            TimeUnit.SECONDS.sleep(6);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return List.of();
    }
}

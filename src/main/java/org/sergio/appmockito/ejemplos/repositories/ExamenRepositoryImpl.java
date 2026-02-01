package org.sergio.appmockito.ejemplos.repositories;
import org.sergio.appmockito.ejemplos.Datos;
import org.sergio.appmockito.ejemplos.models.Examen;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamenRepositoryImpl implements ExamenRepository{
    @Override
    public Examen guardar(Examen examen) {
        System.out.println("ExamenRepositoryImpl.guardar");
        return Datos.EXAMEN;
    }

    @Override
    public List<Examen> findAll() {
        try {
            System.out.println("ExamenRepositoryImpl.findAll");
            TimeUnit.SECONDS.sleep(6);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Datos.EXAMENES;
    }
}

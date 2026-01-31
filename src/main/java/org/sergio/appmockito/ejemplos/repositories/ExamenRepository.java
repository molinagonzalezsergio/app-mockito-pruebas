package org.sergio.appmockito.ejemplos.repositories;
import org.sergio.appmockito.ejemplos.models.Examen;
import java.util.List;

public interface ExamenRepository {
    Examen guardar(Examen examen);
    List<Examen> findAll();
}

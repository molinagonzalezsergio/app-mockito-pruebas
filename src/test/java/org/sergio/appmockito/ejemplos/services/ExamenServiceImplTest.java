package org.sergio.appmockito.ejemplos.services;

import org.junit.jupiter.api.*;

import static org.mockito.Mockito.*;
import org.sergio.appmockito.ejemplos.models.Examen;
import org.sergio.appmockito.ejemplos.repositories.ExamenRepository;
import org.sergio.appmockito.ejemplos.repositories.PreguntaRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class ExamenServiceImplTest {
    private ExamenRepository repository;
    private ExamenService service;
    PreguntaRepository preguntaRepository;

    @BeforeEach
    void setUp() {
        this.repository= mock(ExamenRepository.class);
        preguntaRepository=mock(PreguntaRepository.class);
        this.service=new ExamenServiceImpl(repository,preguntaRepository);
    }

    @Tag("Buscar_Examen")
    @Nested
    @DisplayName("*********** Prueba Búsquedas ***********")
    class pruebasUnitarias{
        @Test
        @DisplayName("Prueba Búsqueda con Mockito Lista No Vacía")
        void findExamenPorNomnre() {
            when(repository.findAll()).thenReturn(Datos.EXAMENES);
            Optional<Examen> examen=service.findExamenPorNombre("Matemáticas");
            assertNotNull(examen.isPresent());
            assertEquals(5L,examen.orElseThrow().getId());
            assertEquals("Matemáticas",examen.get().getNombre());
        }

        @Test
        @DisplayName("Prueba Búsqueda con Mockito Lista Vacía")
        void findExamenPorNomnreListaVacía() {
            List<Examen> datos= Collections.emptyList();
            when(repository.findAll()).thenReturn(datos);
            Optional<Examen> examen=service.findExamenPorNombre("Matemáticas");
            assertNotNull(examen.isEmpty());
            assertFalse(examen.isPresent());
        }

        @Test
        @DisplayName("Prueba Obtener Lista de Preguntas Existentes")
        void testPreguntasExamenMatematicas() {
            when(repository.findAll()).thenReturn(Datos.EXAMENES);
            when(preguntaRepository.findPreguntasPorExamenId(5L)).thenReturn(Datos.PREGUNTAS);
            Examen examen=service.findExamenPorNombreConPreguntas("Matemáticas");
            assertEquals(5,examen.getPreguntas().size());
        }

        @Test
        @DisplayName("Prueba Obtener Lista de Preguntas")
        void testPreguntasExamenHistoria() {
            when(repository.findAll()).thenReturn(Datos.EXAMENES);
            when(preguntaRepository.findPreguntasPorExamenId(7L)).thenReturn(Datos.PREGUNTAS);
            Examen examen=service.findExamenPorNombreConPreguntas("Historia");
            assertTrue(examen.getPreguntas().contains("Aritméticas"));
        }

        @Test
        @DisplayName("Prueba Obtener Lista de Preguntas")
        void testPreguntasExamenAny() {
            when(repository.findAll()).thenReturn(Datos.EXAMENES);
            when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
            Examen examen=service.findExamenPorNombreConPreguntas("Historia");
            assertTrue(examen.getPreguntas().contains("Aritméticas"));
        }
    }

}
package org.sergio.appmockito.ejemplos.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sergio.appmockito.ejemplos.models.Examen;
import org.sergio.appmockito.ejemplos.repositories.ExamenRepositoryImpl;
import org.sergio.appmockito.ejemplos.repositories.PreguntaRepositoryImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest2 {

    @Spy
    ExamenRepositoryImpl repository;

    @Spy
    PreguntaRepositoryImpl preguntaRepository;

    @InjectMocks
    ExamenServiceImpl service;

    @BeforeEach
    void setUp() {
    }

    @Tag("Buscar_Examen")
    @Nested
    @DisplayName("*********** Prueba Búsquedas ***********")
    class pruebasUnitarias{

        @Test
        @DisplayName("Prueba Invocar el método SPY")
        void testSpy() {
            // DADO

            List<String> preguntas= Arrays.asList("Geometría");
            //when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);
            doReturn(preguntas).when(preguntaRepository).findPreguntasPorExamenId(anyLong());
            Examen examen=service.findExamenPorNombreConPreguntas("Matemáticas");
            assertEquals(5L,examen.getId());
            assertEquals("Matemáticas",examen.getNombre());
            assertEquals(1,examen.getPreguntas().size());
            assertTrue(examen.getPreguntas().contains("Geometría"));
            verify(repository).findAll();
            verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
        }

    }


}
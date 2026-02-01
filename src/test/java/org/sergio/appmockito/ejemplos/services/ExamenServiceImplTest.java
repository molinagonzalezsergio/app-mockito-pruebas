package org.sergio.appmockito.ejemplos.services;

import org.junit.jupiter.api.*;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.sergio.appmockito.ejemplos.models.Examen;
import org.sergio.appmockito.ejemplos.repositories.ExamenRepository;
import org.sergio.appmockito.ejemplos.repositories.PreguntaRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

    @Mock
    ExamenRepository repository;

    @Mock
    PreguntaRepository preguntaRepository;

    @InjectMocks
    ExamenServiceImpl service;

    @BeforeEach
    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        Usando las anotaciones de MOCKITO, sería lo mismo, que lo comentadol..
//        this.repository= mock(ExamenRepository.class);
//        this.preguntaRepository=mock(PreguntaRepository.class);
//        this.service=new ExamenServiceImpl(repository,preguntaRepository);

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
        @DisplayName("Prueba Obtener Lista de Preguntas Existentes Para Matemáticas")
        void testPreguntasExamenMatematicas() {
            when(repository.findAll()).thenReturn(Datos.EXAMENES);
            when(preguntaRepository.findPreguntasPorExamenId(5L)).thenReturn(Datos.PREGUNTAS);
            Examen examen=service.findExamenPorNombreConPreguntas("Matemáticas");
            assertEquals(5,examen.getPreguntas().size());
        }

        @Test
        @DisplayName("Prueba Obtener Lista de Preguntas Para Historia")
        void testPreguntasExamenHistoria() {
            when(repository.findAll()).thenReturn(Datos.EXAMENES);
            when(preguntaRepository.findPreguntasPorExamenId(7L)).thenReturn(Datos.PREGUNTAS);
            Examen examen=service.findExamenPorNombreConPreguntas("Historia");
            assertTrue(examen.getPreguntas().contains("Aritméticas"));
        }

        @Test
        @DisplayName("Prueba Obtener Lista de Preguntas Para Cualquiera")
        void testPreguntasExamenAny() {
            when(repository.findAll()).thenReturn(Datos.EXAMENES);
            when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
            Examen examen=service.findExamenPorNombreConPreguntas("Historia");
            assertTrue(examen.getPreguntas().contains("Integrales"));
        }

        @Test
        @DisplayName("Prueba Verificar Lista de Preguntas")
        void testPreguntasExamenVerify() {
            when(repository.findAll()).thenReturn(Datos.EXAMENES);
            when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
            Examen examen=service.findExamenPorNombreConPreguntas("Matemáticas");
            assertEquals(5,examen.getPreguntas().size());
            assertTrue(examen.getPreguntas().contains("Trigonometrías"));
            verify(repository).findAll();
            verify(preguntaRepository).findPreguntasPorExamenId(5L);
        }

        @Test
        @DisplayName("Prueba No existe Lista de Preguntas")
        void testNoExisteExamenVerify() {
            when(repository.findAll()).thenReturn(Datos.EXAMENES);
            when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
            Examen examen=service.findExamenPorNombreConPreguntas("Matemáticas");
            assertTrue(examen.getPreguntas().contains("Trigonometrías"));
            verify(repository).findAll();
            verify(preguntaRepository).findPreguntasPorExamenId(5L);
        }

        @Test
        @DisplayName("Prueba Guardar Examen")
        void testGuardarExamen() {
            Examen newExamen=Datos.EXAMEN;
            newExamen.setPreguntas(Datos.PREGUNTAS);
            when(repository.guardar(any(Examen.class))).thenReturn(Datos.EXAMEN);
            Examen examen=service.guardar(newExamen);
            assertNotNull(examen.getId());
            assertEquals(8L,examen.getId());
            assertEquals("Física",examen.getNombre());
            verify(repository).guardar(any(Examen.class));
            verify(preguntaRepository).guardarVarias(anyList());
        }

        @Test
        @DisplayName("Prueba Guardar Examen")
        void testGuardarExamen2() {
            // DADO
            Examen newExamen=Datos.EXAMEN;
            newExamen.setPreguntas(Datos.PREGUNTAS);
            when(repository.guardar(any(Examen.class))).then(new Answer<Examen>() {
                Long secuencia=8L;

                @Override
                public Examen answer(InvocationOnMock invocation) throws Throwable {
                    Examen examen=invocation.getArgument(0);
                    examen.setId(secuencia++);
                    return examen;
                }
            });
            //WHE
            Examen examen=service.guardar(newExamen);
            //THEN
            assertNotNull(examen.getId());
            assertEquals(8L,examen.getId());
            assertEquals("Física",examen.getNombre());
            verify(repository).guardar(any(Examen.class));
            verify(preguntaRepository).guardarVarias(anyList());
        }

        @Test
        @DisplayName("Prueba Guardar Examen Manejo de Excepciones")
        void testManejoEXcepciones() {
            when(repository.findAll()).thenReturn(Datos.EXAMENES);
            when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenThrow(IllegalArgumentException.class);
            Exception exception=assertThrows(IllegalArgumentException.class,()->{
                service.findExamenPorNombreConPreguntas("Matemáticas");
            });
            assertEquals(IllegalArgumentException.class,exception.getClass());
            verify(repository).findAll();
            verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
        }

        @Test
        @DisplayName("Prueba Guardar Examen Manejo de Excepciones")
        void testManejoEXcepcionesIdNull() {
            when(repository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
            when(preguntaRepository.findPreguntasPorExamenId(null)).thenThrow(IllegalArgumentException.class);
            Exception exception=assertThrows(IllegalArgumentException.class,()->{
                service.findExamenPorNombreConPreguntas("Matemáticas");
            });
            assertEquals(IllegalArgumentException.class,exception.getClass());
            verify(repository).findAll();
            verify(preguntaRepository).findPreguntasPorExamenId(null);
        }
    }

}
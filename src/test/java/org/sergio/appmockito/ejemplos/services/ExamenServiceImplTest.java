package org.sergio.appmockito.ejemplos.services;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.sergio.appmockito.ejemplos.Datos;
import org.sergio.appmockito.ejemplos.models.Examen;
import org.sergio.appmockito.ejemplos.repositories.ExamenRepository;
import org.sergio.appmockito.ejemplos.repositories.ExamenRepositoryImpl;
import org.sergio.appmockito.ejemplos.repositories.PreguntaRepository;
import org.sergio.appmockito.ejemplos.repositories.PreguntaRepositoryImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

    @Mock
    ExamenRepositoryImpl repository;

    @Mock
    PreguntaRepositoryImpl preguntaRepository;

    @InjectMocks
    ExamenServiceImpl service;

    @Captor
    ArgumentCaptor<Long> captor;

    @BeforeEach
    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        Usando las anotaciones de MOCKITO, sería lo mismo, que lo comentadol..
//        this.repository= mock(ExamenRepositoryImpl.class);
//        this.preguntaRepository=mock(PreguntaRepositoryImpl.class);
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
    @Tag("Excepciones")
    @Nested
    @DisplayName("*********** Prueba Excepciones Ampliado ***********")
    class pruebasUnitarias2{
        @Test
        @DisplayName("Prueba con Matcher")
        void testArgumentsMatcher() {
            when(repository.findAll()).thenReturn(Datos.EXAMENES);
            when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
            service.findExamenPorNombreConPreguntas("Matemáticas");
            verify(repository).findAll();
            verify(preguntaRepository).findPreguntasPorExamenId(Mockito.argThat(arg->arg!=null && arg.equals(5L)));
            verify(preguntaRepository).findPreguntasPorExamenId(Mockito.argThat(arg->arg!=null && arg>=5L));
            verify(preguntaRepository).findPreguntasPorExamenId(eq(5L));
        }

        public static class MiArgsMatchers implements ArgumentMatcher<Long>{
            private Long argument;
            @Override
            public boolean matches(Long argument) {
                this.argument=argument;
                return argument!=null && argument>0;
            }
            @Override
            public String toString() {
                return "es para implementar un mensaje personalizado de error " +
                        "que impirme mockito en caso que falle el test cuando " +
                        "deeber ssr un entero positivo";
            }
        }

        @Test
        @DisplayName("Prueba con Matcher defino en clase")
        void testArgumentsMatcher2() {
            when(repository.findAll()).thenReturn(Datos.EXAMENES);
            // FALLARÍA CON ID NEGATIVOS Y VEMOS EL MENSAJE
            // when(repository.findAll()).thenReturn(Datos.EXAMENES_ID_NEGATIVOS);
            when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
            service.findExamenPorNombreConPreguntas("Matemáticas");
            verify(repository).findAll();
            verify(preguntaRepository).findPreguntasPorExamenId(argThat(new MiArgsMatchers()));
        }

        @Test
        @DisplayName("Prueba para capturar el argumento en clase")
        void testArgumentsCapture() {
            when(repository.findAll()).thenReturn(Datos.EXAMENES);
            when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
            service.findExamenPorNombreConPreguntas("Matemáticas");
            verify(repository).findAll();
//            La declaración se piuede hacer con Anotaciones.
//            ArgumentCaptor<Long> captor=ArgumentCaptor.forClass(Long.class);
            verify(preguntaRepository).findPreguntasPorExamenId(captor.capture());
            assertEquals(5L,captor.getValue());
        }

        @Test
        @DisplayName("Prueba MÉTODOS VOID")
        void testDoThrow() {
            Examen examen = Datos.EXAMEN;
            examen.setPreguntas(Datos.PREGUNTAS);
            doThrow(IllegalArgumentException.class).when(preguntaRepository).guardarVarias(anyList());
            assertThrows(IllegalArgumentException.class,()->{
                service.guardar(examen);
            });

        }

        @Test
        @DisplayName("Prueba para seleccionar el tipo de preguntas con doAnswer")
        void testDoAnswer() {
            // DADO
            Examen newExamen=Datos.EXAMEN;
            newExamen.setPreguntas(Datos.PREGUNTAS);
            doAnswer(new Answer<Examen>() {
                Long secuencia=8L;
                @Override
                public Examen answer(InvocationOnMock invocation) throws Throwable {
                    Examen examen=invocation.getArgument(0);
                    examen.setId(secuencia++);
                    return  examen;
                }
            }).when(repository).guardar(any(Examen.class));

            Examen examen=service.guardar(newExamen);
            assertEquals(8L,examen.getId());
            assertEquals("Física",examen.getNombre());
            assertTrue(examen.getPreguntas().contains("Geometría"));
        }

        @Test
        @DisplayName("Prueba Invocar el método real")
        void testDoCallRealMethod() {
            // DADO
            when(repository.findAll()).thenReturn(Datos.EXAMENES);
            //when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
            doCallRealMethod().when(preguntaRepository).findPreguntasPorExamenId(anyLong());
            Examen examen=service.findExamenPorNombreConPreguntas("Matemáticas");
            assertEquals(5L,examen.getId());
            assertEquals("Matemáticas",examen.getNombre());
            assertTrue(examen.getPreguntas().contains("Geometría"));
        }

        @Test
        @DisplayName("Prueba Invocar el método SPY")
        void testSpy() {
            // DADO
            ExamenRepository examenRepository=spy(ExamenRepositoryImpl.class);
            PreguntaRepository preguntaRepository=spy(PreguntaRepositoryImpl.class);
            ExamenService examenService=new ExamenServiceImpl(examenRepository,preguntaRepository);

            List<String> preguntas= Arrays.asList("Geometría");
            //when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);
            doReturn(preguntas).when(preguntaRepository).findPreguntasPorExamenId(anyLong());
            Examen examen=examenService.findExamenPorNombreConPreguntas("Matemáticas");
            assertEquals(5L,examen.getId());
            assertEquals("Matemáticas",examen.getNombre());
            assertEquals(1,examen.getPreguntas().size());
            assertTrue(examen.getPreguntas().contains("Geometría"));
            verify(examenRepository).findAll();
            verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
        }

        @Test
        @DisplayName("Prueba Invocar el orden de invocacioNES")
        void testOrdenDeInvocaciones() {
            when(repository.findAll()).thenReturn(Datos.EXAMENES);
            service.findExamenPorNombreConPreguntas("Matemáticas");
            service.findExamenPorNombreConPreguntas("Lenguage");
            InOrder inOrder=inOrder(preguntaRepository);
            inOrder.verify(preguntaRepository).findPreguntasPorExamenId(5L);
            inOrder.verify(preguntaRepository).findPreguntasPorExamenId(6L);
        }

        @Test
        @DisplayName("Prueba Invocar el orden de invocacioNES")
        void testOrdenDeInvocaciones2() {
            when(repository.findAll()).thenReturn(Datos.EXAMENES);
            service.findExamenPorNombreConPreguntas("Matemáticas");
            service.findExamenPorNombreConPreguntas("Lenguage");
            InOrder inOrder=inOrder(repository,preguntaRepository);
            inOrder.verify(repository).findAll();
            inOrder.verify(preguntaRepository).findPreguntasPorExamenId(5L);
            inOrder.verify(repository).findAll();
            inOrder.verify(preguntaRepository).findPreguntasPorExamenId(6L);
        }


    }


}
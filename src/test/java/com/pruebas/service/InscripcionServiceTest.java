package com.pruebas.service;

import com.pruebas.model.Inscripcion;
import com.pruebas.repository.InscripcionRepository;
import com.pruebas.model.services.InscripcionService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InscripcionServiceTest {

    @Mock
    private InscripcionRepository repository;

    @InjectMocks
    private InscripcionService inscripcionService;

    @Test
    void testInscribirEstudianteCorrectamente() {
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setCodigoEstudiante("I2024050");
        inscripcion.setCursoId(2L);

        when(repository.existsByCodigoEstudianteAndCursoId(anyString(), anyLong())).thenReturn(false);
        when(repository.save(any(Inscripcion.class))).thenAnswer(invocation -> {
            Inscripcion i = invocation.getArgument(0);
            return i;
        });

        Inscripcion resultado = inscripcionService.inscribir(inscripcion);

        assertNotNull(resultado, "El resultado no debe ser nulo");
        verify(repository, times(1)).save(any(Inscripcion.class));
    }

    @Test
    void testInscribirEstudianteDuplicadoLanzaExcepcion() {
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setCodigoEstudiante("I2024001");
        inscripcion.setCursoId(1L);

        when(repository.existsByCodigoEstudianteAndCursoId(anyString(), anyLong())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> {
            inscripcionService.inscribir(inscripcion);
        });

        verify(repository, never()).save(any(Inscripcion.class));
    }

    @Test
    void testListarInscripcionesDeEstudiante() {
        String codigoBuscado = "I2024001";

        Inscripcion ins1 = new Inscripcion();
        Inscripcion ins2 = new Inscripcion();

        List<Inscripcion> listaMock = Arrays.asList(ins1, ins2);

        when(repository.findByCodigoEstudiante(codigoBuscado)).thenReturn(listaMock);

        List<Inscripcion> resultado = inscripcionService.listarPorEstudiante(codigoBuscado);

        assertNotNull(resultado);
        assertEquals(2, resultado.size(), "La lista debe tener 2 elementos");
    }
}
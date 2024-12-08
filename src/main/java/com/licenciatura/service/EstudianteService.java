package com.licenciatura.service;

import com.licenciatura.model.Estudiante;
import com.licenciatura.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstudianteService {
    
    private static final Logger logger = LoggerFactory.getLogger(EstudianteService.class);

    @Autowired
    private EstudianteRepository estudianteRepository;

    public List<Estudiante> obtenerTodos() {
        return estudianteRepository.findAll();
    }

    public Estudiante guardar(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }

    public void eliminar(Long id) {
        estudianteRepository.deleteById(id);
    }

    public Estudiante obtenerPorId(Long id) {
        return estudianteRepository.findById(id).orElse(null);
    }

    public List<Estudiante> buscarPorNombre(String nombre) {
        logger.debug("Iniciando búsqueda por nombre: {}", nombre);
        
        try {
            // Validar entrada
            if (nombre == null || nombre.trim().isEmpty()) {
                logger.info("Búsqueda con nombre vacío o nulo");
                return new ArrayList<>();
            }

            // Normalizar y procesar nombre
            String nombreNormalizado = normalize(nombre.trim().toLowerCase());
            
            // Obtener todos los estudiantes y filtrar
            List<Estudiante> estudiantes = estudianteRepository.findAll()
                .stream()
                .filter(e -> e.getNombre() != null && 
                           normalize(e.getNombre().toLowerCase())
                           .contains(nombreNormalizado))
                .collect(Collectors.toList());

            logger.info("Búsqueda completada. Encontrados: {} estudiantes", estudiantes.size());
            return estudiantes;

        } catch (Exception e) {
            logger.error("Error al buscar estudiantes por nombre: {}", e.getMessage());
            throw new RuntimeException("Error en la búsqueda de estudiantes: " + e.getMessage());
        }
    }

    private String normalize(String text) {
        if (text == null) return "";
        try {
            String nfdNormalizedString = java.text.Normalizer.normalize(text, java.text.Normalizer.Form.NFD);
            return nfdNormalizedString.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
        } catch (Exception e) {
            logger.error("Error al normalizar texto: {}", e.getMessage());
            return text.toLowerCase();
        }
    }

    public List<Estudiante> buscarPorLegajo(String legajo) {
        return estudianteRepository.findByLegajo(legajo);
    }
}

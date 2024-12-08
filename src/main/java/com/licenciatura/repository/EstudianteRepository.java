package com.licenciatura.repository;

import com.licenciatura.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    List<Estudiante> findByNombreContaining(String nombre);
    List<Estudiante> findByLegajo(String legajo);
}
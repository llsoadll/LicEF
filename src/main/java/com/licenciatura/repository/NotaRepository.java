package com.licenciatura.repository;

import com.licenciatura.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaRepository extends JpaRepository<Nota, Long> {
    Nota findTopByOrderByIdDesc();
}

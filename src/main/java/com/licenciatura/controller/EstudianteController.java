package com.licenciatura.controller;

import com.licenciatura.model.Estudiante;
import com.licenciatura.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

    @GetMapping
    public List<Estudiante> obtenerTodos() {
        return estudianteService.obtenerTodos();
    }

    @PostMapping
    public Estudiante agregarEstudiante(@RequestBody Estudiante estudiante) {
        return estudianteService.guardar(estudiante);
    }

    @DeleteMapping("/{id}")
    public void borrarEstudiante(@PathVariable Long id) {
        estudianteService.eliminar(id);
    }

    @GetMapping("/{id}")
    public Estudiante obtenerEstudiante(@PathVariable Long id) {
        return estudianteService.obtenerPorId(id);
    }

    @GetMapping("/buscar/nombre")
    public List<Estudiante> buscarPorNombre(@RequestParam String nombre) {
        return estudianteService.buscarPorNombre(nombre);
    }

    @GetMapping("/buscar/legajo")
    public List<Estudiante> buscarPorLegajo(@RequestParam String legajo) {
        return estudianteService.buscarPorLegajo(legajo);
    }
}

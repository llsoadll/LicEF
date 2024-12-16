package com.licenciatura.controller;

import com.licenciatura.model.Nota;
import com.licenciatura.repository.NotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NotasController {
    
    @Autowired
    private NotaRepository notaRepository;

    @GetMapping("/estudiantes/cargar-nota")
    @ResponseBody
    public String cargarNota() {
        Nota ultimaNota = notaRepository.findTopByOrderByIdDesc();
        return ultimaNota != null ? ultimaNota.getContenido() : "";
    }

    @PostMapping("/estudiantes/guardar-nota")
    @ResponseBody
    public String guardarNota(@RequestParam String contenido) {
        try {
            Nota nota = new Nota();
            nota.setContenido(contenido);
            notaRepository.save(nota);
            return "OK";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}

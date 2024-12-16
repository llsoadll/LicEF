package com.licenciatura.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class NotasController {
    private static final String RUTA_NOTAS = System.getProperty("user.home") + "/notas_estudiantes.txt";

    @GetMapping("/estudiantes/cargar-nota")
    @ResponseBody
    public String cargarNota() {
        try {
            if (new File(RUTA_NOTAS).exists()) {
                return new String(Files.readAllBytes(Paths.get(RUTA_NOTAS)));
            }
            return "";
        } catch (IOException e) {
            return "";
        }
    }

    @PostMapping("/estudiantes/guardar-nota")
    @ResponseBody
    public String guardarNota(@RequestParam String contenido) {
        try {
            Files.write(Paths.get(RUTA_NOTAS), contenido.getBytes());
            return "OK";
        } catch (IOException e) {
            return "Error";
        }
    }
}

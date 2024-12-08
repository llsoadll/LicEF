package com.licenciatura.controller;

import com.licenciatura.model.Estudiante;
import com.licenciatura.repository.EstudianteRepository;
import com.licenciatura.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.ArrayList;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;



@Controller
@RequestMapping("/estudiantes")
public class EstudianteViewController {

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private EstudianteRepository estudianteRepository;

    // Añade esta anotación para métodos que ambos roles pueden acceder
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INVITADO')")
    @GetMapping("/lista")
    public String listarEstudiantes(Model model) {
        List<Estudiante> estudiantes = estudianteService.obtenerTodos();
        // Asegurarse de que el campo graduado esté inicializado
        estudiantes.forEach(estudiante -> {
            if (estudiante.getGraduado() == null) {
                estudiante.setGraduado(false);
            }
        });
        model.addAttribute("estudiantes", estudiantes);
        return "listaEstudiantes";  // Vista para mostrar la lista de estudiantes
    }

    @GetMapping("/eliminar_estudiante")
    public String eliminarEstudianteForm() {
        return "eliminar_estudiante";
    }

    // Añade esta anotación para métodos que solo el admin puede acceder
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/nuevo")
    public String mostrarFormularioDeAgregarEstudiante(Model model) {
        model.addAttribute("estudiante", new Estudiante());
        return "nuevoEstudiante";  // La vista que mostrará el formulario
    }

    // Agregar un nuevo estudiante
    @PostMapping("/guardar")
    public String guardarEstudiante(@ModelAttribute Estudiante estudiante) {
        estudianteService.guardar(estudiante);
        return "redirect:/estudiantes/lista";  // Redirige a la lista de estudiantes
    }

    // Buscar estudiantes por nombre
    @GetMapping("/buscar")
    public String buscarEstudiantes(@RequestParam("nombre") String nombre, Model model) {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                model.addAttribute("error", "Por favor, ingrese un nombre para buscar");
                return "resultadoBusqueda";
            }

            List<Estudiante> estudiantes = estudianteService.buscarPorNombre(nombre);
            
            if (estudiantes == null) {
                estudiantes = new ArrayList<>();
            }
            
            // Inicializar graduado si es necesario
            estudiantes.forEach(e -> {
                if (e.getGraduado() == null) {
                    e.setGraduado(false);
                }
            });

            model.addAttribute("estudiantes", estudiantes);
            model.addAttribute("busquedaRealizada", true);
            
            return "resultadoBusqueda";
            
        } catch (Exception e) {
            model.addAttribute("error", "Error en la búsqueda: " + e.getMessage());
            model.addAttribute("estudiantes", new ArrayList<>());
            return "resultadoBusqueda";
        }
    }

    // Buscar estudiante por ID
    @GetMapping("/buscar/id")
    public String buscarEstudiantePorId(@RequestParam("id") Long id, Model model) {
        Estudiante estudiante = estudianteService.obtenerPorId(id);
        model.addAttribute("estudiante", estudiante);
        return "resultadoBusqueda";  // Vista para mostrar el resultado
    }

    @GetMapping("/buscar/formulario")
    public String mostrarFormularioDeBusqueda() {
        return "buscarEstudiante"; // Nombre del archivo HTML
    }

    @PostMapping("/eliminar")
    public String eliminarEstudiante(@RequestParam("id") Long id) {
        estudianteService.eliminar(id);  // Llama al servicio para eliminar
        return "redirect:/estudiantes/lista";  // Redirige a la lista de estudiantes después de la eliminación
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Estudiante estudiante = estudianteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID de estudiante inválido:" + id));
        model.addAttribute("estudiante", estudiante);
        return "editarEstudiante";
    }

    @PostMapping("/editar/{id}")
    public String actualizarEstudiante(@PathVariable Long id, @ModelAttribute Estudiante estudiante) {
        estudianteRepository.save(estudiante);
        return "redirect:/estudiantes/lista";
    }

    @GetMapping("/exportar-excel")
    public void exportarExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=estudiantes.xlsx");
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Estudiantes");
        
        // Crear encabezados
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Nombre");
        headerRow.createCell(1).setCellValue("Año de ingreso");
        headerRow.createCell(2).setCellValue("Correo");
        headerRow.createCell(3).setCellValue("DNI");
        headerRow.createCell(4).setCellValue("Legajo");
        headerRow.createCell(5).setCellValue("Título TFL");
        headerRow.createCell(6).setCellValue("Tutor");
        headerRow.createCell(7).setCellValue("CoTutor");
        headerRow.createCell(8).setCellValue("Nota Evaluador 1");
        headerRow.createCell(9).setCellValue("Nota Evaluador 2");
        headerRow.createCell(10).setCellValue("Nota Tutor");
        headerRow.createCell(11).setCellValue("Evaluadores");
        headerRow.createCell(12).setCellValue("Fecha de envío a evaluar");
        headerRow.createCell(13).setCellValue("Observaciones");
        headerRow.createCell(14).setCellValue("Estado");
        headerRow.createCell(15).setCellValue("Secretaría de Redacción");
        headerRow.createCell(16).setCellValue("JAD");
        headerRow.createCell(17).setCellValue("Solictud de tutor");
        headerRow.createCell(18).setCellValue("Consentimiento de tutor");
        headerRow.createCell(19).setCellValue("Espacios curriculares");
        headerRow.createCell(20).setCellValue("Informe de tutor");
        headerRow.createCell(21).setCellValue("Prácticas profesionales");
        headerRow.createCell(22).setCellValue("Proyecto");
        headerRow.createCell(23).setCellValue("TFL");
        headerRow.createCell(24).setCellValue("Notas adicionales");
        
        // Obtener lista de estudiantes usando el método correcto
        List<Estudiante> estudiantes = estudianteService.obtenerTodos();
        
        // Llenar datos
        int rowNum = 1;
        for(Estudiante estudiante : estudiantes) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(estudiante.getNombre());
            row.createCell(1).setCellValue(estudiante.getAnioIngreso());
            row.createCell(2).setCellValue(estudiante.getCorreo());
            row.createCell(3).setCellValue(estudiante.getDni());
            row.createCell(4).setCellValue(estudiante.getLegajo());
            row.createCell(5).setCellValue(estudiante.getTituloTFL());
            row.createCell(6).setCellValue(estudiante.getTutor());
            row.createCell(7).setCellValue(estudiante.getCotutor());
            row.createCell(8).setCellValue(estudiante.getNotaEvaluador1());
            row.createCell(9).setCellValue(estudiante.getNotaEvaluador2());
            row.createCell(10).setCellValue(estudiante.getNotaTutor());
            row.createCell(11).setCellValue(estudiante.getEvaluadores());
            row.createCell(12).setCellValue(estudiante.getFechaEnvioEvaluar());
            row.createCell(13).setCellValue(estudiante.getObservaciones());
            row.createCell(14).setCellValue(Boolean.TRUE.equals(estudiante.getGraduado()) ? "Graduado" : "En curso");
            row.createCell(15).setCellValue(estudiante.isPasarSecretariaRedaccion() ? "Sí" : "No");
            row.createCell(16).setCellValue(estudiante.isPasarJAD() ? "Sí" : "No");
            row.createCell(17).setCellValue(estudiante.isSolicitudTutor() ? "Sí" : "No");
            row.createCell(18).setCellValue(estudiante.isConsentimientoTutor() ? "Sí" : "No");
            row.createCell(19).setCellValue(estudiante.isEspaciosCurriculares() ? "Sí" : "No");
            row.createCell(20).setCellValue(estudiante.isInformeTutor() ? "Sí" : "No");
            row.createCell(21).setCellValue(estudiante.isPracticasProfesionales() ? "Sí" : "No");
            row.createCell(22).setCellValue(estudiante.isProyecto() ? "Sí" : "No");
            row.createCell(23).setCellValue(estudiante.isTfl() ? "Sí" : "No");
            row.createCell(24).setCellValue(estudiante.getNotasAdicionales());

        }
        
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}

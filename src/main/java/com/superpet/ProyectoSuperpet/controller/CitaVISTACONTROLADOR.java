package com.superpet.ProyectoSuperpet.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itextpdf.awt.geom.Rectangle;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.superpet.ProyectoSuperpet.model.Cita;
import com.superpet.ProyectoSuperpet.model.Mascota;
import com.superpet.ProyectoSuperpet.model.Usuario;
import com.superpet.ProyectoSuperpet.model.Veterinario;
import com.superpet.ProyectoSuperpet.service.CitaService;
import com.superpet.ProyectoSuperpet.service.MascotaService;
import com.superpet.ProyectoSuperpet.service.ServicioService;
import com.superpet.ProyectoSuperpet.service.UsuarioService;
import com.superpet.ProyectoSuperpet.service.VeterinarioService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/citas")
public class CitaVISTACONTROLADOR {

	 @Autowired
	    private CitaService citaService;
	    @Autowired
	    private MascotaService mascotaService;
	    @Autowired
	    private ServicioService servicioService;
	    @Autowired
	    private UsuarioService usuarioService;

	    @Autowired
	    private VeterinarioService veterinarioService;
	    @GetMapping("/nueva")
	    public String mostrarFormulario(Model model, Principal principal) {
	        // usuario logeado p papi
	        String email = principal.getName();
	        Usuario usuario = usuarioService.buscarPorEmail(email);
	        Long clienteId = usuario.getCliente().getId();

	        // Mascotas
	        List<Mascota> mascotas = mascotaService.listarPorCliente(clienteId);
	        List<Veterinario> veterinarios = veterinarioService.listarVeterinarios();

	        //llamar a los servicios
	        model.addAttribute("servicios", servicioService.listarServicios());

	        // Generar horarios disponibles (ejemplo: próximos 7 días, de 9am a 5pm cada hora)
	        List<LocalDateTime> horariosDisponibles = new java.util.ArrayList<>();//aca llore para que funcione
	        LocalDate hoy = LocalDate.now();
	        for (int d = 0; d < 7; d++) { //7 días
	            for (int h = 9; h <= 17; h++) { //de 9 a 17 hs osea 5 hrs
	                horariosDisponibles.add(hoy.plusDays(d).atTime(h, 0));
	            }
	        }

	        // Pasar datos al modelo
	        model.addAttribute("cita", new Cita());
	        model.addAttribute("mascotas", mascotas);
	        model.addAttribute("veterinarios",veterinarios);
	        model.addAttribute("horariosDisponibles", horariosDisponibles);

	        return "form_cita"; // busca templates/form_cita.html
	    }



	    @PostMapping("/guardar")
	    public String guardarCita(@ModelAttribute Cita cita, Authentication auth, RedirectAttributes redirectAttrs) {
	        String email = auth.getName();
	        Usuario usuario = usuarioService.buscarPorEmail(email);

	        cita.setCliente(usuario.getCliente());
	        cita.setEstado("Pendiente");
	        citaService.guardarCita(cita);

	        // 🔸 Mandar mensaje de éxito
	        redirectAttrs.addFlashAttribute("mensajeExito", "Cita registrada correctamente");

	        // 🔸 Redirigir nuevamente al formulario (o a la lista si prefieres)
	        return "redirect:/citas/nueva";
	    }
	    
	    @GetMapping("/descargar/{id}")
	    public void descargarCitaPdf(@PathVariable Long id, HttpServletResponse response) {
	        try {
	            // Buscar la cita
	            Cita cita = citaService.obtenerCitaPorId(id);
	            if (cita == null) {
	                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Cita no encontrada");
	                return;
	            }

	            // Configurar cabecera de respuesta
	            response.setContentType("application/pdf");
	            response.setHeader("Content-Disposition", "attachment; filename=cita_" + id + ".pdf");

	            // Crear documento con márgenes
	            com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4, 50, 50, 70, 50);
	            com.itextpdf.text.pdf.PdfWriter writer = com.itextpdf.text.pdf.PdfWriter.getInstance(document, response.getOutputStream());
	            document.open();

	            // 🎨 Definir fuentes
	            BaseColor naranjaBonito = new BaseColor(255, 152, 0); // 🧡 Naranja elegante
	            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.WHITE);
	            Font subtituloFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
	            Font textoFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.DARK_GRAY);
	            Font labelFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
	            Font footerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, naranjaBonito);

	            // 🧡 Encabezado naranja bonito
	            PdfPTable header = new PdfPTable(1);
	            header.setWidthPercentage(100);
	            PdfPCell cell = new PdfPCell(new Phrase("🐾 Patitas Felices - Detalle de Cita", tituloFont));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell.setBackgroundColor(naranjaBonito); // Fondo naranja bonito
	            cell.setPadding(15);
	            cell.setBorder(Rectangle.OUT_BOTTOM);
	            header.addCell(cell);
	            document.add(header);

	            document.add(Chunk.NEWLINE);

	            // 📅 Subtítulo en negro
	            Paragraph subtitulo = new Paragraph("Información general de la cita #" + cita.getId(), subtituloFont);
	            subtitulo.setAlignment(Element.ALIGN_CENTER);
	            document.add(subtitulo);

	            document.add(Chunk.NEWLINE);

	            // 🧾 Tabla de detalles
	            PdfPTable table = new PdfPTable(2);
	            table.setWidthPercentage(90);
	            table.setSpacingBefore(15f);
	            table.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.setWidths(new float[]{2, 4});

	            BiConsumer<String, String> addRow = (label, value) -> {
	                PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
	                labelCell.setBackgroundColor(new BaseColor(224, 224, 224)); // Gris claro
	                labelCell.setPadding(8);
	                table.addCell(labelCell);

	                PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "—", textoFont));
	                valueCell.setPadding(8);
	                table.addCell(valueCell);
	            };

	            addRow.accept("🐶 Mascota:", cita.getMascota() != null ? cita.getMascota().getNombre() : "Sin asignar");
	            addRow.accept("💉 Servicio:", cita.getServicio() != null ? cita.getServicio().getNombre() : "Sin asignar");
	            addRow.accept("📅 Fecha:", cita.getFechaCita() != null ? cita.getFechaCita().toString() : "—");
	            addRow.accept("📋 Estado:", cita.getEstado());

	            document.add(table);
	            document.add(Chunk.NEWLINE);
	            document.add(Chunk.NEWLINE);

	            // 🧡 Pie de página naranja
	            Paragraph footer = new Paragraph("Gracias por confiar en Arriba Patitas 🐕‍🦺", footerFont);
	            footer.setAlignment(Element.ALIGN_CENTER);
	            footer.setSpacingBefore(30);
	            document.add(footer);

	            document.close();

	        } catch (Exception e) {
	            e.printStackTrace();
	            try {
	                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al generar el PDF");
	            } catch (Exception ignored) {}
	        }
	    }







}

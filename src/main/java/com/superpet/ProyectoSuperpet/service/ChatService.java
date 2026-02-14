package com.superpet.ProyectoSuperpet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.superpet.ProyectoSuperpet.dto.AccionDTO;
import com.superpet.ProyectoSuperpet.dto.ChatResponse;
import com.superpet.ProyectoSuperpet.model.Pedido;
import com.superpet.ProyectoSuperpet.model.Producto;
import com.superpet.ProyectoSuperpet.repository.PedidoRepository;
import com.superpet.ProyectoSuperpet.repository.ProductoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.superpet.ProyectoSuperpet.dto.GeminiIntentResponse;


import jakarta.servlet.http.HttpSession;

@Service
public class ChatService {

    private final GeminiService geminiService;
    private final ProductoRepository productoRepo;
    private final PedidoRepository pedidoRepo;

    public ChatService(GeminiService geminiService,
                       ProductoRepository productoRepo,
                       PedidoRepository pedidoRepo) {
        this.geminiService = geminiService;
        this.productoRepo = productoRepo;
        this.pedidoRepo = pedidoRepo;
    }

    public ChatResponse procesarMensaje(String mensaje, boolean autenticado, HttpSession session) {

        // deberia guardar el historial :v xd
        List<String> historial = (List<String>) session.getAttribute("historialChat");
        if (historial == null) historial = new ArrayList<>();

        historial.add("Usuario: " + mensaje);

        //limite
        if (historial.size() > 10) {
            historial = historial.subList(historial.size() - 10, historial.size());
        }

        session.setAttribute("historialChat", historial);

        // por promts  generados por aqu
        String intencion = detectarIntencionLocal(mensaje);

        if (!intencion.equals("DESCONOCIDO")) {
            ChatResponse resp = generarRespuestaPorIntencion(intencion, autenticado);
            historial.add("Bot: " + resp.getMensaje());
            session.setAttribute("historialChat", historial);
            return resp;
        }

        // por palabras clave :v hola procuctos u esoo
        ChatResponse respuestaProducto = detectarProductoEnBD(mensaje);
        if (respuestaProducto != null) {
            historial.add("Bot: " + respuestaProducto.getMensaje());
            session.setAttribute("historialChat", historial);
            return respuestaProducto;
        }

     // aca ya con gemini pero no lo malgastes en 1+1 xd
        String json = geminiService.detectarIntencionJSON(mensaje);
        json = limpiarRespuestaGemini(json);

        if (json != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                GeminiIntentResponse data = mapper.readValue(json, GeminiIntentResponse.class);

                String intent = data.getIntent();

                if (intent != null) {
                    intent = intent.toUpperCase();

                    // Busca ´rpducto
                    if (intent.equals("BUSCAR_PRODUCTO") && data.getKeyword() != null) {
                        ChatResponse resp = responderProducto(data.getKeyword());
                        historial.add("Bot: " + resp.getMensaje());
                        session.setAttribute("historialChat", historial);
                        return resp;
                    }

                    // otros p
                    if (!intent.equals("OTRO")) {
                        ChatResponse resp = generarRespuestaPorIntencion(intent, autenticado);
                        historial.add("Bot: " + resp.getMensaje());
                        session.setAttribute("historialChat", historial);
                        return resp;
                    }
                }

            } catch (Exception e) {
                System.out.println("❌ JSON inválido de Gemini: " + json);
            }
        }


        // gg si nada de atras sirve
        String respuestaJson = geminiService.generarRespuestaConHistorial(mensaje, autenticado, historial);
        respuestaJson = limpiarRespuestaGemini(respuestaJson);

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> jsonMap = mapper.readValue(respuestaJson, Map.class);

            String texto = (String) jsonMap.get("respuesta");

            Map<String, String> accionMap = (Map<String, String>) jsonMap.get("accion");

            AccionDTO accion = null;
            if (accionMap != null) {
                accion = new AccionDTO(accionMap.get("texto"), accionMap.get("url"));
            }

            ChatResponse finalResp = new ChatResponse(texto, accion);

            historial.add("Bot: " + texto);
            session.setAttribute("historialChat", historial);

            return finalResp;

        } catch (Exception e) {
            System.out.println("❌ Respuesta inválida: " + respuestaJson);
        }

        return new ChatResponse(
        		respuestaJson,
                null
        );
    }


    // esto es por local noma asi que gemini nah
    private String detectarIntencionLocal(String mensaje) {
        String lower = mensaje.toLowerCase();

        if (lower.matches(".*(hola|hello|hi|buenas|saludos|hey).*")) return "SALUDO";
        if (lower.matches(".*(perfil|mi cuenta|mis datos|mi información).*")) return "PERFIL";
        if (lower.matches(".*(número|teléfono|whatsapp|contacto|llamar).*")) return "CONTACTO";
        if (lower.matches(".*(dirección|ubicación|dónde están|dónde quedan|cómo llegar).*")) return "DIRECCION";
        if (lower.matches(".*(precio|costo|cuánto cuesta|tarifa|cuánto sale).*")) return "PRECIO";
        if (lower.matches(".*(servicio|qué ofrecen|qué hacen|tratamiento).*")) return "SERVICIO";
        if (lower.matches(".*(cita|agendar|reservar|turno|próxima cita|mis citas).*")) return "CITA";
        if (lower.matches(".*(mascota|peludito|registrar|mis mascotas).*")) return "MASCOTA";
        if (lower.matches(".*(producto|productos|comprar|catalogo|catálogo|tienda).*")) return "PRODUCTOS";




        return "DESCONOCIDO";
    }

    // ver productos en la bd 
    private ChatResponse detectarProductoEnBD(String mensaje) {

        // palabras clave comunes
        String lower = mensaje.toLowerCase();

        // intentamos buscar por palabras típicas
        if (lower.contains("frontline")) return responderProducto("frontline");
        if (lower.contains("antipulgas")) return responderProducto("antipulgas");
        if (lower.contains("alimento")) return responderProducto("alimento");
        if (lower.contains("shampoo")) return responderProducto("shampoo");
        if (lower.contains("vacuna")) return responderProducto("vacuna");

        return null;
    }

    private ChatResponse responderProducto(String keyword) {
        List<Producto> productos = productoRepo.findTop5ByNombreContainingIgnoreCase(keyword);

        if (productos.isEmpty()) {
            return new ChatResponse(
                "No encontré productos relacionados con \"" + keyword + "\" 🐾",
                new AccionDTO("Ver catálogo", "/productos")
            );
        }

        StringBuilder sb = new StringBuilder("Encontré estos productos 🐾:\n\n");

        for (Producto p : productos) {
            sb.append("✅ ")
              .append(p.getNombre())
              .append(" - S/ ")
              .append(p.getPrecio())
              .append("\n");
        }

        return new ChatResponse(
            sb.toString(),
            new AccionDTO("Ver catálogo", "/productos")
        );
    }

    // Respuesta por intención
    private ChatResponse generarRespuestaPorIntencion(String intencion, boolean autenticado) {

        switch (intencion) {

            case "SALUDO":
                return new ChatResponse("¡Hola! 🐾 Soy tu asistente de SuperPet. ¿Qué necesitas hoy?", null);

            case "PERFIL":
                return autenticado
                    ? new ChatResponse("Aquí puedes ver tu perfil 🐾", new AccionDTO("Mi Perfil", "/miperfil"))
                    : new ChatResponse("Debes iniciar sesión para ver tu perfil.", new AccionDTO("Iniciar Sesión", "/login"));

            case "CONTACTO":
                return new ChatResponse("📞 Puedes contactarnos al WhatsApp: +51 987 224 378",
                        new AccionDTO("WhatsApp", "https://wa.me/51987224378"));

            case "DIRECCION":
                return new ChatResponse("📍 Estamos en Lima - Veterinaria PuppyVet 🐾",
                        new AccionDTO("Ver en Maps", "https://www.google.com/maps/place/VETERINARIA+PUPPYVET"));

            case "PRECIO":
                return new ChatResponse("💰 Los precios varían según el producto o servicio. Puedes revisar nuestro catálogo 🐾",
                        new AccionDTO("Ver Productos", "/productos"));

            case "SERVICIO":
                return new ChatResponse("Ofrecemos consultas, vacunación, grooming y productos para mascotas 🐾",
                        new AccionDTO("Ver Servicios", "/menu#acciones"));

            case "CITA":
                return autenticado
                    ? new ChatResponse("Puedes agendar tu cita aquí 🐾", new AccionDTO("Agendar Cita", "/citas/nueva"))
                    : new ChatResponse("Debes iniciar sesión para agendar una cita.", new AccionDTO("Iniciar Sesión", "/login"));

            case "MASCOTA":
                return autenticado
                    ? new ChatResponse("Aquí puedes registrar y ver tus mascotas 🐾", new AccionDTO("Mis Mascotas", "/mascotas/nueva"))
                    : new ChatResponse("Debes iniciar sesión para ver tus mascotas.", new AccionDTO("Iniciar Sesión", "/login"));

            case "PEDIDOS":
                return obtenerPedidosCliente(autenticado);

            case "PRODUCTOS":
                return new ChatResponse("🐾 Aquí tienes nuestro catálogo:", new AccionDTO("Ver Productos", "/productos"));


            default:
                return new ChatResponse("No entendí bien 😅 ¿Quieres ver productos o servicios?", new AccionDTO("Ver Productos", "/productos"));
        }
    }

    // Pedidos reales del cliente logueado
    private ChatResponse obtenerPedidosCliente(boolean autenticado) {

        if (!autenticado) {
            return new ChatResponse("Debes iniciar sesión para ver tus pedidos 🐾", new AccionDTO("Iniciar Sesión", "/login"));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return new ChatResponse(
            "Puedes revisar tus pedidos aquí 🐾",
            new AccionDTO("Mis Pedidos", "/pedidos")
        );
    }


    private AccionDTO detectarAccion(String respuesta, boolean autenticado) {
        String lower = respuesta.toLowerCase();

        if (lower.contains("cita") || lower.contains("agendar"))
            return autenticado ? new AccionDTO("Agendar Cita", "/citas/nueva") : new AccionDTO("Iniciar Sesión", "/login");

        if (lower.contains("mascota") || lower.contains("peludito"))
            return autenticado ? new AccionDTO("Mis Mascotas", "/mascotas/nueva") : new AccionDTO("Iniciar Sesión", "/login");

        if (lower.contains("perfil") || lower.contains("cuenta"))
            return autenticado ? new AccionDTO("Mi Perfil", "/miperfil") : new AccionDTO("Iniciar Sesión", "/login");

        if (lower.contains("producto") || lower.contains("comprar"))
            return new AccionDTO("Ver Productos", "/productos");

        return null;
    }
    
    private String limpiarRespuestaGemini(String texto) {

        if (texto == null) return null;

        // quitar bloques ```json ... ```
        texto = texto.replaceAll("(?s)```json", "");
        texto = texto.replaceAll("(?s)```", "");

        // recortar espacios
        texto = texto.trim();

        // si viene con HTML pegado, quedarnos SOLO con el JSON
        int inicio = texto.indexOf("{");
        int fin = texto.lastIndexOf("}");

        if (inicio != -1 && fin != -1 && fin > inicio) {
            texto = texto.substring(inicio, fin + 1);
        }

        return texto;
    }//gracias ia me tenia harto el formato <json> xdddd

}

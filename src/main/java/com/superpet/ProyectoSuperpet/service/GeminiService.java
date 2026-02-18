package com.superpet.ProyectoSuperpet.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.List;

@Service
public class GeminiService {

	// SOMOS UN  PROMT CAUSAS XD
    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

    public String detectarIntencionJSON(String mensajeUsuario) {

        RestTemplate restTemplate = new RestTemplate();
        String url = BASE_URL + "?key=" + apiKey;

        String prompt = """
            Responde SOLO en JSON válido, sin texto extra.

            Debes devolver siempre este formato:

            {
              "intent": "SALUDO | BUSCAR_PRODUCTO | PEDIDOS | PERFIL | CITA | MASCOTA | OTRO",
              "keyword": "string o null",
              "cantidad": numero o null
            }

            Reglas:
            - BUSCAR_PRODUCTO: si pide alimento, antipulgas, shampoo, collar, etc.
            - keyword debe ser una palabra clave simple (ej: "frontline", "alimento", "antipulgas")
            - cantidad solo si el usuario menciona un número.

            Mensaje: %s
            """.formatted(mensajeUsuario);

        Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(
                    Map.of("text", prompt)
                ))
            )
        );

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            Map<String, Object> response = restTemplate.postForObject(url, entity, Map.class);

            if (response == null) return null;

            List candidates = (List) response.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map firstCandidate = (Map) candidates.get(0);
                Map content = (Map) firstCandidate.get("content");
                List parts = (List) content.get("parts");
                Map firstPart = (Map) parts.get(0);

                return firstPart.get("text").toString().trim();
            }

            return null;

        } catch (Exception e) {
            System.out.println("❌ Error Gemini JSON: " + e.getMessage());
            return null;
        }
    }

    
    public String generarRespuesta(String mensajeUsuario, boolean autenticado) {
        RestTemplate restTemplate = new RestTemplate();
        String url = BASE_URL + "?key=" + apiKey;

        // ✅ PROMPT CONVERSACIONAL
        String prompt = String.format("""
            Eres el asistente virtual de "Arriba Patitas", una veterinaria en Lima, Perú.
            
            INFORMACIÓN DE LA EMPRESA:
            - Teléfono/WhatsApp: +51 987 224 378
            - Dirección: Veterinaria PuppyVet, Lima, Perú
            - Servicios: Consultas veterinarias, vacunación, inscripción de mascotas, agendado de citas
            - Horario: Lunes a Sábado 9:00 AM - 6:00 PM
            
            ESTADO DEL USUARIO:
            - Usuario %s
            
            INSTRUCCIONES:
            - Responde de forma amigable y breve (máximo 2-3 líneas)
            - Si preguntan por precios, explica que varían según el servicio y que pueden agendar cita
            - Si el usuario NO está autenticado y pregunta por citas/mascotas/perfil, menciona que debe iniciar sesión
            - Usa emojis de vez en cuando 🐾
            - NO inventes información que no tengas
            
            Pregunta del usuario: %s
            """, 
            autenticado ? "AUTENTICADO" : "NO AUTENTICADO",
            mensajeUsuario
        );

        Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(
                    Map.of("text", prompt)
                ))
            )
        );

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            System.out.println("🔍 Generando respuesta con Gemini...");

            Map<String, Object> response = restTemplate.postForObject(url, entity, Map.class);

            if (response == null) {
                return "Lo siento, tuve un problema. ¿Puedes intentar de nuevo? 🐾";
            }

            List candidates = (List) response.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map firstCandidate = (Map) candidates.get(0);
                Map content = (Map) firstCandidate.get("content");
                List parts = (List) content.get("parts");
                Map firstPart = (Map) parts.get(0);
                String respuesta = firstPart.get("text").toString().trim();

                System.out.println("✅ Respuesta generada: " + respuesta);
                return respuesta;
            }

            return "No estoy seguro de cómo ayudarte con eso. ¿Puedes reformular tu pregunta? 🐾";

        } catch (Exception e) {
            System.err.println("❌ Error generando respuesta: " + e.getMessage());
            return "Ups, algo salió mal. ¿Intentas de nuevo? 🐾";
        }
    }
    
    public String generarRespuestaConHistorial(String mensajeUsuario, boolean autenticado, List<String> historial) {

        RestTemplate restTemplate = new RestTemplate();
        String url = BASE_URL + "?key=" + apiKey;

        String contexto = String.join("\n", historial);

        String prompt = String.format("""
        		Eres SuperPetBot, asistente oficial de SuperPet (veterinaria y tienda de mascotas en Lima, Perú).

        		REGLAS IMPORTANTES:
        		- Responde máximo 3 líneas.
        		- Usa emojis 🐾 ocasionalmente.
        		- Si no hay datos suficientes, pide aclaración.
        		- NO inventes precios ni stock si no se te proporcionan.
        		- Si detectas urgencia médica (sangrado, convulsiones, vómitos severos), recomienda ir al veterinario de inmediato.

        		FORMATO DE RESPUESTA OBLIGATORIO (JSON):
        		{
        		  "respuesta": "...",
        		  "accion": {
        		    "texto": "...",
        		    "url": "..."
        		  }
        		}

        		HISTORIAL:
        		%s

        		USUARIO: %s

        		MENSAJE ACTUAL:
        		%s
        		""",
        		contexto,
        		autenticado ? "AUTENTICADO" : "NO AUTENTICADO",
        		mensajeUsuario);
        

        Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(
                    Map.of("text", prompt)
                ))
            )
        );

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            Map<String, Object> response = restTemplate.postForObject(url, entity, Map.class);

            if (response == null) return "Lo siento, no pude responder 🐾";

            List candidates = (List) response.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map firstCandidate = (Map) candidates.get(0);
                Map content = (Map) firstCandidate.get("content");
                List parts = (List) content.get("parts");
                Map firstPart = (Map) parts.get(0);
                return firstPart.get("text").toString().trim();
            }

            return "No entendí bien 😅 ¿puedes explicarlo de otra forma? 🐾";

        } catch (Exception e) {
            return "Ups, hubo un error técnico 🐾";
        }
    }

    
}
//```
//
//---
//
//## 🧪 Prueba las nuevas funciones
//
//Reinicia la aplicación y prueba estos mensajes en el chat:
//
//1. **"Ver mi perfil"** → Debería mostrar botón "Mi Perfil"
//2. **"Dame tu número"** → Debería mostrar teléfono y botón de WhatsApp
//3. **"Dónde están ubicados?"** → Debería mostrar dirección y botón de Google Maps
//4. **"Cuánto cuestan los servicios?"** → Debería explicar precios
//
//---
//
//## 📊 Resultado esperado en consola
//```
//Procesando mensaje: dame tu numero
//🔍 Llamando a Gemini 2.5 Flash...
//✅ Gemini clasificó como: CONTACTO
//Intención detectada: CONTACTO
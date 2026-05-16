package com.rplace.clicker.websocket;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rplace.clicker.dto.PurchaseResult;
import com.rplace.clicker.model.Pixel;
import com.rplace.clicker.model.Player;
import com.rplace.clicker.service.GridService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.time.format.DateTimeFormatter;

@ServerEndpoint(value = "/grid", configurator = WebSocketConfig.class)
public class WebSocket {
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) throws IOException {
        try {
            HttpSession httpSession = (HttpSession) endpointConfig.getUserProperties().get(HttpSession.class.getName());
            session.getUserProperties().put("httpSession", httpSession);
            sessions.add(session);

            EntityManagerFactory emf = (EntityManagerFactory) httpSession.getServletContext().getAttribute("EMF");
            GridService gridService = new GridService(emf);
            List<Pixel> pixels = gridService.loadAllPixels();

            JsonArray pixelsJson = new JsonArray();
            for (Pixel p : pixels) {
                JsonObject object = new JsonObject();
                object.addProperty("x", p.getId().getX());
                object.addProperty("y", p.getId().getY());
                object.addProperty("color", p.getColor());
                object.addProperty("player", p.getCurrentPlayer() != null ? p.getCurrentPlayer().getPseudo() : "Personne");
                object.addProperty("price", p.getPrice());
                object.addProperty("date", p.getUpdatedAt() != null ? p.getUpdatedAt().format(DATE_FORMATTER) : "Jamais");
                pixelsJson.add(object);
            }

            JsonObject docJson = new JsonObject();
            docJson.addProperty("type", "init");
            docJson.add("pixels", pixelsJson);
            session.getBasicRemote().sendText(docJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        try {
            HttpSession httpSession = (HttpSession) session.getUserProperties().get("httpSession");
            Player player = (Player) httpSession.getAttribute("player");

            if (player == null) return;

            JsonObject data = JsonParser.parseString(message).getAsJsonObject();
            int x = data.get("x").getAsInt();
            int y = data.get("y").getAsInt();
            String color = data.get("color").getAsString();
            
            EntityManagerFactory emf = (EntityManagerFactory) httpSession.getServletContext().getAttribute("EMF");
            GridService gridService = new GridService(emf);
            PurchaseResult result = gridService.putPixel(player, x, y, color);

            if (!result.success()) {
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("type", "error");
                errorJson.addProperty("message", "Vous n'avez pas assez de crédits !");
                session.getBasicRemote().sendText(errorJson.toString());
                return;
            }
            
            Player updatedPlayer = result.player();
            httpSession.setAttribute("player", updatedPlayer);
            httpSession.setAttribute("nb_Current_Click", updatedPlayer.getCredits());

            Pixel p = gridService.getPixel(x, y);
            
            if (p != null) {
                JsonObject broadCastMessage = new JsonObject();
                broadCastMessage.addProperty("type", "pixel");
                broadCastMessage.addProperty("x", x);
                broadCastMessage.addProperty("y", y);
                broadCastMessage.addProperty("color", p.getColor());
                broadCastMessage.addProperty("player", p.getCurrentPlayer() != null ? p.getCurrentPlayer().getPseudo() : "Personne");
                broadCastMessage.addProperty("date", p.getUpdatedAt() != null ? p.getUpdatedAt().format(DATE_FORMATTER) : "Maintenant");
                broadCastMessage.addProperty("price", p.getPrice());

                JsonObject privateMessage = broadCastMessage.deepCopy();
                privateMessage.addProperty("newBalance", updatedPlayer.getCredits());

                for (Session s : sessions) {
                    if (s.isOpen()) {
                        if (s.equals(session)) {
                            s.getBasicRemote().sendText(privateMessage.toString());
                        } else {
                            s.getBasicRemote().sendText(broadCastMessage.toString());
                        }
                    }
                }         
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        sessions.remove(session);
    }
}
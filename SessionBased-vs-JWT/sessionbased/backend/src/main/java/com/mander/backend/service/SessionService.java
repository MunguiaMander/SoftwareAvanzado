package com.mander.backend.service;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    private final Map<String, Map<String, HttpSession>> sessionsByUser = new ConcurrentHashMap<>();

    public void register(String username, HttpSession session) {
        sessionsByUser.computeIfAbsent(username, k -> new ConcurrentHashMap<>()).put(session.getId(), session);
    }

    public int countActive(String username) {
        Map<String, HttpSession> sessions = sessionsByUser.get(username);
        return sessions == null ? 0 : sessions.size();
    }

    public void invalidateAll(String username) {
        invalidateAllExcept(username, null);
    }

    public void invalidateAllExcept(String username, String keepSessionId) {
        Map<String, HttpSession> sessions = sessionsByUser.get(username);
        if (sessions == null) {
            return;
        }

        for (String id : List.copyOf(sessions.keySet())) {
            if (id.equals(keepSessionId)) {
                continue;
            }
            HttpSession session = sessions.remove(id);
            try {
                session.invalidate();
            } catch (IllegalStateException ignored) {
            }
        }
    }
}

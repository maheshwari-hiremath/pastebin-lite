package com.example.pastebin.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.pastebin.dto.PasteRequest;
import com.example.pastebin.exception.NotFoundException;
import com.example.pastebin.model.Paste;
import com.example.pastebin.repository.PasteRepository;
@Service
public class PasteService {

    private final PasteRepository repo;

    public PasteService(PasteRepository repo) {
        this.repo = repo;
    }

    public Paste create(PasteRequest req) {
        if (req.content == null || req.content.trim().isEmpty())
            throw new IllegalArgumentException();

        Paste p = new Paste();
        p.setId(UUID.randomUUID().toString());
        p.setContent(req.content);
        p.setMaxViews(req.max_views);

        if (req.ttl_seconds != null)
            p.setExpiresAt(Instant.now().plusSeconds(req.ttl_seconds));

        return repo.save(p);
    }

    public Paste fetch(String id, Instant now) {
        Paste p = repo.findById(id).orElseThrow(NotFoundException::new);

        if (p.isExpired(now) || p.isViewLimitExceeded())
            throw new NotFoundException();

        p.setViews(p.getViews() + 1);
        repo.save(p);
        return p;
    }
}

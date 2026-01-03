package com.example.pastebin.controller;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.example.pastebin.dto.PasteRequest;
import com.example.pastebin.dto.PasteResponse;
import com.example.pastebin.model.Paste;
import com.example.pastebin.service.PasteService;

@RestController
@RequestMapping("/api/paste")  // <-- class-level mapping fixes 404
public class PasteController {

    private final PasteService service;

    public PasteController(PasteService service) {
        this.service = service;
    }

    @PostMapping
    public Map<String, String> create(@RequestBody PasteRequest req) {
        Paste p = service.create(req);
        return Map.of(
            "id", p.getId(),
            "url", "/p/" + p.getId()
        );
    }

    @GetMapping("/{id}")
    public PasteResponse fetch(
            @PathVariable String id,
            @RequestHeader(value = "x-test-now-ms", required = false) Long testNow
    ) {
        Instant now = testNow != null ? Instant.ofEpochMilli(testNow) : Instant.now();
        Paste p = service.fetch(id, now);

        PasteResponse res = new PasteResponse();
        res.content = p.getContent();
        res.remaining_views = p.getMaxViews() == null ? null : p.getMaxViews() - p.getViews();
        res.expires_at = p.getExpiresAt();
        return res;
    }

    @GetMapping(value = "/p/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String view(@PathVariable String id) {
        Paste p = service.fetch(id, Instant.now());
        return "<html><body><pre>" + p.getContent().replace("<", "&lt;") + "</pre></body></html>";
    }
}

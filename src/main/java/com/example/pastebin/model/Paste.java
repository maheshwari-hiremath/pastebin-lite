package com.example.pastebin.model;

import jakarta.persistence.*;
import java.time.Instant;
	@Entity
	public class Paste {

	    @Id
	    private String id;

	    @Column(nullable = false, length = 10000)
	    private String content;

	    private Integer maxViews;
	    private Integer views = 0;
	    private Instant expiresAt;

	    public boolean isExpired(Instant now) {
	        return expiresAt != null && now.isAfter(expiresAt);
	    }

	    public boolean isViewLimitExceeded() {
	        return maxViews != null && views >= maxViews;
	    }

	    // getters & setters
	    public String getId() { return id; }
	    public void setId(String id) { this.id = id; }

	    public String getContent() { return content; }
	    public void setContent(String content) { this.content = content; }

	    public Integer getMaxViews() { return maxViews; }
	    public void setMaxViews(Integer maxViews) { this.maxViews = maxViews; }

	    public Integer getViews() { return views; }
	    public void setViews(Integer views) { this.views = views; }

	    public Instant getExpiresAt() { return expiresAt; }
	    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
	}

package com.talkingnews.backend.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Feed.
 */
@Entity
@Table(name = "feed")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Feed implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "category", nullable = false)
    private String category;

    @NotNull
    @Column(name = "feed_url", nullable = false)
    private String feedUrl;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("feed(category)S")
    private Newspaper newspaper;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public Feed category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public Feed feedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
        return this;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public Newspaper getNewspaper() {
        return newspaper;
    }

    public Feed newspaper(Newspaper newspaper) {
        this.newspaper = newspaper;
        return this;
    }

    public void setNewspaper(Newspaper newspaper) {
        this.newspaper = newspaper;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Feed feed = (Feed) o;
        if (feed.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), feed.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Feed{" +
            "id=" + getId() +
            ", category='" + getCategory() + "'" +
            ", feedUrl='" + getFeedUrl() + "'" +
            "}";
    }
}

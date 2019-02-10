package com.talkingnews.backend.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Newspaper.
 */
@Entity
@Table(name = "newspaper")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Newspaper implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "pattern", nullable = false)
    private String pattern;

    @OneToMany(mappedBy = "newspaper")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Feed> feeds = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Newspaper name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPattern() {
        return pattern;
    }

    public Newspaper pattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Set<Feed> getFeeds() {
        return feeds;
    }

    public Newspaper feeds(Set<Feed> feeds) {
        this.feeds = feeds;
        return this;
    }

    public Newspaper addFeed(Feed feed) {
        this.feeds.add(feed);
        feed.setNewspaper(this);
        return this;
    }

    public Newspaper removeFeed(Feed feed) {
        this.feeds.remove(feed);
        feed.setNewspaper(null);
        return this;
    }

    public void setFeeds(Set<Feed> feeds) {
        this.feeds = feeds;
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
        Newspaper newspaper = (Newspaper) o;
        if (newspaper.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), newspaper.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Newspaper{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", pattern='" + getPattern() + "'" +
            "}";
    }
}

package com.talkingnews.backend.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A NewsPaper.
 */
@Entity
@Table(name = "news_paper")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class NewsPaper implements Serializable {

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

    @OneToMany(mappedBy = "newsPaper")
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

    public NewsPaper name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPattern() {
        return pattern;
    }

    public NewsPaper pattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Set<Feed> getFeeds() {
        return feeds;
    }

    public NewsPaper feeds(Set<Feed> feeds) {
        this.feeds = feeds;
        return this;
    }

    public NewsPaper addFeed(Feed feed) {
        this.feeds.add(feed);
        feed.setNewsPaper(this);
        return this;
    }

    public NewsPaper removeFeed(Feed feed) {
        this.feeds.remove(feed);
        feed.setNewsPaper(null);
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
        NewsPaper newsPaper = (NewsPaper) o;
        if (newsPaper.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), newsPaper.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NewsPaper{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", pattern='" + getPattern() + "'" +
            "}";
    }
}

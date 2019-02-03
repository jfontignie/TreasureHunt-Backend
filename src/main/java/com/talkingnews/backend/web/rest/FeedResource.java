package com.talkingnews.backend.web.rest;
import com.talkingnews.backend.domain.Feed;
import com.talkingnews.backend.repository.FeedRepository;
import com.talkingnews.backend.web.rest.errors.BadRequestAlertException;
import com.talkingnews.backend.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Feed.
 */
@RestController
@RequestMapping("/api")
public class FeedResource {

    private final Logger log = LoggerFactory.getLogger(FeedResource.class);

    private static final String ENTITY_NAME = "feed";

    private final FeedRepository feedRepository;

    public FeedResource(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    /**
     * POST  /feeds : Create a new feed.
     *
     * @param feed the feed to create
     * @return the ResponseEntity with status 201 (Created) and with body the new feed, or with status 400 (Bad Request) if the feed has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/feeds")
    public ResponseEntity<Feed> createFeed(@Valid @RequestBody Feed feed) throws URISyntaxException {
        log.debug("REST request to save Feed : {}", feed);
        if (feed.getId() != null) {
            throw new BadRequestAlertException("A new feed cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Feed result = feedRepository.save(feed);
        return ResponseEntity.created(new URI("/api/feeds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /feeds : Updates an existing feed.
     *
     * @param feed the feed to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated feed,
     * or with status 400 (Bad Request) if the feed is not valid,
     * or with status 500 (Internal Server Error) if the feed couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/feeds")
    public ResponseEntity<Feed> updateFeed(@Valid @RequestBody Feed feed) throws URISyntaxException {
        log.debug("REST request to update Feed : {}", feed);
        if (feed.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Feed result = feedRepository.save(feed);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, feed.getId().toString()))
            .body(result);
    }

    /**
     * GET  /feeds : get all the feeds.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of feeds in body
     */
    @GetMapping("/feeds")
    public List<Feed> getAllFeeds() {
        log.debug("REST request to get all Feeds");
        return feedRepository.findAll();
    }

    /**
     * GET  /feeds/:id : get the "id" feed.
     *
     * @param id the id of the feed to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the feed, or with status 404 (Not Found)
     */
    @GetMapping("/feeds/{id}")
    public ResponseEntity<Feed> getFeed(@PathVariable Long id) {
        log.debug("REST request to get Feed : {}", id);
        Optional<Feed> feed = feedRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(feed);
    }

    /**
     * DELETE  /feeds/:id : delete the "id" feed.
     *
     * @param id the id of the feed to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/feeds/{id}")
    public ResponseEntity<Void> deleteFeed(@PathVariable Long id) {
        log.debug("REST request to delete Feed : {}", id);
        feedRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

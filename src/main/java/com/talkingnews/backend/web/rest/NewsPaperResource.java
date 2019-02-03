package com.talkingnews.backend.web.rest;
import com.talkingnews.backend.domain.NewsPaper;
import com.talkingnews.backend.service.NewsPaperService;
import com.talkingnews.backend.web.rest.errors.BadRequestAlertException;
import com.talkingnews.backend.web.rest.util.HeaderUtil;
import com.talkingnews.backend.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing NewsPaper.
 */
@RestController
@RequestMapping("/api")
public class NewsPaperResource {

    private final Logger log = LoggerFactory.getLogger(NewsPaperResource.class);

    private static final String ENTITY_NAME = "newsPaper";

    private final NewsPaperService newsPaperService;

    public NewsPaperResource(NewsPaperService newsPaperService) {
        this.newsPaperService = newsPaperService;
    }

    /**
     * POST  /news-papers : Create a new newsPaper.
     *
     * @param newsPaper the newsPaper to create
     * @return the ResponseEntity with status 201 (Created) and with body the new newsPaper, or with status 400 (Bad Request) if the newsPaper has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/news-papers")
    public ResponseEntity<NewsPaper> createNewsPaper(@Valid @RequestBody NewsPaper newsPaper) throws URISyntaxException {
        log.debug("REST request to save NewsPaper : {}", newsPaper);
        if (newsPaper.getId() != null) {
            throw new BadRequestAlertException("A new newsPaper cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NewsPaper result = newsPaperService.save(newsPaper);
        return ResponseEntity.created(new URI("/api/news-papers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /news-papers : Updates an existing newsPaper.
     *
     * @param newsPaper the newsPaper to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated newsPaper,
     * or with status 400 (Bad Request) if the newsPaper is not valid,
     * or with status 500 (Internal Server Error) if the newsPaper couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/news-papers")
    public ResponseEntity<NewsPaper> updateNewsPaper(@Valid @RequestBody NewsPaper newsPaper) throws URISyntaxException {
        log.debug("REST request to update NewsPaper : {}", newsPaper);
        if (newsPaper.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NewsPaper result = newsPaperService.save(newsPaper);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, newsPaper.getId().toString()))
            .body(result);
    }

    /**
     * GET  /news-papers : get all the newsPapers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of newsPapers in body
     */
    @GetMapping("/news-papers")
    public ResponseEntity<List<NewsPaper>> getAllNewsPapers(Pageable pageable) {
        log.debug("REST request to get a page of NewsPapers");
        Page<NewsPaper> page = newsPaperService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/news-papers");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /news-papers/:id : get the "id" newsPaper.
     *
     * @param id the id of the newsPaper to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the newsPaper, or with status 404 (Not Found)
     */
    @GetMapping("/news-papers/{id}")
    public ResponseEntity<NewsPaper> getNewsPaper(@PathVariable Long id) {
        log.debug("REST request to get NewsPaper : {}", id);
        Optional<NewsPaper> newsPaper = newsPaperService.findOne(id);
        return ResponseUtil.wrapOrNotFound(newsPaper);
    }

    /**
     * DELETE  /news-papers/:id : delete the "id" newsPaper.
     *
     * @param id the id of the newsPaper to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/news-papers/{id}")
    public ResponseEntity<Void> deleteNewsPaper(@PathVariable Long id) {
        log.debug("REST request to delete NewsPaper : {}", id);
        newsPaperService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

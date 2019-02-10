package com.talkingnews.backend.web.rest;
import com.talkingnews.backend.domain.Newspaper;
import com.talkingnews.backend.service.NewspaperService;
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
 * REST controller for managing Newspaper.
 */
@RestController
@RequestMapping("/api")
public class NewspaperResource {

    private final Logger log = LoggerFactory.getLogger(NewspaperResource.class);

    private static final String ENTITY_NAME = "newspaper";

    private final NewspaperService newspaperService;

    public NewspaperResource(NewspaperService newspaperService) {
        this.newspaperService = newspaperService;
    }

    /**
     * POST  /newspapers : Create a new newspaper.
     *
     * @param newspaper the newspaper to create
     * @return the ResponseEntity with status 201 (Created) and with body the new newspaper, or with status 400 (Bad Request) if the newspaper has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/newspapers")
    public ResponseEntity<Newspaper> createNewspaper(@Valid @RequestBody Newspaper newspaper) throws URISyntaxException {
        log.debug("REST request to save Newspaper : {}", newspaper);
        if (newspaper.getId() != null) {
            throw new BadRequestAlertException("A new newspaper cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Newspaper result = newspaperService.save(newspaper);
        return ResponseEntity.created(new URI("/api/newspapers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /newspapers : Updates an existing newspaper.
     *
     * @param newspaper the newspaper to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated newspaper,
     * or with status 400 (Bad Request) if the newspaper is not valid,
     * or with status 500 (Internal Server Error) if the newspaper couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/newspapers")
    public ResponseEntity<Newspaper> updateNewspaper(@Valid @RequestBody Newspaper newspaper) throws URISyntaxException {
        log.debug("REST request to update Newspaper : {}", newspaper);
        if (newspaper.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Newspaper result = newspaperService.save(newspaper);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, newspaper.getId().toString()))
            .body(result);
    }

    /**
     * GET  /newspapers : get all the newspapers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of newspapers in body
     */
    @GetMapping("/newspapers")
    public ResponseEntity<List<Newspaper>> getAllNewspapers(Pageable pageable) {
        log.debug("REST request to get a page of Newspapers");
        Page<Newspaper> page = newspaperService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/newspapers");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /newspapers/:id : get the "id" newspaper.
     *
     * @param id the id of the newspaper to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the newspaper, or with status 404 (Not Found)
     */
    @GetMapping("/newspapers/{id}")
    public ResponseEntity<Newspaper> getNewspaper(@PathVariable Long id) {
        log.debug("REST request to get Newspaper : {}", id);
        Optional<Newspaper> newspaper = newspaperService.findOne(id);
        return ResponseUtil.wrapOrNotFound(newspaper);
    }

    /**
     * DELETE  /newspapers/:id : delete the "id" newspaper.
     *
     * @param id the id of the newspaper to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/newspapers/{id}")
    public ResponseEntity<Void> deleteNewspaper(@PathVariable Long id) {
        log.debug("REST request to delete Newspaper : {}", id);
        newspaperService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

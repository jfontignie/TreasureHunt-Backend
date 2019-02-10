package com.talkingnews.backend.service;

import com.talkingnews.backend.domain.Newspaper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Newspaper.
 */
public interface NewspaperService {

    /**
     * Save a newspaper.
     *
     * @param newspaper the entity to save
     * @return the persisted entity
     */
    Newspaper save(Newspaper newspaper);

    /**
     * Get all the newspapers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Newspaper> findAll(Pageable pageable);


    /**
     * Get the "id" newspaper.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Newspaper> findOne(Long id);

    /**
     * Delete the "id" newspaper.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

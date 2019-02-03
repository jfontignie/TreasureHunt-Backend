package com.talkingnews.backend.service;

import com.talkingnews.backend.domain.NewsPaper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing NewsPaper.
 */
public interface NewsPaperService {

    /**
     * Save a newsPaper.
     *
     * @param newsPaper the entity to save
     * @return the persisted entity
     */
    NewsPaper save(NewsPaper newsPaper);

    /**
     * Get all the newsPapers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<NewsPaper> findAll(Pageable pageable);


    /**
     * Get the "id" newsPaper.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<NewsPaper> findOne(Long id);

    /**
     * Delete the "id" newsPaper.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

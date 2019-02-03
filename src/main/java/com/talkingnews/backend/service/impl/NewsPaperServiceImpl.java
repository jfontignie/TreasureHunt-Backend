package com.talkingnews.backend.service.impl;

import com.talkingnews.backend.service.NewsPaperService;
import com.talkingnews.backend.domain.NewsPaper;
import com.talkingnews.backend.repository.NewsPaperRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing NewsPaper.
 */
@Service
@Transactional
public class NewsPaperServiceImpl implements NewsPaperService {

    private final Logger log = LoggerFactory.getLogger(NewsPaperServiceImpl.class);

    private final NewsPaperRepository newsPaperRepository;

    public NewsPaperServiceImpl(NewsPaperRepository newsPaperRepository) {
        this.newsPaperRepository = newsPaperRepository;
    }

    /**
     * Save a newsPaper.
     *
     * @param newsPaper the entity to save
     * @return the persisted entity
     */
    @Override
    public NewsPaper save(NewsPaper newsPaper) {
        log.debug("Request to save NewsPaper : {}", newsPaper);
        return newsPaperRepository.save(newsPaper);
    }

    /**
     * Get all the newsPapers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NewsPaper> findAll(Pageable pageable) {
        log.debug("Request to get all NewsPapers");
        return newsPaperRepository.findAll(pageable);
    }


    /**
     * Get one newsPaper by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<NewsPaper> findOne(Long id) {
        log.debug("Request to get NewsPaper : {}", id);
        return newsPaperRepository.findById(id);
    }

    /**
     * Delete the newsPaper by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete NewsPaper : {}", id);        newsPaperRepository.deleteById(id);
    }
}

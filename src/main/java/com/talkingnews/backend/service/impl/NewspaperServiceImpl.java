package com.talkingnews.backend.service.impl;

import com.talkingnews.backend.service.NewspaperService;
import com.talkingnews.backend.domain.Newspaper;
import com.talkingnews.backend.repository.NewspaperRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Newspaper.
 */
@Service
@Transactional
public class NewspaperServiceImpl implements NewspaperService {

    private final Logger log = LoggerFactory.getLogger(NewspaperServiceImpl.class);

    private final NewspaperRepository newspaperRepository;

    public NewspaperServiceImpl(NewspaperRepository newspaperRepository) {
        this.newspaperRepository = newspaperRepository;
    }

    /**
     * Save a newspaper.
     *
     * @param newspaper the entity to save
     * @return the persisted entity
     */
    @Override
    public Newspaper save(Newspaper newspaper) {
        log.debug("Request to save Newspaper : {}", newspaper);
        return newspaperRepository.save(newspaper);
    }

    /**
     * Get all the newspapers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Newspaper> findAll(Pageable pageable) {
        log.debug("Request to get all Newspapers");
        return newspaperRepository.findAll(pageable);
    }


    /**
     * Get one newspaper by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Newspaper> findOne(Long id) {
        log.debug("Request to get Newspaper : {}", id);
        return newspaperRepository.findById(id);
    }

    /**
     * Delete the newspaper by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Newspaper : {}", id);        newspaperRepository.deleteById(id);
    }
}

package com.talkingnews.backend.web.rest;

import com.talkingnews.backend.TalkingNewsBackendApp;

import com.talkingnews.backend.domain.NewsPaper;
import com.talkingnews.backend.repository.NewsPaperRepository;
import com.talkingnews.backend.service.NewsPaperService;
import com.talkingnews.backend.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static com.talkingnews.backend.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the NewsPaperResource REST controller.
 *
 * @see NewsPaperResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalkingNewsBackendApp.class)
public class NewsPaperResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PATTERN = "AAAAAAAAAA";
    private static final String UPDATED_PATTERN = "BBBBBBBBBB";

    @Autowired
    private NewsPaperRepository newsPaperRepository;

    @Autowired
    private NewsPaperService newsPaperService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restNewsPaperMockMvc;

    private NewsPaper newsPaper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NewsPaperResource newsPaperResource = new NewsPaperResource(newsPaperService);
        this.restNewsPaperMockMvc = MockMvcBuilders.standaloneSetup(newsPaperResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NewsPaper createEntity(EntityManager em) {
        NewsPaper newsPaper = new NewsPaper()
            .name(DEFAULT_NAME)
            .pattern(DEFAULT_PATTERN);
        return newsPaper;
    }

    @Before
    public void initTest() {
        newsPaper = createEntity(em);
    }

    @Test
    @Transactional
    public void createNewsPaper() throws Exception {
        int databaseSizeBeforeCreate = newsPaperRepository.findAll().size();

        // Create the NewsPaper
        restNewsPaperMockMvc.perform(post("/api/news-papers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newsPaper)))
            .andExpect(status().isCreated());

        // Validate the NewsPaper in the database
        List<NewsPaper> newsPaperList = newsPaperRepository.findAll();
        assertThat(newsPaperList).hasSize(databaseSizeBeforeCreate + 1);
        NewsPaper testNewsPaper = newsPaperList.get(newsPaperList.size() - 1);
        assertThat(testNewsPaper.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNewsPaper.getPattern()).isEqualTo(DEFAULT_PATTERN);
    }

    @Test
    @Transactional
    public void createNewsPaperWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = newsPaperRepository.findAll().size();

        // Create the NewsPaper with an existing ID
        newsPaper.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNewsPaperMockMvc.perform(post("/api/news-papers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newsPaper)))
            .andExpect(status().isBadRequest());

        // Validate the NewsPaper in the database
        List<NewsPaper> newsPaperList = newsPaperRepository.findAll();
        assertThat(newsPaperList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = newsPaperRepository.findAll().size();
        // set the field null
        newsPaper.setName(null);

        // Create the NewsPaper, which fails.

        restNewsPaperMockMvc.perform(post("/api/news-papers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newsPaper)))
            .andExpect(status().isBadRequest());

        List<NewsPaper> newsPaperList = newsPaperRepository.findAll();
        assertThat(newsPaperList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPatternIsRequired() throws Exception {
        int databaseSizeBeforeTest = newsPaperRepository.findAll().size();
        // set the field null
        newsPaper.setPattern(null);

        // Create the NewsPaper, which fails.

        restNewsPaperMockMvc.perform(post("/api/news-papers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newsPaper)))
            .andExpect(status().isBadRequest());

        List<NewsPaper> newsPaperList = newsPaperRepository.findAll();
        assertThat(newsPaperList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNewsPapers() throws Exception {
        // Initialize the database
        newsPaperRepository.saveAndFlush(newsPaper);

        // Get all the newsPaperList
        restNewsPaperMockMvc.perform(get("/api/news-papers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(newsPaper.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].pattern").value(hasItem(DEFAULT_PATTERN.toString())));
    }
    
    @Test
    @Transactional
    public void getNewsPaper() throws Exception {
        // Initialize the database
        newsPaperRepository.saveAndFlush(newsPaper);

        // Get the newsPaper
        restNewsPaperMockMvc.perform(get("/api/news-papers/{id}", newsPaper.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(newsPaper.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.pattern").value(DEFAULT_PATTERN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNewsPaper() throws Exception {
        // Get the newsPaper
        restNewsPaperMockMvc.perform(get("/api/news-papers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNewsPaper() throws Exception {
        // Initialize the database
        newsPaperService.save(newsPaper);

        int databaseSizeBeforeUpdate = newsPaperRepository.findAll().size();

        // Update the newsPaper
        NewsPaper updatedNewsPaper = newsPaperRepository.findById(newsPaper.getId()).get();
        // Disconnect from session so that the updates on updatedNewsPaper are not directly saved in db
        em.detach(updatedNewsPaper);
        updatedNewsPaper
            .name(UPDATED_NAME)
            .pattern(UPDATED_PATTERN);

        restNewsPaperMockMvc.perform(put("/api/news-papers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNewsPaper)))
            .andExpect(status().isOk());

        // Validate the NewsPaper in the database
        List<NewsPaper> newsPaperList = newsPaperRepository.findAll();
        assertThat(newsPaperList).hasSize(databaseSizeBeforeUpdate);
        NewsPaper testNewsPaper = newsPaperList.get(newsPaperList.size() - 1);
        assertThat(testNewsPaper.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNewsPaper.getPattern()).isEqualTo(UPDATED_PATTERN);
    }

    @Test
    @Transactional
    public void updateNonExistingNewsPaper() throws Exception {
        int databaseSizeBeforeUpdate = newsPaperRepository.findAll().size();

        // Create the NewsPaper

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNewsPaperMockMvc.perform(put("/api/news-papers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newsPaper)))
            .andExpect(status().isBadRequest());

        // Validate the NewsPaper in the database
        List<NewsPaper> newsPaperList = newsPaperRepository.findAll();
        assertThat(newsPaperList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNewsPaper() throws Exception {
        // Initialize the database
        newsPaperService.save(newsPaper);

        int databaseSizeBeforeDelete = newsPaperRepository.findAll().size();

        // Delete the newsPaper
        restNewsPaperMockMvc.perform(delete("/api/news-papers/{id}", newsPaper.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<NewsPaper> newsPaperList = newsPaperRepository.findAll();
        assertThat(newsPaperList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NewsPaper.class);
        NewsPaper newsPaper1 = new NewsPaper();
        newsPaper1.setId(1L);
        NewsPaper newsPaper2 = new NewsPaper();
        newsPaper2.setId(newsPaper1.getId());
        assertThat(newsPaper1).isEqualTo(newsPaper2);
        newsPaper2.setId(2L);
        assertThat(newsPaper1).isNotEqualTo(newsPaper2);
        newsPaper1.setId(null);
        assertThat(newsPaper1).isNotEqualTo(newsPaper2);
    }
}

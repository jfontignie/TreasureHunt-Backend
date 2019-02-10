package com.talkingnews.backend.web.rest;

import com.talkingnews.backend.TalkingNewsBackendApp;

import com.talkingnews.backend.domain.Newspaper;
import com.talkingnews.backend.repository.NewspaperRepository;
import com.talkingnews.backend.service.NewspaperService;
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
 * Test class for the NewspaperResource REST controller.
 *
 * @see NewspaperResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalkingNewsBackendApp.class)
public class NewspaperResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PATTERN = "AAAAAAAAAA";
    private static final String UPDATED_PATTERN = "BBBBBBBBBB";

    @Autowired
    private NewspaperRepository newspaperRepository;

    @Autowired
    private NewspaperService newspaperService;

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

    private MockMvc restNewspaperMockMvc;

    private Newspaper newspaper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NewspaperResource newspaperResource = new NewspaperResource(newspaperService);
        this.restNewspaperMockMvc = MockMvcBuilders.standaloneSetup(newspaperResource)
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
    public static Newspaper createEntity(EntityManager em) {
        Newspaper newspaper = new Newspaper()
            .name(DEFAULT_NAME)
            .pattern(DEFAULT_PATTERN);
        return newspaper;
    }

    @Before
    public void initTest() {
        newspaper = createEntity(em);
    }

    @Test
    @Transactional
    public void createNewspaper() throws Exception {
        int databaseSizeBeforeCreate = newspaperRepository.findAll().size();

        // Create the Newspaper
        restNewspaperMockMvc.perform(post("/api/newspapers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newspaper)))
            .andExpect(status().isCreated());

        // Validate the Newspaper in the database
        List<Newspaper> newspaperList = newspaperRepository.findAll();
        assertThat(newspaperList).hasSize(databaseSizeBeforeCreate + 1);
        Newspaper testNewspaper = newspaperList.get(newspaperList.size() - 1);
        assertThat(testNewspaper.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNewspaper.getPattern()).isEqualTo(DEFAULT_PATTERN);
    }

    @Test
    @Transactional
    public void createNewspaperWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = newspaperRepository.findAll().size();

        // Create the Newspaper with an existing ID
        newspaper.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNewspaperMockMvc.perform(post("/api/newspapers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newspaper)))
            .andExpect(status().isBadRequest());

        // Validate the Newspaper in the database
        List<Newspaper> newspaperList = newspaperRepository.findAll();
        assertThat(newspaperList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = newspaperRepository.findAll().size();
        // set the field null
        newspaper.setName(null);

        // Create the Newspaper, which fails.

        restNewspaperMockMvc.perform(post("/api/newspapers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newspaper)))
            .andExpect(status().isBadRequest());

        List<Newspaper> newspaperList = newspaperRepository.findAll();
        assertThat(newspaperList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPatternIsRequired() throws Exception {
        int databaseSizeBeforeTest = newspaperRepository.findAll().size();
        // set the field null
        newspaper.setPattern(null);

        // Create the Newspaper, which fails.

        restNewspaperMockMvc.perform(post("/api/newspapers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newspaper)))
            .andExpect(status().isBadRequest());

        List<Newspaper> newspaperList = newspaperRepository.findAll();
        assertThat(newspaperList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNewspapers() throws Exception {
        // Initialize the database
        newspaperRepository.saveAndFlush(newspaper);

        // Get all the newspaperList
        restNewspaperMockMvc.perform(get("/api/newspapers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(newspaper.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].pattern").value(hasItem(DEFAULT_PATTERN.toString())));
    }
    
    @Test
    @Transactional
    public void getNewspaper() throws Exception {
        // Initialize the database
        newspaperRepository.saveAndFlush(newspaper);

        // Get the newspaper
        restNewspaperMockMvc.perform(get("/api/newspapers/{id}", newspaper.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(newspaper.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.pattern").value(DEFAULT_PATTERN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNewspaper() throws Exception {
        // Get the newspaper
        restNewspaperMockMvc.perform(get("/api/newspapers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNewspaper() throws Exception {
        // Initialize the database
        newspaperService.save(newspaper);

        int databaseSizeBeforeUpdate = newspaperRepository.findAll().size();

        // Update the newspaper
        Newspaper updatedNewspaper = newspaperRepository.findById(newspaper.getId()).get();
        // Disconnect from session so that the updates on updatedNewspaper are not directly saved in db
        em.detach(updatedNewspaper);
        updatedNewspaper
            .name(UPDATED_NAME)
            .pattern(UPDATED_PATTERN);

        restNewspaperMockMvc.perform(put("/api/newspapers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNewspaper)))
            .andExpect(status().isOk());

        // Validate the Newspaper in the database
        List<Newspaper> newspaperList = newspaperRepository.findAll();
        assertThat(newspaperList).hasSize(databaseSizeBeforeUpdate);
        Newspaper testNewspaper = newspaperList.get(newspaperList.size() - 1);
        assertThat(testNewspaper.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNewspaper.getPattern()).isEqualTo(UPDATED_PATTERN);
    }

    @Test
    @Transactional
    public void updateNonExistingNewspaper() throws Exception {
        int databaseSizeBeforeUpdate = newspaperRepository.findAll().size();

        // Create the Newspaper

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNewspaperMockMvc.perform(put("/api/newspapers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newspaper)))
            .andExpect(status().isBadRequest());

        // Validate the Newspaper in the database
        List<Newspaper> newspaperList = newspaperRepository.findAll();
        assertThat(newspaperList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNewspaper() throws Exception {
        // Initialize the database
        newspaperService.save(newspaper);

        int databaseSizeBeforeDelete = newspaperRepository.findAll().size();

        // Delete the newspaper
        restNewspaperMockMvc.perform(delete("/api/newspapers/{id}", newspaper.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Newspaper> newspaperList = newspaperRepository.findAll();
        assertThat(newspaperList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Newspaper.class);
        Newspaper newspaper1 = new Newspaper();
        newspaper1.setId(1L);
        Newspaper newspaper2 = new Newspaper();
        newspaper2.setId(newspaper1.getId());
        assertThat(newspaper1).isEqualTo(newspaper2);
        newspaper2.setId(2L);
        assertThat(newspaper1).isNotEqualTo(newspaper2);
        newspaper1.setId(null);
        assertThat(newspaper1).isNotEqualTo(newspaper2);
    }
}

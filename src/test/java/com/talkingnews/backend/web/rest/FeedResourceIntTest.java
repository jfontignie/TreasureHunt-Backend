package com.talkingnews.backend.web.rest;

import com.talkingnews.backend.TalkingNewsBackendApp;

import com.talkingnews.backend.domain.Feed;
import com.talkingnews.backend.domain.Newspaper;
import com.talkingnews.backend.repository.FeedRepository;
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
 * Test class for the FeedResource REST controller.
 *
 * @see FeedResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TalkingNewsBackendApp.class)
public class FeedResourceIntTest {

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_FEED_URL = "AAAAAAAAAA";
    private static final String UPDATED_FEED_URL = "BBBBBBBBBB";

    @Autowired
    private FeedRepository feedRepository;

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

    private MockMvc restFeedMockMvc;

    private Feed feed;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FeedResource feedResource = new FeedResource(feedRepository);
        this.restFeedMockMvc = MockMvcBuilders.standaloneSetup(feedResource)
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
    public static Feed createEntity(EntityManager em) {
        Feed feed = new Feed()
            .category(DEFAULT_CATEGORY)
            .feedUrl(DEFAULT_FEED_URL);
        // Add required entity
        Newspaper newspaper = NewspaperResourceIntTest.createEntity(em);
        em.persist(newspaper);
        em.flush();
        feed.setNewspaper(newspaper);
        return feed;
    }

    @Before
    public void initTest() {
        feed = createEntity(em);
    }

    @Test
    @Transactional
    public void createFeed() throws Exception {
        int databaseSizeBeforeCreate = feedRepository.findAll().size();

        // Create the Feed
        restFeedMockMvc.perform(post("/api/feeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feed)))
            .andExpect(status().isCreated());

        // Validate the Feed in the database
        List<Feed> feedList = feedRepository.findAll();
        assertThat(feedList).hasSize(databaseSizeBeforeCreate + 1);
        Feed testFeed = feedList.get(feedList.size() - 1);
        assertThat(testFeed.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testFeed.getFeedUrl()).isEqualTo(DEFAULT_FEED_URL);
    }

    @Test
    @Transactional
    public void createFeedWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = feedRepository.findAll().size();

        // Create the Feed with an existing ID
        feed.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeedMockMvc.perform(post("/api/feeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feed)))
            .andExpect(status().isBadRequest());

        // Validate the Feed in the database
        List<Feed> feedList = feedRepository.findAll();
        assertThat(feedList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedRepository.findAll().size();
        // set the field null
        feed.setCategory(null);

        // Create the Feed, which fails.

        restFeedMockMvc.perform(post("/api/feeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feed)))
            .andExpect(status().isBadRequest());

        List<Feed> feedList = feedRepository.findAll();
        assertThat(feedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFeedUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedRepository.findAll().size();
        // set the field null
        feed.setFeedUrl(null);

        // Create the Feed, which fails.

        restFeedMockMvc.perform(post("/api/feeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feed)))
            .andExpect(status().isBadRequest());

        List<Feed> feedList = feedRepository.findAll();
        assertThat(feedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFeeds() throws Exception {
        // Initialize the database
        feedRepository.saveAndFlush(feed);

        // Get all the feedList
        restFeedMockMvc.perform(get("/api/feeds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feed.getId().intValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].feedUrl").value(hasItem(DEFAULT_FEED_URL.toString())));
    }
    
    @Test
    @Transactional
    public void getFeed() throws Exception {
        // Initialize the database
        feedRepository.saveAndFlush(feed);

        // Get the feed
        restFeedMockMvc.perform(get("/api/feeds/{id}", feed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(feed.getId().intValue()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.feedUrl").value(DEFAULT_FEED_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFeed() throws Exception {
        // Get the feed
        restFeedMockMvc.perform(get("/api/feeds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFeed() throws Exception {
        // Initialize the database
        feedRepository.saveAndFlush(feed);

        int databaseSizeBeforeUpdate = feedRepository.findAll().size();

        // Update the feed
        Feed updatedFeed = feedRepository.findById(feed.getId()).get();
        // Disconnect from session so that the updates on updatedFeed are not directly saved in db
        em.detach(updatedFeed);
        updatedFeed
            .category(UPDATED_CATEGORY)
            .feedUrl(UPDATED_FEED_URL);

        restFeedMockMvc.perform(put("/api/feeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFeed)))
            .andExpect(status().isOk());

        // Validate the Feed in the database
        List<Feed> feedList = feedRepository.findAll();
        assertThat(feedList).hasSize(databaseSizeBeforeUpdate);
        Feed testFeed = feedList.get(feedList.size() - 1);
        assertThat(testFeed.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testFeed.getFeedUrl()).isEqualTo(UPDATED_FEED_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingFeed() throws Exception {
        int databaseSizeBeforeUpdate = feedRepository.findAll().size();

        // Create the Feed

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedMockMvc.perform(put("/api/feeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feed)))
            .andExpect(status().isBadRequest());

        // Validate the Feed in the database
        List<Feed> feedList = feedRepository.findAll();
        assertThat(feedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFeed() throws Exception {
        // Initialize the database
        feedRepository.saveAndFlush(feed);

        int databaseSizeBeforeDelete = feedRepository.findAll().size();

        // Delete the feed
        restFeedMockMvc.perform(delete("/api/feeds/{id}", feed.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Feed> feedList = feedRepository.findAll();
        assertThat(feedList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Feed.class);
        Feed feed1 = new Feed();
        feed1.setId(1L);
        Feed feed2 = new Feed();
        feed2.setId(feed1.getId());
        assertThat(feed1).isEqualTo(feed2);
        feed2.setId(2L);
        assertThat(feed1).isNotEqualTo(feed2);
        feed1.setId(null);
        assertThat(feed1).isNotEqualTo(feed2);
    }
}

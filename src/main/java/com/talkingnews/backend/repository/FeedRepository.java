package com.talkingnews.backend.repository;

import com.talkingnews.backend.domain.Feed;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Feed entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {

}

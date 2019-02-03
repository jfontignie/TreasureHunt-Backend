package com.talkingnews.backend.repository;

import com.talkingnews.backend.domain.NewsPaper;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the NewsPaper entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NewsPaperRepository extends JpaRepository<NewsPaper, Long> {

}

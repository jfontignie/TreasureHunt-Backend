package com.talkingnews.backend.repository;

import com.talkingnews.backend.domain.Newspaper;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Newspaper entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NewspaperRepository extends JpaRepository<Newspaper, Long> {

}

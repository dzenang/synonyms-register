package com.dzenang.synonymsregister.repository;

import com.dzenang.synonymsregister.model.Meaning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeaningRepository extends JpaRepository<Meaning, Long> {
}

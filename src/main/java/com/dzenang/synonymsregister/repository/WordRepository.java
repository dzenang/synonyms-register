package com.dzenang.synonymsregister.repository;

import com.dzenang.synonymsregister.dto.WordDTO;
import com.dzenang.synonymsregister.model.Meaning;
import com.dzenang.synonymsregister.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    Optional<Word> findByWord(String word);

    @Query(value = "select new com.dzenang.synonymsregister.dto.WordDTO(w.word) from Word w " +
            "where w.meaning = (select w1.meaning from Word w1 where w1.word = :value and w1.meaning is not null) and w.word <> :value")
    List<WordDTO> findSynonyms(String value);

    Long countWordByMeaning(Meaning meaning);
}


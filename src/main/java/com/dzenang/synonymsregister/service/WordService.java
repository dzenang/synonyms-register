package com.dzenang.synonymsregister.service;

import com.dzenang.synonymsregister.dto.WordDTO;
import com.dzenang.synonymsregister.model.Meaning;
import com.dzenang.synonymsregister.model.Word;
import com.dzenang.synonymsregister.repository.MeaningRepository;
import com.dzenang.synonymsregister.repository.WordRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class WordService {

    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private MeaningRepository meaningRepository;
    private static final String notFoundMessage = "Word '%s' does not exist!";

    /**
     * Retrieving word from db.
     * @param value of the word object wanted
     * @return WordDTO object
     */
    public WordDTO retrieveWord(String value) {
        Word word = wordRepository.findByWord(value)
                .orElseThrow(() -> new EntityNotFoundException(String.format(notFoundMessage, value)));
        return new WordDTO(word.getWord());
    }

    /**
     * Retrieving synonyms for specified word.
     * @param value word for synonyms
     * @return list of synonyms (can be empty)
     */
    public List<WordDTO> retrieveSynonyms(String value) {
        return wordRepository.findSynonyms(value);
    }

    /**
     * Creating new word in the db.
     * @param wordDTO word object
     * @return saved word
     */
    public WordDTO createWord(WordDTO wordDTO) {
        wordRepository.findByWord(wordDTO.getValue())
                .orElseGet(() -> saveWord(wordDTO));
        return wordDTO;
    }

    /**
     * Updating existing word with new value.
     * @param oldValue old string value of the word
     * @param wordDTO new word object
     * @return updated word
     */
    public WordDTO updateWord(String oldValue, WordDTO wordDTO) {
        Word word = wordRepository.findByWord(oldValue)
                .orElseThrow(() -> new EntityNotFoundException(String.format(notFoundMessage, oldValue)));
        word.setWord(wordDTO.getValue());
        wordRepository.saveAndFlush(word);
        return wordDTO;
    }

    /**
     * Assigning a synonym to the existing word.
     * @param existingWordValue word string value
     * @param synonymDTO synonym object (if not exist in db, will be created)
     * @return assigned synonym
     */
    @Transactional
    public WordDTO assignSynonym(String existingWordValue, WordDTO synonymDTO) {
        Word word = wordRepository.findByWord(existingWordValue)
                .orElseThrow(() -> new EntityNotFoundException(String.format(notFoundMessage, existingWordValue)));

        Word synonym = // checking if synonym exists as word, saving if not
                wordRepository.findByWord(synonymDTO.getValue())
                        .orElseGet(() -> saveWord(synonymDTO));

        Meaning usedMeaning = Optional.ofNullable(word.getMeaning())
                .orElseGet(() -> {
                    if(synonym.getMeaning() == null) {
                        return new Meaning();
                    } else {
                        return synonym.getMeaning();
                    }
                });
        // handling scenario when word and synonym both have meaning, removing synonym's meaning from db
        if (word.getMeaning() != null) {
            removeMeaningIfNotNeeded(synonym.getMeaning());
        }
        word.setMeaning(usedMeaning);
        synonym.setMeaning(usedMeaning);
        meaningRepository.save(usedMeaning);
        wordRepository.save(synonym);
        wordRepository.saveAndFlush(word);
        return synonymDTO;
    }

    /**
     * Removing relation between word and synonym.
     * @param existingWordValue word string value
     * @param synonymDTO unattached synonym
     */
    public void deassignSynonym(String existingWordValue, WordDTO synonymDTO) {
        Word word = wordRepository.findByWord(existingWordValue)
                .orElseThrow(() -> new EntityNotFoundException(String.format(notFoundMessage, existingWordValue)));
        Word synonym = wordRepository.findByWord(synonymDTO.getValue())
                .orElseThrow(() -> new EntityNotFoundException(String.format(notFoundMessage, synonymDTO.getValue())));
        if(word.getMeaning() == synonym.getMeaning()) {
            synonym.setMeaning(null);
            wordRepository.saveAndFlush(synonym);
        } else {
            throw new UnsupportedOperationException(
                    String.format("Synonym '%s' is not assigned to word '%s'", synonym.getWord(), word.getWord()));
        }
    }

    /**
     * Deleting a word from db.
     * @param value word string to be deleted
     */
    public void deleteWord(String value) {
        Word word = wordRepository.findByWord(value)
                .orElseThrow(() -> new EntityNotFoundException(String.format(notFoundMessage, value)));
        wordRepository.delete(word);
    }

    private Word saveWord(WordDTO wordDTO) {
        Word w = new Word();
        w.setWord(wordDTO.getValue());
        return wordRepository.save(w);
    }

    private void removeMeaningIfNotNeeded(Meaning meaning) {
        if(wordRepository.countWordByMeaning(meaning) == 1) {
            meaningRepository.delete(meaning);
        }
    }
}

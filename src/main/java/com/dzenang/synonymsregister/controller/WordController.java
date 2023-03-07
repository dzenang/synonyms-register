package com.dzenang.synonymsregister.controller;

import com.dzenang.synonymsregister.dto.WordDTO;
import com.dzenang.synonymsregister.service.WordService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Word controller for operations with words and their synonyms
 * GET operations are allowed to roles 'writer' and 'reader'.
 * All "modify" operations are only allowed to role 'writer'.
 */
@RestController
@RequestMapping("/v1/words")
@PreAuthorize("hasAuthority('ROLE_writer')")
public class WordController {

    @Autowired
    private WordService wordService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_writer', 'ROLE_reader')")
    @Operation(summary = "Fetching existing word")
    public ResponseEntity<WordDTO> fetchWord(@RequestParam String value) {
        return new ResponseEntity<>(wordService.retrieveWord(value), HttpStatus.OK);
    }

    @GetMapping("/{wordValue}/synonyms")
    @PreAuthorize("hasAnyAuthority('ROLE_writer', 'ROLE_reader')")
    @Operation(summary = "Fetching synonyms for existing word")
    public ResponseEntity<List<WordDTO>> fetchSynonyms(@PathVariable String wordValue) {
        return new ResponseEntity<>(wordService.retrieveSynonyms(wordValue), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Creating new word")
    public ResponseEntity<WordDTO> addWord(@RequestBody WordDTO word) {
        return new ResponseEntity<>(wordService.createWord(word), HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(summary = "Editing existing word")
    public ResponseEntity<WordDTO> editWord(@RequestParam String oldValue, @RequestBody WordDTO newWord) {
        return new ResponseEntity<>(wordService.updateWord(oldValue, newWord), HttpStatus.OK);
    }

    @PutMapping("/{wordValue}/synonym")
    @Operation(summary = "Assigning synonym to the existing word")
    public ResponseEntity<WordDTO> assignSynonym(@PathVariable String wordValue, @RequestBody WordDTO synonym) {
        return new ResponseEntity<>(wordService.assignSynonym(wordValue, synonym), HttpStatus.OK);
    }

    @DeleteMapping("/{wordValue}/synonym")
    @Operation(summary = "Deassigning synonym of the existing word")
    public ResponseEntity<Void> deassignSynonym(@PathVariable String wordValue, @RequestBody WordDTO synonym) {
        wordService.deassignSynonym(wordValue, synonym);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Operation(summary = "Deleting existing word")
    public ResponseEntity<Void> deleteWord(@RequestParam String value) {
        wordService.deleteWord(value);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

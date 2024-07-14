package jpa.spring.controller;

import org.springframework.web.bind.annotation.RestController;

import jpa.spring.service.GennerService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import jpa.spring.core.ResponseObject;
import jpa.spring.model.dto.GenreDetailDTO;
import jpa.spring.model.entities.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {

    @Autowired
    private final GennerService gennerService;

    @GetMapping("/getAll")
    public ResponseEntity<ResponseObject<GenreDetailDTO>> getAll() {
        ResponseObject<GenreDetailDTO> result = new ResponseObject<>();
        List<GenreDetailDTO> genres = gennerService.getAllGenres();
        result.setMessage("get all genre succsefully");
        result.setDaList(genres);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<Genre>> getOneGenre(@PathVariable Long id) {
        ResponseObject<Genre> result = new ResponseObject<>();
        Optional<Genre> genre = gennerService.getGenreById(id);
        result.setMessage("get one genre succsefully");
        result.setData(genre.get());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/addGenre")
    public ResponseEntity<ResponseObject<GenreDetailDTO>> createGenre(@RequestBody Genre genreDTO) {
        ResponseObject<GenreDetailDTO> result = new ResponseObject<>();
        GenreDetailDTO resutlGenre = gennerService.createGenre(genreDTO);
        result.setMessage("get one genre succsefully");
        result.setData(resutlGenre);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject<Genre>> updateGenre(@PathVariable("id") Long id, @RequestBody Genre genre) {
        ResponseObject<Genre> result = new ResponseObject<>();
        Genre resutlGenre = gennerService.updatGenre(id, genre);
        result.setMessage("get one genre succsefully");
        result.setData(resutlGenre);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject<Void>> deleteGenner(@PathVariable("id") Long id) {
        ResponseObject<Void> result = new ResponseObject<>();
        gennerService.deleteGenre(id);
        return new ResponseEntity<ResponseObject<Void>>(result, HttpStatus.OK);
    }

    @GetMapping("/getAllGenres")
    public ResponseEntity<ResponseObject<Genre>> getAllGenres() {
        List<Genre> genres = gennerService.getAllGenresTrue();
        ResponseObject<Genre> result = new ResponseObject<>();
        result.setMessage("Get genres successfully");
        result.setDaList(genres);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}

package jpa.spring.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jpa.spring.core.ResponseObject;
import jpa.spring.model.dto.ActordetailDTO;
import jpa.spring.model.entities.Actor;
import jpa.spring.service.ActorService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/actors")
public class ActorController {

    @Autowired
    private final ActorService actorService;

    @GetMapping("/getAllActor")
    public ResponseEntity<ResponseObject<ActordetailDTO>> getAll() {
        List<ActordetailDTO> actors = actorService.getAllActor();
        ResponseObject<ActordetailDTO> result = new ResponseObject<>();
        result.setMessage("Get profile successfully");
        result.setDaList(actors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<Actor>> getOne(@PathVariable Long id) {
        Optional<Actor> actor = actorService.getbyId(id);
        ResponseObject<Actor> result = new ResponseObject<>();
        result.setMessage("get one actor successfully");
        result.setData(actor.get());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseObject<ActordetailDTO>> addActor(@RequestBody Actor actor) {
        ResponseObject<ActordetailDTO> result = new ResponseObject<>();
        ActordetailDTO savedActor = actorService.createActor(actor);
        result.setMessage("Actor added successfully");
        result.setData(savedActor);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/edit/{actorId}")
    public ResponseEntity<ResponseObject<ActordetailDTO>> update(@PathVariable("actorId") Long actorId, @RequestBody Actor actor) {
        ResponseObject<ActordetailDTO> result = new ResponseObject<>();
        ActordetailDTO newActor = actorService.updateActor(actorId, actor);
        result.setMessage("update actor susscesfully");
        result.setData(newActor);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}") 
    public ResponseEntity<ResponseObject<Void>> deleteActor(@PathVariable("id") Long id) {
        ResponseObject<Void> result = new ResponseObject<>();
        actorService.deleteActor(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/with-movies")
    public List<Actor> getActorsWithMovies() {
        return actorService.getActorsWithMovies();
    }

}

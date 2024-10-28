package jpa.spring.controller;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RestController;

import jpa.spring.core.ErrorObject;
import jpa.spring.core.ResponseObject;
import jpa.spring.model.dto.ActordetailDTO;
import jpa.spring.model.entities.Actor;
import jpa.spring.service.ActorService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ActorController {

    @Autowired
    private final ActorService actorService;

    @GetMapping("/user/actors/getAllActor")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<ResponseObject<ActordetailDTO>> getAll() {

        ResponseObject<ActordetailDTO> result = new ResponseObject<>();
        try {
            List<ActordetailDTO> actors = actorService.getAllActor();
            result.setMessage("Get profile successfully");
            result.setDaList(actors);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            ErrorObject error = new ErrorObject(ErrorObject.ErrorCode.UNKNOW, "Data null");
            result.setError(error);
            result.setMessage("Get all in failed");
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/actors/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseObject<Actor>> getOne(@PathVariable Long id) {
        ResponseObject<Actor> result = new ResponseObject<>();
        try {
            Optional<Actor> actor = actorService.getbyId(id);
            result.setMessage("Get one actor successfully");
            result.setData(actor.orElse(null));
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            ErrorObject error = new ErrorObject(ErrorObject.ErrorCode.UNKNOW, "Data null");
            result.setError(error);
            result.setMessage("Get one in failed");
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/admin/actors/create")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject<ActordetailDTO>> addActor(@RequestBody Actor actor) {
        ResponseObject<ActordetailDTO> result = new ResponseObject<>();
        try {
            ActordetailDTO savedActor = actorService.createActor(actor);
            result.setMessage("Actor added successfully");
            result.setData(savedActor);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            ErrorObject error = new ErrorObject(ErrorObject.ErrorCode.DATA_INVALID,
                    "Unexpected error: " + e.getMessage());
            result.setError(error);
            result.setMessage("Add actor failed");
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/actors/edit/{actorId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject<ActordetailDTO>> update(@PathVariable("actorId") Long actorId,
            @RequestBody Actor actor) {
        ResponseObject<ActordetailDTO> result = new ResponseObject<>();
        try {
            ActordetailDTO newActor = actorService.updateActor(actorId, actor);
            result.setMessage("Update actor successfully");
            result.setData(newActor);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            ErrorObject error = new ErrorObject(ErrorObject.ErrorCode.UNKNOW, "Unexpected error: " + e.getMessage());
            result.setError(error);
            result.setMessage("Get one in failed");
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/admin/actors/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject<Void>> deleteActor(@PathVariable("id") Long id) {

        ResponseObject<Void> result = new ResponseObject<>();
        try {
            actorService.deleteActor(id);
            result.setMessage("Delete actor successfully");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            ErrorObject error = new ErrorObject(ErrorObject.ErrorCode.UNKNOW, "Unexpected error: " + e.getMessage());
            result.setError(error);
            result.setMessage("Delete in failed");
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/user/actors/with-movies")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<ResponseObject<List<Actor>>> getActorsWithMovies() {

        ResponseObject<List<Actor>> result = new ResponseObject<>();
        try {
            List<Actor> actors = actorService.getActorsWithMovies();
            result.setMessage("Get Actors with Movives successfully");
            result.setData(actors);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            ErrorObject error = new ErrorObject(ErrorObject.ErrorCode.UNKNOW, "Unexpected error: " + e.getMessage());
            result.setError(error);
            result.setMessage("Delete in failed");
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
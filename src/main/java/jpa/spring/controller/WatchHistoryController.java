package jpa.spring.controller;

import org.springframework.web.bind.annotation.RestController;

import jpa.spring.core.ResponseObject;
import jpa.spring.model.entities.WatchHistory;
import jpa.spring.service.WatchHistoryService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/watchhistories")
@RequiredArgsConstructor
public class WatchHistoryController {

    @Autowired
    private final WatchHistoryService watchHistoryService;

    @GetMapping("/getall")
    public ResponseEntity<ResponseObject<WatchHistory>> getall(){
        ResponseObject<WatchHistory> result = new ResponseObject<>();
        List<WatchHistory> histories = watchHistoryService.getall();
        result.setDaList(histories);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getby")
    public ResponseEntity<ResponseObject<WatchHistory>> getBy(@PathVariable("historyId") Long historyId ){
        ResponseObject<WatchHistory> result = new ResponseObject<>();
        WatchHistory histories = watchHistoryService.getWatchHistorybyId(historyId);
        result.setMessage("get by success");
        result.setData(histories);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PostMapping("/add/{userId}/{movieId}")
    public ResponseEntity<ResponseObject<WatchHistory>> addWatchHistory(@PathVariable("userId") Long userId,
            @PathVariable("movieId") Long movieId) {
        ResponseObject<WatchHistory> result = new ResponseObject<>();
        WatchHistory watchHistory2 = watchHistoryService.createWatchHistory(userId, movieId);
        result.setMessage("Create WatchHistory succcess ");
        result.setData(watchHistory2);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseObject<WatchHistory>> getWatchHistoryByUser(@PathVariable("userId") Long userId) {
        ResponseObject<WatchHistory> responseObject = new ResponseObject<>();
        List<WatchHistory> watchHistories = watchHistoryService.getWatchHistoryByUser(userId);
        responseObject.setMessage("getWatchHistoryByUser succcess");
        responseObject.setDaList(watchHistories);
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{historyId}")
    public ResponseEntity<String> deleteWatchHistory(@PathVariable Long historyId) {
        watchHistoryService.deleteWatchHistory(historyId);
        return ResponseEntity.ok("Watch history deleted successfully.");
    }
}

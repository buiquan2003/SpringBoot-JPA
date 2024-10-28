package jpa.spring.service;

import org.springframework.stereotype.Service;
import jpa.spring.config.exception.UnknowException;
import jpa.spring.model.entities.*;
import jpa.spring.repository.*;
import lombok.RequiredArgsConstructor;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@RequiredArgsConstructor
public class WatchHistoryService {

    @Autowired
    private final WatchHistoryRepository historyRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final MovieRepository movieRepository;

    public List<WatchHistory> getall() {
        return historyRepository.findAll();
    }

    public WatchHistory getWatchHistorybyId(Long watchHistoryID) {
        Optional<WatchHistory> optional = historyRepository.findById(watchHistoryID);
        if (!optional.isPresent()) {
            throw new UnknowException("watchHistory with ID " + optional + " does not exist.");
        }
        return optional.get();
    }

    public WatchHistory createWatchHistory(Long userId, Long movieId) {
        Optional<User> idUser = userRepository.findById(userId);
        if (!idUser.isPresent()) {
            throw new UnknowException("user with ID " + idUser + " does not exist.");
        }

        Optional<Movie> idMovie = movieRepository.findById(userId);
        if (!idMovie.isPresent()) {
            throw new UnknowException("movie with ID " + idMovie + " does not exist.");
        }
        WatchHistory watchHistory = new WatchHistory();
        watchHistory.setOwner(idUser.get());
        watchHistory.setMovie(idMovie.get());
        watchHistory.setDelFlag(false);
        watchHistory.setCreatedAt(ZonedDateTime.now());
        return historyRepository.save(watchHistory);
    }

    public List<WatchHistory> getWatchHistoryByUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new UnknowException("User with ID " + userId + " does not exist.");
        }

        User user = userOpt.get();
        return historyRepository.findByOwnerAndDelFlagFalse(user);
    }

    public void deleteWatchHistory(Long historyId) {
        Optional<WatchHistory> watchHistoryOpt = historyRepository.findById(historyId);
        if (!watchHistoryOpt.isPresent()) {
            throw new UnknowException("WatchHistory with ID " + historyId + " does not exist.");
        }

        WatchHistory watchHistory = watchHistoryOpt.get();
        watchHistory.setDelFlag(true);
        historyRepository.save(watchHistory);
    }

}

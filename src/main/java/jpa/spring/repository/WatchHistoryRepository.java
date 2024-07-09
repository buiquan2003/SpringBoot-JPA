package jpa.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import jpa.spring.model.entities.User;
import jpa.spring.model.entities.WatchHistory;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {
    //sử dụng để tải trước thông tin các muốn quan hệ
    @EntityGraph(attributePaths = {"movie"})
    List<WatchHistory> findByOwnerAndDelFlagFalse(User user);

    
} 
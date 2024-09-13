package com.example.bloombackend.donelist.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bloombackend.donelist.entity.DoneList;

public interface DoneListRepository extends JpaRepository<DoneList, Long> {
	List<DoneList> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
}

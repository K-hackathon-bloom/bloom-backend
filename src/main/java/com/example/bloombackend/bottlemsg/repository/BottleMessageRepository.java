package com.example.bloombackend.bottlemsg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bloombackend.bottlemsg.entity.BottleMessageEntity;
import com.example.bloombackend.bottlemsg.repository.querydsl.BottleMessageRepositoryCustom;

public interface BottleMessageRepository
	extends JpaRepository<BottleMessageEntity, Long>, BottleMessageRepositoryCustom {
	List<BottleMessageEntity> findBySenderId(Long userId);
}
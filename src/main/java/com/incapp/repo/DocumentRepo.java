package com.incapp.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incapp.entity.DocumentChunk;

@Repository
public interface DocumentRepo extends JpaRepository<DocumentChunk, Long> {

	List<DocumentChunk> findAllByUserUsername(String userName);
	Optional<DocumentChunk> findByIdAndUserUsername(Long id, String username);
	

}

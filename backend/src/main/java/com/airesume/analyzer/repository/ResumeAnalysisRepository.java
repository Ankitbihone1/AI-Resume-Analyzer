package com.airesume.analyzer.repository;

import com.airesume.analyzer.entity.ResumeAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ResumeAnalysisRepository
 * Spring Data JPA repository for user-scoped resume evaluations.
 */
@Repository
public interface ResumeAnalysisRepository extends JpaRepository<ResumeAnalysis, Long> {

    /**
     * Retrieve all evaluations belonging to a specific user, ordered from most recent to oldest.
     */
    List<ResumeAnalysis> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * Secure retrieval ensuring a user can only access their own analysis report.
     */
    Optional<ResumeAnalysis> findByIdAndUserId(Long id, Long userId);

    /**
     * Delete an analysis scoped to the authenticated owner.
     */
    void deleteByIdAndUserId(Long id, Long userId);

    /**
     * Count total analyses run by the specified user.
     */
    long countByUserId(Long userId);
}

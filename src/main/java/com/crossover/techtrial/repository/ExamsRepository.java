package com.crossover.techtrial.repository;

import com.crossover.techtrial.model.TestExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Test exam data access object.
 */
@Repository
public interface ExamsRepository extends JpaRepository<TestExam, Long>
{
}

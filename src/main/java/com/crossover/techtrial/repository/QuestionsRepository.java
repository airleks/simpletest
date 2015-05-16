package com.crossover.techtrial.repository;

import com.crossover.techtrial.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Test exam question data access object.
 */
@Repository
public interface QuestionsRepository extends JpaRepository<Question, Long>
{
}

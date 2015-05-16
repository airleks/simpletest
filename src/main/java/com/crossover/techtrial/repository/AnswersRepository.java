package com.crossover.techtrial.repository;

import com.crossover.techtrial.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Answer choice data access object.
 */
@Repository
public interface AnswersRepository extends JpaRepository<Answer, Long>
{
}

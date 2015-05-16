package com.crossover.techtrial.repository;

import com.crossover.techtrial.model.Grade;
import com.crossover.techtrial.model.TestExam;
import com.crossover.techtrial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User test result data access object.
 */
@Repository
public interface GradesRepository extends JpaRepository<Grade, Long>
{
    List<Grade> findByExamOrderByStartedDesc(TestExam exam);
    Grade findByExamAndUser(TestExam exam, User user);
}

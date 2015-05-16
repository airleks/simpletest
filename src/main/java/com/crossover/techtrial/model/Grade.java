package com.crossover.techtrial.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * User test result data object.
 */
@Entity
public class Grade implements Serializable
{
    private static final long serialVersionUID = 7949429753780487091L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date started;
    private Date finished;

    private Integer score;

    @Basic
    @Column(name = "passed", columnDefinition = "BIT", length = 1)
    private Boolean passed;     // in case when test exam "pass score" value will be changed after user was graded

    @ManyToOne
    private User user;

    @ManyToOne
    private TestExam exam;

    public Grade() {
    }

    public Grade(User user, TestExam exam, Date started, Date finished, Integer score, Boolean passed)
    {
        this.user = user;
        this.exam = exam;
        this.started = started;
        this.finished = finished;
        this.score = score;
        this.passed = passed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public Date getFinished() {
        return finished;
    }

    public void setFinished(Date finished) {
        this.finished = finished;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getPassed() {
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TestExam getExam() {
        return exam;
    }

    public void setExam(TestExam exam) {
        this.exam = exam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Grade grade = (Grade) o;

        if (!exam.equals(grade.exam)) return false;
        if (id != null ? !id.equals(grade.id) : grade.id != null) return false;
        if (!user.equals(grade.user)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + user.hashCode();
        result = 31 * result + exam.hashCode();
        return result;
    }
}

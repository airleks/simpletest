package com.crossover.techtrial.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Test exam data object.
 */
@Entity
@Table(name = "test_exam")
public class TestExam implements Serializable
{
    private static final long serialVersionUID = 4877034486816618784L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;

    @Column(name = "pass_score")
    private Integer passScore;

    @Column(name = "total_score")
    private Integer totalScore;

    @Column(name = "exam_duration")
    private Integer duration;

    @OneToMany(mappedBy = "exam", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<Question> questions;

    @OneToMany(mappedBy = "exam", cascade = {CascadeType.REMOVE})
    private Set<Grade> grades;

    public TestExam() {
    }

    public TestExam(String name, String description, Integer passScore, Integer totalScore, Integer duration) {
        this.name = name;
        this.description = description;
        this.passScore = passScore;
        this.totalScore = totalScore;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPassScore() {
        return passScore;
    }

    public void setPassScore(Integer passScore) {
        this.passScore = passScore;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Set<Grade> getGrades() {
        return grades;
    }

    public void setGrades(Set<Grade> grades) {
        this.grades = grades;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestExam testExam = (TestExam) o;
        if (id != null ? !id.equals(testExam.id) : testExam.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

package com.crossover.techtrial.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Test exam question data object.
 */
@Entity
public class Question implements Serializable
{
    private static final long serialVersionUID = 8922367225738761894L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;

    @Basic
    @Column(name = "multiple", columnDefinition = "BIT", length = 1)
    private Boolean multiple;

    @Column(name = "d_order")
    private Integer order;

    @ManyToOne
    private TestExam exam;

    @OneToMany(mappedBy = "question", cascade = {CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private List<Answer> answers;

    public Question() {
    }

    public Question(TestExam exam, String title, String description, Boolean multiple, Integer order) {
        this.exam = exam;
        this.title = title;
        this.description = description;
        this.multiple = multiple;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getMultiple() {
        return multiple;
    }

    public void setMultiple(Boolean multiple) {
        this.multiple = multiple;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public TestExam getExam() {
        return exam;
    }

    public void setExam(TestExam exam) {
        this.exam = exam;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        if (id != null ? !id.equals(question.id) : question.id != null) return false;
        if (!order.equals(question.order)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

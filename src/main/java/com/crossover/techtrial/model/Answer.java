package com.crossover.techtrial.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Answer choice data object.
 */
@Entity
public class Answer implements Serializable
{
    private static final long serialVersionUID = 86928249323157477L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "a_text")
    private String text;

    @Column(name = "d_order")
    private Integer order;

    @Basic
    @Column(name = "is_valid", columnDefinition = "BIT", length = 1)
    private Boolean valid;

    @ManyToOne
    private Question question;

    public Answer() {
    }

    public Answer(Question question, String text, Integer order, Boolean valid) {
        this.question = question;
        this.text = text;
        this.order = order;
        this.valid = valid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        if (id != null ? !id.equals(answer.id) : answer.id != null) return false;
        if (!order.equals(answer.order)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

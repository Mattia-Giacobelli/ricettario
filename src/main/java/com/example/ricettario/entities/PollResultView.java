package com.example.ricettario.entities;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "v_poll_results")
@Immutable
@Getter
public class PollResultView {

    @Id
    @Column(name = "candidate_id")
    private Integer candidateId;

    @Column(name = "poll_id")
    private Integer pollId;

    @Column(name = "recipe_id")
    private Integer recipeId;

    @Column(name = "recipe_name")
    private String recipeName;

    @Column(name = "votes")
    private Long votes;
}

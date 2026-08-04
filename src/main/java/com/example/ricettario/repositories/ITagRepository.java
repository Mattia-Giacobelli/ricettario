package com.example.ricettario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ricettario.entities.Tag;

public interface ITagRepository extends JpaRepository<Tag, Integer> {

}

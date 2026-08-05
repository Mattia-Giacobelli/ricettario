package com.example.ricettario.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ricettario.entities.Tag;

public interface ITagRepository extends JpaRepository<Tag, Integer> {

    public Optional<Tag> findByName(String name);

}

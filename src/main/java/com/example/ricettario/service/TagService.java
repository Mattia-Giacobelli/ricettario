package com.example.ricettario.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ricettario.entities.Tag;
import com.example.ricettario.repositories.ITagRepository;

@Service
@Transactional(readOnly = true)
public class TagService {

    ITagRepository tagRepository;

    public TagService(ITagRepository tagRepository) {

        this.tagRepository = tagRepository;

    }

    public Page<Tag> index(Pageable pageable) {

        return tagRepository.findAll(pageable);

    }

    public Tag findById(int id) {

        return tagRepository.findById(id).orElseThrow();

    }

    @Transactional
    public Tag create(Tag tag) {

        return tagRepository.save(tag);

    }

    @Transactional
    public Tag update(Tag tag) {

        return tagRepository.save(tag);

    }

    @Transactional
    public void delete(int id) {

        tagRepository.deleteById(id);

    }

}

package com.example.ricettario.controller.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ricettario.DTO.APITagDTO;
import com.example.ricettario.entities.Tag;
import com.example.ricettario.service.TagService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/tags")
public class APITagController {

    private final TagService tagService;

    public APITagController(TagService tagService) {

        this.tagService = tagService;

    }

    @GetMapping("")
    public ResponseEntity<?> getTags() {

        List<APITagDTO> apiTags = new ArrayList<>();

        List<Tag> tags = tagService.findAll();

        tags.stream().forEach(tag -> {

            APITagDTO newTag = new APITagDTO();

            newTag.setId(tag.getId());
            newTag.setName(tag.getName());

            apiTags.add(newTag);

        });

        System.out.println(apiTags);

        return ResponseEntity.ok(apiTags);

    }

}

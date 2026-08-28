package com.example.ricettario.DTO;

import java.util.Set;

public class TagsResponseDTO {

    Set<APITagDTO> tags;

    public void setTags(Set<APITagDTO> tags) {
        this.tags = tags;
    }

    public Set<APITagDTO> getTags() {
        return tags;
    }

}

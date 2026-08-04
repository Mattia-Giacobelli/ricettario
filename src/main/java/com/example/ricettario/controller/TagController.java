package com.example.ricettario.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.ricettario.entities.Tag;
import com.example.ricettario.service.TagService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
@RequestMapping("/tags")
public class TagController {

    TagService tagService;

    public TagController(TagService tagService) {

        this.tagService = tagService;

    }

    @GetMapping("")
    public String index(@RequestParam(defaultValue = "0") int page, Model tagsM) {

        Pageable pageable = PageRequest.of(page, 20, Sort.by("name").ascending());
        Page<Tag> tags = tagService.index(pageable);

        tagsM.addAttribute("tags", tags);

        return "pages/tags/index";

    }

    @GetMapping("/{id}")
    public String show(@PathVariable int id, Model tagM) {

        tagM.addAttribute("tag", tagService.findById(id));

        return "pages/tags/show";

    }

    @GetMapping("/create")
    public String createForm(Model tagM) {

        Tag newTag = new Tag();

        tagM.addAttribute("tag", newTag);

        return "pages/tags/newTagForm";

    }

    @PostMapping("/create")
    public String create(@Validated @ModelAttribute("tag") Tag tag, BindingResult result) {

        if (result.hasErrors()) {

            return "pages/tags/newTagForm";

        } else {

            Tag newTag = tagService.create(tag);

            return "redirect:/tags/" + newTag.getId();

        }

    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable int id, Model tagM) {

        tagM.addAttribute("tag", tagService.findById(id));

        return "pages/tags/newTagForm";

    }

    @PutMapping("/{id}")
    public String update(@PathVariable int id, @Validated @ModelAttribute("tag") Tag tag, Model tagM,
            BindingResult result, RedirectAttributes red) {

        if (result.hasErrors()) {

            return "pages/tags/newTagForm";

        } else {

            Tag oldTag = tagService.findById(id);

            if (oldTag.equals(tag)) {

                red.addFlashAttribute("msg", tag.getName() + ", Nessuna modifica apportata");

                return "redirect:/tags";

            }

            tag.setId(id);

            tagService.update(tag);

            return "redirect:/tags/" + tag.getId();

        }

    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id, RedirectAttributes red) {

        Tag tag = tagService.findById(id);

        tagService.delete(id);

        red.addFlashAttribute("msg", tag.getName() + ", Tag eliminato correttamente");

        return "redirect:/tags";

    }

}

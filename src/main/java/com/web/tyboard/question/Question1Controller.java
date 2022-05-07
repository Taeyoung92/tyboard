package com.web.tyboard.question;

import com.web.tyboard.answer.Answer1Form;
import com.web.tyboard.user.SiteUser;
import com.web.tyboard.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;

@RequestMapping(value = {"/question1", "/"})
@RequiredArgsConstructor
@Controller
public class Question1Controller {
    private final Question1Service question1Service;
    private final UserService userService;

    @RequestMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Question1> paging = this.question1Service.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "question1_list";
    }

    @RequestMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, Answer1Form answer1Form) {
        Question1 question1 = this.question1Service.getQuestion1(id);
        model.addAttribute("question1", question1);
        return "question1_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String question1Create(Question1Form question1Form) {
        return "question1_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String question1Create(@Valid Question1Form question1Form,
                                  BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question1_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.question1Service.create(question1Form.getSubject(), question1Form.getContent(), siteUser);
        return "redirect:/question1/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String question1Modify(Question1Form question1Form, @PathVariable("id") Integer id, Principal principal) {
        Question1 question1 = this.question1Service.getQuestion1(id);
        if(!question1.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        question1Form.setSubject(question1.getSubject());
        question1Form.setContent(question1.getContent());
        return "question1_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String question1Modify(@Valid Question1Form question1Form, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question1_form";
        }
        Question1 question1 = this.question1Service.getQuestion1(id);
        if (!question1.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.question1Service.modify(question1, question1Form.getSubject(), question1Form.getContent());
        return String.format("redirect:/question1/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String question1Delete(Principal principal, @PathVariable("id") Integer id) {
        Question1 question1 = this.question1Service.getQuestion1(id);
        if (!question1.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.question1Service.delete(question1);
        return "redirect:/question1/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String question1Vote(Principal principal, @PathVariable("id") Integer id) {
        Question1 question1 = this.question1Service.getQuestion1(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.question1Service.vote(question1, siteUser);
        return String.format("redirect:/question1/detail/%s", id);
    }
}

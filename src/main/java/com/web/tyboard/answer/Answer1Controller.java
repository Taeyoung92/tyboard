package com.web.tyboard.answer;

import com.web.tyboard.question.Question1;
import com.web.tyboard.question.Question1Service;
import com.web.tyboard.user.SiteUser;
import com.web.tyboard.user.UserService;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/answer1")
@RequiredArgsConstructor
@Controller
public class Answer1Controller {
    private final Question1Service question1Service;
    private final Answer1Service answer1Service;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id, @RequestParam String content,
                               @Valid Answer1Form answer1Form, BindingResult bindingResult, Principal principal) {
        Question1 question1 = this.question1Service.getQuestion1(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("question1", question1);
            return "question1_detail";
        }
        Answer1 answer1 = this.answer1Service.create(question1,
                answer1Form.getContent(), siteUser);
        return String.format("redirect:/question1/detail/%s#answer1_%s",
                answer1.getQuestion1().getId(), answer1.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answer1Modify(Answer1Form answer1Form, @PathVariable("id") Integer id, Principal principal) {
        Answer1 answer1 = this.answer1Service.getAnswer1(id);
        if (!answer1.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.answer1Service.modify(answer1, answer1Form.getContent());
        return String.format("redirect:/question1/detail/%s#answer1_%s",
                answer1.getQuestion1().getId(), answer1.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answer1Modify(@Valid Answer1Form answer1Form, BindingResult bindingResult,
                               @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "answer1_form";
        }
        Answer1 answer1 = this.answer1Service.getAnswer1(id);
        if (!answer1.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.answer1Service.modify(answer1, answer1Form.getContent());
        return String.format("redirect:/question1/detail/%s#answer1_%s", answer1.getQuestion1().getId(), answer1.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answer1Delete(Principal principal, @PathVariable("id") Integer id) {
        Answer1 answer1 = this.answer1Service.getAnswer1(id);
        if (!answer1.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.answer1Service.delete(answer1);
        return String.format("redirect:/question1/detail/%s", answer1.getQuestion1().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answer1Vote(Principal principal, @PathVariable("id") Integer id) {
        Answer1 answer1 = this.answer1Service.getAnswer1(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.answer1Service.vote(answer1, siteUser);
        return String.format("redirect:/question1/detail/%s#answer1_%s",
                answer1.getQuestion1().getId(), answer1.getId());
    }
}

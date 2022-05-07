package com.web.tyboard.question;

import com.web.tyboard.answer.Answer3Form;
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

@RequestMapping("/question3")
@RequiredArgsConstructor
@Controller
public class Question3Controller {
    private final Question3Service question3Service;
    private final UserService userService;

    @RequestMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Question3> paging = this.question3Service.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "question3_list";
    }

    @RequestMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, Answer3Form answer3Form) {
        Question3 question3 = this.question3Service.getQuestion3(id);
        model.addAttribute("question3", question3);
        return "question3_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String question3Create(Question3Form question3Form) {
        return "question3_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String question3Create(@Valid Question3Form question3Form,
                                  BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question3_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.question3Service.create(question3Form.getSubject(), question3Form.getContent(), siteUser);
        return "redirect:/question3/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String question3Modify(Question3Form question3Form, @PathVariable("id") Integer id, Principal principal) {
        Question3 question3 = this.question3Service.getQuestion3(id);
        if(!question3.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        question3Form.setSubject(question3.getSubject());
        question3Form.setContent(question3.getContent());
        return "question3_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String question3Modify(@Valid Question3Form question3Form, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question3_form";
        }
        Question3 question3 = this.question3Service.getQuestion3(id);
        if (!question3.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.question3Service.modify(question3, question3Form.getSubject(), question3Form.getContent());
        return String.format("redirect:/question3/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String question3Delete(Principal principal, @PathVariable("id") Integer id) {
        Question3 question3 = this.question3Service.getQuestion3(id);
        if (!question3.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.question3Service.delete(question3);
        return "redirect:/question3/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String question3Vote(Principal principal, @PathVariable("id") Integer id) {
        Question3 question3 = this.question3Service.getQuestion3(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.question3Service.vote(question3, siteUser);
        return String.format("redirect:/question3/detail/%s", id);
    }
}

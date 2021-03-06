package com.axmor.axmortest.controller;

import com.axmor.axmortest.dao.CommentMapper;
import com.axmor.axmortest.dao.IssueMapper;
import com.axmor.axmortest.model.Comment;
import com.axmor.axmortest.model.Issue;
import com.axmor.axmortest.model.IssueStatus;
import com.axmor.axmortest.model.User;
import com.axmor.axmortest.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Controller
public class MainController {

    @Autowired
    private IssueMapper issueMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private IssueService issueService;

    @GetMapping("/")
    public String getIssueTracker(@AuthenticationPrincipal User user,
                                  @RequestParam(required = false, defaultValue = "") String filter,
                                  @RequestParam(required = false, defaultValue = "") String error,
                                  Model model) {
        switch (error) {
            case "1":
                model.addAttribute("errmsg", "You try to send message to wrong issue");
                break;
            case "5":
                model.addAttribute("errmsg", "That project not exist");
                break;
            default:
                break;
        }
        List<Issue> issues;
        if (!filter.isEmpty()) {
            issues = issueMapper.findByName(filter);
        } else {
            issues = issueMapper.findAll();
        }
        model.addAttribute("user", user);
        model.addAttribute("issues", issues);
        return "index";
    }

    @GetMapping("/createissue")
    public String getCreateIssue(@AuthenticationPrincipal User user,
                                 @RequestParam(required = false, defaultValue = "") String error,
                                 Model model) {
        model.addAttribute("user", user);
        switch (error) {
            case "4":
                model.addAttribute("errmsg", "Name or description must be filled!");
                break;
            case "6":
                model.addAttribute("errmsg", "Name length must be less than 30 symbols");
                break;
            case "7":
                model.addAttribute("errmsg", "Description length must be less than 1000 symbols");
                break;
            default:
                break;
        }
        return "createIssue";
    }

    @PostMapping("/createissue")
    public ModelAndView postCreateIssue(@AuthenticationPrincipal User user,
                                        @RequestParam(defaultValue = "") String name,
                                        @RequestParam(defaultValue = "") String description) {
        ModelAndView modelAndView = new ModelAndView("redirect:/");
        if (!name.isEmpty() && !description.isEmpty()) {
            if (name.length() > 30) {
                modelAndView.addObject("error", "6");
                modelAndView.setViewName("redirect:/createissue");
                return modelAndView;
            }
            if (description.length() > 1000) {
                modelAndView.addObject("error", "7");
                modelAndView.setViewName("redirect:/createissue");
                return modelAndView;
            }
            issueService.createIssue(user, name, description);
        } else {
            modelAndView.addObject("error", "4");
            modelAndView.setViewName("redirect:/createissue");
            return modelAndView;
        }
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getIssue(@AuthenticationPrincipal User user,
                                 @PathVariable String id,
                                 @RequestParam(required = false, defaultValue = "") String error) {
        ModelAndView modelAndView = new ModelAndView("issue");
        try {
            long issueId = Long.parseLong(id);
            Issue issue = issueMapper.findById(issueId);
            if (issue != null) {
                modelAndView.addObject("issue", issue);
                List<Comment> comments = commentMapper.findAllByIssueId(issueId);
                modelAndView.addObject("comments", comments);
                modelAndView.addObject("user", user);
            } else {
                modelAndView.setViewName("redirect:/");
                modelAndView.addObject("error", "5");
                return modelAndView;
            }
        } catch (NumberFormatException e) {
            modelAndView.setViewName("redirect:/");
            modelAndView.addObject("error", "5");
            return modelAndView;
        }
        switch (error) {
            case "2":
                modelAndView.addObject("errmsg", "Wrong status!");
                break;
            case "3":
                modelAndView.addObject("errmsg", "Comment text must be filled");
                break;
            case "8":
                modelAndView.addObject("errmsg", "Comment text length must be less than 1000 symbols");
                break;
            default:
                break;
        }
        return modelAndView;
    }

    @PostMapping("/addcomment")
    public ModelAndView postAddComment(@AuthenticationPrincipal User user,
                                       @RequestParam String status,
                                       @RequestParam String text,
                                       @RequestParam String id) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            long issueId = Long.parseLong(id);
            IssueStatus.valueOf(status);
            Issue issue = issueMapper.findById(issueId);
            if (issue != null) {
                if (text == null || text.isEmpty()) {
                    modelAndView.addObject("error", "3");
                    modelAndView.setViewName("redirect:/" + id);
                    return modelAndView;
                }
                if (text.length() > 1000) {
                    modelAndView.addObject("error", "8");
                    modelAndView.setViewName("redirect:/" + id);
                    return modelAndView;
                }
                issueService.addComment(user, issue, status, text);
                modelAndView.setViewName("redirect:/" + id);
            } else {
                modelAndView.addObject("error", "1");
                modelAndView.setViewName("redirect:/");
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            modelAndView.addObject("error", "2");
            modelAndView.setViewName("redirect:/" + id);
        }
        return modelAndView;
    }
}

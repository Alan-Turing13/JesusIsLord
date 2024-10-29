package com.dominicjmarshall.springbootday1.controller;

import com.dominicjmarshall.springbootday1.models.Post;
import com.dominicjmarshall.springbootday1.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class HomeRestController {

    @Autowired
    private PostService postService;
    
    @GetMapping("/")
    public List<Post> home(){
        return postService.getAll();
    }
}

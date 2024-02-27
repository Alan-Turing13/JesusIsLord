package com.dominicjmarshall.springbootday1.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.dominicjmarshall.springbootday1.models.Account;
import com.dominicjmarshall.springbootday1.models.Post;
import com.dominicjmarshall.springbootday1.services.AccountService;
import com.dominicjmarshall.springbootday1.services.PostService;

import jakarta.validation.Valid;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    // READ get method
    @GetMapping("/post/{id}")
    public String getPost(@PathVariable Long id, Model model) {

        // if there's a post with this id, show the post
        Optional<Post> optionalPost = postService.getById(id);

        if (optionalPost.isPresent()){
            Post post = optionalPost.get();
            model.addAttribute("post", post);

            // if user is logged in
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            if (currentUser != null) {

                 // if logged in user is the creator of this post, flag isOwner as true
                if (currentUser.equals(post.getAccount().getEmail())) {
                    model.addAttribute("isOwner", true);
                }
            } 
            // otherwise flag isOwner as false            
             else {
                model.addAttribute("isOwner", false);
            }

            return "post_views/post"; 
        
        // no post with that id, then show a 404
        } else {
            return "post_views/404";
        }
    } 

    // CREATE get method (load the form)
    @GetMapping("/posts/add")
    public String create(Model model){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Account> optionalAccount = accountService.findOneByEmail(currentUser);
        if(optionalAccount.isPresent()){
            Post post = new Post();
            post.setAccount(optionalAccount.get());
            model.addAttribute("post", post);
            return "post_views/create";
        } else {
            return "redirect:/";
        }
    }

    // CREATE post method (save the post)
    @PostMapping("/posts/add")
    @PreAuthorize("isAuthenticated()")
    public String savePost(@Valid @ModelAttribute Post post, BindingResult result){

        if (result.hasErrors()){
            System.out.println("\nBindingResult: " + result);
            return "post_views/create";
        }

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("User: " + currentUser);
        
        if (post.getAccount().getEmail().compareToIgnoreCase(currentUser) < 0) {
            return "redirect:/?error";
        }

        postService.save(post);
        System.out.println("Post saved: " + post);
        
        return "redirect:/post/" + post.getId();
    }

    // UPDATE get method (load the form)
    @GetMapping("/post/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String editPost(@PathVariable Long id, Model model){
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()){
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return "post_views/edit";
        } else {
            return "404";
        }            
    }

    // UPDATE post method (save the post)
    @PostMapping("/post/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String saveEdit(@Valid @ModelAttribute Post post, BindingResult result, @PathVariable Long id){

        if(result.hasErrors()) {
            return "post_views/edit";
        }

        Optional<Post> optionalPost = postService.getById(id);
        if(optionalPost.isPresent()){
            Post existingPost = optionalPost.get();
            existingPost.setTitle(post.getTitle());
            existingPost.setBody(post.getBody());
            postService.save(existingPost);
        }

        return "redirect:/post/" + post.getId();        
    }  
    
     // DELETE get method (load the form)
    @GetMapping("/post/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public String deletePost(@PathVariable Long id){
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()){
            Post post = optionalPost.get();
            postService.delete(post);
            return "redirect:/";
        } else {
            return "404";
        }            
    }   
    
}

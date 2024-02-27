package com.dominicjmarshall.springbootday1.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dominicjmarshall.springbootday1.models.Post;
import com.dominicjmarshall.springbootday1.services.PostService;

@Controller
public class HomeController {

    @Autowired
    private PostService postService;
    
    @GetMapping("/")
    public String home(Model model, 
                      @RequestParam(required=false, name="sort_by", defaultValue="createdAt") String field,
                      @RequestParam(required=false, name="per_page", defaultValue="2") String pageSize,
                      @RequestParam(required=false, name="page", defaultValue="1") String offset){
        
        // check if the user is logged in
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !(authentication instanceof AnonymousAuthenticationToken)) {
            System.out.println("\nUSER LOGGED IN \n" + authentication.getAuthorities() + "\n");
            } else {
                System.out.println("\nUSER NOT LOGGED IN\n");
            }
        
        // take user input on how the posts should be formatted in the view
        Page<Post> postsOnPage = postService.getAll(Integer.parseInt(offset)-1, Integer.parseInt(pageSize), field);
        int totalPages = postsOnPage.getTotalPages();
        
        // give each page a number
        List<Integer> pages = new ArrayList<>();
        if (totalPages>0){
            pages = IntStream.rangeClosed(0, totalPages-1)
            .boxed().collect(Collectors.toList());
        }
        
        // give each page number a hyperlink
        List<String> links = new ArrayList<>();
        if(pages.size()>0){
            for (int page: pages){
                String navItem = "";
                // getNumber() method from Slice (super-interface of Page)
                if(page==postsOnPage.getNumber()){
                    navItem = "active";
                }
                String _tempLink = "/?per_page=" + pageSize + "&page=" + (page+1) + "&sort_by=" + field;
                links.add("<li class=\"page-item " + navItem + "\"><a href=\"" + _tempLink + "\" class='page-link'>" +
                    (page+1) + "</a></li>");
            }
            model.addAttribute("links", links);
        }
        model.addAttribute("posts", postsOnPage);
        model.addAttribute("per_page", pageSize);
        model.addAttribute("sort_by", field);

        return "home_views/home";
    }
}

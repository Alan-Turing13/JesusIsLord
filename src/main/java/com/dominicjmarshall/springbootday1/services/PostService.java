package com.dominicjmarshall.springbootday1.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.dominicjmarshall.springbootday1.models.Post;
import com.dominicjmarshall.springbootday1.repositories.PostRepository;

@Service
public class PostService {
    
    // PostRepository interface is used for all these methods
    @Autowired
    private PostRepository postRepository;

    // READ: individual post
    public Optional<Post> getById(Long id){
        return postRepository.findById(id);
    }

    // READ: all posts
    public List<Post> getAll(){
        return postRepository.findAllByOrderByUpdatedAtDesc();
    }

    // READ: posts as specified by the user in /home's nav bar
    public Page<Post> getAll(int offset, int pageSize, String field){
        return postRepository.findAll(PageRequest.of(offset, pageSize)
                             .withSort(Direction.DESC, field));
    }

    // DELETE
    public void delete(Post post){ 
        postRepository.delete(post);
    }

    // CREATE/UPDATE
    public Post save(Post post){
        if (post.getId() == null){
            post.setCreatedAt(LocalDateTime.now());
        }
        post.setUpdatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }
}

package com.dominicjmarshall.springbootday1.repositories;

import com.dominicjmarshall.springbootday1.models.Post;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    /* JPARepository provides CRUD functionality.
    With these generics it is tailored to work for my Post model. */
    
    // because updatedAt is annotated as a column, this method builds in a database command.
    List<Post> findAllByOrderByUpdatedAtDesc();
}

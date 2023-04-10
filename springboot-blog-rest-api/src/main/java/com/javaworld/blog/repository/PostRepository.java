package com.javaworld.blog.repository;

import com.javaworld.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

//no need of annotating with @Repository as JpaRepository has impl class SimpleJpaRepository
// which is annotated with @Repository and @Transactional
public interface PostRepository extends JpaRepository<Post,Long> {
}

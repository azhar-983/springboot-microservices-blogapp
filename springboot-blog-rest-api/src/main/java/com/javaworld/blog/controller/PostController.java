package com.javaworld.blog.controller;

import com.javaworld.blog.payload.PostDto;
import com.javaworld.blog.payload.PostResponse;
import com.javaworld.blog.service.PostService;
import com.javaworld.blog.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.PublicKey;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService;

    public PostController(PostService postService){
        this.postService=postService;
    }
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }
    @GetMapping
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false)String sortDir){
        return postService.getAllPosts(pageNo,pageSize,sortBy,sortDir);
    }

    @GetMapping(value = "{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("postId") long id){
        return new ResponseEntity<>(postService.getPostById(id),HttpStatus.OK);
    }

    @PutMapping(value = "{postId}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable("postId") long id) {
        return new ResponseEntity<>(postService.updatePost(postDto,id),HttpStatus.OK);
    }

    @DeleteMapping("{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("postId") long id){
        postService.deletePost(id);
        return new ResponseEntity<>("Post deleted successfully",HttpStatus.OK);
    }
}

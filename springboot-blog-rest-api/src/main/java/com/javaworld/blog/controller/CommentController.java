package com.javaworld.blog.controller;

import com.javaworld.blog.payload.CommentDto;
import com.javaworld.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService=commentService;
    }
    @PostMapping("/posts/{postId}/comments/")
    public ResponseEntity<CommentDto> createComment(@Valid @PathVariable("postId") long postId,
                                                    @RequestBody CommentDto commentDto){
        return new ResponseEntity<>(commentService.createComment(postId,commentDto), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable("postId") long postId){
        return new ResponseEntity<>(commentService.getCommentsByPostId(postId),HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("postId") Long postId,
                                                     @PathVariable("commentId") Long commentId){
        CommentDto commentDto = commentService.getCommentById(postId,commentId);
        return new ResponseEntity<>(commentDto,HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@Valid @PathVariable("postId") Long postId,
                                                     @PathVariable("commentId") Long commentId,
                                                    @RequestBody CommentDto commentDto){

        return new ResponseEntity<>(commentService.updateComment(postId,commentId,commentDto),HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("postId") Long postId,
                                                    @PathVariable("commentId") Long commentId){
        commentService.deleteComment(postId,commentId);
        return new ResponseEntity<>("Comment deleted successfully",HttpStatus.OK);
    }
}

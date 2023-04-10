package com.javaworld.blog.service.impl;

import com.javaworld.blog.entity.Comment;
import com.javaworld.blog.entity.Post;
import com.javaworld.blog.exception.BlogAPIException;
import com.javaworld.blog.exception.ResourceNotFoundException;
import com.javaworld.blog.payload.CommentDto;
import com.javaworld.blog.repository.CommentRepository;
import com.javaworld.blog.repository.PostRepository;
import com.javaworld.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    private PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository,PostRepository postRepository){
        this.commentRepository=commentRepository;
        this.postRepository=postRepository;
    }
    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);

        Post post = postRepository.findById(postId)
                                .orElseThrow(()->new ResourceNotFoundException("post","id",postId));
        //Set post to comment entity
        comment.setPost(post);

        //comment entity to db
        Comment newComment = commentRepository.save(comment);
        return mapToDto(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());

    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        //retrieve post entity by id
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("post","id",postId));

        //retrieve comment by id
        Comment comment = commentRepository.findById(commentId)
                        .orElseThrow(()->new ResourceNotFoundException("comment","id",commentId));

        if(!(comment.getPost().getId()==post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }

        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long commentId, CommentDto commentDto) {

        Post post = postRepository.findById(postId)
                            .orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
        //retrieve comment by id
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("comment","id",commentId));
        if(!(comment.getPost().getId()==post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }

        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        Comment updatedComment = commentRepository.save(comment);

        return mapToDto(updatedComment);
    }

    @Override
    public void deleteComment(long postId, long commentId) {
        //retrieve post by id
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
        //retrieve comment by id
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("comment","id",commentId));
        if(!(comment.getPost().getId()==post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }
        commentRepository.delete(comment);
    }

    private CommentDto mapToDto(Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = new Comment();
        //comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        return comment;
    }
}

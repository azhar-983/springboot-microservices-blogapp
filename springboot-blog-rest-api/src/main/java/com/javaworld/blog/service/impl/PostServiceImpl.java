package com.javaworld.blog.service.impl;

import com.javaworld.blog.entity.Post;
import com.javaworld.blog.exception.ResourceNotFoundException;
import com.javaworld.blog.payload.PostDto;
import com.javaworld.blog.payload.PostResponse;
import com.javaworld.blog.repository.PostRepository;
import com.javaworld.blog.service.PostService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    PostRepository postRepository;
    ModelMapper modelMapper;

    /*public PostServiceImpl(PostRepository postRepository){
        this.postRepository=postRepository;
    }*/
    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = mapToEntity(postDto);
        /*post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());*/

        Post savedPost=postRepository.save(post);
        PostDto savedPostDto = mapToDto(savedPost);
        /*savedPostDto.setId(savedPost.getId());
        savedPostDto.setTitle(savedPost.getTitle());
        savedPostDto.setDescription(savedPost.getDescription());
        savedPostDto.setContent(savedPost.getContent());*/

        return savedPostDto;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy,String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending():
                    Sort.by(sortBy).descending();
        //Create pageable interface
        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);
        List<Post> postList = posts.getContent();
        List<PostDto> content = postList.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(pageNo);
        postResponse.setPageSize(pageSize);
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());
        return postResponse;
    }

    @Override
    public PostDto getPostById(long id){
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("post","id",id));
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("post","id",id));
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        Post updatedPost = postRepository.save(post);

        return mapToDto(updatedPost);
    }

    @Override
    public void deletePost(long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("post","id",id));

        postRepository.delete(post);
    }

    private PostDto mapToDto(Post post){
        PostDto postDto = modelMapper.map(post,PostDto.class);
        /*postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());*/
        return postDto;
    }
    private Post mapToEntity(PostDto postDto){
        Post post = modelMapper.map(postDto,Post.class);

/*        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());*/
        return post;
    }
}

package com.javaworld.blog.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class PostDto {
    private long id;
    @NotEmpty
    @Size(min = 2,message = "post title must have at least 2 characters")
    private String title;
    @NotEmpty
    @Size(min = 10, message = "post description must have at least 10 characters")
    private String description;
    @NotEmpty
    private String content;
    private Set<CommentDto> comments;
}

package com.poly.blogmanagment.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    private Long id;

    @NotBlank
    @Size(min = 10)
    private String title;

    @NotBlank
    @Size(min = 50)
    private String body;

    @NotNull
    private Long categoryId;

    private List<String> tags;

    private List<Long> tagIds; // IDs of tags to associate with the post

    private MultipartFile image;

}
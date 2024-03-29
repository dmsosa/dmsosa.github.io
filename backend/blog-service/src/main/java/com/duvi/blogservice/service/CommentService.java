package com.duvi.blogservice.service;

import com.duvi.blogservice.model.Comment;
import com.duvi.blogservice.model.dto.CommentDTO;
import com.duvi.blogservice.model.exceptions.ArticleDoNotExistsException;
import com.duvi.blogservice.model.exceptions.CommentNotFoundException;
import com.duvi.blogservice.model.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {

    //Create DTO
    CommentDTO createDTO(Comment comment);
    //Basic CRUD
    CommentDTO createComment(CommentDTO commentDTO) throws UserNotFoundException, ArticleDoNotExistsException;
    List<CommentDTO> getComments();
    CommentDTO getCommentById(Long id) throws CommentNotFoundException;
    CommentDTO updateComment(Long commentId, CommentDTO newCommentDTO) throws UserNotFoundException, ArticleDoNotExistsException;
    void deleteComment(Long id) throws CommentNotFoundException;

    //Operations with Users

    List<CommentDTO> getCommentOfUser(String username) throws UserNotFoundException;
    List<CommentDTO> getCommentOfUser(Long userId) throws UserNotFoundException;

//    Operations with Articles
    List<CommentDTO> getCommentOfArticle(String slug) throws ArticleDoNotExistsException;
    List<CommentDTO> getCommentOfArticle(Long articleId) throws ArticleDoNotExistsException;

}

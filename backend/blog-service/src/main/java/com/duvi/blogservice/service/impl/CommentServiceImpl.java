package com.duvi.blogservice.service.impl;

import com.duvi.blogservice.model.Article;
import com.duvi.blogservice.model.Comment;
import com.duvi.blogservice.model.User;
import com.duvi.blogservice.model.dto.CommentDTO;
import com.duvi.blogservice.model.exceptions.ArticleDoNotExistsException;
import com.duvi.blogservice.model.exceptions.CommentNotFoundException;
import com.duvi.blogservice.model.exceptions.UserNotFoundException;
import com.duvi.blogservice.repository.ArticleRepository;
import com.duvi.blogservice.repository.CommentRepository;
import com.duvi.blogservice.repository.UserRepository;
import com.duvi.blogservice.service.CommentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private ArticleRepository articleRepository;
    public CommentServiceImpl(CommentRepository commentRepository,
                              UserRepository userRepository,
                              ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public CommentDTO createDTO(Comment comment) {
        return new CommentDTO(comment.getArticle().getId(), comment.getUser().getId(), comment.getBody(), comment.getPostedAt(), comment.getUpdatedAt());
    }

    @Override
    public CommentDTO createComment(CommentDTO commentDTO) throws UserNotFoundException, ArticleDoNotExistsException {
        Optional<User> user = userRepository.findById(commentDTO.userId());
        Optional<Article> article = articleRepository.findById(commentDTO.articleId());
        if (user.isEmpty()) { throw new UserNotFoundException(commentDTO.userId()); }
        if (article.isEmpty()) { throw new ArticleDoNotExistsException(commentDTO.articleId()); }
        Comment comment = new Comment(user.get(), article.get(), commentDTO.body());
        return createDTO(commentRepository.save(comment));

    }

    @Override
    public List<CommentDTO> getComments() {
        List<Comment> commentList = commentRepository.findAll();
        return commentList.stream().map(this::createDTO).toList();
    }

    @Override
    public CommentDTO getCommentById(Long id) throws CommentNotFoundException {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) throw new CommentNotFoundException(id);
        return createDTO(comment.get());
    }

    @Override
    public CommentDTO updateComment(Long commentId, CommentDTO newCommentDTO) throws UserNotFoundException, ArticleDoNotExistsException {
        Optional<User> user = userRepository.findById(newCommentDTO.userId());
        Optional<Article> article = articleRepository.findById(newCommentDTO.articleId());
        if (user.isEmpty()) { throw new UserNotFoundException(newCommentDTO.userId()); }
        if (article.isEmpty()) { throw new ArticleDoNotExistsException(newCommentDTO.articleId()); }
        Comment newComment = new Comment(user.get(), article.get(), newCommentDTO.body());
        Optional<Comment> oldComment = commentRepository.findById(commentId);
        if (oldComment.isEmpty()) {
            return createDTO(commentRepository.save(newComment));
        };
        oldComment.get().updateWith(newComment);
        return createDTO(oldComment.get());
    }

    @Override
    public void deleteComment(Long id) throws CommentNotFoundException {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) throw new CommentNotFoundException(id);
        commentRepository.deleteById(id);
    }

    @Override
    public List<CommentDTO> getCommentOfUser(String username) throws UserNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) { throw new UserNotFoundException("User with username %s can not post a comment since it does not exists!".formatted(username)); }
        List<Comment> commentList = commentRepository.findByUserId(user.get().getId());
        return commentList.stream().map(this::createDTO).toList();
    }

    @Override
    public List<CommentDTO> getCommentOfUser(Long userId) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) { throw new UserNotFoundException(userId); }
        List<Comment> commentList = commentRepository.findByUserId(userId);
        return commentList.stream().map(this::createDTO).toList();
    }

    @Override
    public List<CommentDTO> getCommentOfArticle(String slug) throws ArticleDoNotExistsException {
        Optional<Article> article = articleRepository.findBySlug(slug);
        if (article.isEmpty()) { throw new ArticleDoNotExistsException("Article with slug %s can not have any comments since it does not even exists!".formatted(slug)); }
        List<Comment> commentList = commentRepository.findByArticleId(article.get().getId());
        return commentList.stream().map(this::createDTO).toList();
    }

    @Override
    public List<CommentDTO> getCommentOfArticle(Long articleId) throws ArticleDoNotExistsException {
        Optional<Article> article = articleRepository.findById(articleId);
        if (article.isEmpty()) { throw new ArticleDoNotExistsException(articleId); }
        List<Comment> commentList = commentRepository.findByArticleId(articleId);
        return commentList.stream().map(this::createDTO).toList();
    }
}

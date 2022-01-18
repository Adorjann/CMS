package com.fullstack.cms.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;

import com.fullstack.cms.DTO.BlogPostDTO;
import com.fullstack.cms.model.BlogPost;

public interface BlogPostService {
	
	List<BlogPost> findByHeadline(String headline);
	
	BlogPost findOne(Long id);
	
	List<BlogPost> findAll();
	
	List<BlogPost> findAllDeleted();
	
	BlogPost save(BlogPost post);
	
	BlogPost update(BlogPost post);
	
	BlogPost delete (Long id);
	
	BlogPost softDelete(Long id);
	
	BlogPost undoSoftDelete(Long id);
	
	List<BlogPostDTO> paginatedBlogPosts (boolean publish, boolean softDelete,LocalDate publishFROM,LocalDate publishTO, String headline,String content, int pageNum);
	
	List<BlogPostDTO> searchBlogPosts (boolean publish, boolean softDelete,LocalDate publishFROM,LocalDate publishTO, String headline,String content);
	
	BlogPost publishThePost(Long id);
	
	BlogPost undoPublishThePost(Long id);

	BlogPostDTO createNewBlogPost(String autor, String headline, Long albumId, String blogText);

	BlogPost editBlogPost(Long id, String autor, String headLine, Long albumId, String blogText);

}

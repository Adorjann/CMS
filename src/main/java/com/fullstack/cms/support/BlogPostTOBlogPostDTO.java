package com.fullstack.cms.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.fullstack.cms.DTO.BlogPostDTO;
import com.fullstack.cms.model.BlogPost;

@Component
public class BlogPostTOBlogPostDTO implements Converter<BlogPost, BlogPostDTO>{

	@Override
	public BlogPostDTO convert(BlogPost blogPost) {
		
		BlogPostDTO dto = new BlogPostDTO();
		
		if(blogPost.getAlbum() != null) {
			dto.setAlbumID(blogPost.getAlbum().getId());
		}
		
		dto.setId(blogPost.getId());
		dto.setAuthor(blogPost.getAutor());
		dto.setBlogText(blogPost.getBlogText());
		dto.setHeadline(blogPost.getHeadline());
		dto.setPublish(blogPost.isPublish());
		dto.setSoftDelete(blogPost.isSoftDelete());
		
		if(blogPost.isPublish()) {
			dto.setPublishDate(blogPost.getPublishDate().toString());
		}else {
			dto.setPublishDate(null);
		}
		
		
		return dto;
	}

	
	
}

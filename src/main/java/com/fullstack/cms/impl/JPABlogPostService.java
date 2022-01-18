package com.fullstack.cms.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.fullstack.cms.DTO.BlogPostDTO;
import com.fullstack.cms.model.BlogPost;
import com.fullstack.cms.model.ImageAlbum;
import com.fullstack.cms.repository.BlogPostRepository;
import com.fullstack.cms.service.BlogPostService;
import com.fullstack.cms.service.ImageAlbumService;
import com.fullstack.cms.support.BlogPostTOBlogPostDTO;
@Service
public class JPABlogPostService implements BlogPostService{
	
	@Autowired
	private BlogPostRepository repository;
	
	@Autowired
	private ImageAlbumService albumService;
	
	@Autowired
	private BlogPostTOBlogPostDTO toBlogPostDTO;

	@Override
	public List<BlogPost> findByHeadline(String headline) {
		
		return repository.searchHeadline(headline);
	}

	@Override
	public BlogPost findOne(Long id) {
		Optional<BlogPost> blog = repository.findById(id);
		if(blog.isPresent()) {
			return blog.get();
		}
		return null;
	}

	@Override
	public List<BlogPost> findAll() {
		
		return repository.findAll();
		
	}
	@Override
	public List<BlogPost> findAllDeleted() {
		
		return repository.findAllDeleted();
		
	}
	
	

	@Override
	public BlogPost save(BlogPost post) {
		
		return repository.save(post);
	}

	@Override
	public BlogPost update(BlogPost post) {
		
		return repository.save(post);
	}

	@Override
	public BlogPost delete(Long id) {
			
		BlogPost post = findOne(id);
		
		if(post != null && post.isSoftDelete()) {
			repository.delete(post);
			return post;
		}
		return null;
		
	}

	@Override
	public BlogPost softDelete(Long id) {
		BlogPost post = findOne(id);
		
		if(post != null) {
			if(post.isPublish()) {
				undoPublishThePost(post.getId());
			}
			post.setSoftDelete(true);
			//soft-deleting the album
			albumService.softDelete(post.getAlbum().getId());
			BlogPost updatedPost = update(post);
			
			if(updatedPost != null) {
				return updatedPost;
			}
		}
		return null;
	}

	@Override
	public BlogPost undoSoftDelete(Long id) {
		
		BlogPost post = findOne(id);
		
		if(post != null) {
			
			post.setSoftDelete(false);
			//UNsoft-deleting the album
			albumService.undoSoftDelete(post.getAlbum().getId());
			BlogPost updatedPost = update(post);
			
			if(updatedPost != null) {
				return updatedPost;
			}
		}
		return null;
	}
	
	
	public Page<BlogPost> search(boolean publish, boolean softDelete,LocalDate publishFROM, LocalDate publishTO, String headline, String content,
			int pageNum) {
		Page<BlogPost> posts = repository.search(publishFROM, publishTO, headline, content, publish, softDelete, PageRequest.of(pageNum, 8));
		if(posts.isEmpty()) {
			System.out.println("posts is empty ***search**(paginated)**");
		}
		
		return posts;
	}
	
	@Override
	public List<BlogPostDTO> paginatedBlogPosts (boolean publish, boolean softDelete,LocalDate publishFROM,LocalDate publishTO, String headline,String content, int pageNum){
		
		List<BlogPostDTO> retVal = new LinkedList<>();
		
		Page<BlogPost> posts = search(publish, softDelete, publishFROM, publishTO, headline, content, pageNum);
			if(posts.isEmpty()) {
				System.out.println("posts is empty *****paginatetBlogPost***");
			}
		
		posts.getContent().stream().forEach(post ->{
			System.out.println("PAGINATED POST: "+post.getId());
			
			retVal.add(toBlogPostDTO.convert(post));
		});
		
		return retVal;
	}
	

	@Override
	public List<BlogPostDTO> searchBlogPosts(boolean publish, boolean softDelete, LocalDate publishFROM,
			LocalDate publishTO, String headline,String content) {
		
		List<BlogPostDTO> retVal = new LinkedList<>();
		
		List<BlogPost> result = repository.searchPosts(publishFROM, publishTO, headline,content, publish, softDelete); 
		
		result.stream().forEach(post -> {
			retVal.add(toBlogPostDTO.convert(post));
		});
		
		return retVal;
	}

	@Override
	public BlogPost publishThePost(Long id) {
		
		BlogPost post = findOne(id);
		LocalDate publishDate =  LocalDate.now();
		
		if(!post.isPublish() && !post.isSoftDelete()) {
			post.setPublish(true);
			post.setPublishDate(publishDate);
			
			//publishing the album in the blogPost (if the album exists)
			if(post.getAlbum() != null) {
				ImageAlbum album = post.getAlbum();
				albumService.publishTheAlbum(album.getId());
			}
			
			
			BlogPost updatedPost = update(post);
			if(updatedPost != null) {
				return updatedPost;
			}
		}
		
		return null;
	}

	@Override
	public BlogPost undoPublishThePost(Long id) {
		
		BlogPost post = findOne(id);
		
		if(post.isPublish() && !post.isSoftDelete()) {
			post.setPublish(false);
			post.setPublishDate(null);
			
			//UNpublishing the album in the blogPost
			ImageAlbum album = post.getAlbum();
			albumService.undoPublishTheAlbum(album.getId());
			
			BlogPost updatedPost = update(post);
			if(updatedPost != null) {
				return updatedPost;
			}
		}
		
		return null;
	}

	@Override
	public BlogPostDTO createNewBlogPost(String autor, String headline, Long albumId, String blogText) {
		
		BlogPost  newPost = new BlogPost();
		
		if(albumId != null) {
			newPost.setAlbum(albumService.findOne(albumId));
		}
		
		newPost.setAutor(autor);
		newPost.setBlogText(blogText);
		newPost.setHeadline(headline);
		newPost.setPublish(false);
		newPost.setSoftDelete(false);
		newPost.setPublishDate(null);
		
		BlogPost savedBlogPost = save(newPost);
		return toBlogPostDTO.convert(savedBlogPost);
		
	}

	@Override
	public BlogPost editBlogPost(Long id, String autor, String headLine, Long albumId, String blogText) {
		
		BlogPost blog = findOne(id);
		ImageAlbum newAlbum = albumService.findOne(albumId);
		
		if(blog != null && newAlbum != null) {
			blog.setAlbum(newAlbum);
			blog.setAutor(autor);
			blog.setHeadline(headLine);
			blog.setBlogText(blogText);
		}
		
		BlogPost updatedBlog = update(blog);
		if(updatedBlog != null) {
			return updatedBlog;
		}
		return null;
	}

	
	
	

}

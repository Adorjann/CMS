package com.fullstack.cms.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fullstack.cms.DTO.BlogPostDTO;
import com.fullstack.cms.model.BlogPost;
import com.fullstack.cms.service.BlogPostService;
import com.fullstack.cms.support.BlogPostTOBlogPostDTO;

@Controller
@RequestMapping(value = "/BlogPosts")
public class BlogPostController {

	@Autowired
	private BlogPostService blogService;
	
	@Autowired
	private BlogPostTOBlogPostDTO toBlogPostDTO;
	
	
	@GetMapping
	@ResponseBody
	private Map<String,Object> index(
			@RequestParam(required = false)@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate publishFROM,
			@RequestParam(required = false)@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate publishTO,
			@RequestParam(required = false)String headline,
			@RequestParam(required = false)String content,
			@RequestParam(required = false)Boolean publish,
			@RequestParam(required = false)Boolean softDelete,
			@RequestParam(required = false,defaultValue = "1")Integer pageNum){
		
		Map<String,Object> response = new LinkedHashMap<>();
		
		try {
		 	List<BlogPostDTO> pBlogPosts = blogService.paginatedBlogPosts(publish, softDelete, publishFROM, publishTO, headline, content, pageNum);
			//List<BlogPostDTO> pBlogPosts = blogService.searchBlogPosts(publish, softDelete, publishFROM, publishTO, headline,content);
			
			
			if(!pBlogPosts.isEmpty()) {
				
				System.out.println(".....Response: ");
				pBlogPosts.stream().forEach(blog -> {
					System.out.println(blog.getHeadline());
				});
				
				response.put("status", "200");
				response.put("blogPosts", pBlogPosts);
				response.put("pageNum", pageNum);
			}else {
				response.put("status", "404");
			}
			
		} catch (Exception e) {
			response.put("status", "500");
			response.put("exception", e.getMessage());
			
		}
		
		return response;
	}
	@PostMapping(value = "/Create")
	@ResponseBody
	public Map<String, Object> create (
			@RequestParam(required = false)String autor,
			@RequestParam(required = true,defaultValue = "Naslov")String headline,
			@RequestParam(required = false)Long albumId,
			@RequestParam(required = true,defaultValue = "blogText")String blogText){
		
		Map<String,Object> response = new LinkedHashMap();
		
		try {
			BlogPostDTO dto = blogService.createNewBlogPost(autor,headline,albumId,blogText);
			
			response.put("status", "201");
			response.put("newBlogPost", dto);
			
		} catch (Exception e) {
			
			response.put("status", "401");
			response.put("exception", e.getMessage());
		}
		
		
		return response;
		
	}
	
	@PostMapping(value = "/Edit")
	@ResponseBody
	public Map<String,Object> edit(
			@RequestParam Long id,
			@RequestParam String autor,
			@RequestParam String headLine,
			@RequestParam Long albumId,
			@RequestParam String blogText){
		Map<String,Object> response = new LinkedHashMap();
		
		try {
			BlogPost blog = blogService.editBlogPost(id,autor,headLine,albumId,blogText);
			if(blog != null) {
				response.put("status", "200");
				return response;
			}
			response.put("status", "401-500");
					
		} catch (Exception e) {
			response.put("status", "401-500");
			response.put("exception", e.getMessage());
		}
		return response;
	}
	
	
	
	
	@GetMapping(value = "/Details")
	@ResponseBody
	public Map<String,Object> getOne (@RequestParam Long id){
		
		Map<String,Object> response = new HashMap();
		
		try {
			BlogPost post = blogService.findOne(id);
			
			if(post != null) {
				BlogPostDTO dto = toBlogPostDTO.convert(post);
				
				response.put("status", "200");
				response.put("BlogPost", dto);
			}else {
				
				response.put("status", "404");
			}
		} catch (Exception e) {
			
			response.put("status", "401 || 500");
			response.put("exception", e.getMessage());
		}
		
		
		return response;
		
	}
	@PostMapping(value = "/Publish")
	@ResponseBody
	public Map<String,Object> publish(
			@RequestParam Long id,
			@RequestParam boolean publish){
		
		Map<String,Object> response = new LinkedHashMap<>();
		
		try {
			BlogPost updatedPost = null;
			
			if(publish) {
				updatedPost = blogService.publishThePost(id);
			}else {
				updatedPost = blogService.undoPublishThePost(id);
			}
			
			if(updatedPost != null) {
				response.put("status", "200");
			}else {
				response.put("status", "500");
			}
			return response;
			
		} catch (Exception e) {
			response.put("status", "500");
			response.put("exception", e.getMessage());
			return response;
		}
	}
	@PostMapping(value = "/SoftDelete")
	@ResponseBody
	public Map<String,Object> softDelete(
			@RequestParam Long id,
			@RequestParam boolean publish){
		
		Map<String,Object> response = new LinkedHashMap<>();
		
		try {
			BlogPost updatedPost = null;
			
			if(publish) {
				updatedPost = blogService.softDelete(id);
			}else {
				updatedPost = blogService.undoSoftDelete(id);
			}
			
			if(updatedPost != null) {
				response.put("status", "200");
			}else {
				response.put("status", "500");
			}
			return response;
			
		} catch (Exception e) {
			response.put("status", "500");
			response.put("exception", e.getMessage());
			return response;
		}
	}
	@PostMapping(value = "/Delete")
	@ResponseBody
	public Map<String, Object> Delete(@RequestParam Long id){
		
		
		Map<String, Object> response = new LinkedHashMap<>();
		
		BlogPost deletedPost = blogService.delete(id);
		if( deletedPost !=null ) {
			
			response.put("status", "200");
			return response;
		}
		response.put("status", "404");
		return response;
	}
	
}

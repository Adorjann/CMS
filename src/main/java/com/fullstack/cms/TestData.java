package com.fullstack.cms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fullstack.cms.DTO.BlogPostDTO;
import com.fullstack.cms.DTO.ImageDTO;
import com.fullstack.cms.DTO.imageAlbumDTO;
import com.fullstack.cms.model.BlogPost;
import com.fullstack.cms.model.ImageAlbum;
import com.fullstack.cms.model.UserProfile;
import com.fullstack.cms.service.BlogPostService;
import com.fullstack.cms.service.ImageAlbumService;
import com.fullstack.cms.service.ImageService;
import com.fullstack.cms.service.UserProfileService;

@Component
public class TestData {
	
	@Autowired
	private UserProfileService userProfileService;
	
	@Autowired
	private BlogPostService blogService;
	
	@Autowired
	private ImageAlbumService albumService;
	@Autowired
	private ImageService imageService;
	
	@PostConstruct
	public void init() {
		
		//creating the user
		UserProfile user = new UserProfile((long) 1,"a","a");
		UserProfile savedUser = userProfileService.save(user);
		
		//creating the blog post
		BlogPostDTO dto = blogService.createNewBlogPost(
				
				"Adorjan Cizmar",
				"Just Some Text I Copy Paste to use as headline", 
				null,
				"You just call the method on the object. That's one of the big benefits of annotation-driven controllers,"
				+ " you can write just the methods your code needs without having to bend things out of shape to conform to the interfaces. p<>"
				+ "So just wire your controller bean into your code, and call the method directly.p<>"
				+ " Or are you saying you want to re-invent the mechanism by which Spring maps requests on to annotated-controllers? "
				+ "If so, have a look at the source for Spring's AnnotationMethodHandlerAdapter and DefaultAnnotationHandlerMapping classes. p<>"
				+ "It's complex, though - annotated controllers make it easier to write controllers, but the underlying mechanism is unpleasant.p");
		System.out.println(dto.getId());
		
		BlogPostDTO dto2 = blogService.createNewBlogPost(
				"Milos Milosevic",
				"Short Headline", 
				null,
				"You just call the method on the object. That's one of the big benefits of annotation-driven controllers,"
				+ " you can write just the methods your code needs without having to bend things out of shape to conform to the interfaces. p<>"
				+"You just call the method on the object. That's one of the big benefits of annotation-driven controllers,"
				+ " you can write just the methods your code needs without having to bend things out of shape to conform to the interfaces. p<>"
				+"You just call the method on the object. That's one of the big benefits of annotation-driven controllers,"
				+ " you can write just the methods your code needs without having to bend things out of shape to conform to the interfaces. p<>"
				+"You just call the method on the object. That's one of the big benefits of annotation-driven controllers,"
				+ " you can write just the methods your code needs without having to bend things out of shape to conform to the interfaces. p<>"
				+"You just call the method on the object. That's one of the big benefits of annotation-driven controllers,"
				+ " you can write just the methods your code needs without having to bend things out of shape to conform to the interfaces. p<>"
				+ "So just wire your controller bean into your code, and call the method directly.p<>"
				+ " Or are you saying you want to re-invent the mechanism by which Spring maps requests on to annotated-controllers? "
				+ "If so, have a look at the source for Spring's AnnotationMethodHandlerAdapter and DefaultAnnotationHandlerMapping classes. "
				+ "It's complex, though - annotated controllers make it easier to write controllers, but the underlying mechanism is unpleasant.p"
				+"You just call the method on the object. That's one of the big benefits of annotation-driven controllers,"
				+ " you can write just the methods your code needs without having to bend things out of shape to conform to the interfaces. p<>"
				+ "So just wire your controller bean into your code, and call the method directly.p<>"
				+ " Or are you saying you want to re-invent the mechanism by which Spring maps requests on to annotated-controllers? "
				+ "If so, have a look at the source for Spring's AnnotationMethodHandlerAdapter and DefaultAnnotationHandlerMapping classes. "
				+ "It's complex, though - annotated controllers make it easier to write controllers, but the underlying mechanism is unpleasant.p");
		System.out.println(dto2.getId());
		List<BlogPostDTO> posts = new ArrayList<>(Arrays.asList(dto,dto2));
		
		posts.stream().forEach(post -> {
			if(post != null) {
				System.out.println("Blog Post "+post.getId()+" is CREATED *********");
				
				//Publishing the BlogPost
				BlogPost blogPost = blogService.publishThePost(post.getId());
				if(blogPost != null) {
					System.out.println("Blog Post "+post.getId()+" is PUBLISHED *********");
				}
			}
		});
		BlogPostDTO dto1 = blogService.createNewBlogPost(
				"Petar Petrovic",
				"Headline", 
				null,
				"You just call the method on the object. That's one of the big benefits of annotation-driven controllers,"
				+ " you can write just the methods your code needs without having to bend things out of shape to conform to the interfaces. p<>"
				+ "So just wire your controller bean into your code, and call the method directly."
				+ "Or are you saying you want to re-invent the mechanism by which Spring maps requests on to annotated-controllers? p<>"
				+ "If so, have a look at the source for Spring's AnnotationMethodHandlerAdapter and DefaultAnnotationHandlerMapping classes. "
				+ "It's complex, though - annotated controllers make it easier to write controllers, but the underlying mechanism is unpleasant.p");
		System.out.println(dto1.getId());
			
		
		List<BlogPost> pBlogPosts = blogService.findAll();
		pBlogPosts.stream().forEach(post ->{
			System.out.println("searchBlogPosts POST ID:  "+ post.getId());
			System.out.println("POST HEADLINE: "+ post.getHeadline());
		});
		
		// Be back on this the pagination is bugging for some reason
		
//		List<BlogPostDTO> pBlogPosts2 = blogService.paginatedBlogPosts(true, false,null,  null, null, null, "three".compareTo("3"));
//		pBlogPosts2.stream().forEach(post ->{
//			System.out.println("findAll POST ID:  "+ post.getId());
//		});
		
		
		imageAlbumDTO testAlbum1 = albumService.createNewImageAlbum("portreti");
		System.out.println( "Test album 1 is created");
		imageAlbumDTO testAlbum2 = albumService.createNewImageAlbum("pejzazi");
		System.out.println( "Test album 2 is created");
		imageAlbumDTO testAlbum3 = albumService.createNewImageAlbum("renoviranje zadnje supe");
		System.out.println( "Test album 3 is created");
		imageAlbumDTO testAlbum4 = albumService.createNewImageAlbum("govno sa makom");
		System.out.println( "Test album 4 is created");
		
		//publishing albums 3 and 4
		List<imageAlbumDTO> albums3and4 = new ArrayList<>(Arrays.asList(testAlbum3,testAlbum4));
		albums3and4.stream().forEach(album -> {
			ImageAlbum publishedAlbum = albumService.publishTheAlbum(album.getId());
			if(publishedAlbum != null) {
				System.out.println("PUBLISHED ALBUM ID: "+publishedAlbum.getId());
			}
		});
		
		
		
		
		List<imageAlbumDTO> pImageAlbums = albumService.paginatedImageAlbums(false,false,null, null, null, 0);
		System.out.println("Paginated albums :");
		
		if(!pImageAlbums.isEmpty()) {
			
			pImageAlbums.stream().forEach(album ->{
				System.out.println("album: "+album.getAlbumName());
			});
		}else {
			System.out.println("list is empty | no results");
		}
		
		
	List<ImageDTO> slike = imageService.paginatedImages(false, false, null, null, 1L, 0);
	if(!slike.isEmpty()) {
		System.out.println("------- Slika je pronadjena ----------");
	}
	
	}	
	
	
}

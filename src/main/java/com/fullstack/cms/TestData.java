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
		
		
		
		
		
		imageAlbumDTO testAlbum1 = albumService.createNewImageAlbum("portreti");
		System.out.println( "Test album 1 is created");
		imageAlbumDTO testAlbum2 = albumService.createNewImageAlbum("pejzazi");
		System.out.println( "Test album 2 is created");
		imageAlbumDTO testAlbum3 = albumService.createNewImageAlbum("renoviranje zadnje supe");
		System.out.println( "Test album 3 is created");
		imageAlbumDTO testAlbum4 = albumService.createNewImageAlbum("strudla sa makom");
		System.out.println( "Test album 4 is created");
		
		//publishing albums 3 and 4
		List<imageAlbumDTO> albums3and4 = new ArrayList<>(Arrays.asList(testAlbum3,testAlbum4));
		albums3and4.stream().forEach(album -> {
			ImageAlbum publishedAlbum = albumService.publishTheAlbum(album.getId());
			if(publishedAlbum != null) {
				System.out.println("PUBLISHED ALBUM ID: "+publishedAlbum.getId());
			}
		});
		
		imageAlbumDTO testAlbum5 = albumService.createNewImageAlbum("Frausilovicka i 7 patuljaka");
		System.out.println( "Test album 5 is created");
		imageAlbumDTO testAlbum6 = albumService.createNewImageAlbum("Kvalitet");
		System.out.println( "Test album 6 is created");
		
		//softDelete albums 5 and 6
				List<imageAlbumDTO> albums5and6 = new ArrayList<>(Arrays.asList(testAlbum5,testAlbum6));
				albums5and6.stream().forEach(album -> {
					ImageAlbum softDeleted = albumService.softDelete(album.getId());
					if(softDeleted != null) {
						System.out.println("SOFT DELETED ALBUM ID: "+softDeleted.getId());
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

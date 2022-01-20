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

import com.fullstack.cms.DTO.imageAlbumDTO;
import com.fullstack.cms.model.ImageAlbum;
import com.fullstack.cms.service.ImageAlbumService;
import com.fullstack.cms.support.ImageAlbumToImageAlbumDTO;

@Controller
@RequestMapping(value = "/Albums")
public class ImageAlbumController {
	
	@Autowired
	private ImageAlbumService albumService;
	
	
	@Autowired 
	private ImageAlbumToImageAlbumDTO toAlbumDTO;
	
	@GetMapping
	@ResponseBody
	public Map<String,Object> index(
			@RequestParam(required = false)@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate publishFROM,
			@RequestParam(required = false)@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate publishTO,
			@RequestParam(required = false)String albumName,
			@RequestParam(required = false)Boolean publish,
			@RequestParam(required = false)Boolean softDelete,
			@RequestParam(required = false,defaultValue = "1")Integer pageNum){
		
		Map<String,Object> response = new LinkedHashMap<>();
		try {
			List<imageAlbumDTO> pImageAlbums = albumService.paginatedImageAlbums(publish,softDelete,publishFROM, publishTO, albumName, pageNum);
			
			if(!pImageAlbums.isEmpty()) {
				
			System.out.println(".....Response: ");
			pImageAlbums.stream().forEach(album -> {
				System.out.println(album.getAlbumName());
			});
				
				response.put("status", "200");
				response.put("albums", pImageAlbums);
				response.put("pageNum", pageNum);
				
			}else {
				
				response.put("status", "404");
			}
			
		} catch (Exception e) {
			response.put("status", "500");
			response.put("Exception", e.getMessage());
		}
		return response;
	}
	
	@PostMapping(value = "/Create")
	@ResponseBody
	public Map<String, Object> create (
			@RequestParam(required = true)String albumName){
		
		Map<String,Object> response = new HashMap<>();
		
		try {
			imageAlbumDTO dto = albumService.createNewImageAlbum(albumName);
			
			response.put("status", "201");
			response.put("newImageAlbum", dto);
			
		} catch (Exception e) {
			
			response.put("status", "401");
			response.put("exception", e.getMessage());
		}
		
		
		return response;
		
	}
	@PostMapping(value = "/Edit")
	@ResponseBody
	public Map<String,Object> edit (@RequestParam Long id,
			@RequestParam String albumName,
			@RequestParam Long coverImageId){
		
		Map<String,Object> response = new HashMap<>();
		
		try {
			ImageAlbum album = albumService.editTheAlbum(id,albumName,coverImageId);
			
			if(album != null) {
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
	public Map<String,Object> getOne (@RequestParam(required = true)Long id){
		
		Map<String,Object> response = new HashMap<>();
		
		try {
			ImageAlbum album = albumService.findOne(id);
			
			if(album != null) {
				imageAlbumDTO dto = toAlbumDTO.convert(album);
				
				response.put("status", "200");
				response.put("newBlogPost", dto);
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
			ImageAlbum updatedAlbum = null;
			
			if(publish) {
				updatedAlbum = albumService.publishTheAlbum(id);
			}else {
				updatedAlbum = albumService.undoPublishTheAlbum(id);
			}
			
			if(updatedAlbum != null) {
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
			@RequestParam boolean softDelete){
		
		Map<String,Object> response = new LinkedHashMap<>();
		
		try {
			ImageAlbum updatedAlbum = null;
			
			if(softDelete) {
				updatedAlbum = albumService.softDelete(id);
			}else {
				updatedAlbum = albumService.undoSoftDelete(id);
			}
			
			if(updatedAlbum != null) {
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
		
		ImageAlbum deletedAlbum = albumService.delete(id);
		if( deletedAlbum !=null ) {
			
			response.put("status", "200");
			return response;
		}
		response.put("status", "404");
		return response;
	}

}

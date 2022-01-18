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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fullstack.cms.DTO.ImageDTO;
import com.fullstack.cms.model.Image;
import com.fullstack.cms.service.ImageService;
import com.fullstack.cms.support.ImageToImageDTO;

@Controller
@RequestMapping(value = "/Images")
public class ImageController {
	
	@Autowired
	private ImageService imageService;
	
	@GetMapping
	@ResponseBody
	public Map<String,Object> index(
			@RequestParam(required = false)@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate publishFROM,
			@RequestParam(required = false)@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate publishTO,
			@RequestParam(required = false)Long imageAlbumId,
			@RequestParam(required = false)Boolean publish,
			@RequestParam(required = false)Boolean softDelete,
			@RequestParam(required = false,defaultValue = "1")Integer pageNum){
		
		Map<String,Object> response = new LinkedHashMap<>();
		try {
			List<ImageDTO> pImages = imageService.paginatedImages(publish,softDelete,publishFROM, publishTO, imageAlbumId, pageNum);
			
			if(!pImages.isEmpty()) {
				
				System.out.println(".....Response: ");
				pImages.stream().forEach(image -> {
					System.out.println(image.getFileName());
				});
				
				response.put("status", "200");
				response.put("images", pImages);
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
	
	
	@PostMapping(value = "/Create", consumes = {"multipart/form-data"})
	@ResponseBody
	public Map<String,Object> create(
			@RequestParam Long albumId,
			@RequestParam MultipartFile file){
		
		System.out.println("FILE ALBUM ID:" +albumId);
		
		
		Map<String,Object> response = new HashMap<>();
		
		try {
			imageService.uploadImageToS3(file,albumId);
			
			response.put("status", "201");
			
		} catch (Exception e) {
			response.put("status", "401");
			response.put("exception", e.getMessage());
		}
		
		
		return response;
	}
	
	//Changing the Image Album
	@PostMapping(value = "/Edit")
	@ResponseBody
	public Map<String,Object> edit(@RequestParam Long id,
			@RequestParam Long newImageAlbumId){
		Map<String,Object> response = new LinkedHashMap<>();
		
		try {
			Image updatedImage = imageService.changeImageAlbum(id,newImageAlbumId);
			
			if(updatedImage != null) {
				
				response.put("status", "200");
				
				return response;
			}else {
				response.put("status", "401");
				return response;
			}
			
		} catch (Exception e) {
			response.put("status", "401 || 500");
			response.put("exception", e.getMessage());
		}
		
		
		
		
		return response;
	}
	
	
	@GetMapping(value = "/Details")
	@ResponseBody
	public Map<String,Object> getOne(@RequestParam Long id){
		Map<String,Object> response = new LinkedHashMap<>();
		
		try {
			Image image = imageService.findOne(id);
			byte[] realImage = null;
			
			if(image != null) {
				
				realImage = imageService.downloadImageFromS3(image);
				response.put("status", "200");
				response.put("image", realImage);
				
			}
		} catch (Exception e) {
			response.put("status", "404");
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
			Image updatedImage = null;
			
			if(publish) {
				updatedImage = imageService.publishTheImage(id);
			}else {
				updatedImage = imageService.undoPublishTheImage(id);
			}
			
			if(updatedImage != null) {
				response.put("status", "200");
			}else {
				response.put("status", "401 || 500");
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
			Image updatedImage = null;
			
			if(softDelete) {
				updatedImage = imageService.softDelete(id);
			}else {
				updatedImage = imageService.undoSoftDelete(id);
			}
			
			if(updatedImage != null) {
				response.put("status", "200");
			}else {
				response.put("status", "401 || 500");
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
		
		Image deletedImage = imageService.delete(id);
		if(deletedImage !=null) {
			response.put("status", "200");
			return response;
		}
		response.put("status", "404");
		return response;
	}

}

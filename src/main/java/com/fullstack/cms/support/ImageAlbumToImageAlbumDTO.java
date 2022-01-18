package com.fullstack.cms.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.fullstack.cms.DTO.imageAlbumDTO;
import com.fullstack.cms.model.Image;
import com.fullstack.cms.model.ImageAlbum;
import com.fullstack.cms.service.ImageService;

@Component
public class ImageAlbumToImageAlbumDTO implements Converter<ImageAlbum, imageAlbumDTO>{
	
	@Autowired
	private ImageToImageDTO toImageDTO;
	
	@Autowired
	private ImageService imageService;

	@Override
	public imageAlbumDTO convert(ImageAlbum album) {
		
		imageAlbumDTO dto = new imageAlbumDTO();
		
		dto.setAlbumName(album.getAlbumName());
		dto.setId(album.getId());
		dto.setPublish(album.isPublish());
		dto.setSoftDelete(album.isSoftDelete());
		if(album.getPublishDate() != null) {
			dto.setPublishDate(album.getPublishDate().toString());
		}
		
		
		if(album.getCoverImageId() != null) {
			
			Image coverImage = imageService.findOne(album.getCoverImageId());
			
			dto.setCoverImage(
					toImageDTO.convert(coverImage,imageService.downloadImageFromS3(coverImage) 
							));
			
		}else {
			dto.setCoverImage(null);
		}
	
		
		return dto;
	}

	
	
}

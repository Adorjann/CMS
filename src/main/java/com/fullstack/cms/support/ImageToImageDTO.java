package com.fullstack.cms.support;



import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fullstack.cms.DTO.ImageDTO;
import com.fullstack.cms.model.Image;

@Component
public class ImageToImageDTO {
	
	
	public ImageDTO convert(Image image, byte[] realImage) {
		
		ImageDTO dto = new ImageDTO();
		
		dto.setAlbumId(image.getAlbum().getId());
		dto.setFileName(image.getFileName());
		dto.setId(image.getId());
		dto.setImagePath(image.getImagePath());
		dto.setPublish(image.isPublish());
		
		if(image.isPublish()) {
			dto.setPublishDate(image.getPublishDate().toString());
		}
		
		dto.setSoftDelete(image.isSoftDelete());
		dto.setRealPicture(realImage);
		
		return dto;
	}
	
	
	
	

}

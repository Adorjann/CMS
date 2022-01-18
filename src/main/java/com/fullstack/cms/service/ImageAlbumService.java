package com.fullstack.cms.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import com.fullstack.cms.DTO.imageAlbumDTO;
import com.fullstack.cms.model.ImageAlbum;

public interface ImageAlbumService {
	
	ImageAlbum findOne(Long id);
	
	List<ImageAlbum> findAll();
	
	List<ImageAlbum> findAllDeleted();
	
	ImageAlbum save (ImageAlbum album);
	
	ImageAlbum update (ImageAlbum album);
	
	ImageAlbum delete(Long id);
	
	ImageAlbum softDelete(Long id);
	
	ImageAlbum undoSoftDelete(Long id);
	
	ImageAlbum publishTheAlbum(Long id);
	
	ImageAlbum undoPublishTheAlbum(Long id);
	
	
	List<imageAlbumDTO> paginatedImageAlbums(Boolean publish, Boolean softDelete, LocalDate publishFROM,
			LocalDate publishTO, String albumName, Integer pageNum);

	imageAlbumDTO createNewImageAlbum(String albumName);

	ImageAlbum editTheAlbum(Long id, String albumName, Long coverImageId);


}

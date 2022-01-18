package com.fullstack.cms.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.fullstack.cms.DTO.ImageDTO;
import com.fullstack.cms.model.Image;

public interface ImageService {
	
	Image findOne(Long id);
	
	List<Image> findAll();
	
	List<Image> findAllDeleted();
	
	Image save(Image image);
	
	Image update(Image image);
	
	Image delete(Long id);
	
	Image softDelete(Long id);
	
	Image undoSoftDelete(Long id);
	
	Image publishTheImage (Long id);
	
	Image undoPublishTheImage (Long id);
	
	void uploadImageToS3(MultipartFile file,Long albumId);
	
	byte[] downloadImageFromS3(Image image);
	
	List<ImageDTO> paginatedImages(boolean publish, boolean softDelete,LocalDate publishFROM,LocalDate publishTO,Long imageAlbumId, int pageNum);
	
	Page<Image> search (boolean publish, boolean softDelete,LocalDate publishFROM,LocalDate publishTO,Long imageAlbumId, int pageNum);

	Image changeImageAlbum(Long id, Long newImageAlbumId);
}

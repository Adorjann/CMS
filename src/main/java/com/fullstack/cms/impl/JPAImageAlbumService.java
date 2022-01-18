package com.fullstack.cms.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;import org.springframework.dao.IncorrectUpdateSemanticsDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.amazonaws.services.simpleworkflow.flow.core.TryCatch;
import com.fullstack.cms.DTO.imageAlbumDTO;
import com.fullstack.cms.model.Image;
import com.fullstack.cms.model.ImageAlbum;
import com.fullstack.cms.repository.ImageAlbumRepository;
import com.fullstack.cms.service.ImageAlbumService;
import com.fullstack.cms.service.ImageService;
import com.fullstack.cms.support.ImageAlbumToImageAlbumDTO;

@Service
public class JPAImageAlbumService implements ImageAlbumService{
	
	@Autowired
	private ImageAlbumRepository albumRepository;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private ImageAlbumToImageAlbumDTO toAlbumDTO;

	@Override
	public ImageAlbum findOne(Long id) {

		Optional<ImageAlbum> album = albumRepository.findById(id);
		
		if(album.isPresent()) {
			return album.get();
		}
		return null;
	}

	@Override
	public List<ImageAlbum> findAll() {
		
		return albumRepository.findAll();
	}

	@Override
	public List<ImageAlbum> findAllDeleted() {
		
		return albumRepository.findAllDeleted();
	}

	@Override
	public ImageAlbum save(ImageAlbum album) {
		
		return albumRepository.save(album);
	}

	@Override
	public ImageAlbum update(ImageAlbum album) {
		
		return albumRepository.save(album);
	}

	@Override
	public ImageAlbum delete(Long id) {
		ImageAlbum album = findOne(id);
		
		if(album != null) {
			albumRepository.delete(album);
			return album;
		}
		return null;
		
	}

	@Override
	public ImageAlbum softDelete(Long id) {
		ImageAlbum album = findOne(id);
		
		if(album != null) {
			album.setSoftDelete(true);
			if(album.isPublish()) {
				undoPublishTheAlbum(album.getId());
			}
			//soft-deleting the images inside the album
			album.getImages().stream().forEach(image -> {
				imageService.softDelete(image.getId());
			});
			ImageAlbum updatedAlbum = update(album);
			
			if(updatedAlbum != null) {
				return updatedAlbum;
			}
		}
		return null;
	}
	
	@Override
	public ImageAlbum undoSoftDelete(Long id) {
		
		ImageAlbum album = findOne(id);
		
		if(album != null) {
			album.setSoftDelete(false);
			
			//UNSoft-deleting the images inside the album
			album.getImages().stream().forEach(image -> {
				imageService.undoSoftDelete(image.getId());
			});
			ImageAlbum updatedAlbum = update(album);
			
			if(updatedAlbum != null) {
				return updatedAlbum;
			}
		}
		
		return null;
	}

	@Override
	public ImageAlbum publishTheAlbum(Long id) {
		
		ImageAlbum album = findOne(id);
		LocalDate publishDate =  LocalDate.now();
		
		if(album != null) {
			
			if(!album.isPublish() && !album.isSoftDelete()) {
				
				try {
					
					if(!album.getImages().isEmpty()) {
						try {
							//publishing the images inside the album
							album.getImages().stream().forEach(image -> {
								Image publishedImage = imageService.publishTheImage(image.getId());
								if(publishedImage != null) {
									System.out.println("Image: "+publishedImage.getId()+" is published");
								}
						
							});
							
						}catch(Exception e) {
							System.out.println("Exception during publishing images");
							e.printStackTrace();
							throw new IllegalArgumentException("Exception during publishing images");
						}
					}
					
						album.setPublish(true);
						album.setPublishDate(publishDate);
						ImageAlbum updatedAlbum = update(album);
						
						
						if(updatedAlbum != null) {
							return updatedAlbum;
						}	
					
				}catch(Exception e){
					System.out.println("Exception during publishing the album");
					e.printStackTrace();
				}
			}
		}
		
		
		return null;
	}
	
	@Override
	public ImageAlbum undoPublishTheAlbum(Long id) {
		
		ImageAlbum album = findOne(id);
		if(album != null) {
			if(album.isPublish() && !album.isSoftDelete()) {
				try {
					
						//UNpublishing the images inside the album
						if(!album.getImages().isEmpty()) {
							try {
								
								album.getImages().stream().forEach(image -> {
									imageService.undoPublishTheImage(image.getId());
								});
								
							}catch(Exception e) {
								System.out.println("Exception during UNpublishing images");
								e.printStackTrace();
								throw new IllegalArgumentException("Exception during UNpublishing images");
							}
							
						}
					
						album.setPublish(false);
						album.setPublishDate(null);
						ImageAlbum updatedAlbum = update(album);
						
						if(updatedAlbum != null) {
							return updatedAlbum;
						}
					}catch(Exception e) {
						System.out.println("Exception during UNpublishing the album");
						e.printStackTrace();
					}	
			}
		}
		
		return null;
	}
	
	
	
	@Override
	public List<imageAlbumDTO> paginatedImageAlbums(Boolean publish, Boolean softDelete, LocalDate publishFROM,
			LocalDate publishTO, String albumName, Integer pageNum) {
		
		List<imageAlbumDTO> retVal = new LinkedList<imageAlbumDTO>();
		
		albumRepository.search(albumName, publishFROM, publishTO,softDelete,publish,PageRequest.of(pageNum, 10)).get().forEach(album -> {
			retVal.add(toAlbumDTO.convert(album));
		});
		
		return retVal;
		
	}

	@Override
	public imageAlbumDTO createNewImageAlbum(String albumName) {
		
		
		ImageAlbum album = new ImageAlbum();
		
		album.setAlbumName(albumName);
		album.setPublish(false);
		album.setSoftDelete(false);
		album.setPublishDate(null);
		album.setCoverImageId(null);
		
		ImageAlbum savedImageAlbum = save(album);
		return toAlbumDTO.convert(savedImageAlbum);
	}

	@Override
	public ImageAlbum editTheAlbum(Long id, String albumName, Long coverImageId) {
		
		ImageAlbum album = findOne(id);
		album.setAlbumName(albumName);
		
		//checking is coverImageId valid
		Image image = imageService.findOne(coverImageId);
		if(image != null) {
			album.setCoverImageId(coverImageId);
		}
		
		ImageAlbum updatedAlbum = update(album);
		if(updatedAlbum != null) {
			return updatedAlbum;
		}
		return null;
	}

	
	
	

	
	
	

}

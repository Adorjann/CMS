package com.fullstack.cms.impl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.util.IOUtils;
import com.fullstack.cms.AwsBucket.BucketName;
import com.fullstack.cms.DTO.ImageDTO;
import com.fullstack.cms.fileStore.FileStore;
import com.fullstack.cms.model.Image;
import com.fullstack.cms.model.ImageAlbum;
import com.fullstack.cms.repository.ImageRepository;
import com.fullstack.cms.service.ImageAlbumService;
import com.fullstack.cms.service.ImageService;
import com.fullstack.cms.support.ImageToImageDTO;

@Service
public class JPAImageService implements ImageService{
	
	@Autowired
	private ImageRepository repository;
	
	@Autowired
	private ImageAlbumService albumService;
	
	@Autowired
	private ImageToImageDTO toImageDTO;
	
	@Autowired 
	private FileStore filestore;

	@Override
	public Image findOne(Long id) {
		
		Optional<Image> image = repository.findById(id);
		if(image.isPresent()) {
			return image.get();
		}
		return null;
	}

	@Override
	public List<Image> findAll() {
		
		return repository.findAll();
	}
	@Override
	public List<Image> findAllDeleted() {
		
		return repository.findAllDeleted();

	}

	@Override
	public Image save(Image image) {
		
		return repository.save(image);
	}

	@Override
	public Image update(Image image) {
		
		return repository.save(image);
	}

	@Override
	public Image delete(Long id) {
		Image image = findOne(id);
		
		if(image != null && deleteImageOnS3(image)) {
			repository.delete(image);
			return image;
		}
		return null;
	}

	@Override
	public Image softDelete(Long id) {
		Image image = findOne(id);
		
		if(image != null) {
			
			if(image.isPublish()) {
				undoPublishTheImage(image.getId());
			}
			
			image.setSoftDelete(true);
			Image updatedImage = update(image);
			
			if(updatedImage != null) {
				return updatedImage;
			}
		}
		
		return null;
	}
	
	@Override
	public Image undoSoftDelete(Long id) {
		
		Image image = findOne(id);
		
		if(image != null) {
			
			image.setSoftDelete(false);
			Image updatedImage = update(image);
			
			if(updatedImage != null) {
				return updatedImage;
			}
		}
		
		return null;
	}

	@Override
	public Image publishTheImage(Long id) {
		
		Image image = findOne(id);
		LocalDate publishDate =  LocalDate.now();
		
		if(!image.isPublish() && !image.isSoftDelete()) {
			
			image.setPublish(true);
			image.setPublishDate(publishDate);
			
			Image updatedImage = update(image);
			if(updatedImage != null) {
				return updatedImage;
			}
		}
		
		return null;
	}
	public Image publishTheImage(Image image) {
		
		LocalDate publishDate =  LocalDate.now();
		
		if(!image.isPublish() && !image.isSoftDelete()) {
			
			image.setPublish(true);
			image.setPublishDate(publishDate);
			
			Image updatedImage = update(image);
			if(updatedImage != null) {
				return updatedImage;
			}
		}
		
		return null;
	}
	
	@Override
	public Image undoPublishTheImage(Long id) {
		
		Image image = findOne(id);
		
		if(image.isPublish() && !image.isSoftDelete()) {
			
			image.setPublish(false);
			image.setPublishDate(null);
			
			Image updatedImage = update(image);
			if(updatedImage != null) {
				return updatedImage;
			}
		}
		
		return null;
	}
	

	@Override
	public Page<Image> search(boolean publish,boolean softDelete,LocalDate publishFROM, LocalDate publishTO, Long imageAlbumId, int pageNum) {
		
		return repository.search(publishFROM,publishTO,imageAlbumId,softDelete,publish,PageRequest.of(pageNum,10));
	}
	
	

	@Override
	public void uploadImageToS3(MultipartFile file, Long albumId) {
		ImageAlbum album = albumService.findOne(albumId);
		
		if((file.isEmpty()) || (album == null))  {
			throw new IllegalStateException("Cannot upload emty file [ "+file.getSize()+" ]");
		}
		if(album.isSoftDelete()) {
			throw new IllegalStateException("Cannot upload image to deleted album.");	
		}
		
		//checking file type
		if(!Arrays.asList(ContentType.IMAGE_JPEG.getMimeType(),
						  ContentType.IMAGE_PNG.getMimeType())
						  .contains(file.getContentType())) {
			
			throw new IllegalStateException("File must be an Image jpg or png");
		}
		//collecting metadata
		Map<String,String> metadata = new HashMap<>();
		metadata.put("Content-Type", file.getContentType());
		metadata.put("dataLength", String.valueOf(file.getSize()));
		
		
		
		
		//storing the image
		
		String path = String.format("%s/%s", BucketName.IMAGE_STORAGE.getBucketName(),albumId);
		String fileName = file.getOriginalFilename();
		
		try {
			//TODO debugg this compression
//			//Image resize *** testing feature 
//			BufferedImage original = ImageIO.read(file.getInputStream());
//			BufferedImage resized = new BufferedImage(200, 200, original.getType());
//			Graphics2D g2 = resized.createGraphics();
//			g2.drawImage(original, 0, 0, 200, 200, null);
//			g2.dispose();
//			
//			ByteArrayOutputStream os = new ByteArrayOutputStream();
//			ImageIO.write(resized,"jpeg",os);
//			InputStream is = new ByteArrayInputStream(os.toByteArray());
//			//Image resize ***
			
			
			//actual saving to S3
			//filestore.save(path, fileName, Optional.of(metadata), file.getInputStream());
			
			File file2 = new File(file.getOriginalFilename());
			byte[] fileContent = file.getBytes();
			Path path2 = Paths.get(file2.getAbsolutePath());
			
			try {
				Files.write(path2, fileContent);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			filestore.save(path, fileName, file2);
			
			//is.close();
			
			//creating a model for the image and persisting to the database
			Image image = new Image();
			
			System.out.println("ALBUM NAME: "+album.getAlbumName()+", PICTURE UPLOADED.");
			image.setImagePath(path);
			image.setFileName(fileName);
			image.setSoftDelete(false);
			image.setAlbum(album);
			
			Image savedImage = save(image);
			
			if(album.isPublish()) {
				publishTheImage(savedImage);
			}
			
			
			
			
		} catch (IOException e) {
			throw new IllegalStateException("Exception during filestore.save() ");
		}
		
		
	}

	@Override
	public byte[] downloadImageFromS3(Image image) {
		
		try {
			
			return filestore.download(image.getImagePath(), image.getFileName());
			
		} catch (IllegalStateException e) {
			System.out.println("downloadImageFromS3 throwing exception: " +e);
			return new byte[0];
		}
		
		
	}
	
	private String getImageKeyFromImage(Image image) {
		String[] tokens = image.getImagePath().split("/");
		
		return tokens[1];
	}
	private boolean deleteImageOnS3(Image image) {
		
		String imageKeyPrefix = getImageKeyFromImage(image);
		
		if(image != null) {
			try {
				return filestore.deleteObject(imageKeyPrefix+"/"+image.getFileName());
				
			}catch(Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	@Override
	public List<ImageDTO> paginatedImages(boolean publish,boolean softDelete,LocalDate publishFROM, LocalDate publishTO, Long imageAlbumId, int pageNum) {
		
		List<ImageDTO> retVal = new LinkedList<ImageDTO>();
		
		Page<Image> pImages = search(publish,softDelete,publishFROM, publishTO, imageAlbumId, pageNum);
		
		if(!pImages.isEmpty()) {
			
			pImages.get().forEach(image -> {
				ImageDTO dto = toImageDTO.convert(image, downloadImageFromS3(image));
				retVal.add(dto);
				
			});
		}
		return retVal;
		
	}

	@Override
	public Image changeImageAlbum(Long id, Long newImageAlbumId) {
		
		if(newImageAlbumId == null) {
			return null;
		}
		Image image = findOne(id);
		
		ImageAlbum  newAlbum = albumService.findOne(newImageAlbumId); 
		
		image.setAlbum(newAlbum);
		
		Image updatedImage = update(image);
		if(updatedImage != null) {
			return updatedImage;
		}
		return null;
	}

	
	
	

}

package com.fullstack.cms.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Image {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private boolean publish;
	
	@Column
	private boolean softDelete;
	
	@Column
	private LocalDate publishDate;
	
	@Column
	private String imagePath;
	
	@Column 
	private String fileName;
	
	@ManyToOne
	private ImageAlbum album;

	
	public Image(Long id, boolean publish, boolean softDelete, LocalDate publishDate, String imagePath, String fileName,
			ImageAlbum album) {
		super();
		this.id = id;
		this.publish = publish;
		this.softDelete = softDelete;
		this.publishDate = publishDate;
		this.imagePath = imagePath;
		this.fileName = fileName;
		this.album = album;
	}

	public Image() {
		
	}

	

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ImageAlbum getAlbum() {
		return album;
	}

	public void setAlbum(ImageAlbum album) {
		this.album = album;
		
		if(this.album != null && 
				!this.album.getImages().contains(this)) {
			this.album.addImage(this);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	

	public boolean isPublish() {
		return publish;
	}

	public void setPublish(boolean publish) {
		
			this.publish = publish;
		
		
		
	}

	public boolean isSoftDelete() {
		return softDelete;
	}

	public void setSoftDelete(boolean softDelete) {
		this.softDelete = softDelete;
	}

	

	public LocalDate getPublishDate() {
		return publishDate;
	}



	public void setPublishDate(LocalDate publishDate) {
		this.publishDate = publishDate;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Image other = (Image) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}

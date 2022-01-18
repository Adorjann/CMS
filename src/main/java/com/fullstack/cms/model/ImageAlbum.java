package com.fullstack.cms.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ImageAlbum {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private boolean publish;
	
	@Column
	private String albumName;
	
	@Column
	private boolean softDelete;
	
	@Column
	private LocalDate publishDate;
	
	@Column
	private Long coverImageId;
	
	@OneToMany(mappedBy = "album",
			cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private List<Image> images = new ArrayList<>();

	
	
	public ImageAlbum(Long id, boolean publish, String albumName, boolean softDelete, LocalDate publishDate,
			Long coverImageId, List<Image> images) {
		super();
		this.id = id;
		this.publish = publish;
		this.albumName = albumName;
		this.softDelete = softDelete;
		this.publishDate = publishDate;
		this.coverImageId = coverImageId;
		this.images = images;
	}

	public ImageAlbum() {
	}
	
	public String getAlbumName() {
		return albumName;
	}



	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}





	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	



	public Long getCoverImageId() {
		return coverImageId;
	}

	public void setCoverImageId(Long coverImageId) {
		this.coverImageId = coverImageId;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}
	

	public void addImage(Image image) {
		images.add(image);
		if(!image.getAlbum().equals(this)) {
			image.setAlbum(this);
		}
		
	}
	public void removeImage(Image image) {
		images.remove(image);
		image.setAlbum(null);
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
		ImageAlbum other = (ImageAlbum) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}

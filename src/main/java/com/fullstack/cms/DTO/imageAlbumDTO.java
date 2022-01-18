package com.fullstack.cms.DTO;


public class imageAlbumDTO {
	
	private Long id;
	
	private boolean publish;
	
	private boolean softDelete;
	
	private String albumName;
	
	private String publishDate;
	
	private ImageDTO coverImage;
	
	
	public imageAlbumDTO(Long id, boolean publish, boolean softDelete, String albumName, String publishDate,
			ImageDTO coverImage) {
		super();
		this.id = id;
		this.publish = publish;
		this.softDelete = softDelete;
		this.albumName = albumName;
		this.publishDate = publishDate;
		this.coverImage = coverImage;
	}


	public imageAlbumDTO() {
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

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}


	public ImageDTO getCoverImage() {
		return coverImage;
	}


	public void setCoverImage(ImageDTO coverImage) {
		this.coverImage = coverImage;
	}


}

package com.fullstack.cms.DTO;





public class ImageDTO{
	
	private byte[] realPicture;

	private long id;
	
	private boolean publish;
	
	private boolean softDelete;
	
	private String publishDate;
	
	private String imagePath;
	
	private String fileName;
	
	private Long albumId;

	
	
	public ImageDTO(byte[] realPicture, long id, boolean publish, boolean softDelete, String publishDate,
			String imagePath, String fileName, Long albumId) {
		super();
		this.realPicture = realPicture;
		this.id = id;
		this.publish = publish;
		this.softDelete = softDelete;
		this.publishDate = publishDate;
		this.imagePath = imagePath;
		this.fileName = fileName;
		this.albumId = albumId;
	}

	public ImageDTO() {}

	public byte[] getRealPicture() {
		return realPicture;
	}

	public void setRealPicture(byte[] realPicture) {
		this.realPicture = realPicture;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Long albumId) {
		this.albumId = albumId;
	};
	
	
	
	
	

}

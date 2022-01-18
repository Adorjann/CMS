package com.fullstack.cms.DTO;

public class BlogPostDTO {
	
	private Long id;
	
	private String author;
	
	private String headline;
	
	private boolean publish;
	
	private boolean softDelete;
	
	private String publishDate;
	
	private Long albumID;
	
	private String blogText;

	public BlogPostDTO() {
		
	}

	public BlogPostDTO(Long id, String author, String headline, boolean publish, boolean softDelete, String publishDate,
			Long albumID, String blogText) {
		super();
		this.id = id;
		this.author = author;
		this.headline = headline;
		this.publish = publish;
		this.softDelete = softDelete;
		this.publishDate = publishDate;
		this.albumID = albumID;
		this.blogText = blogText;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
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

	public Long getAlbumID() {
		return albumID;
	}

	public void setAlbumID(Long albumID) {
		this.albumID = albumID;
	}

	public String getBlogText() {
		return blogText;
	}

	public void setBlogText(String blogText) {
		this.blogText = blogText;
	}
	
	
	

}

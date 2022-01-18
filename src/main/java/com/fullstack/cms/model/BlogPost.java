package com.fullstack.cms.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class BlogPost {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String autor;
	
	@Column
	private String headline;
	
	@Column
	private boolean publish;
	
	@Column
	private boolean softDelete;
	
	@Column
	private LocalDate publishDate;
	
	@OneToOne
	@JoinColumn(name = "album_id",referencedColumnName = "id")
	private ImageAlbum album;
	
	@Column(columnDefinition = "text")
    private String blogText;

	public BlogPost(Long id, String autor, String headline, boolean publish, boolean softDelete, LocalDate publishDate,
			ImageAlbum album, String blogText) {
		super();
		this.id = id;
		this.autor = autor;
		this.headline = headline;
		this.publish = publish;
		this.softDelete = softDelete;
		this.publishDate = publishDate;
		this.album = album;
		this.blogText = blogText;
	}





	public BlogPost() {
	}





	public String getHeadline() {
		return headline;
	}



	public void setHeadline(String headline) {
		this.headline = headline;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public boolean isPublish() {
		return publish;
	}

	public void setPublish(boolean publish) {
		this.publish = publish;
	}

	

	public LocalDate getPublishDate() {
		return publishDate;
	}





	public void setPublishDate(LocalDate publishDate) {
		this.publishDate = publishDate;
	}





	public String getBlogText() {
		return blogText;
	}

	public void setBlogText(String blogText) {
		this.blogText = blogText;
	}
	
	

	public boolean isSoftDelete() {
		return softDelete;
	}

	public void setSoftDelete(boolean softDelete) {
		this.softDelete = softDelete;
	}

	public ImageAlbum getAlbum() {
		return album;
	}

	public void setAlbum(ImageAlbum album) {
		this.album = album;
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
		BlogPost other = (BlogPost) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
}

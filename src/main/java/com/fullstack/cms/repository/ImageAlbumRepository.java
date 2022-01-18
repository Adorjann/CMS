package com.fullstack.cms.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fullstack.cms.model.ImageAlbum;

@Repository
public interface ImageAlbumRepository extends JpaRepository<ImageAlbum, Long>{
	
	@Query("SELECT ia FROM ImageAlbum ia WHERE "
			+" ia.softDelete = false")
	List<ImageAlbum> findAll();
	
	@Query("SELECT ia FROM ImageAlbum ia WHERE "
			+" ia.softDelete = true")
	List<ImageAlbum> findAllDeleted();
	
	
	
	
	@Query("SELECT ia FROM ImageAlbum ia WHERE "
			+"(:publishFrom IS NULL OR ia.publishDate >= :publishFrom) AND "
			+"(:publishTO IS NULL OR ia.publishDate <= :publishTO) AND "
			+"(:albumName IS NULL OR ia.albumName LIKE %:albumName%) AND "
			+"(ia.softDelete = :softDelete) AND "
			+"(ia.publish = :publish)")
	Page<ImageAlbum> search(
			@Param("albumName") String albumName,
			@Param("publishFrom")LocalDate publishFROM,
			@Param("publishTO") LocalDate publishTO,
			@Param("softDelete")Boolean softDelete,
			@Param("publish")Boolean publish,
			Pageable pageable);
	
	

}

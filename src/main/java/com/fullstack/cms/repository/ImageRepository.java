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

import com.fullstack.cms.model.Image;


@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{

	
	@Query("SELECT i FROM Image i WHERE "
			+" i.softDelete = false")
	List<Image> findAll();
	
	@Query("SELECT i FROM Image i WHERE"
			+" i.softDelete = true ")	
	List<Image> findAllDeleted();
	
	
	@Query("SELECT i FROM Image i WHERE"
			+"(:publishFROM IS NULL OR i.publishDate >= :publishFROM) AND "
			+"(:publishTO IS NULL OR i.publishDate <= :publishTO) AND "
			+"(:imageAlbumId IS NULL OR i.album.id = :imageAlbumId) AND "
			+"(i.softDelete = :softDelete) AND "
			+"(i.publish = :publish)")
	Page<Image> search(@Param("publishFROM")LocalDate publishFROM,
			@Param("publishTO") LocalDate publishTO,
			@Param("imageAlbumId")Long imageAlbumId,
			@Param("softDelete") boolean softDelete,
			@Param("publish") boolean publish,
			Pageable pageable);
	
	
}

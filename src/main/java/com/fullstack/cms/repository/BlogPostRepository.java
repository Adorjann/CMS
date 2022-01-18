package com.fullstack.cms.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fullstack.cms.model.BlogPost;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long>{
	
	@Query("SELECT bp FROM BlogPost bp WHERE "
		+" (bp.headline LIKE %:headline%) AND "
			+" (bp.softDelete = false)")
	List<BlogPost> searchHeadline(@Param("headline") String headline);
	
	@Query("SELECT bp FROM BlogPost bp WHERE"
			+" (:publishFROM = NULL OR bp.publishDate >= :publishFROM) AND "
			+" (:publishTO = NULL OR bp.publishDate <= :publishTO) AND " 
			+" (:headline = NULL OR bp.headline LIKE %:headline%) AND "
			+" (:content = NULL OR bp.blogText LIKE %:content%) AND "
			+" (bp.softDelete = :softDelete) AND "
			+" (bp.publish = :publish) ")
	Page<BlogPost> search(@Param("publishFROM") LocalDate publishFROM,
			@Param("publishTO") LocalDate publishTO,
			@Param("headline") String headline, 
			@Param("content") String content,
			@Param("publish") Boolean publish,
			@Param("softDelete") Boolean softDelete,
			Pageable pageable);
	
	@Query("SELECT bp FROM BlogPost bp WHERE"
			+" (:publishFROM = NULL OR bp.publishDate >= :publishFROM) AND "
			+" (:publishTO = NULL OR bp.publishDate <= :publishTO) AND " 
			+" (:headline = NULL OR bp.headline LIKE %:headline%) AND "
			+" (:content = NULL OR bp.blogText LIKE %:content%) AND "
			+" (bp.softDelete = :softDelete) AND "
			+" (bp.publish = :publish) ")
	List<BlogPost> searchPosts(@Param("publishFROM") LocalDate publishFROM,
			@Param("publishTO") LocalDate publishTO,
			@Param("headline") String headline,
			@Param("content") String content,
			@Param("publish") Boolean publish,
			@Param("softDelete") Boolean softDelete);
	
	@Query("SELECT bp FROM BlogPost bp WHERE"
		+" bp.softDelete = false ")
	List<BlogPost> findAll();
	
	@Query("SELECT bp FROM BlogPost bp WHERE"
			+" bp.softDelete = true ")
		List<BlogPost> findAllDeleted();
	
}

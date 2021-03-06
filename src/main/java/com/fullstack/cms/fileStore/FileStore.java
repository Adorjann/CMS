package com.fullstack.cms.fileStore;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.fullstack.cms.AwsBucket.BucketName;
import com.fullstack.cms.model.Image;


@Service
public class FileStore {
	
	private final AmazonS3 s3;
	
	@Autowired
	public FileStore(AmazonS3 s3) {
		this.s3 = s3;
	}
	
	
	// upload to the amazon s3
	public void save(String path,
					String fileName,
					Optional<Map<String, String>> optionalMetadata,
					InputStream inputStream) {
		
		ObjectMetadata metadata = new ObjectMetadata();
		
		optionalMetadata.ifPresent(map ->{
			if(!map.isEmpty()) {
				map.forEach((key, value)-> metadata.addUserMetadata(key,value));
			}
		});
		
		
		
		
		try {
			s3.putObject(path, fileName, inputStream, metadata);
			
		} catch (AmazonServiceException e) {
			System.out.println("----- amazon exteption"+e);
			throw new IllegalStateException("failed to store to s3"+ e);
		}
		
		
	}
	//download from amazon s3
	public byte[] download(String path, String fileName) {
		try {
			
			S3Object object = s3.getObject(path, fileName);
			S3ObjectInputStream inputStream = object.getObjectContent();
			
			byte[] picture = IOUtils.toByteArray(inputStream);
			
			
			inputStream.close();
			return picture;
			
		} catch (Exception e) {
			throw new IllegalStateException("Failed to download file from s3",e);
		}
		
		
	}
	public void save(String path, String fileName, File file) {
		
		try {
			s3.putObject(path, fileName, file);
			
		}  catch (AmazonServiceException e) {
			System.out.println("----- amazon exteption"+e);
			throw new IllegalStateException("failed to store to s3"+ e);
		}
		
	}


	public boolean deleteObject(String key) {
		
		try {
			s3.deleteObject(BucketName.bucketName,key);
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		
	}

}

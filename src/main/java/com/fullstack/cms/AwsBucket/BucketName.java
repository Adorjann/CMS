package com.fullstack.cms.AwsBucket;

//Containing the actual Bucket name where the images will be storaged on aws;

public enum BucketName {
	
	IMAGE_STORAGE("demo-buket-cms");
	
	private final String bucketName;
	
	BucketName(String bucketName){
		this.bucketName = bucketName;
	}
	
	public String getBucketName() {
		return bucketName;
	}

}

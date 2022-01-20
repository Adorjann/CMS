package com.fullstack.cms.AwsBucket;

//Containing the actual Bucket name where the images will be storaged on aws;

public enum BucketName {
	
	IMAGE_STORAGE("demo-buket-cms");
	
	public static final String bucketName = "demo-buket-cms";
	
	BucketName(String bucketName){
//		this.bucketName = bucketName;
	}
	
	public String getBucketName() {
		return bucketName;
	}

}

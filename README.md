# CMS
Full Stack Content Management System (work in progress)

To run this app:</br>
1. In the application.properties:
   - change the credentials to your PostgreSql db
   - IF you want to use MySql, uncomment the MySql settings and enter your credentials,</br>
   also uncoment the proper Hibernate dialect for MySql and last in Pom.xml, uncomment the MySql dependecy
2. In the AmazonConfig.java:   
   - insert the access key as well as the secret key to your Aws s3</br>otherwise you wount be able to upload/download images
   
    
			

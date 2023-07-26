# synonyms-register
SpringBoot3 REST api for registering synonyms using MySQL db for storage and Keycloak for authentication.  
- The register stores words with their synonyms.
- Every word could be a synonym to another word. For example, If "wash" is a synonym to "clean", then the api should return “clean” as result when we ask for the “wash” synonyms and vice versa.
- A word may have multiple synonyms.
- The register should support something we call a transitive rule. It assumes the following implementation, i.e. if "B" is a synonym to "A" and "C" a synonym to "B", then "C" should automatically, by transitive rule, also be the synonym for "A".

---------
### How to run locally
1. Clone the project and navigate to synonyms-register project folder in terminal
2. Make sure you have docker set up on your local machine
3. Run `docker-compose up -d`  
   (this will start keycloak container, import realm and start db container)
4. Run `docker exec -it mysqldb mysql -uroot -proot` to access mysql shell
5. Run `UPDATE mysql.user SET host='%' WHERE user='root';` and `FLUSH PRIVILEGES;`  
   (this is to enable connection to mysql db from any host, obviously it should be used only in local setup)
6. Create two users, `userreader` and `userwriter` in keycloak admin console. If you call users differently, make sure to update postman collection.  
- Open http://localhost:8080/admin in your browser and login with username `admin` and password `admin`
- Select `ree_dzenan` realm in left upper dropdown 
- Navigate to `Users` in `Manage` menu section
- Clic on `Add User` button and enter `userreader` as `Username` and click `Create`
- Go to `Credentials` tab and `Set Password` to `password`, turn off `Temporary` flag (or in case of different password make sure to update postman collection later)
- Go to `Role mapping` tab and select `app_reader` role in  `Assign Role` modal
- Do the same steps for `userwriter` and assign `app_writer` role to it
7. Run `mvn clean package`
8. Run `java -jar target/synonyms-register-1.0.0-SNAPSHOT.jar`
9. Import `ree_dzenan.postman_collection.json` into postman, generate token for both users and test endpoints

---------

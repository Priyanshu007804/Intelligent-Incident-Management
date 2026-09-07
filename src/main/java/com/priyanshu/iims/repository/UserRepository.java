package com.priyanshu.iims.repository;
import  org.springframework.data.mongodb.repository.MongoRepository;
import com.priyanshu.iims.model.User;
public interface UserRepository extends MongoRepository <User,String>{
	User findByEmail(String email);

}

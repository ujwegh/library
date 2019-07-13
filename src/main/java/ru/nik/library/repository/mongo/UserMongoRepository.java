package ru.nik.library.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.nik.library.domain.User;

public interface UserMongoRepository extends MongoRepository<User, String> {

}

package com.bank.config;

import com.bank.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
/**

 The @UserIndexCreator class is responsible for creating an index for the userName field in the User collection.
 This index improves the performance of queries that involve searching or sorting based on the userName.
 By creating this index, database operations related to the userName field will be faster and more efficient.
 @author Lakhan Singh
 */
@Component
public class UserIndexCreator {
    private final ReactiveMongoOperations mongoOperations;
    @Autowired
    public UserIndexCreator(ReactiveMongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @PostConstruct
    public void createIndexes() {
        mongoOperations.indexOps(Users.class)
                .ensureIndex(new Index().unique().on("userName", Sort.Direction.ASC))
                .subscribe();
    }
}

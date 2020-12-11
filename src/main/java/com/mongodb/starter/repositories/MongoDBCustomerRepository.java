package com.mongodb.starter.repositories;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.WriteModel;
 
import com.mongodb.starter.models.CustomerParameter;
import org.bson.BsonDocument;
 
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

 
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
 
import static com.mongodb.client.model.ReturnDocument.AFTER;
 

@Repository
public class MongoDBCustomerRepository implements CustomerRepository {

    private static final TransactionOptions txnOptions = TransactionOptions.builder()
                                                                           .readPreference(ReadPreference.primary())
                                                                           .readConcern(ReadConcern.MAJORITY)
                                                                           .writeConcern(WriteConcern.MAJORITY)
                                                                           .build();
    @Autowired
    private MongoClient Customer;
    private MongoCollection<CustomerParameter> customerCollection;

    @PostConstruct
    void init() {
    	customerCollection = Customer.getDatabase("test").getCollection("Customers", CustomerParameter.class);
    }

    @Override
    public CustomerParameter save(CustomerParameter customerParameter) {
        customerParameter.setId(new ObjectId());
        customerCollection.insertOne(customerParameter);
        return customerParameter;
    }

    @Override
    public List<CustomerParameter> saveAll(List<CustomerParameter> customerParameters) {
        try (ClientSession clientSession = Customer.startSession()) {
            return clientSession.withTransaction(() -> {
                customerParameters.forEach(p -> p.setId(new ObjectId()));
                customerCollection.insertMany(clientSession, customerParameters);
                return customerParameters;
            }, txnOptions);
        }
    }

    @Override
    public List<CustomerParameter> findAll() {
        return customerCollection.find().into(new ArrayList<>());
    }

    @Override
    public List<CustomerParameter> findAll(List<String> ids) {
        return customerCollection.find(in("_id", mapToObjectIds(ids))).into(new ArrayList<>());
    }

    @Override
    public CustomerParameter findOne(String id) {
        return customerCollection.find(eq("_id", new ObjectId(id))).first();
    }

    @Override
    public long count() {
        return customerCollection.countDocuments();
    }

    @Override
    public long delete(String id) {
        return customerCollection.deleteOne(eq("_id", new ObjectId(id))).getDeletedCount();
    }

    @Override
    public long delete(List<String> ids) {
        try (ClientSession clientSession = Customer.startSession()) {
            return clientSession.withTransaction(
                    () -> customerCollection.deleteMany(clientSession, in("_id", mapToObjectIds(ids))).getDeletedCount(),
                    txnOptions);
        }
    }

    @Override
    public long deleteAll() {
        try (ClientSession clientSession = Customer.startSession()) {
            return clientSession.withTransaction(
                    () -> customerCollection.deleteMany(clientSession, new BsonDocument()).getDeletedCount(), txnOptions);
        }
    }

    @Override
    public CustomerParameter update(CustomerParameter customerParameter) {
        FindOneAndReplaceOptions options = new FindOneAndReplaceOptions().returnDocument(AFTER);
        return customerCollection.findOneAndReplace(eq("_id", customerParameter.getId()), customerParameter, options);
    }

    @Override
    public long update(List<CustomerParameter> customerParameters) {
        List<WriteModel<CustomerParameter>> writes = customerParameters.stream()
                                                 .map(p -> new ReplaceOneModel<>(eq("_id", p.getId()), p))
                                                 .collect(Collectors.toList());
        try (ClientSession clientSession = Customer.startSession()) {
            return clientSession.withTransaction(
                    () -> customerCollection.bulkWrite(clientSession, writes).getModifiedCount(), txnOptions);
        }
    }

   

    private List<ObjectId> mapToObjectIds(List<String> ids) {
        return ids.stream().map(ObjectId::new).collect(Collectors.toList());
    }

	 
}

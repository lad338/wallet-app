package com.example.walletapp.repository;

import com.example.walletapp.model.repository.Transaction;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, ObjectId> {
  List<Transaction> getTransactionsByUserIdOrTargetIdOrderByExecutedAt(
    String userId,
    String targetId
  );
}

package com.watchtogether.watchtogether.history.point.repository;

import com.watchtogether.watchtogether.history.point.entity.PointTransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<PointTransactionHistory,Long> {

}

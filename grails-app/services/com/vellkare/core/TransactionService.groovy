package com.vellkare.core

import org.springframework.transaction.TransactionStatus


class TransactionService {

  def rollback(TransactionStatus status) {
    status.setRollbackOnly();
  }
}

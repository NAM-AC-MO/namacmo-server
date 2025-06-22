package com.payment.walletservice.v1.adapter.out.persistence.repository

import com.namacmo.appcommon.domain.valueobject.Money
import com.payment.walletservice.v1.adapter.out.persistence.entity.JpaWalletMapper
import com.payment.walletservice.v1.adapter.out.persistence.exception.RetryExhaustedWithOptimisticLockingFailureException
import com.payment.walletservice.v1.application.service.SettlementService
import com.payment.walletservice.v1.domain.model.Wallet
import org.slf4j.LoggerFactory
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.stereotype.Repository
import org.springframework.transaction.support.TransactionTemplate

@Repository
class JpaWalletRepository (
  private val springDataJpaWalletRepository: SpringDataJpaWalletRepository,
  private val jpaWalletMapper: JpaWalletMapper,
  private val walletTransactionRepository: WalletTransactionRepository,
  private val transactionTemplate: TransactionTemplate
) : WalletRepository {
  private val log = LoggerFactory.getLogger(JpaWalletRepository::class.java)


  override fun getWallets(sellerIds: Set<Long>): Set<Wallet> {
    return springDataJpaWalletRepository.findByUserIdIn(sellerIds)
      .map { jpaWalletMapper.mapToDomainEntity(it) }
      .toSet()
  }

  override fun save(wallets: List<Wallet>) {
    try {
      performSaveOperation(wallets)
      log.info("performSaveOperation wallet ids={}", wallets.map { it.id })
    } catch (e: ObjectOptimisticLockingFailureException) {
      // @Version 에노테이션을 사용해서 동시성을 제어 시 쓰기 충돌이 발생하는 경우 (Srping 예외 추상화)
      retrySaveOperation(wallets)
    }
  }

  private fun performSaveOperation(wallets: List<Wallet>) {
    // @Transactional 은 AOP라서 public만 가능한데 transaction template를 사용하면 private으로 캡슐화 가능하다.
    transactionTemplate.execute {
      springDataJpaWalletRepository.saveAll(wallets.map { jpaWalletMapper.mapToJpaEntity(it) })
      walletTransactionRepository.save(wallets.flatMap { it.walletTransactions })
    }
  }

  private fun retrySaveOperation(
    wallets: List<Wallet>,
    maxRetries: Int = 3,
    baseDelay: Int = 100
  ) {
    var retryCount = 0

    while (true) {
      try {
        performSaveOperationWithRecent(wallets)
        break
      } catch (e: ObjectOptimisticLockingFailureException) {
        if (++retryCount > maxRetries) {
          throw RetryExhaustedWithOptimisticLockingFailureException(e.message ?: "exhausted retry count.")
        }
        waitForNextRetry(baseDelay)
      }
    }
  }

  private fun performSaveOperationWithRecent(wallets: List<Wallet>) {
    val recentWallets = springDataJpaWalletRepository.findByIdIn(wallets.map { it.id }.toSet())
    val recentWalletsById = recentWallets.associateBy { it.id }

    val walletPairs = wallets.map { wallet ->
      Pair(wallet, recentWalletsById[wallet.id]!!)
    }

    val updatedWallet = walletPairs.map {
      it.second.addBalance(it.first.walletTransactions.fold(Money.ZERO) {acc, walletTransaction ->  acc.add(walletTransaction.amount)})
    }

    transactionTemplate.execute {
      springDataJpaWalletRepository.saveAll(updatedWallet)
      walletTransactionRepository.save(wallets.flatMap { it.walletTransactions })
    }
  }

  private fun waitForNextRetry(baseDelay: Int) {
    val jitter = (Math.random() * baseDelay).toLong()

    try {
      Thread.sleep(jitter)
    } catch (e: InterruptedException) {
      Thread.currentThread().interrupt()
      throw RuntimeException("Interrupted during retry wait", e)
    }
  }
}

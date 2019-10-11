package com.rabobank.bankaccountmanager.domain.model;

import com.rabobank.bankaccountmanager.domain.type.StatementType;
import com.rabobank.bankaccountmanager.domain.type.TransactionStatus;
import com.rabobank.bankaccountmanager.domain.type.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * transaction_history table java object
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction_history")
@EntityListeners(AuditingEntityListener.class)
public class TransactionHistory implements Serializable {

    @Id
    @GenericGenerator(name = "transaction_generator", strategy = "increment")
    @GeneratedValue(generator = "transaction_generator")
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private StatementType statementType;

    private Long customerId;

    private Long bankAccountId;

    private Long cardId;

    private BigDecimal amount;

    private BigDecimal fee;

    private BigDecimal totalAmount;

    private BigDecimal beforeBalance;

    private BigDecimal afterBalance;

    private Long correlationId;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private String failingReason;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}

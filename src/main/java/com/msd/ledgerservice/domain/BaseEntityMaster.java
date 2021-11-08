package com.msd.ledgerservice.domain;

import com.msd.ledgerservice.util.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@MappedSuperclass
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
public class BaseEntityMaster implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(BaseEntityMaster.class);

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @PrePersist
    protected void onCreate() {
        Long userId = 0L;
        try {
            CustomUserDetail customUserDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userId = customUserDetail.getUser().getId();
        } catch (Exception e) {
            logger.info("User get issue on Application context");
        }

        createdDate = LocalDateTime.now(ZoneId.systemDefault());
        createdBy = userId;
        version = 0L;
    }

    @PreUpdate
    protected void onUpdate() {
        Long userId = 0L;
        try {
            CustomUserDetail customUserDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userId = customUserDetail.getUser().getId();
        } catch (Exception e) {
            logger.info("User get issue on Application context");
        }
        updatedDate = LocalDateTime.now(ZoneId.systemDefault());
        updatedBy = userId;
    }
}

package com.network.franchise.infrastructure.adapters.persistence.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("franchises")
public class FranchiseEntity extends Auditable {
    @Id
    private Long id;

    @NotBlank
    private String name;
}

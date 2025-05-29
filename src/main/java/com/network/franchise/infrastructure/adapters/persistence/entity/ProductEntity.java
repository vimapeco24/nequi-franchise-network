package com.network.franchise.infrastructure.adapters.persistence.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table("products")
@EqualsAndHashCode(callSuper=false)
public class ProductEntity extends Auditable {
    @Id
    private Long id;

    @NotBlank
    private String name;

    private int stock;

    private Long branchId;
}

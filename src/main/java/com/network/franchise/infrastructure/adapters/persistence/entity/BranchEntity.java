package com.network.franchise.infrastructure.adapters.persistence.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table("branches")
@EqualsAndHashCode(callSuper=false)
public class BranchEntity {
    @Id
    private Long id;

    @NotBlank
    private String name;

    private Long franchiseId;
}

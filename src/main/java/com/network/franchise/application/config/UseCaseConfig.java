package com.network.franchise.application.config;

import com.network.franchise.domain.api.FranchisePersistenceAdapterPort;
import com.network.franchise.domain.mapper.FranchiseDomainMapper;
import com.network.franchise.domain.usecase.command.CreateFranchiseUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateFranchiseUseCase createFranchiseUseCase(FranchisePersistenceAdapterPort franchisePersistenceAdapterPort,
                                                         FranchiseDomainMapper mapper) {
        return new CreateFranchiseUseCase(franchisePersistenceAdapterPort, mapper);
    }
}

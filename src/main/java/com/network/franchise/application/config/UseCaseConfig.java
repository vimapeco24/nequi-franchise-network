package com.network.franchise.application.config;

import com.network.franchise.domain.api.AppPersistenceAdapterPort;
import com.network.franchise.domain.mapper.BranchesDtoMapper;
import com.network.franchise.domain.mapper.FranchiseDtoMapper;
import com.network.franchise.domain.mapper.ProductDtoMapper;
import com.network.franchise.domain.usecase.command.*;
import com.network.franchise.domain.usecase.queries.GetTopProductsPerBranchUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

@Configuration
@EnableR2dbcAuditing
public class UseCaseConfig {

    @Bean
    public CreateFranchiseUseCase createFranchiseUseCase(AppPersistenceAdapterPort appPersistenceAdapterPort,
                                                         FranchiseDtoMapper mapper) {
        return new CreateFranchiseUseCase(appPersistenceAdapterPort, mapper);
    }

    @Bean
    public AddBranchUseCase addBranchUseCase(AppPersistenceAdapterPort appPersistenceAdapterPort,
                                             BranchesDtoMapper mapper) {
        return new AddBranchUseCase(appPersistenceAdapterPort, mapper);
    }

    @Bean
    public AddProductUseCase addProductUseCase(AppPersistenceAdapterPort appPersistenceAdapterPort,
                                               ProductDtoMapper mapper) {
        return new AddProductUseCase(appPersistenceAdapterPort, mapper);
    }

    @Bean
    public DeleteProductUseCase deleteProductUseCase(AppPersistenceAdapterPort appPersistenceAdapterPort) {
        return new DeleteProductUseCase(appPersistenceAdapterPort);
    }

    @Bean
    public UpdateStockUseCase updateStockUseCase(AppPersistenceAdapterPort appPersistenceAdapterPort,
                                                 ProductDtoMapper mapper) {
        return new UpdateStockUseCase(appPersistenceAdapterPort, mapper);
    }

    @Bean
    public GetTopProductsPerBranchUseCase getTopProductsPerBranchUseCase(AppPersistenceAdapterPort appPersistenceAdapterPort) {
        return new GetTopProductsPerBranchUseCase(appPersistenceAdapterPort);
    }

    @Bean
    public UpdateFranchiseNameUseCase updateFranchiseNameUseCase(AppPersistenceAdapterPort appPersistenceAdapterPort,
                                                                 FranchiseDtoMapper mapper) {
        return new UpdateFranchiseNameUseCase(appPersistenceAdapterPort, mapper);
    }
}

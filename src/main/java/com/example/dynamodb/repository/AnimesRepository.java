package com.example.dynamodb.repository;

import com.example.dynamodb.entity.Animes;
import io.micrometer.core.annotation.Timed;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

@EnableScan
public interface AnimesRepository extends PagingAndSortingRepository<Animes, String> {
    @EnableScanCount
    @Timed(value = "tempo_consulta_nome_dynamodb")
    Page<Animes> findByName(String name, Pageable pageable);

    @EnableScanCount
    Page<Animes> findByNote(int note, Pageable pageable);
}

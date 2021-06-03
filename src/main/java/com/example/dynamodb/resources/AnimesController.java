package com.example.dynamodb.resources;

import com.example.dynamodb.entity.Animes;
import com.example.dynamodb.repository.AnimesRepository;
import com.example.dynamodb.repository.SdkRepository;
import com.example.dynamodb.repository.SdkRepositoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

@RestController
@RequestMapping(path = "/v1")
@RequiredArgsConstructor
public class AnimesController {

    private final AnimesRepository animesRepository;
    private final SdkRepository sdkRepository;
    private final ObjectMapper mapper;
    //    private final ClientRepository repository;
    private final List<String> names = List.of("Bakugo", "Goku", "Vegeta", "Gohan", "Kuabara", "Sanji", "Zoro", "Sasuka", "Eren", "Ligth", "Itachi",
            "Levi", "Luffy", "Midorya", "Alphonse", "Edward", "Naruto", "Ichigo", "Toguro", "yusuke", "Kaido", "Ichigo", "Roy",
            "Killua", "Saitama", "Gon", "Hisoka", "Shoto", "Kaneki");

    @PostMapping(path = "/animes")
    @ResponseStatus(HttpStatus.CREATED)
    public Animes saveAnimes(@RequestBody Animes animes) {
//        names.forEach(name -> animesRepository.save(Animes.builder()
//                .name(name)
//                .id(UUID.randomUUID().toString())
//                .note(ThreadLocalRandom.current().nextInt(5, 10))
//                .build()));
        return sdkRepository.save(animes);
//        Animes animeSaved = animesRepository.save(animes);
//        return animeSaved;
    }

    @GetMapping(path = "/animes")
    @ResponseStatus(HttpStatus.OK)
    public Page<Animes> findbyName(@PathParam(value = "name") String name, Pageable page) {

        return animesRepository.findByName(name, page);
    }


}

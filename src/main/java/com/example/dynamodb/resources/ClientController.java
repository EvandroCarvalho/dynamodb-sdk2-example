package com.example.dynamodb.resources;

import com.example.dynamodb.entity.Address;
import com.example.dynamodb.entity.Client;
import com.example.dynamodb.repository.ClientRepository;
import com.example.dynamodb.service.GetS3LinkPresigned;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping(path = "/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientRepository repository;
    private final GetS3LinkPresigned getS3LinkPresigned;
    private final List<String> names = List.of("Bakugo", "Goku", "Vegeta", "Gohan", "Kuabara", "Sanji", "Zoro", "Sasuka", "Eren", "Ligth", "Itachi",
            "Levi", "Luffy", "Midorya", "Alphonse", "Edward", "Naruto", "Ichigo", "Toguro", "yusuke", "Kaido", "Ichigo", "Roy",
            "Killua", "Saitama", "Gon", "Hisoka", "Shoto", "Kaneki");

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client save(@RequestBody Client client) {
            repository.save(client);
        return client;
    }

    @GetMapping(path = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestParam(value = "amount", defaultValue = "1000") int amount,
                         @RequestParam(value = "same-partition-key", defaultValue = "true") boolean samePartitionValue) {
        String partitionKey = UUID.randomUUID().toString();
        for(int i = amount; i > 0; i--) {
            int ints = ThreadLocalRandom.current().ints(0, names.size()).findFirst().getAsInt();
            Client client = Client.builder()
                    .id(samePartitionValue ? partitionKey : UUID.randomUUID().toString())
                    .address(Address.builder()
                            .city("Uberlândia")
                            .neighborhood("algum bairro")
                            .street("xpto")
                            .number(000)
                            .build())
                    .clientName(names.get(ints))
                    .email(names.get(ints).concat(String.valueOf(i)).concat("@email.com"))
                    .build();
            repository.save(client);
        }
        return String.format("%s clients ware created", amount);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Client> getAll() {
        return repository.findAll();
    }

    @GetMapping(path = "/name-index/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<Client> findByName(@PathVariable("name") String name) {
        return repository.findByNameIndex(name);
    }

    @GetMapping(path = "/name-without-index/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<Client> find(@PathVariable("name") String name) {
        return repository.findByNameWithoutIndex(name);
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Client findByIdPartition(@PathVariable("id") String id) {
        return repository.findByIdParition(id);
    }


    @GetMapping(path = "/link")
    public URL getUrl() {
        return getS3LinkPresigned.getUri();
    }

}

package com.example.userService.service;

import com.example.userService.model.Postal;
import com.example.userService.repository.PostalRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
public class PostalService {
    @Value("${api.postal.apiKey}")
    private String apiKey;

    private PostalRepository repo;

    public PostalService(PostalRepository repo){
        this.repo = repo;
    }

    public List<Postal> getAll(){
        return repo.findAll();
    }

    public Postal getPostal(Long id){
        return repo.getReferenceById(id);
    }

    public void savePostal(Postal postal){
        repo.save(postal);
    }

    public Postal apiLookup(Long zip) throws IOException {
        URL url = new URL("https://app.zipcodebase.com/api/v1/search?apikey="+ apiKey +"&country=US&codes="+zip);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        if(responseCode == HttpURLConnection.HTTP_OK){
            StringBuilder sb = new StringBuilder();
            try (Scanner scanner = new Scanner(connection.getInputStream())) {
                while (scanner.hasNext()){
                    sb.append(scanner.nextLine());
                }
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(sb.toString());
            jsonNode = jsonNode.path("results").path(zip.toString()).get(0);

            return objectMapper.readValue(jsonNode.toString(), Postal.class);
        }
        else throw new ConnectException();
    }

    public List<Object[]> byState(){
        return repo.usersInStates();
    }

    public Optional<Postal> checkExistence(Long zip){

        return repo.findById(zip);

    }

    public List<Object[]> byCity() {
        return repo.usersInCities();
    }
}

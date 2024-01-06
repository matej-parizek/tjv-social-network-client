package cz.cvut.fit.tjv.social_network.web_client.api;

import cz.cvut.fit.tjv.social_network.web_client.model.CommentDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.awt.desktop.OpenFilesEvent;
import java.util.*;

@Component
public class CommentClient {
    private RestClient commentClient;

    public CommentClient(@Value("${api.url}") String url) {
        this.commentClient = RestClient.builder()
                .baseUrl(
                        url+"/user/{author}/posts/{id}/comments").build();
    }

    public Collection<CommentDto> getComments(String author, Long id){
        try {
            return List.of(commentClient.get()
                    .uri("", Map.of("author",author,"id",id))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(CommentDto[].class)
                    .getBody());
        }catch (HttpClientErrorException.Conflict e){
            return new ArrayList<>();
        }
    }

   public Optional<CommentDto> setComments(String author, Long id, CommentDto comment){
        try {
            return Optional.of(commentClient.post()
                    .uri("", Map.of("author",author,"id",id))
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(comment)
                    .retrieve()
                    .toEntity(CommentDto.class)
                    .getBody());
        }catch (HttpClientErrorException.Conflict e){
            return Optional.empty();
        }
    }
}

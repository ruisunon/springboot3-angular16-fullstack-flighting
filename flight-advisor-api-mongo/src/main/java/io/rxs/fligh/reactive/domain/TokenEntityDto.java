package io.rxs.fligh.reactive.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@Getter
@Setter
@RedisHash("token")
public class TokenEntityDto {

    @Id
    private String id = UUID.randomUUID().toString();
    private String value;
    public TokenEntityDto(String value) {
        this.value = value;
    }
}
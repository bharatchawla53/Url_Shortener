package com.url.shortener.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UrlShortenerResponse {
    private String id;
    private String shortUrl;
    private String longUrl;
}

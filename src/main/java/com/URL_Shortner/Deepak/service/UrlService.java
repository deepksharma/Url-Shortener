package com.URL_Shortner.Deepak.service;

import com.URL_Shortner.Deepak.entity.UrlMapping;
import com.URL_Shortner.Deepak.repo.UrlRepository;
import com.URL_Shortner.Deepak.util.Base62Encoder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;

    @Transactional
    public String shortenUrl(String longUrl) {
        Optional<UrlMapping> existing = urlRepository.findByLongUrl(longUrl);
        if(existing.isPresent()) {
            return existing.get().getShortCode();
        }
        // else create new entry
        UrlMapping mapping = UrlMapping.builder()
                .longUrl(longUrl)
                .shortCode("temp")
                .build();

        UrlMapping saved = urlRepository.save(mapping);

        String shortCode = Base62Encoder.encode(saved.getId());
        saved.setShortCode(shortCode);
        urlRepository.save(saved);

        return shortCode;
    }

    public Optional<String> getLongUrl(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .map(mapping -> {
                    mapping.setClickCount((mapping.getClickCount() == null ? 0L : mapping.getClickCount())+1);
                    urlRepository.save(mapping);
                    return mapping.getLongUrl();
                });
    }

    public Optional<UrlMapping> getUrlMappingByShortCode(String shortCode) {
    return urlRepository.findByShortCode(shortCode);
    }

    public boolean isAlreadyStored(String longUrl) {
        return urlRepository.findByLongUrl(longUrl).isPresent();
    }
}

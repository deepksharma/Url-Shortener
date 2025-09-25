package com.URL_Shortner.Deepak.controller;

import com.URL_Shortner.Deepak.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UrlService urlService;

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @PostMapping("/shorten")
    public String shortenUrl(@RequestParam String longUrl , Model model){
        String shortCode = urlService.shortenUrl(longUrl);
        String shortUrl = "http://localhost:8080/"+ shortCode;
        model.addAttribute("shortUrl" , shortUrl);

        // Adding message if already exists
        if(urlService.isAlreadyStored(longUrl)){
            model.addAttribute("message","This URL was already stored in Database");
        }else{
            model.addAttribute("message" , "New short URL is created sucessfully");
        }
        // Adding click count to model
        urlService.getUrlMappingByShortCode(shortCode).ifPresent(mapping ->
                model.addAttribute("clickCount", mapping.getClickCount())
        );

        return "index";
    }

    @GetMapping("/{shortCode}")
    public String redirect(@PathVariable String shortCode){
        return urlService.getLongUrl(shortCode)
                .map(url -> "redirect:" + url)
                .orElse("redirect:/");
    }
}

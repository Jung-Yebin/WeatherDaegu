package com.bank_of_korea.bank_of_korea.service;

import com.bank_of_korea.bank_of_korea.entity.WeatherData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    private final ResourcePatternResolver resourcePatternResolver;

    private final ObjectMapper objectMapper;

    public WeatherService(ObjectMapper objectMapper){
        this.resourcePatternResolver = new PathMatchingResourcePatternResolver();
        this.objectMapper = objectMapper;
    }

    public List<WeatherData> getForecastsByLocation(String location) throws IOException {
        Resource[] resources = resourcePatternResolver.getResources("classpath:forecasts/*" + location + "*.json");

        List<WeatherData> forecasts = new ArrayList<>();

        for (Resource resource : resources) {
            List<WeatherData> forecastList = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<WeatherData>>() {});
            forecasts.addAll(forecastList);
        }

        System.out.println(forecasts);
        return forecasts;
    }


}

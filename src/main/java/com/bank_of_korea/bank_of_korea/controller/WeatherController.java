package com.bank_of_korea.bank_of_korea.controller;

import com.bank_of_korea.bank_of_korea.entity.BookmarkData;
import com.bank_of_korea.bank_of_korea.repository.BookmarkRepository;
import com.bank_of_korea.bank_of_korea.service.BookmarkService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import com.bank_of_korea.bank_of_korea.entity.WeatherData;
import com.bank_of_korea.bank_of_korea.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private BookmarkService bookmarkService;

    @GetMapping("/weather")
    public String fetchWeater(){
        try {
            weatherService.getForecastsByLocation("일최고기온");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "index";
    }

    @GetMapping("/weather/test")
    public String test(){
        return "test";
    }

    @GetMapping("weather/main")
    public String main(@RequestParam(name = "userInput", required = false) String userInput,
                       Model model)
    {
        String searchInput = "";
        //임의의 현재시각
        String day = "18";
        String hour = "1100";

        try {
            if (userInput != null){
                searchInput = userInput;
                System.out.println("searchInput" + searchInput);
                try {
                    // 1.강수확률
                    List<WeatherData> Precipitation_probability = new ArrayList<>();
                    List<WeatherData> forecasts = weatherService.getForecastsByLocation(searchInput + "_강수확률");
                    // 2.일 최고기온
                    List<WeatherData> max_temp = weatherService.getForecastsByLocation(searchInput + "_일최고기온");
                    // 3.일 최저기온
                    List<WeatherData> min_temp = weatherService.getForecastsByLocation(searchInput + "_일최저기온");
                    // 4.하늘상태
                    List<WeatherData> sky_state = new ArrayList<>();
                    List<WeatherData> sky_state_temp = weatherService.getForecastsByLocation(searchInput + "_하늘상태");
                    // 5.1시간 기온
                    List<WeatherData> current_temperature = new ArrayList<>();
                    List<WeatherData> current_temperature_temp = weatherService.getForecastsByLocation(searchInput + "_1시간기온");
                    // 5-2. 오늘 기온 전체
                    Map<String, Integer> temperatureDict = new LinkedHashMap<>();
//                    HashMap<String, Double> temperatureDict = new HashMap<>();
                    // 6. 오늘 풍속 전체
                    HashMap<String, Double> windSpeedDict = new HashMap<>();
                    List<WeatherData> wind_speed = weatherService.getForecastsByLocation(searchInput + "_풍속");
                    // 1-2 오늘 강수확률 전체
                    Map<String, Integer> precipitation_probabilityDict = new LinkedHashMap<>();

                    //필터링 로직
                    double Precipitation_probability_value_sum = 0;

                    //강수확률
                    ArrayList<String>precipitation_hourList = new ArrayList<>();
                    HashMap<String, Integer>precipitation_count = new HashMap<>();
                    HashMap<String, Double>precipitation_sum = new HashMap<>();

                    for (WeatherData data : forecasts) {
                        if (String.valueOf(data.getDay()).equals(day) && String.valueOf(data.getHour()).equals(hour)){
                            Precipitation_probability.add(data);
                            Precipitation_probability_value_sum += data.getValue();
                        }
                        if (String.valueOf(data.getDay()).equals(day)){
                            String precipitation_hour = data.getHour();
                            double precipitation_value = data.getValue();

                            if (!precipitation_hourList.contains(precipitation_hour)){
                                precipitation_hourList.add(precipitation_hour);
                                precipitation_count.put(precipitation_hour, 1);
                                precipitation_sum.put(precipitation_hour, precipitation_value);
                            }
                            else {
                                precipitation_count.put(precipitation_hour, precipitation_count.get(precipitation_hour)+1);
                                precipitation_sum.put(precipitation_hour, precipitation_sum.get(precipitation_hour)+precipitation_value);
                            }
                        }
                    }
                    System.out.println("pre sum" + precipitation_sum);

                    if(precipitation_count != null && precipitation_sum != null){
                        for (String precipitation_hour : precipitation_hourList){
                            int count = precipitation_count.get(precipitation_hour);
                            double sum = precipitation_sum.get(precipitation_hour);

                            int average = (int) Math.round(sum / count);
                            precipitation_probabilityDict.put(precipitation_hour, average);
                        }
                    }

                    //일최대기온
                    double maxValue = Double.MIN_VALUE;
                    for (WeatherData data : max_temp){
                        if (String.valueOf(data.getDay()).equals(day)){
                            if (data.getValue() > maxValue) {
                                maxValue = data.getValue();
                            }
                        }
                    }
                    //일최저기온
                    double minValue = Double.MAX_VALUE;
                    for (WeatherData data : min_temp){
                        if (String.valueOf(data.getDay()).equals(day)){
                            if (data.getValue() < minValue) {
                                minValue = data.getValue();

                            }
                        }
                    }
                    //하늘상태
                    double sky_state_temp_sum = 0;
                    for (WeatherData data : sky_state_temp){
                        if (String.valueOf(data.getDay()).equals(day) && String.valueOf(data.getHour()).equals(hour)){
                            sky_state.add(data);
                            sky_state_temp_sum += data.getValue();
                        }
                    }

                    // 현재 기온 및 오늘 기온
                    double current_temperature_sum = 0;
                    ArrayList<String>todayTemperatureList = new ArrayList<>();
                    HashMap<String, Integer>temperature_count = new HashMap<>();
                    HashMap<String, Double>temperature_sum = new HashMap<>();
                    for (WeatherData data : current_temperature_temp){
                        if (String.valueOf(data.getDay()).equals(day) && String.valueOf(data.getHour()).equals(hour)){
                            current_temperature.add(data);
                            current_temperature_sum += data.getValue();
                        }
                        if (String.valueOf(data.getDay()).equals(day)){
                            String current_hour = data.getHour();
                            double temperature_value = data.getValue();

                            if(!todayTemperatureList.contains(data.getHour())){
                                todayTemperatureList.add(current_hour);
                                temperature_count.put(current_hour, 1);
                                temperature_sum.put(current_hour, temperature_value);
                            }
                            else{
                                temperature_count.put(current_hour, temperature_count.get(current_hour)+1);
                                temperature_sum.put(current_hour, temperature_sum.get(current_hour)+temperature_value);
                            }
                        }
                    }

                    for (String current_hour: todayTemperatureList){
                        double sum = temperature_sum.get(current_hour);
                        int count = temperature_count.get(current_hour);

                        int average = (int)Math.round(sum / count);
//                        System.out.println(sum + " " + count + " " + average);
                        temperatureDict.put(current_hour, average);
                    }

                    // 오늘 풍속
                    List<String>windSpeed_hourlist = new ArrayList<>();
                    Map<String, Double> windSpeed_sum = new HashMap<>();
                    Map<String, Integer> windSpeed_count = new HashMap<>();
                    for(WeatherData data : wind_speed){
                        if (String.valueOf(data.getDay()).equals(day)){
                            String current_hour = data.getHour();
                            double wind_value = data.getValue();

                            if(!windSpeed_count.containsKey(data.getHour())){
                                windSpeed_hourlist.add(current_hour);
                                windSpeed_count.put(current_hour, 1);
                                windSpeed_sum.put(current_hour, wind_value);
                            }
                            else{
                                windSpeed_count.put(current_hour, windSpeed_count.get(current_hour)+1);
                                windSpeed_sum.put(current_hour, windSpeed_sum.get(current_hour)+wind_value);
                            }
                        }
                    }

                    for (String current_hour : windSpeed_hourlist){
                        double sum = windSpeed_sum.get(current_hour);
                        int count = windSpeed_count.get(current_hour);

                        double average = sum / count;
                        windSpeedDict.put(current_hour, average);
                    }

                    List<Map.Entry<String, Double>> correct_windSpeedDict = new ArrayList<>(windSpeedDict.entrySet());
                    correct_windSpeedDict.sort(((o1, o2) -> Integer.compare(Integer.parseInt(o1.getKey()), Integer.parseInt(o2.getKey()))));

                    Map<String, Double> sorted_windSpeedDict = new LinkedHashMap<>();
                    for (Map.Entry<String, Double> entry : correct_windSpeedDict){
                        double val = Double.parseDouble(String.format("%.1f", entry.getValue()));
                        sorted_windSpeedDict.put(entry.getKey(), val);
                    }

                    double Precipitation_probability_value_average = Precipitation_probability_value_sum / Precipitation_probability.size();
                    double sky_state_temp_average = sky_state_temp_sum / sky_state.size();
                    double current_temperature_average = current_temperature_sum / current_temperature.size();
                    System.out.println("일 최고기온" + maxValue);
                    System.out.println("일 최저기온" + minValue);
                    System.out.println("현재 하늘 상태" + sky_state_temp_average);
                    System.out.println("현시각 강수확률 : " + Precipitation_probability_value_average);
                    System.out.println("현시각 기온 : " + current_temperature_average);
                    System.out.println("오늘 기온" + temperatureDict);
                    System.out.println("오늘 풍속 : " + sorted_windSpeedDict);
                    System.out.println("오늘 강수확률 : " + precipitation_probabilityDict);

                    model.addAttribute("Precipitation_probability_value_average", (int) Math.round(Precipitation_probability_value_average));
                    model.addAttribute("max_temperature", maxValue);
                    model.addAttribute("min_temperature", minValue);
                    model.addAttribute("sky_state", (int) Math.round(sky_state_temp_average));
                    model.addAttribute("current_temperature", (int) Math.round(current_temperature_average));
                    model.addAttribute("userInput", userInput);
                    model.addAttribute("Today_Temperature", temperatureDict);
                    model.addAttribute("Today_windSpeed", sorted_windSpeedDict);
                    model.addAttribute("Today_precipitation_probability", precipitation_probabilityDict);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpSession session = request.getSession(false); // 세션이 없으면 새로 생성하지 않음
            if (session != null){
                String sessionValue = (String) session.getAttribute("loginEmail");
                System.out.println("현재 세션의 값 : " + sessionValue);
                String location = bookmarkService.findLocation(sessionValue);

                System.out.println("DB에 저장된 location" + location);
                model.addAttribute("location", location);
            }
        }

        catch (Exception e){
            System.out.println("에러 발생: " + e.getMessage());
            return "error"; // 예외 발생 시 처리
        }

        return "main";
    }

}

package com.korbiak.service.service.impl;

import com.korbiak.service.clients.OpenWeatherHttpClient;
import com.korbiak.service.dto.WeatherConditionDto;
import com.korbiak.service.dto.bing.Option;
import com.korbiak.service.mapper.WeatherInfoMapper;
import com.korbiak.service.messagesenders.EmailSender;
import com.korbiak.service.messagesenders.SmsSender;
import com.korbiak.service.model.entities.AlertInfo;
import com.korbiak.service.model.entities.User;
import com.korbiak.service.model.entities.WeatherConditions;
import com.korbiak.service.model.weathermodels.Weather;
import com.korbiak.service.model.weathermodels.WeatherApiResponse;
import com.korbiak.service.repos.AlertInfoRepo;
import com.korbiak.service.repos.UserRepo;
import com.korbiak.service.repos.WeatherCondRepo;
import com.korbiak.service.security.jwt.JwtUser;
import com.korbiak.service.service.ElectTreatmentService;
import com.korbiak.service.service.WeatherScheduler;
import com.korbiak.service.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    private final OpenWeatherHttpClient openWeatherHttpClient;
    private final ElectTreatmentService treatmentService;

    private final WeatherScheduler weatherScheduler;

    private final AlertInfoRepo alertInfoRepo;

    private final UserRepo userRepo;

    private final SmsSender smsSender;

    private final EmailSender emailSender;

    private final WeatherCondRepo weatherRepo;

    private final WeatherInfoMapper weatherInfoMapper;

    /**
     * Get weather conditional
     *
     * @param lat latitude
     * @param lon longitude
     * @return object of weather info
     */
    @Override
    public WeatherApiResponse getCurrentWeather(String lon, String lat) {
        //call open weather api
        WeatherApiResponse response = openWeatherHttpClient.call(lat, lon);
        if (response == null) {
            throw new RestClientException("Error calling weather api");
        }

        Weather criticalWeather = weatherScheduler.getCriticalWeather(response);
        int treatmentLevel = criticalWeather.getCriticalLevel();

        if (treatmentLevel > 5) {
            // send alerts via sms amd email
            sendAlert(treatmentLevel, response);
        }

        response.setTreatmentLevel(treatmentLevel);

        Option color = treatmentService.getColor(treatmentLevel);
        response.setTreatmentColor(color.getFillColor());

        return response;
    }

    /**
     * Send sms and email with alert info if needed
     *
     * @param treatmentLevel
     * @param response
     */
    private void sendAlert(int treatmentLevel, WeatherApiResponse response) {
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) user.getAuthorities();
        GrantedAuthority grantedAuthority = authorities.get(0);
        if (grantedAuthority.getAuthority().equals("USER")) {
            Optional<User> repoUser = userRepo.findById(user.getId());
            if (repoUser.isPresent()) {
                List<AlertInfo> alertInfos = alertInfoRepo.findAlertInfoByUserFirstNameAndUserSecondNameOrderByDateTimeDesc
                        (repoUser.get().getFirstName(), repoUser.get().getSecondName());
                String message = String.format("%s регіон в якому ви зараз знаходитесь має погодню умову: %s, " +
                                "з рівнема загрози: %s, будьте обережні", response.getName(),
                        response.getWeather()
                                .stream()
                                .map(weather -> weather.getDescription() + " ")
                                .collect(Collectors.toList()), treatmentLevel);
                if (!alertInfos.isEmpty()) {
                    Timestamp latestDateTime = alertInfos.get(0).getDateTime();
                    Timestamp currentDateTime = Timestamp.from(new Date().toInstant());
                    long different = currentDateTime.getTime() - latestDateTime.getTime();
                    if (different / 1000L / 60L / 60L >= 1) { //more than hour
                        smsSender.sendSms(repoUser.get().getPhone(), message);
                        emailSender.sendEmail(repoUser.get().getEmail(), message);
                        saveCurrentAlertInfo(repoUser);
                    }
                } else {
                    smsSender.sendSms(repoUser.get().getPhone(), message);
                    emailSender.sendEmail(repoUser.get().getEmail(), message);
                    saveCurrentAlertInfo(repoUser);
                }
            }
        }
    }

    private void saveCurrentAlertInfo(Optional<User> repoUser) {
        AlertInfo currentAlertInfo = new AlertInfo();
        currentAlertInfo.setUser(repoUser.get());
        currentAlertInfo.setDateTime(Timestamp.from(new Date().toInstant()));

        alertInfoRepo.save(currentAlertInfo);
    }

    @Override
    public List<WeatherConditionDto> getAllWeather() {
        LocalDate current = LocalDate.now();
        LocalDate localDate = current.minusMonths(1);
        List<WeatherConditions> weatherRepoAll = weatherRepo.findAll();
        return weatherInfoMapper.toDtos(weatherRepoAll);
    }
}

// /backend/src/main/java/com/service_booking_system/service/service/Admin/SettingService.java

package com.service_booking_system.service.service.Admin;

import com.service_booking_system.service.dto.Admin.RevenueSettingRequestDTO;
import com.service_booking_system.service.dto.Admin.RevenueSettingResponseDTO;
import com.service_booking_system.service.dto.CityDTO;
import com.service_booking_system.service.dto.StateDTO;
import com.service_booking_system.service.enums.CurrentStatus;
import com.service_booking_system.service.model.Cities;
import com.service_booking_system.service.model.RevenueBreakDown;
import com.service_booking_system.service.model.States;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.repository.CityRepository;
import com.service_booking_system.service.repository.RevenueBreakDownRepository;
import com.service_booking_system.service.repository.StatesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SettingService {

    @Autowired private RevenueBreakDownRepository revenueBreakDownRepository;

    @Autowired private StatesRepository statesRepository;

    @Autowired private CityRepository cityRepository;

    private static final Logger logger = LoggerFactory.getLogger(SettingService.class);

    // Set revenue breakdown
    public RevenueSettingResponseDTO setRevenue(RevenueSettingRequestDTO revenueSettingRequestDTO, Users user) {

        CurrentStatus status = revenueSettingRequestDTO.getCurrentStatus();
        LocalDateTime active = null;

        // Raise popup for confirmation( logic is available in frontend for that )
        if(status.equals(CurrentStatus.ACTIVE)){

            active = LocalDateTime.now();

            RevenueBreakDown breakDown = revenueBreakDownRepository.findByCurrentStatus(CurrentStatus.ACTIVE);

            if(breakDown != null){
                breakDown.setCurrentStatus(CurrentStatus.INACTIVE);
                breakDown.setDeactivateAt(LocalDateTime.now());
                revenueBreakDownRepository.save(breakDown);
            }

        }

        RevenueBreakDown revenueBreakDown = RevenueBreakDown.builder()
                .serviceProvider(revenueSettingRequestDTO.getServiceProviderRevenue())
                .currentStatus(revenueSettingRequestDTO.getCurrentStatus())
                .activeAt(active)
                .user(user)
                .build();

        revenueBreakDownRepository.save(revenueBreakDown);

        logger.info("Revenue is set as Service Provider : {} %",  revenueSettingRequestDTO.getServiceProviderRevenue());
        return new RevenueSettingResponseDTO(revenueSettingRequestDTO.getServiceProviderRevenue(),
                "Revenue is set as Service Provider : " + revenueSettingRequestDTO.getServiceProviderRevenue() + "%");

    }

    public List<RevenueBreakDown> getRevenue() {
        List<RevenueBreakDown> revenueBreakDowns = revenueBreakDownRepository.findAll();
        return revenueBreakDowns;
    }

    public String changeRevenueBreakdownStatus(Long id) {
        RevenueBreakDown revenueBreakDown = revenueBreakDownRepository.findById(id).orElse(null);

        if(revenueBreakDown.getCurrentStatus() == CurrentStatus.INACTIVE){
            revenueBreakDown.setCurrentStatus(CurrentStatus.ACTIVE);
            revenueBreakDown.setActiveAt(LocalDateTime.now());
        } else {
            revenueBreakDown.setCurrentStatus(CurrentStatus.INACTIVE);
            revenueBreakDown.setDeactivateAt(LocalDateTime.now());
        }

        revenueBreakDownRepository.save(revenueBreakDown);

        logger.info("Status of {} is changed successfull.", revenueBreakDown.getRevenueId());
        return "Status of " + revenueBreakDown.getRevenueId() + "is changed successfully.";
    }

    public String addState(StateDTO stateDTO) {

        States state = States.builder()
                .stateName(stateDTO.getStateName())
                .build();

        statesRepository.save(state);

        logger.info("State : {} added successfully", stateDTO.getStateName());
        return "State : " + stateDTO.getStateName() + " added successfully";
    }

    public String addCity(CityDTO cityDTO) {

        States state = statesRepository.findByStateName(cityDTO.getStateName());

        if(state == null) {
            logger.error("State not exist");
            throw new IllegalArgumentException("State not exist");
        }

        Cities city = Cities.builder()
                .cityName(cityDTO.getCityName())
                .states(state)
                .build();

        cityRepository.save(city);

        logger.info("City : {} added successfully", cityDTO.getCityName());
        return "City : " + cityDTO.getCityName() + " added successfully";
    }
}


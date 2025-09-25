// /backend/src/main/java/com/service_booking_system/service/service/Admin/AdminUserService.java

package com.service_booking_system.service.service.Admin;

import com.service_booking_system.service.controller.Admin.RepeatedCode;
import com.service_booking_system.service.dto.Admin.CustomerGraphOverviewDTO;
import com.service_booking_system.service.dto.Admin.CustomerResponseDTO;
import com.service_booking_system.service.dto.Admin.ServiceProviderResponseDTO;
import com.service_booking_system.service.enums.UserRoles;
import com.service_booking_system.service.model.Prices;
import com.service_booking_system.service.model.ServiceProvider;
import com.service_booking_system.service.model.UserAddress;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.repository.PriceRepository;
import com.service_booking_system.service.repository.ServiceProviderRepository;
import com.service_booking_system.service.repository.UserAddressRepository;
import com.service_booking_system.service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminUserService {

    @Autowired private UserRepository userRepository;

    @Autowired private UserAddressRepository userAddressRepository;

    @Autowired private PriceRepository priceRepository;

    @Autowired private ServiceProviderRepository serviceProviderRepository;

    @Autowired private RepeatedCode repeatedCode;

    private static final Logger logger = LoggerFactory.getLogger(AdminUserService.class);

    public CustomerGraphOverviewDTO getGraphsForUsers(UserRoles role) {
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        int currentMonth = today.getMonthValue();

        // 1. Monthly trend for current year
        List<CustomerGraphOverviewDTO.MonthlyUserTrendDTO> monthlyTrend = new ArrayList<>();
        for (int m = 1; m <= currentMonth; m++) {
            LocalDateTime start = LocalDate.of(currentYear, m, 1).atStartOfDay();
            LocalDateTime end = start.plusMonths(1).minusNanos(1);
            long count = userRepository.countByRoleAndCreatedAtBetween(role, start, end);
            String monthLabel = Month.of(m).name().substring(0, 3); // "JAN", "FEB", etc.
            monthlyTrend.add(new CustomerGraphOverviewDTO.MonthlyUserTrendDTO(monthLabel, count));
        }

        // 2. Daily trend for current month
        int daysInMonth = today.getDayOfMonth();
        List<CustomerGraphOverviewDTO.DailyUserTrendDTO> dailyTrend = new ArrayList<>();
        for (int d = 1; d <= daysInMonth; d++) {
            LocalDateTime start = LocalDate.of(currentYear, currentMonth, d).atStartOfDay();
            LocalDateTime end = start.plusDays(1).minusNanos(1);
            long count = userRepository.countByRoleAndCreatedAtBetween(role, start, end);
            dailyTrend.add(new CustomerGraphOverviewDTO.DailyUserTrendDTO(d, count));
        }

        // 3. Yearly growth
        List<CustomerGraphOverviewDTO.YearlyUserCountDTO> yearlyTrend = new ArrayList<>();
        for (int y = currentYear - 4; y <= currentYear; y++) {
            LocalDateTime start = LocalDate.of(y, 1, 1).atStartOfDay();
            LocalDateTime end = LocalDate.of(y, 12, 31).atTime(23, 59, 59);
            long count = userRepository.countByRoleAndCreatedAtBetween(role, start, end);
            yearlyTrend.add(new CustomerGraphOverviewDTO.YearlyUserCountDTO(y, count));
        }

        // 4. Region-wise customer count by city
        List<Object[]> raw = userRepository.countCustomersGroupedByCity(role);
        List<CustomerGraphOverviewDTO.RegionUserDistributionDTO> regionWise = raw.stream()
                .map(r -> new CustomerGraphOverviewDTO.RegionUserDistributionDTO((String) r[0], (Long) r[1]))
                .toList();

        return CustomerGraphOverviewDTO.builder()
                .usersThisYearMonthlyTrend(monthlyTrend)
                .newUsersThisMonthDailyTrend(dailyTrend)
                .userGrowthTrend(yearlyTrend)
                .regionWiseDistribution(regionWise)
                .build();
    }

    @Transactional
    public List<CustomerResponseDTO> getFilteredCustomers(String keyword, LocalDate startDate, LocalDate endDate, String sortBy) {
        Specification<Users> spec = null;

        // Always filter by customer role
        spec = addSpec(spec, (root, query, cb) -> cb.equal(root.get("role"), UserRoles.CUSTOMER));

        if (keyword != null && !keyword.isBlank()) {
            spec = addSpec(spec, UserSpecification.searchByEmailOrPhone(keyword));
        }

        // Apply date filter only if either is not null
        if (startDate != null || endDate != null) {
            if (startDate == null) startDate = LocalDate.of(2025, 5, 1);
            if (endDate == null) endDate = LocalDate.now();
            spec = addSpec(spec, UserSpecification.joinDateBetween(startDate, endDate));
        }

        // Default sort by userId
        Sort sort = "joinDate".equalsIgnoreCase(sortBy)
                ? Sort.by(Sort.Direction.ASC, "createdAt")
                : Sort.by(Sort.Direction.ASC, "userId");

        List<Users> users = userRepository.findAll(spec, sort);

        return users.stream().map(this::mapToCustomerDTO).collect(Collectors.toList());
    }

    private CustomerResponseDTO mapToCustomerDTO(Users user) {
        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhoneNo());
        dto.setEmail(user.getEmail());
        dto.setJoinAt(user.getCreatedAt());

        UserAddress address = userAddressRepository.findByUser(user);

        if (address != null) {

            CustomerResponseDTO.AddressDTO addressDTO = new CustomerResponseDTO.AddressDTO();
            addressDTO.setName(address.getName());
            addressDTO.setAreaName(address.getAreaName());
            addressDTO.setPincode(address.getPincode());

            if (address.getCity() != null) {
                addressDTO.setCityName(address.getCity().getCityName());
            } else {
                logger.warn("City is null for user ID {}", user.getUserId());
                addressDTO.setCityName(null);
            }

            dto.setAddresses(addressDTO);
        }

        return dto;
    }

    private <T> Specification<T> addSpec(Specification<T> base, Specification<T> toAdd) {
        return base == null ? toAdd : base.and(toAdd);
    }


    @Transactional
    public List<ServiceProviderResponseDTO> getFilteredServiceProviders(String keyword, LocalDate startDate, LocalDate endDate, String sortBy) {

        Specification<Users> spec = (root, query, cb) -> cb.equal(root.get("role"), UserRoles.SERVICE_PROVIDER);

        if (keyword != null && !keyword.isBlank()) {
            spec = addSpec(spec, UserSpecification.searchByEmailOrPhone(keyword));
        }

        // Apply date filter only if either is not null
        if (startDate != null || endDate != null) {
            if (startDate == null) startDate = LocalDate.of(2025, 5, 1);
            if (endDate == null) endDate = LocalDate.now();
            spec = addSpec(spec, UserSpecification.joinDateBetween(startDate, endDate));
        }

        Sort sort = "joinDate".equalsIgnoreCase(sortBy)
                ? Sort.by(Sort.Direction.ASC, "createdAt")
                : Sort.by(Sort.Direction.ASC, "userId");

        List<Users> users = userRepository.findAll(spec, sort);

        return users.stream().map(this::mapToServiceProviderDTO).collect(Collectors.toList());
    }

    @Transactional
    private ServiceProviderResponseDTO mapToServiceProviderDTO(Users user) {
        ServiceProviderResponseDTO dto = new ServiceProviderResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhoneNo());
        dto.setEmail(user.getEmail());
        dto.setJoinedAt(user.getCreatedAt());

        ServiceProvider serviceProvider = serviceProviderRepository.getByUser(user)
                .orElse(null);
        if (serviceProvider == null) {
            return null;
        }

        List<Prices> prices = priceRepository.findByServiceProvider(serviceProvider);

        List<ServiceProviderResponseDTO.priceDTO> priceDTOs = prices.stream()
                .map(price -> {
                    ServiceProviderResponseDTO.priceDTO pdto = new ServiceProviderResponseDTO.priceDTO();
                    pdto.setServiceId(price.getSubServices().getServices().getServiceId());
                    pdto.setPrice(price.getAmount());
                    pdto.setServiceProviderId(price.getServiceProvider().getServiceProviderId());
                    return pdto;
                })
                .collect(Collectors.toList());

        UserAddress userAddresses = userAddressRepository.findByUser(user);

        ServiceProviderResponseDTO.AddressDTO addressDTO = ServiceProviderResponseDTO.AddressDTO.builder()
                .name(userAddresses.getName())
                .areaName(userAddresses.getAreaName())
                .pincode(userAddresses.getPincode())
                .cityName(userAddresses.getCity().getCityName())
                .build();

        dto.setServiceProviderId(serviceProvider.getServiceProviderId());
        dto.setBusinessName(serviceProvider.getBusinessName());
        dto.setBusinessLicenseNumber(serviceProvider.getBusinessLicenseNumber());
        dto.setGstNumber(serviceProvider.getGstNumber());
        dto.setBankName(serviceProvider.getBankName());
        dto.setBankAccountNumber(serviceProvider.getBankAccountNumber());
        dto.setAccountHolderName(serviceProvider.getAccountHolderName());
        dto.setIfscCode(serviceProvider.getIfscCode());
        dto.setAadharCardPhoto(serviceProvider.getAadharCardImage() != null ? "/image/provider/aadhar/" + user.getUserId() : null);
        dto.setProfilePhoto(serviceProvider.getProfileImage() != null ? "/image/provider/profile/" + user.getUserId() : null);
        dto.setPanCardPhoto(serviceProvider.getPanCardImage() != null ? "/image/provider/pan/" + user.getUserId() : null);
        dto.setBusinessUtilityBillPhoto(serviceProvider.getBusinessUtilityBillImage() != null ? "/image/provider/utilitybill/" + user.getUserId() : null);
        dto.setPriceDTO(priceDTOs);
        dto.setAddresses(addressDTO);

        return dto;
    }

    public void toggleUserBlockStatus(Long userId, boolean block) {

        Users user = repeatedCode.checkUser(userId);

        boolean blockStatus = user.isBlocked();

        if(blockStatus == block) {
            if(blockStatus == true) {
                throw new RuntimeException("User already blocked");
            } else {
                throw new RuntimeException("User already unblocked");
            }
        }

        user.setBlocked(block);
        userRepository.save(user);

    }

    public void deleteCustomer(Long userId) {
        Users user = repeatedCode.checkUser(userId);

        // Prevent admin from deleting themselves or other admins
        if (user.getRole() == UserRoles.ADMIN) {
            throw new AccessDeniedException("Cannot delete an admin user.");
        }

        userRepository.delete(user);
    }

    public void deleteServiceProvider(Long providerId) {

        ServiceProvider serviceProvider = serviceProviderRepository.findById(providerId)
                .orElse(null);

        Users user = userRepository.findById(serviceProvider.getUser().getUserId()).orElse(null);

        serviceProviderRepository.delete(serviceProvider);
        userRepository.delete(user);
    }



}


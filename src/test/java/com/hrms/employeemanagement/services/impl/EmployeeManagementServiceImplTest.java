//package com.hrms.employeemanagement.services.impl;
//
//import com.hrms.careerpathmanagement.repositories.SkillEvaluationRepository;
//import com.hrms.careerpathmanagement.repositories.SkillTargetRepository;
//import com.hrms.careerpathmanagement.specification.CareerSpecification;
//import com.hrms.digitalassetmanagement.service.DamService;
//import com.hrms.employeemanagement.dto.EmployeeInputDTO;
//import com.hrms.employeemanagement.repositories.*;
//import com.hrms.employeemanagement.specification.EmployeeSpecification;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@Slf4j
//@ExtendWith(MockitoExtension.class)
//class EmployeeManagementServiceImplTest {
//
//    @Mock
//    EmployeeRepository employeeRepository;
//
//    @Mock
//    DepartmentRepository departmentRepository;
//
//    @Mock
//    EmergencyContactRepository emergencyContactRepository;
//
//    @Mock
//    EmployeeDamInfoRepository employeeDamInfoRepository;
//
//    @Mock
//    SkillEvaluationRepository skillEvaluationRepository;
//
//    @Mock
//    SkillTargetRepository skillTargetRepository;
//
//    @Mock
//    EmployeeSpecification employeeSpecification;
//
//    @Mock
//    DamService damService;
//
//    @Mock
//    CareerSpecification careerSpecification;
//
//    @Mock
//    PositionDepartmentRepository positionDepartmentRepository;
//
//    @InjectMocks
//    EmployeeManagementServiceImpl employeeManagementService;
//
//    @Test
//    void testAddEmployee() {
//        employeeManagementService.setUpMapper();
//
//        EmployeeInputDTO input = Mockito.mock(EmployeeInputDTO.class);
//
//
//        employeeManagementService.createEmployee(input);
//        log.info(String.valueOf(employeeManagementService.getAllEmployees().size()));
//    }
//
//    @Test
//    void testGetProfilePicture_whenEmployeeHasProfilePicture() {
//        // given
//        EmployeeManagementServiceImpl employeeManagementService = mock(EmployeeManagementServiceImpl.class);
//        Integer employeeId = 1;
//        String profilePicture = "profilePicture";
//
//        // when
//        Mockito.when(employeeManagementService.getProfilePicture(employeeId)).thenReturn(profilePicture);
//
//        // then
//        assertEquals(profilePicture, employeeManagementService.getProfilePicture(employeeId));
//    }
//
//    @Test
//    void test() {
//        List<String> mockedList = mock(List.class);
//        mockedList.addAll(List.of("one", "two"));
//
//        mockedList.size();
//        mockedList.size();
//
//        verify(mockedList, times(2)).size();
//        verify(mockedList, times(1)).addAll(List.of("one", "two"));
//    }
//}
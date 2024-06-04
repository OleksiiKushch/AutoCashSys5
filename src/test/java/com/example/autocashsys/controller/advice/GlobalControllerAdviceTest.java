package com.example.autocashsys.controller.advice;

import com.example.autocashsys.constants.AppConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;

import java.util.Map;

import static org.mockito.Mockito.verify;

public class GlobalControllerAdviceTest {

    @Mock
    private Model model;

    @Mock
    private Map<String, String> availableLocales;

    @InjectMocks
    private GlobalControllerAdvice globalControllerAdvice;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addAttributesTest() {
        String appNameTest = "test";
        ReflectionTestUtils.setField(globalControllerAdvice, "appName", appNameTest);
        globalControllerAdvice.addAttributes(model);

        verify(model).addAttribute(AppConstants.TITLE_ATTR, appNameTest);
        verify(model).addAttribute(AppConstants.AVAILABLE_LOCALES_ATTR, availableLocales);
    }
}
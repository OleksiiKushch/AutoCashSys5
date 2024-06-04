package com.example.autocashsys.controller.advice;

import com.example.autocashsys.constants.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalControllerAdvice {

    @Value("${spring.application.name}")
    private String appName;

    private final Map<String, String> availableLocales;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute(AppConstants.TITLE_ATTR, appName);
        model.addAttribute(AppConstants.AVAILABLE_LOCALES_ATTR, availableLocales);
    }
}

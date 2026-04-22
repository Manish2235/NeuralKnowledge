package com.incapp.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public String handleRuntimeException(RuntimeException ex, Model model) {

	model.addAttribute("errorMessage", ex.getMessage());
	model.addAttribute("exceptionType", ex.getClass().getSimpleName());

	return "exception";
	}

	@ExceptionHandler(Exception.class)
	public String handleAll(Exception ex, Model model) {
	model.addAttribute("errorMessage", "Internal Server Error: "+ex.getMessage());
	return "exception";
	}
}

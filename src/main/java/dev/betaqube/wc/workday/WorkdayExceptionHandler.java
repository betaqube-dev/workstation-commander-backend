package dev.betaqube.wc.workday;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice(basePackages = "dev.betaqube.wc.workday")
public class WorkdayExceptionHandler {

	@ExceptionHandler(WorkdayOperationException.class)
	public ProblemDetail handleIllegalState(WorkdayOperationException ex, HttpServletRequest request) {
		ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
		detail.setTitle("Invalid workday operation");
		detail.setProperty("path", request.getRequestURI());
		return detail;
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ProblemDetail handleUserNotFound(UsernameNotFoundException ex, HttpServletRequest request) {
		ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
		detail.setTitle("User not found");
		detail.setProperty("path", request.getRequestURI());
		return detail;
	}
}

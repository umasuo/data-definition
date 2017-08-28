package com.umasuo.datapoint.infrastructure.exception;

import com.umasuo.exception.handler.ExceptionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * DataDefinitionExceptionHandler.
 */
@Component
public class DataDefinitionExceptionHandler implements ExceptionHandler, HandlerExceptionResolver {

  /**
   * Resolve exception.
   * @param request
   * @param response
   * @param handler
   * @param ex
   * @return
   */
  @Override
  public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                       Object handler, Exception ex) {
    setResponse(request, response, handler, ex);
    return new ModelAndView();
  }

}

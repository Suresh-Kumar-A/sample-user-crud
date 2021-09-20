package com.gmail.creativegeeksuresh.crud.user.security;

import com.gmail.creativegeeksuresh.crud.user.service.util.AppConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception)
      throws IOException, ServletException {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(auth.getAuthorities());
    if ((!(auth instanceof AnonymousAuthenticationToken))
        && authorities.get(0).getAuthority().equals(AppConstants.ROLE_STRING + AppConstants.USER_ROLE_STRING)) {
      response.sendRedirect(request.getContextPath() + "/user/view-books");
    } else {
      response.sendRedirect(request.getContextPath() + "/login?access-denied");
    }
  }
}

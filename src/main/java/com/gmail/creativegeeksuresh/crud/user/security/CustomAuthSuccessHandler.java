package com.gmail.creativegeeksuresh.crud.user.security;

import com.gmail.creativegeeksuresh.crud.user.service.util.AppConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
      throws IOException, ServletException {
    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(auth.getAuthorities());
    if ((!(auth instanceof AnonymousAuthenticationToken))
        && authorities.get(0).getAuthority().equals(AppConstants.ROLE_STRING + AppConstants.USER_ROLE_STRING)) {
      response.sendRedirect(request.getContextPath() + "/user/view-books");
    } else if ((!(auth instanceof AnonymousAuthenticationToken))
        && authorities.get(0).getAuthority().equals(AppConstants.ROLE_STRING + AppConstants.ADMIN_ROLE_STRING)) {
      response.sendRedirect(request.getContextPath() + "/admin/dashboard");
    } else {
      response.sendRedirect(request.getContextPath() + "/login?access-denied");
    }
  }
}

package org.example.forum.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Zadaniem tej klasy jest sprawdzenie czy użytkownk próbujący nawiązać połączenie jest zalogowany i może uzyskać
 * dostęp do danych ukrytych przed niezalogowanymi użytkownikami.
 * @author Artur Leszczak
 * @version 1.0.0
 */

@WebFilter("/protected/*")
public class AuthorizationFilter implements Filter
{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (isAuthorized(httpRequest)) {
            chain.doFilter(request, response);
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.sendRedirect("/login");
            httpResponse.getWriter().write("Brak dostępu!");
        }
    }

    @Override
    public void destroy() { }

    private boolean isAuthorized(HttpServletRequest request) {

        HttpSession session = request.getSession();

        if(session.getAttribute("isLogged") == "true")
        {
            return true;
        }
        return false;
    }
}

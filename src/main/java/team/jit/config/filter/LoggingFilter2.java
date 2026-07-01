package team.jit.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

@Slf4j
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class LoggingFilter2 extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("DRUGI FILTR BLABLA");

        Enumeration<String> headers = request.getHeaderNames();

        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            String val = request.getHeader(header);
            log.info("HEADER: " + header + " VALUE: " + val);
        }

        log.info("Headery:" + headers.toString());

        request.getHeader("Authorization");

        filterChain.doFilter(request, response);
    }
}

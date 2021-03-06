package edu.nju.mall.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class OptionsRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(Objects.equals("OPTIONS", request.getMethod())) {
            response.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,HEAD");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type,XFILENAME," +
                    "XFILECATEGORY,XFILESIZE,Accept,,Referer,Sec-Fetch-Dest,User-Agent");
            response.setHeader("Access-Control-Allow-Origin", "*");
            return;
        }
        filterChain.doFilter(request, response);
    }

}

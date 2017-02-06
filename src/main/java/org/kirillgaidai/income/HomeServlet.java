package org.kirillgaidai.income;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({ "/index.html" })
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        request.setAttribute("message", "Hello, World!");
        request.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(request, response);
    }

}

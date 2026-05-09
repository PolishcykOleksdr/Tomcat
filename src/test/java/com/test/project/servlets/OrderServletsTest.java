package com.test.project.servlets;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServletsTest {

    @Test
    void createOrderShouldReturnJson() throws Exception {
        CreateProductServlet servlet = new CreateProductServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        ByteArrayOutputStream body = new ByteArrayOutputStream();

        when(request.getInputStream()).thenReturn(inputStream("""
                [
                  {
                    "id": 1,
                    "name": "Phone",
                    "cost": 1000.0
                  }
                ]
                """));
        when(response.getOutputStream()).thenReturn(outputStream(body));

        servlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);

        String json = body.toString(StandardCharsets.UTF_8);
        assertTrue(json.contains("\"id\""));
        assertTrue(json.contains("\"date\""));
        assertTrue(json.contains("\"cost\":1000.0"));
        assertTrue(json.contains("\"name\":\"Phone\""));
    }

    @Test
    void getOrderByIdShouldReturnJson() throws Exception {
        int id = createOrderAndReturnId("Keyboard", 250.0);

        GetOrderByIdServlet servlet = new GetOrderByIdServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream body = new ByteArrayOutputStream();

        when(request.getRequestURI()).thenReturn("/get/" + id);
        when(response.getOutputStream()).thenReturn(outputStream(body));

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);

        String json = body.toString(StandardCharsets.UTF_8);
        assertTrue(json.contains("\"id\":" + id));
        assertTrue(json.contains("\"name\":\"Keyboard\""));
    }

    @Test
    void getAllOrdersShouldReturnJsonArray() throws Exception {
        createOrderAndReturnId("Mouse", 80.0);

        GetAllOrdersServlet servlet = new GetAllOrdersServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream body = new ByteArrayOutputStream();

        when(response.getOutputStream()).thenReturn(outputStream(body));

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);

        String json = body.toString(StandardCharsets.UTF_8);
        assertTrue(json.startsWith("["));
        assertTrue(json.endsWith("]"));
        assertTrue(json.contains("\"products\""));
    }

    @Test
    void updateOrderShouldReturnUpdatedOrderAsJson() throws Exception {
        int id = createOrderAndReturnId("Monitor", 500.0);

        UpdateOrderServlet servlet = new UpdateOrderServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream body = new ByteArrayOutputStream();

        when(request.getRequestURI()).thenReturn("/update/" + id);
        when(request.getInputStream()).thenReturn(inputStream("""
                [
                  {
                    "id": 20,
                    "name": "Cable",
                    "cost": 15.0
                  }
                ]
                """));
        when(response.getOutputStream()).thenReturn(outputStream(body));

        servlet.doPut(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);

        String json = body.toString(StandardCharsets.UTF_8);
        assertTrue(json.contains("\"id\":" + id));
        assertTrue(json.contains("\"name\":\"Monitor\""));
        assertTrue(json.contains("\"name\":\"Cable\""));
    }

    @Test
    void deleteOrderShouldReturnPlainText() throws Exception {
        int id = createOrderAndReturnId("Tablet", 400.0);

        DeleteOrderById servlet = new DeleteOrderById();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter body = new StringWriter();

        when(request.getRequestURI()).thenReturn("/delete/" + id);
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        servlet.doDelete(request, response);

        verify(response).setContentType("text/plain");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(body.toString().contains("Deleted order with id - " + id));
    }

    private int createOrderAndReturnId(String productName, double cost) throws Exception {
        CreateProductServlet servlet = new CreateProductServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream body = new ByteArrayOutputStream();

        when(request.getInputStream()).thenReturn(inputStream("""
                [
                  {
                    "id": 1,
                    "name": "%s",
                    "cost": %s
                  }
                ]
                """.formatted(productName, cost)));
        when(response.getOutputStream()).thenReturn(outputStream(body));

        servlet.doPost(request, response);

        return extractId(body.toString(StandardCharsets.UTF_8));
    }

    private int extractId(String json) {
        int fieldStart = json.indexOf("\"id\":") + 5;
        int fieldEnd = json.indexOf(",", fieldStart);
        return Integer.parseInt(json.substring(fieldStart, fieldEnd));
    }

    private ServletInputStream inputStream(String body) {
        ByteArrayInputStream input = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));

        return new ServletInputStream() {
            @Override
            public int read() {
                return input.read();
            }

            @Override
            public boolean isFinished() {
                return input.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }

    private ServletOutputStream outputStream(ByteArrayOutputStream output) {
        return new ServletOutputStream() {
            @Override
            public void write(int b) {
                output.write(b);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
            }
        };
    }
}

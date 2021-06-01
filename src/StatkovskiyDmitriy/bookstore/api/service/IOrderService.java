package StatkovskiyDmitriy.bookstore.api.service;

import StatkovskiyDmitriy.bookstore.dao.OrderDao;
import StatkovskiyDmitriy.bookstore.model.Book;
import StatkovskiyDmitriy.bookstore.model.Order;
import StatkovskiyDmitriy.bookstore.model.enums.OrderStatus;
import StatkovskiyDmitriy.bookstore.service.OrderService;

import java.util.List;

public interface IOrderService {
    Order createNew();

    Order addBook(Order order, Book book);

    Order completeOrder(Order order);

    double calculateOrderPrice(Order order);

    List<Order> sortOrdersByPrice(OrderService orderDao);

    List<Order> sortOrdersByStatus(OrderDao orderDao);

    List<Order> sortOrdersByFulfillmentDate(OrderDao orderDao);

    Order changeOrderStatus(Order order, OrderStatus status);

    Order cancelOrder(Order order);

    Order getOrder(String id);

    Order delete(String id);
}

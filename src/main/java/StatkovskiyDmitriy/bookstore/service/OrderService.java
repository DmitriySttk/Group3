package StatkovskiyDmitriy.bookstore.service;

import StatkovskiyDmitriy.bookstore.api.dao.IOrderDao;
import StatkovskiyDmitriy.bookstore.api.service.IBookService;
import StatkovskiyDmitriy.bookstore.api.service.IOrderService;
import StatkovskiyDmitriy.bookstore.api.service.IRequestService;
import StatkovskiyDmitriy.bookstore.dao.OrderDao;
import StatkovskiyDmitriy.bookstore.model.Book;
import StatkovskiyDmitriy.bookstore.model.Order;
import StatkovskiyDmitriy.bookstore.model.enums.OrderStatus;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService implements IOrderService {
    private static OrderService instance;
    private IOrderDao orderDao = OrderDao.getInstance();
    private IBookService bookService = BookService.getInstance();
    private IRequestService requestService;

    private OrderService() {

    }

    public static OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }

    public OrderService(IOrderDao orderDao, IBookService bookService, IRequestService requestService) {
        this.orderDao = orderDao;
        this.bookService = bookService;
        this.requestService = requestService;
    }

    @Override
    public Order createNew() {
        return orderDao.createOrder();
    }

    @Override
    public Order addBook(Order order, Book book) {
        order.getBooks().add(book);
        return order;
    }

    @Override
    public Order completeOrder(Order order) {
        if (!order.getStatus().equals(OrderStatus.NEW)) {
            return order;
        }
        List<Book> outOfStockBook = bookService.getOutOfStockBooks(order);
        if (outOfStockBook.size() == 0) {
            order.setOrderClosedDate(LocalDate.now());
            order.setStatus(OrderStatus.COMPLETED);
        } else {
            Book book = outOfStockBook.stream()
                    .findFirst()
                    .get();
            requestService.createRequest(book);
        }
        return order;
    }

    @Override
    public Order cancelOrder(Order order) {
        if (order.getStatus().equals(OrderStatus.NEW)) {
            order.setStatus(OrderStatus.CANCELED);
        }
        return order;
    }

    public Order changeOrderStatus(Order order, OrderStatus status) {
        order.setStatus(status);
        return order;
    }

    public double calculateOrderPrice(Order order) {

        List<Book> bookOlds = order.getBooks();
        return bookOlds.stream()
                .map(book -> book.getPrice())
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public List<Order> sortOrdersByFulfillmentDate() {
        List<Order> orders = orderDao.getAll();
        return orders.stream()
                .filter(order -> order.getOrderClosedDate() != null)
                .sorted(Comparator.comparing(o -> o.getOrderClosedDate()))
                .collect(Collectors.toList());
    }

    public List<Order> sortOrdersByStatus() {
        List<Order> orders = orderDao.getAll();
        return orders.stream()
                .sorted(Comparator.comparing(o -> o.getStatus()))
                .collect(Collectors.toList());
    }

    public List<Order> sortOrdersByPrice() {
        List<Order> orders = orderDao.getAll();
        orders.forEach(order -> order.setOrderPrice(calculateOrderPrice(order)));
        return orders.stream()
                .sorted(Comparator.comparing(o -> o.getOrderPrice()))
                .collect(Collectors.toList());
    }

    public List<Order> sortCompletedOrdersByPriceFromRange(LocalDate from, LocalDate to) {
        List<Order> orders = orderDao.getAll();
        orders.stream()
                .filter(order -> order.getOrderClosedDate() != null)
                .filter(order -> order.getOrderClosedDate().isBefore(to.plusDays(1)))
                .filter(order -> order.getOrderClosedDate().isAfter(from))
                .filter(order -> order.getStatus().equals(OrderStatus.COMPLETED))
                .forEach(order -> order.setOrderPrice(calculateOrderPrice(order)));

        return orders.stream()
                .sorted(Comparator.comparing(o -> o.getOrderPrice()))
                .collect(Collectors.toList());
    }

    public List<Order> sortCompletedOrdersByCompletedDateFromRange(LocalDate from, LocalDate to) {
        List<Order> orders = orderDao.getAll();
        return orders.stream()
                .filter(order -> order.getOrderClosedDate() != null)
                .filter(order -> order.getOrderClosedDate().isBefore(to.plusDays(1)))
                .filter(order -> order.getOrderClosedDate().isAfter(from))
                .filter(order -> order.getStatus().equals(OrderStatus.COMPLETED))
                .sorted(Comparator.comparing(o -> o.getOrderClosedDate()))
                .collect(Collectors.toList());
    }

    public double calculateEarnedMoneyFromRange(LocalDate from, LocalDate to) {
        List<Order> orders = orderDao.getAll();
        orders.stream()
                .filter(order -> order.getOrderClosedDate() != null)
                .filter(order -> order.getOrderClosedDate().isBefore(to.plusDays(1)))
                .filter(order -> order.getOrderClosedDate().isAfter(from))
                .filter(order -> order.getStatus().equals(OrderStatus.COMPLETED))
                .forEach(order -> order.setOrderPrice(calculateOrderPrice(order)));

        return orders.stream()
                .map(order -> order.getOrderPrice())
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public int numberOfCompletedOrdersFromRange(LocalDate from, LocalDate to) {
        List<Order> orders = orderDao.getAll();
        List<Order> completedOrders = orders.stream()
                .filter(order -> order.getOrderClosedDate() != null)
                .filter(order -> order.getOrderClosedDate().isAfter(from))
                .filter(date -> date.getOrderClosedDate().isBefore(to.plusDays(1)))
                .filter(order -> order.getStatus().equals(OrderStatus.COMPLETED))
                .collect(Collectors.toList());
        int number = completedOrders.size();
        return number;
    }

    public Order showOrderInformation(String customerName) {
        List<Order> units = orderDao.getAll();
        Order details = units.stream()
                .filter(order -> order.getCustomerName().equals(customerName))
                .findFirst()
                .get();
        return details;
    }

    @Override
    public List<Order> getAll() {
        return orderDao.getAll();
    }

    public Order getOrderById(String id) {
        Order order = orderDao.getAll().stream()
                .filter(order1 -> order1.getOrderNumber().equals(id))
                .findFirst()
                .get();
        return order;
    }
}
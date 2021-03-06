package StatkovskiyDmitriy.bookstoreUI;

import DmitriyStatkovskiy.ioc.annotation.Autowired;
import DmitriyStatkovskiy.ioc.annotation.Component;
import StatkovskiyDmitriy.bookstore.api.service.IBookService;
import StatkovskiyDmitriy.bookstore.api.service.IOrderService;
import StatkovskiyDmitriy.bookstore.api.service.IRequestService;
import StatkovskiyDmitriy.bookstore.model.Book;
import StatkovskiyDmitriy.bookstore.model.Order;
import StatkovskiyDmitriy.bookstore.model.Request;
import StatkovskiyDmitriy.bookstore.model.enums.OrderStatus;
import StatkovskiyDmitriy.bookstore.model.enums.RequestStatus;
import StatkovskiyDmitriy.bookstore.service.BookService;
import StatkovskiyDmitriy.bookstore.service.OrderService;
import StatkovskiyDmitriy.bookstore.service.RequestService;

import java.time.LocalDate;
import java.util.List;

@Component
public class Manager implements IManager {
    private static Manager instance;
    @Autowired
    private IBookService bookService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IRequestService requestService;

    public static Manager getInstance() {
        if (instance == null) {
            instance = new Manager();
        }
        return instance;
    }

    private Manager() {
    }

    public void addBook(Book book) {
        bookService.addBook(book);
    }

    public List<Book> getBooks() {
        return bookService.getAllBooks();
    }

    public void changeStatus(String name) {
        bookService.changeStatusByName(name);
    }

    public void addBookAndCloseRequest(String name) {
        bookService.addBookAndCloseRequest(name);
    }

    public String showDescription(String name) {
        return bookService.showDescription(name);
    }

    public List<Book> getOutOfStock() {
        return bookService.getOutBooks();
    }

    public List<Book> sortByName() {
        return bookService.sortBooksByName();
    }

    public List<Book> sortByPrice() {
        return bookService.sortBooksByPrice();
    }

    public List<Book> sortByStatus() {
        return bookService.sortBooksByStatus();
    }

    public List<Book> sortOldByIncDate() {
        return bookService.sortOldBooksByIncomingDate();
    }

    public List<Book> sortOldByPrice() {
        return bookService.sortOldBooksByPrice();
    }

    public List<Order> getOrders() {
        return orderService.getAll();
    }

    public void setOldBooks(int month) {
        bookService.manualSetOldBooks(month);
    }

    public void addOrder() {
        orderService.createNew();
    }

    public void addBookToOrder(String orderId, String bookName) {
        Order order = orderService.getOrderById(orderId);
        Book book = bookService.getBookByName(bookName);
        orderService.addBook(order, book);
    }

    public void completeOrder(String orderId) {
        Order order = orderService.getOrderById(orderId);
        orderService.completeOrder(order);
    }

    public void cancelOrder(String orderId) {
        Order order = orderService.getOrderById(orderId);
        orderService.cancelOrder(order);
    }

    public void changeOrderStatus(String orderId, String status) {
        Order order = orderService.getOrderById(orderId);
        if (status.equals("cancel")) {
            cancelOrder(orderId);
        }
        if (status.equals("complete")) {
            completeOrder(orderId);
        } else orderService.changeOrderStatus(order, OrderStatus.NEW);
    }

    public List<Order> sortOrdersByPrice() {
        return orderService.sortOrdersByPrice();
    }

    public List<Order> sortOrdersByCompletedDate() {
        return orderService.sortOrdersByFulfillmentDate();
    }

    public List<Order> sortOrdersByStatus() {
        return orderService.sortOrdersByStatus();
    }

    public List<Order> sortCompletedOrdersByPrice(LocalDate from, LocalDate to) {
        return orderService.sortCompletedOrdersByPriceFromRange(from, to);
    }

    public List<Order> sortCompletedOrdersByDate(LocalDate from, LocalDate to) {
        return orderService.sortCompletedOrdersByCompletedDateFromRange(from, to);
    }

    public void showOrderDetails(String id) {
        Order order = orderService.getOrderById(id);
        System.out.println(order);
    }

    public double showProfit(LocalDate from, LocalDate to) {

        return orderService.calculateEarnedMoneyFromRange(from, to);
    }

    public int showCompletedOrderValue(LocalDate from, LocalDate to) {
        return orderService.numberOfCompletedOrdersFromRange(from, to);
    }

    public void createRequest(String name) {
        Book book = bookService.getBookByName(name);
        requestService.createRequest(book);
    }

    public List<Request> getRequests() {
        return requestService.getAll();
    }

    public void changeRequestStatus(String name) {
        Request request = requestService.getRequestByName(name);
        if (request.getStatus().equals(RequestStatus.OPEN)) {
            requestService.changeRequestStatusByBookName(name, RequestStatus.CLOSED);
        } else {
            requestService.changeRequestStatusByBookName(name, RequestStatus.OPEN);
        }
    }

    public List<Request> sortRequestsQuantity() {
        return requestService.sortRequestsByQuantity();
    }

    public List<Request> sortRequestsByBookName() {
        return requestService.sortRequestsByBookName();
    }

    public void setIncomingDate(String book, LocalDate date) {
        bookService.setIncomingDate(book, date);
    }

    public void deleteAllRequests() {
        requestService.deleteAll();
    }

    public void deleteAllOrders() {
        orderService.deleteAll();
    }

    public void deleteAllBooks() {
        bookService.deleteAll();
    }
}

package StatkovskiyDmitriy.bookstore;

import StatkovskiyDmitriy.bookstore.api.dao.IOrderDao;
import StatkovskiyDmitriy.bookstore.api.dao.IRequestDao;
import StatkovskiyDmitriy.bookstore.api.service.IBookService;
import StatkovskiyDmitriy.bookstore.api.service.IRequestService;
import StatkovskiyDmitriy.bookstore.dao.BookDao;
import StatkovskiyDmitriy.bookstore.dao.OrderDao;
import StatkovskiyDmitriy.bookstore.dao.RequestDao;
import StatkovskiyDmitriy.bookstore.model.Book;
import StatkovskiyDmitriy.bookstore.model.Order;
import StatkovskiyDmitriy.bookstore.model.enums.BookStatus;
import StatkovskiyDmitriy.bookstore.model.enums.OrderStatus;
import StatkovskiyDmitriy.bookstore.service.BookService;
import StatkovskiyDmitriy.bookstore.service.OrderService;
import StatkovskiyDmitriy.bookstore.service.RequestService;

import java.time.LocalDate;

/**
 *
 */

public class Main {
    public static void main(String[] args) {

        IOrderDao orderDao = OrderDao.getInstance();
        IRequestDao requestDao = RequestDao.getInstance();
        IRequestService requestService = RequestService.getInstance();

        BookDao bookDao = BookDao.getInstance();

        IBookService bookService = new BookService(bookDao, requestService);

        OrderService orderService = new OrderService(orderDao, bookService, requestService);

        Order order = orderService.createNew();
        Order order2 = orderService.createNew();
        Order order3 = orderService.createNew();

        Book bookA = new Book("AAA", "1", 10, "aaa");
        Book bookB = new Book("BBB", "2", 20, "bbb");
        Book bookC = new Book("CCC", "3", 30, "ccc");
        Book bookD = new Book("DDD", "4", 40, "ddd");
        Book bookE = new Book("EEE", "5", 50, "eee");

        bookDao.addBook(bookA);
        bookDao.addBook(bookB);
        bookDao.addBook(bookD);
        bookDao.addBook(bookE);
        bookDao.addBook(bookC);
        bookService.changeBookStatus(bookDao.getBookByBookId(bookB.getId()), BookStatus.OUT_OF_STOCK);

        bookService.sortBooksByName();
        bookService.sortBooksByStatus();

        bookService.sortBooksByName();
        bookService.sortBooksByStatus();

        orderService.addBook(order, bookA);
        orderService.addBook(order, bookB);
        orderService.addBook(order2, bookE);
        orderService.addBook(order3, bookA);

        requestService.createRequest(bookB);
        requestService.createRequest(bookA);
        requestService.createRequest(bookB);
        requestService.createRequest(bookB);
        requestService.createRequest(bookB);
        requestService.createRequest(bookB);
        requestService.createRequest(bookC);
        requestService.createRequest(bookA);
        requestService.createRequest(bookA);

        orderService.cancelOrder(order);
        System.out.println(order);
        orderService.completeOrder(order);

        System.out.println(order);
        System.out.println("Created requests:");
        requestDao.getAll().forEach(System.out::println);
        bookService.addBookToStock(bookB);
        requestDao.getAll().forEach(System.out::println);
        orderService.completeOrder(order);
        System.out.println(order);

        System.out.println();

        System.out.println();
        bookService.sortBooksByName();

        requestService.sortRequestsByBookName().forEach(System.out::println);

        System.out.println(orderService.calculateOrderPrice(order));
        System.out.println("Orders without sort:");
        orderDao.getAll().forEach(System.out::println);
        System.out.println("sorted orders:");
        orderService.sortOrdersByPrice().forEach(System.out::println);

        System.out.println(bookService.showDescription("DDD"));

        orderService.addBook(order2, bookD);
        System.out.println(order2);
        orderService.changeOrderStatus(order2, OrderStatus.CANCELED);
        orderService.completeOrder(order3);
        System.out.println(order2);
        System.out.println(order3);

        requestService.sortRequestsByQuantity().forEach(System.out::println);
        System.out.println(orderService.numberOfCompletedOrdersFromRange(LocalDate.of(2021, 6, 1), LocalDate.of(2021, 6, 12)));
        bookService.changeStatusByName("AAA");

        bookService.addBookToStock(bookA);
        requestService.sortRequestsByQuantity().forEach(System.out::println);



    }
}
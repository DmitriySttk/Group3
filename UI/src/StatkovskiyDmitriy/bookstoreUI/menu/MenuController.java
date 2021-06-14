package StatkovskiyDmitriy.bookstoreUI.menu;

import StatkovskiyDmitriy.bookstore.dao.BookDao;
import StatkovskiyDmitriy.bookstore.dao.OrderDao;
import StatkovskiyDmitriy.bookstore.model.Book;
import StatkovskiyDmitriy.bookstore.model.Order;
import StatkovskiyDmitriy.bookstore.service.OrderService;

import java.util.Scanner;

public class MenuController {
    private static MenuController instance;
    private final Builder builder;
    private final Navigator navigator;

    private MenuController() {
        builder = Builder.getInstance();
        builder.buildMenu();
        navigator = Navigator.getInstance();
    }

    public static MenuController getInstance() {
        if (instance == null) {
            instance = new MenuController();
        }
        return instance;
    }

    public void run() {
        initDao();
        Integer index = -1;
        Scanner scanner = new Scanner(System.in);
        navigator.setCurrentMenu(builder.getRootMenu());
        navigator.printMenu();
        while (!index.equals(123)) {
            index = scanner.nextInt();
            navigator.navigate(index);
            navigator.printMenu();
        }
    }

    private void initDao() {
        BookDao bookInstance = BookDao.getInstance();
        OrderDao orderInstance = OrderDao.getInstance();
        OrderService orderService = OrderService.getInstance();
        Book bookA = new Book("AAA", "1", 10, "aaa");
        Book bookB = new Book("BBB", "2", 20, "bbb");
        Book bookC = new Book("CCC", "3", 30, "ccc");
        Book bookD = new Book("DDD", "4", 40, "ddd");
        Book bookE = new Book("EEE", "5", 50, "eee");
        bookInstance.addBook(bookA);
        bookInstance.addBook(bookB);
        bookInstance.addBook(bookD);
        bookInstance.addBook(bookE);
        bookInstance.addBook(bookC);

        Order order1 = orderInstance.createOrder();
        Order order2 = orderInstance.createOrder();
        orderService.addBook(order1,bookA);
        orderService.addBook(order2,bookB);
        orderService.addBook(order2,bookC);
    }
}

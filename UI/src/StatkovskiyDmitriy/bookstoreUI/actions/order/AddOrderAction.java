package StatkovskiyDmitriy.bookstoreUI.actions.order;

import StatkovskiyDmitriy.bookstoreUI.actions.AbstractAction;
import StatkovskiyDmitriy.bookstoreUI.actions.IAction;

public class AddOrderAction extends AbstractAction implements IAction {
    @Override
    public void execute() {
        manager.addOrder();
    }
}
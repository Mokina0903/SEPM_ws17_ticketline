package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class LocalizationSubject {

    private List<LocalizationObserver> observers = new ArrayList<>();

    public void attach(LocalizationObserver observer){
        observers.add(observer);
    }

    public void notifyAllObservers(){
        for (LocalizationObserver observer : observers) {
            observer.update();
        }
    }
}

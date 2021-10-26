package web.service;

import web.model.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CarServiceImp implements CarService {
    List<Car> cars = new ArrayList<>();
    {
        cars.add(new Car("Tesla", "First", "Black"));
        cars.add(new Car("BMW", "Second", "White"));
        cars.add(new Car("Ford", "Third", "Red"));
        cars.add(new Car("Porshe", "Fourth", "Yellow"));
        cars.add(new Car("Bently", "Fifth", "Blue"));
    }

    @Override
    public List<Car> getCars (int count) {
        return cars.stream().limit(count).collect(Collectors.toList());
    }
}

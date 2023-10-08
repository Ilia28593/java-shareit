package ru.practicum.shareit.booking.BookingStateFetchByBooker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingStatusFilter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class BookingStateFetchBookerStrategyFactory {
    private final Map<BookingStatusFilter, BookingStateFetchByBookerStrategy> strategyMap;

    @Autowired
    public BookingStateFetchBookerStrategyFactory(Set<BookingStateFetchByBookerStrategy> strategySet) {
        this.strategyMap = new HashMap<>();
        for (BookingStateFetchByBookerStrategy strategy : strategySet) {
            strategyMap.put(strategy.getStrategyName(), strategy);
        }
    }

    public BookingStateFetchByBookerStrategy findStrategy(BookingStatusFilter state) {
        return strategyMap.get(state);
    }
}

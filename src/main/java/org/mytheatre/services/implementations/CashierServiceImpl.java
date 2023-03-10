package org.mytheatre.services.implementations;

import org.mytheatre.Theatre;
import org.mytheatre.customer.Customer;
import org.mytheatre.movie.Movie;
import org.mytheatre.services.CashierService;

import java.util.ArrayList;
import java.util.List;

public class CashierServiceImpl implements CashierService {
    private final Theatre theatre;

    public CashierServiceImpl(Theatre theatre) {
        this.theatre = theatre;
    }

    @Override
    public void attendToCustomer(Customer customer) {
        double totalCostOfMovies = 0.0;
        List<Movie> removeFromCart = new ArrayList<>();
        for (Movie currentMovie : customer.getCart()) {
            if (isMovieAvailable(currentMovie)) {
                totalCostOfMovies += currentMovie.getPrice() * currentMovie.getQuantity();
            } else {
                System.out.println("Sorry " + customer.getName() + " we don't have " + currentMovie.getName() + " in stock..");
                removeFromCart.add(currentMovie);
                System.out.println("Removed: " + currentMovie.getName());
            }
        }
        customer.getCart().removeAll(removeFromCart);

        if (customer.getCreditCard() >= totalCostOfMovies) {
            for (Movie currentMovie : customer.getCart()) {
                updateMoviesSoldQty(currentMovie, currentMovie.getQuantity());
            }
            customer.setCreditCard(customer.getCreditCard() - totalCostOfMovies);
            theatre.setAccountBalance(theatre.getAccountBalance() + totalCostOfMovies);
        }
    }

    @Override
    public boolean isMovieAvailable(Movie movie) {
        for (Movie mov : theatre.getMoviesCatalog()) {
            if (mov.getName().equals(movie.getName()) && mov.getQuantity() >= movie.getQuantity()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateMoviesSoldQty(Movie movie, int quantity) {
        for (Movie theatreMovie : theatre.getMoviesCatalog()) {
            if (movie.getName().equals(theatreMovie.getName())) {
                theatreMovie.setQuantity(theatreMovie.getQuantity() - quantity);
                break;
            }
        }
    }
}

package market;

import java.util.ArrayList;
import java.util.List;

class Cart {
    private List<Game> cartItems = new ArrayList<>();

    public void addGame(Game game) {
        cartItems.add(game);
    }

    public void removeGameByName(String gameName) {
        cartItems.removeIf(game -> game.getName().equalsIgnoreCase(gameName));
    }

    public void clear() {
        cartItems.clear();
    }

    public List<Game> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
}




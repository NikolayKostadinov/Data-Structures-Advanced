import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class RoyaleArena implements IArena {
    private final Map<Integer, Battlecard> cards;

    public RoyaleArena() {
        this.cards = new LinkedHashMap<>();
    }

    @Override
    public void add(Battlecard card) {
        this.cards.put(card.getId(), card);
    }

    @Override
    public boolean contains(Battlecard card) {
        return this.cards.containsKey(card.getId());
    }

    @Override
    public int count() {
        return this.cards.size();
    }

    @Override
    public void changeCardType(int id, CardType type) {
        if (!this.cards.containsKey(id)) throw new IllegalArgumentException();
        this.cards.get(id).setType(type);
    }

    @Override
    public Battlecard getById(int id) {
        ensureCard(id);
        return this.cards.get(id);
    }

    @Override
    public void removeById(int id) {
        ensureCard(id);
        this.cards.remove(id);
    }

    @Override
    public Iterable<Battlecard> getByCardType(CardType type) {
        return getBattleCards(c -> c.getType().equals(type));
    }

    @Override
    public Iterable<Battlecard> getByTypeAndDamageRangeOrderedByDamageThenById(CardType type, int lo, int hi) {
        return getBattleCards(c -> c.getType().equals(type) && lo < c.getDamage() && c.getDamage() < hi);
    }

    @Override
    public Iterable<Battlecard> getByCardTypeAndMaximumDamage(CardType type, double damage) {
        return getBattleCards(c -> c.getType().equals(type) && c.getDamage() <= damage);
    }

    private List<Battlecard> getBattleCards(Predicate<Battlecard> cardFilter) {
        List<Battlecard> cards = this.cards.values().stream().filter(cardFilter).sorted((c1, c2) -> {
            int result = Double.compare(c2.getDamage(), c1.getDamage());
            if (result == 0) {
                result = Integer.compare(c1.getId(), c2.getId());
            }
            return result;
        }).collect(Collectors.toList());

        ensureCards(cards);
        return cards;
    }

    @Override
    public Iterable<Battlecard> getByNameOrderedBySwagDescending(String name) {
        return getBattleCardsByName(c -> c.getName().equals(name));
    }

    @Override
    public Iterable<Battlecard> getByNameAndSwagRange(String name, double lo, double hi) {
        return getBattleCardsByName(c -> c.getName().equals(name) && lo <= c.getSwag() && c.getSwag() < hi);
    }

    private List<Battlecard> getBattleCardsByName(Predicate<Battlecard> cardPredicate) {
        List<Battlecard> cards = this.cards.values().stream().filter(cardPredicate).sorted((c1, c2) -> {
            int result = Double.compare(c2.getSwag(), c1.getSwag());
            if (result == 0) {
                result = Integer.compare(c1.getId(), c2.getId());
            }
            return result;
        }).collect(Collectors.toList());

        ensureCards(cards);
        return cards;
    }

    @Override
    public Iterable<Battlecard> getAllByNameAndSwag() {
        if (this.cards.isEmpty()) return new ArrayList<>();
        Map<String, Battlecard> result = new LinkedHashMap<>();

        for (Battlecard battlecard : this.cards.values()) {
            result.putIfAbsent(battlecard.getName(), battlecard);
            if (battlecard.getSwag() > result.get(battlecard.getName()).getSwag()) {
                result.put(battlecard.getName(), battlecard);
            }
        }
        return result.values();
    }

    @Override
    public Iterable<Battlecard> findFirstLeastSwag(int n) {
        if (n > this.cards.size()) throw new UnsupportedOperationException();
        return this.cards.values().stream().sorted(Comparator.comparingDouble(Battlecard::getSwag).thenComparingInt(Battlecard::getId)).limit(n).collect(Collectors.toList());
    }

    @Override
    public Iterable<Battlecard> getAllInSwagRange(double lo, double hi) {
        return this.cards.values().stream().filter(c -> lo <= c.getSwag() && c.getSwag() <= hi).sorted(Comparator.comparingDouble(Battlecard::getSwag)).collect(Collectors.toList());
    }

    @Override
    public Iterator<Battlecard> iterator() {
        return this.cards.values().iterator();
    }

    private void ensureCard(int id) {
        if (!this.cards.containsKey(id)) throw new UnsupportedOperationException();
    }

    private void ensureCards(List<Battlecard> cards) {
        if (cards.isEmpty()) throw new UnsupportedOperationException();
    }
}

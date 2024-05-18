import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
public class SimpleTest {
    @Test
    @DisplayName("Should display simple assertion")
    void shouldShowSimpleAssertion() {
        assertEquals(1, 1);
    }

    @Test
    @DisplayName("Should check all items in the list")
    void shouldCheckAllItemsInTheList() {
        List<Integer> list = List.of(2, 2, 3, 4);
        Assertions.assertAll(()-> assertEquals(2, list.get(0)),
                ()-> assertEquals(2, list.get(1)),
                ()-> assertEquals(1, list.get(2)),
                ()-> assertEquals(1, list.get(3)));
    }
}

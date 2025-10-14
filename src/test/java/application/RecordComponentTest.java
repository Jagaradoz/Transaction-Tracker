package application;

import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Initialize RecordComponentTest")
class RecordComponentTest {
    private RecordComponent record;

    @BeforeEach
    public void setUp() {
        record = new RecordComponent(new TransactionTrackerPanel(), UUID.randomUUID().toString(), "Eggs", 0, 100);
    }

    @Test
    @DisplayName("Should have detail")
    public void shouldHaveDetail() {
        assertNotEquals("", record.getDetailLabel().getText());
        assertNotEquals(" ", record.getDetailLabel().getText());
    }

    @Test
    @DisplayName("Should have income")
    public void shouldHaveIncome() {
        assertEquals(0, record.getIncome());
    }

    @Test
    @DisplayName("Should have expense")
    public void shouldHaveExpense() {
        assertEquals(100, record.getExpense());
    }

    @Test
    @DisplayName("Should generate label properly")
    public void shouldGenerateLabelProperly() {
        assertNotNull(record.generateLabel("TEST"));
    }

    @Test
    @DisplayName("Should set select properly")
    public void shouldSetSelectProperly(){
        record.setSelected(true);

        assertTrue(record.isSelected());
    }
}
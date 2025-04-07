import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User userJohn;
    private User userSarmad;
    private User userJessica;
    private User userHenri;

    @BeforeEach
    public void setUp() {
        // Create users with initial balances
        userJohn = new User("john_doe", "1234", 100.0);
        userSarmad = new User("Sarmad", "1234", 200.0);
        userJessica = new User("Jessica", "1234", 500.0);
        userHenri = new User("Henri", "1234", 100.0);
    }

    // ======== john_doe Tests ========
    @Test
    public void testJohnAuthenticateSuccess() {
        assertTrue(userJohn.authenticate("john_doe", "1234"));
    }

    @Test
    public void testJohnWithdraw() {
        boolean result = userJohn.withdraw(50.0);
        assertTrue(result);
        assertEquals(50.0, userJohn.getBalance(), 0.001);
    }

    @Test
    public void testJohnDeposit() {
        userJohn.deposit(20.0);
        assertEquals(120.0, userJohn.getBalance(), 0.001);
    }

    @Test
    public void testJohnInsufficientFunds() {
        boolean result = userJohn.withdraw(200.0);
        assertFalse(result);
        assertEquals(100.0, userJohn.getBalance(), 0.001);
    }

    // ======== Sarmad Tests ========
    @Test
    public void testSarmadAuthenticateSuccess() {
        assertTrue(userSarmad.authenticate("Sarmad", "1234"));
    }

    @Test
    public void testSarmadWithdraw() {
        boolean result = userSarmad.withdraw(50.0);
        assertTrue(result);
        assertEquals(150.0, userSarmad.getBalance(), 0.001);
    }

    @Test
    public void testSarmadDeposit() {
        userSarmad.deposit(100.0);
        assertEquals(300.0, userSarmad.getBalance(), 0.001);
    }

    @Test
    public void testSarmadInsufficientWithdraw() {
        boolean result = userSarmad.withdraw(1000.0);
        assertFalse(result);
        assertEquals(200.0, userSarmad.getBalance(), 0.001);
    }

    // ======== Jessica Tests ========
    @Test
    public void testJessicaAuthenticateSuccess() {
        assertTrue(userJessica.authenticate("Jessica", "1234"));
    }

    @Test
    public void testJessicaWithdraw() {
        boolean result = userJessica.withdraw(100.0);
        assertTrue(result);
        assertEquals(400.0, userJessica.getBalance(), 0.001);
    }

    @Test
    public void testJessicaDeposit() {
        userJessica.deposit(200.0);
        assertEquals(700.0, userJessica.getBalance(), 0.001);
    }

    @Test
    public void testJessicaIncorrectPin() {
        assertFalse(userJessica.authenticate("Jessica", "0000"));
    }

    @Test
    public void testJessicaGetUserId() {
        assertEquals("Jessica", userJessica.getUsername());
    }

    // ======== Henri Tests ========
    @Test
    public void testHenriAuthenticateSuccess() {
        assertTrue(userHenri.authenticate("Henri", "1234"));
    }

    @Test
    public void testHenriWithdraw() {
        boolean result = userHenri.withdraw(25.0);
        assertTrue(result);
        assertEquals(75.0, userHenri.getBalance(), 0.001);
    }

    @Test
    public void testHenriDeposit() {
        userHenri.deposit(50.0);
        assertEquals(150.0, userHenri.getBalance(), 0.001);
    }

    @Test
    public void testHenriInsufficientFunds() {
        boolean result = userHenri.withdraw(200.0);
        assertFalse(result);
        assertEquals(100.0, userHenri.getBalance(), 0.001);
    }

    @Test
    public void testHenriGetUserId() {
        assertEquals("Henri", userHenri.getUsername());
    }
}

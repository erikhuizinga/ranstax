import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StackTest {
    @Test
    fun givenAnEmptyName_ThenIsValidIsFalse() {
        assertFalse(Stack(name = "", size = 0).isValid)
    }

    @Test
    fun givenABlankName_ThenIsValidIsFalse() {
        assertFalse(Stack(name = " ", size = 0).isValid)
    }

    @Test
    fun givenANegativeSize_ThenIsValidIsFalse() {
        assertFalse(Stack(name = "name", size = -1).isValid)
    }

    @Test
    fun givenANonBlankNameAndNonNegativeSize_ThenIsValidIsTrue() {
        assertTrue(Stack(name = "name", size = 0).isValid)
    }
}
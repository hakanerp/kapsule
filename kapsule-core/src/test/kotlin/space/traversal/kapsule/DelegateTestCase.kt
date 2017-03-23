package space.traversal.kapsule

import junit.framework.TestCase
import org.junit.Test
import org.mockito.Mockito
import kotlin.reflect.KProperty

/**
 * Test case for [Delegate].
 */
class DelegateTestCase : TestCase() {

    @Test fun testInitialize_required() {
        val delegate = Delegate<RequiredModule, String>({ value }, true)
        assertEquals(null, delegate.value)

        listOf("one", "two").forEach { value ->
            val module = RequiredModule(value)
            delegate.initialize(module)
            assertEquals(value, delegate.value)
        }
    }

    @Test fun testInitialize_optional() {
        val delegate = Delegate<OptionalModule, String?>({ value }, false)
        assertEquals(null, delegate.value)

        listOf(null, "three").forEach { value ->
            val module = OptionalModule(value)
            delegate.initialize(module)
            assertEquals(value, delegate.value)
        }
    }

    @Test fun testGetValue_required() {
        val delegate = Delegate<RequiredModule, String>({ value }, true)
        val prop = Mockito.mock(KProperty::class.java)

        try {
            delegate.getValue(null, prop)
            assertTrue(false)
        } catch (e: KotlinNullPointerException) {
            assertTrue(true)
        }

        val expected = "test1"
        delegate.value = expected
        assertEquals(expected, delegate.getValue(null, prop))
    }

    @Test fun testGetValue_optional() {
        val delegate = Delegate<OptionalModule, String?>({ value }, false)
        val prop = Mockito.mock(KProperty::class.java)

        assertEquals(null, delegate.getValue(null, prop))

        val expected = "test2"
        delegate.value = expected
        assertEquals(expected, delegate.getValue(null, prop))
    }

    @Test fun testSetValue_required() {
        val delegate = Delegate<RequiredModule, String>({ value }, true)
        val prop = Mockito.mock(KProperty::class.java)

        listOf("test3", "test4").forEach { expected ->
            delegate.setValue(null, prop, expected)
            assertEquals(expected, delegate.value)
        }
    }

    @Test fun testSetValue_optional() {
        val delegate = Delegate<OptionalModule, String?>({ value }, false)
        val prop = Mockito.mock(KProperty::class.java)

        listOf("test3", null).forEach { expected ->
            delegate.setValue(null, prop, expected)
            assertEquals(expected, delegate.value)
        }
    }
}
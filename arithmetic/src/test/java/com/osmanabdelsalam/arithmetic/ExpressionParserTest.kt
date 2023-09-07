package com.osmanabdelsalam.arithmetic

import org.junit.Assert
import org.junit.Test

class ExpressionParserTest {
    @Test
    fun `evaluate 3+3`() {
        Assert.assertEquals(ExpressionParser().evaluate("3+3"), 6.0, .5)
    }

    @Test
    fun `evaluate 3-3`() {
        Assert.assertEquals(ExpressionParser().evaluate("3-3"), 0.0, .5)
    }

    @Test
    fun `evaluate 3-2*3`() {
        Assert.assertEquals(ExpressionParser().evaluate("3-2*3"), -3.0, .5)
    }

    @Test
    fun `evaluate 3-(4+5)*2`() {
        Assert.assertEquals(ExpressionParser().evaluate("3-(4+5)*2"), -15.0, .5)
    }

    @Test
    fun `evaluate 3-(4+5*(19-2))*2`() {
        Assert.assertEquals(ExpressionParser().evaluate("3-(4+5*(19-2))*2"), -175.0, .5)
    }

    @Test
    fun `evaluate 3-(4+5*(19-2))^2`() {
        Assert.assertEquals(ExpressionParser().evaluate("3-(4+5*(19-2))^2"), -7918.0, .5 )
    }

    @Test
    fun `evaluate 3-(4+5*(19-2))%2`() {
        Assert.assertEquals(ExpressionParser().evaluate("3-(4+5*(19-2))%2"), -1.11, .5 )
    }

    @Test
    fun `evaluate 5(4+5*(19-2))2`() {
        Assert.assertEquals(ExpressionParser().evaluate("5(4+5*(19-2))2"), 890.0, .5 )
    }

    @Test
    fun `evaluate factorial(5)`() {
        Assert.assertEquals(ExpressionParser().evaluate("(5)!"), 120.0, .5 )
    }

    @Test
    fun `evaluate 55 divided by 2`() {
        Assert.assertEquals(ExpressionParser().evaluate("55/2"), 27.5, .5 )
    }
}
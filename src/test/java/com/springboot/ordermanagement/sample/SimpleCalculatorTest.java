package com.springboot.ordermanagement.sample;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
// Junit - java unit testing used to write test cases for methods and classes which contains logic
// We are using Junit version 4
@RunWith(SpringRunner.class) // this helps to run SimpleCalculatorTest as part of junit
public class SimpleCalculatorTest {
	
	SimpleCalculator simpleCalculator = new SimpleCalculator();
	
	@Test // this method is considered as test case
	public void addTestPositive() {
		int res = simpleCalculator.add(10, 5);
		assertNotNull(res);
		assertEquals(15, res); // it checks whether expected is matching with actual or not
	}
	
	@Test // this method is considered as test case
	public void addTestNegative() {
		int res = simpleCalculator.add(10, 5);
		assertNotNull(res);
		assertNotEquals(10, res); // it checks whether expected is not matching with actual or not
	}
	
	@Test // this method is considered as test case
	public void subtractTestPositive() {
		int res = simpleCalculator.subtract(10, 5);
		assertNotNull(res);
		assertEquals(5, res); // it checks whether expected is matching with actual or not
	}
	
	@Test // this method is considered as test case
	public void subtractTestNegative() {
		int res = simpleCalculator.subtract(10, 5);
		assertNotNull(res);
		assertNotEquals(10, res); // it checks whether expected is not matching with actual or not
	}
	
	@Test // this method is considered as test case
	public void multiplyTestPositive() {
		int res = simpleCalculator.multiply(10, 5);
		assertNotNull(res);
		assertEquals(50, res); // it checks whether expected is matching with actual or not
	}
	
	@Test // this method is considered as test case
	public void multiplyTestNegative() {
		int res = simpleCalculator.multiply(10, 5);
		assertNotNull(res);
		assertNotEquals(10, res); // it checks whether expected is not matching with actual or not
	}
	
	@Test // this method is considered as test case
	public void divideTestPositive() {
		int res = simpleCalculator.divide(10, 5);
		assertNotNull(res);
		assertEquals(2, res); // it checks whether expected is matching with actual or not
	}
	
	@Test // this method is considered as test case
	public void divideTestNegative() {
		int res = simpleCalculator.divide(10, 5);
		assertNotNull(res);
		assertNotEquals(10, res); // it checks whether expected is not matching with actual or not
	}

}

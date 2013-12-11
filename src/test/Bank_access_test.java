package test;

import static org.junit.Assert.fail;
import junit.framework.Assert;

import org.junit.Test;

import bank_access.Account;
import bank_access.OverdraftException;

public class Bank_access_test {

	@Test
	public void account() {
		Account a = new Account();
		Assert.assertEquals(0.0, a.getBalance());
		try {
			a.transfer(123.0);
		} catch (OverdraftException e) {
		}
		Assert.assertEquals(123.0, a.getBalance());
	}

	@Test
	public void account_fail() {
		Account a = new Account();
		Assert.assertEquals(0.0, a.getBalance());
		try {
			a.transfer(-123.0);
			fail("OverdraftException");
		} catch (OverdraftException e) {
		}
	}
}

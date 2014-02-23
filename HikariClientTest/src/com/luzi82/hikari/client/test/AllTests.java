package com.luzi82.hikari.client.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AdminTest.class, CacheTest.class, ClientTest.class,
		QuestTest.class, SystemTest.class, UserTest.class })
public class AllTests {

}

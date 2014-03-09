package com.luzi82.hikari.client.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AdminTest.class, CacheTest.class, CardTest.class,
		ClientTest.class, GachaTest.class, L10nTest.class, MailBoxTest.class,
		QuestTest.class, ResourceTest.class, SystemTest.class, UserTest.class })
public class AllTests {

}

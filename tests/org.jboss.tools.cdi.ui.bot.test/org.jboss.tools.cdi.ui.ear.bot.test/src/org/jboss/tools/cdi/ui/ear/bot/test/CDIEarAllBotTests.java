package org.jboss.tools.cdi.ui.ear.bot.test;


import org.jboss.tools.cdi.ui.ear.bot.test.create.CreateBeansTest;
import org.jboss.tools.cdi.ui.ear.bot.test.create.CreateProjectsTest;
import org.jboss.tools.cdi.ui.ear.bot.test.validate.BeansDeploymentTest;
import org.jboss.tools.cdi.ui.ear.bot.test.validate.ELAutoCompletionTest;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;



@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
	CreateProjectsTest.class,
	CreateBeansTest.class,
	ELAutoCompletionTest.class,
	BeansDeploymentTest.class
})
@Server
public class CDIEarAllBotTests {
	
}

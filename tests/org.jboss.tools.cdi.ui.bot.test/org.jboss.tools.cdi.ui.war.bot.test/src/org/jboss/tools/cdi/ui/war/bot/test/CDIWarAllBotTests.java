package org.jboss.tools.cdi.ui.war.bot.test;


import org.jboss.tools.cdi.ui.war.bot.test.create.CreateAndVerifyDecorator;
import org.jboss.tools.cdi.ui.war.bot.test.create.CreateAndVerifyInterceptor;
import org.jboss.tools.cdi.ui.war.bot.test.create.CreateAndVerifyQualifier;
import org.jboss.tools.cdi.ui.war.bot.test.create.CreateAndVerifySimpleBean;
import org.jboss.tools.cdi.ui.war.bot.test.create.CreateProjectTest;
import org.jboss.tools.cdi.ui.war.bot.test.validate.BeansValidator;
import org.jboss.tools.cdi.ui.war.bot.test.validate.ELAutoCompletionTest;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;



@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
	CreateProjectTest.class,
	CreateAndVerifySimpleBean.class,
	CreateAndVerifyInterceptor.class,
	CreateAndVerifyDecorator.class,
	CreateAndVerifyQualifier.class,
	ELAutoCompletionTest.class
	BeansValidator.class
})
@Server
public class CDIWarAllBotTests {
	
}

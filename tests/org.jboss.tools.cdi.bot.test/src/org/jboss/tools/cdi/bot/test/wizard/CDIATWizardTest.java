/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test.wizard;

import java.util.logging.Logger;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.jboss.tools.cdi.bot.test.CDIAllBotTests;
import org.jboss.tools.cdi.bot.test.CDISmokeBotTests;
import org.jboss.tools.cdi.bot.test.uiutils.actions.CDIBase;
import org.jboss.tools.cdi.bot.test.uiutils.actions.CDIUtil;
import org.jboss.tools.cdi.bot.test.uiutils.actions.NewCDIFileWizard;
import org.jboss.tools.cdi.bot.test.uiutils.wizards.CDIWizard;
import org.jboss.tools.cdi.bot.test.uiutils.wizards.CDIWizardType;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Lukas Jungmann
 * @author jjankovi
 */
@Require(clearProjects = false, perspective = "Java EE", server = @Server(state = ServerState.NotRunning, version = "6.0", operator = ">="))
@RunWith(RequirementAwareSuite.class)
@SuiteClasses({ CDIAllBotTests.class, CDISmokeBotTests.class })
public class CDIATWizardTest extends CDIBase {

	private static final String PROJECT_NAME = "CDIProject";
	private static final String PACKAGE_NAME = "cdi";
	private static final Logger L = Logger.getLogger(CDIATWizardTest.class.getName());

	@After
	public void waitForJobs() {
		util.waitForNonIgnoredJobs();
	}
	
	@BeforeClass
	public static void checkAndCreateProject() {
		if (!projectExists(PROJECT_NAME)) {
			createAndCheckCDIProject(bot, util, projectExplorer,PROJECT_NAME);
		}
	}
		
	@Test
	public void testQualifier() {
		CDIUtil.qualifier(PACKAGE_NAME, "Q1", false, false).finish();
		util.waitForNonIgnoredJobs();
		SWTBotEditor ed = new SWTWorkbenchBot().activeEditor();
		assertTrue(("Q1.java").equals(ed.getTitle()));
		String code = ed.toTextEditor().getText();
		L.fine(code);
		assertTrue(code.contains("@Qualifier"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ TYPE, METHOD, PARAMETER, FIELD })"));
		assertFalse(code.contains("@Inherited"));
		assertFalse(code.startsWith("/**"));

		CDIUtil.qualifier(PACKAGE_NAME, "Q2", true, true).finish();
		util.waitForNonIgnoredJobs();
		ed = new SWTWorkbenchBot().activeEditor();
		assertTrue(("Q2.java").equals(ed.getTitle()));
		code = ed.toTextEditor().getText();
		L.fine(code);
		assertTrue(code.contains("@Qualifier"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ TYPE, METHOD, PARAMETER, FIELD })"));
		assertTrue(code.contains("@Inherited"));
		assertTrue(code.startsWith("/**"));
	}
	
	@Test
	public void testScope() {
		CDIUtil.scope(PACKAGE_NAME, "Scope1", true, false, true, false).finish();
		util.waitForNonIgnoredJobs();
		SWTBotEditor ed = new SWTWorkbenchBot().activeEditor();
		assertTrue(("Scope1.java").equals(ed.getTitle()));
		String code = ed.toTextEditor().getText();
		L.fine(code);
		assertTrue(code.contains("@NormalScope"));
		assertFalse(code.contains("@Scope"));
		assertFalse(code.contains("passivating"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ TYPE, METHOD, FIELD })"));
		assertTrue(code.contains("@Inherited"));
		assertFalse(code.startsWith("/**"));

		CDIUtil.scope(PACKAGE_NAME, "Scope2", false, true, true, true).finish();
		util.waitForNonIgnoredJobs();
		ed = new SWTWorkbenchBot().activeEditor();
		assertTrue(("Scope2.java").equals(ed.getTitle()));
		code = ed.toTextEditor().getText();
		L.fine(code);
		assertTrue(code.contains("@NormalScope(passivating = true)"));
		assertFalse(code.contains("@Scope"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ TYPE, METHOD, FIELD })"));
		assertFalse(code.contains("@Inherited"));
		assertTrue(code.startsWith("/**"));

		CDIUtil.scope(PACKAGE_NAME, "Scope3", false, true, false, false).finish();
		util.waitForNonIgnoredJobs();
		ed = new SWTWorkbenchBot().activeEditor();
		assertTrue(("Scope3.java").equals(ed.getTitle()));
		code = ed.toTextEditor().getText();
		L.fine(code);
		assertTrue(code.contains("@Scope"));
		assertFalse(code.contains("@NormalScope"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ TYPE, METHOD, FIELD })"));
		assertFalse(code.contains("@Inherited"));
		assertTrue(code.startsWith("/**"));
	}
	
	@Test
	public void testIBinding() {
		CDIWizard w = CDIUtil.binding(PACKAGE_NAME, "B1", null, true, false);
		assertEquals(2, w.getTargets().size());
		w.finish();
		util.waitForNonIgnoredJobs();
		SWTBotEditor ed = new SWTWorkbenchBot().activeEditor();
		assertTrue(("B1.java").equals(ed.getTitle()));
		String code = ed.toTextEditor().getText();
		L.fine(code);
		assertTrue(code.contains("@InterceptorBinding"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ TYPE, METHOD })"));
		assertTrue(code.contains("@Inherited"));
		assertFalse(code.startsWith("/**"));

		CDIUtil.binding(PACKAGE_NAME, "B2", "TYPE", false, true).finish();
		util.waitForNonIgnoredJobs();
		ed = new SWTWorkbenchBot().activeEditor();
		assertTrue(("B2.java").equals(ed.getTitle()));
		code = ed.toTextEditor().getText();
		L.fine(code);
		assertTrue(code.contains("@InterceptorBinding"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ TYPE })"));
		assertFalse(code.contains("@Inherited"));
		assertTrue(code.startsWith("/**"));

		CDIUtil.binding(PACKAGE_NAME, "B3", "TYPE", false, true).finish();
		util.waitForNonIgnoredJobs();
		ed = new SWTWorkbenchBot().activeEditor();
		assertTrue(("B3.java").equals(ed.getTitle()));
		code = ed.toTextEditor().getText();
		L.fine(code);
		assertTrue(code.contains("@InterceptorBinding"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ TYPE })"));
		assertFalse(code.contains("@Inherited"));
		assertTrue(code.startsWith("/**"));

		w = CDIUtil.binding(PACKAGE_NAME, "B4", "TYPE", true, false);
		w.addIBinding(PACKAGE_NAME + ".B2");
		w.finish();
		util.waitForNonIgnoredJobs();
		ed = new SWTWorkbenchBot().activeEditor();
		assertTrue(("B4.java").equals(ed.getTitle()));
		code = ed.toTextEditor().getText();
		L.fine(code);
		assertTrue(code.contains("@InterceptorBinding"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ TYPE })"));
		assertTrue(code.contains("@Inherited"));
		assertFalse(code.startsWith("/**"));
		assertTrue(code.contains("@B2"));
	}
	
	@Test
	public void testStereotype() {
		CDIWizard w = CDIUtil.stereotype(PACKAGE_NAME, "S1", null, null, false, false, false, false,
				false);
		assertEquals(9, w.getScopes().size());
		assertEquals(5, w.getTargets().size());
		w.finish();
		util.waitForNonIgnoredJobs();
		SWTBotEditor ed = new SWTWorkbenchBot().activeEditor();
		assertTrue(("S1.java").equals(ed.getTitle()));
		String code = ed.toTextEditor().getText();
		L.fine(code);
		assertTrue(code.contains("@Stereotype"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ TYPE, METHOD, FIELD })"));
		assertFalse(code.contains("@Named"));
		assertFalse(code.contains("@Alternative"));
		assertFalse(code.contains("@Inherited"));
		assertFalse(code.startsWith("/**"));

		CDIUtil.stereotype(PACKAGE_NAME, "S2", "@Scope3", "FIELD", true, true, true, false, true)
				.finish();
		util.waitForNonIgnoredJobs();
		ed = new SWTWorkbenchBot().activeEditor();
		assertTrue(("S2.java").equals(ed.getTitle()));
		code = ed.toTextEditor().getText();
		L.fine(code);
		assertTrue(code.contains("@Stereotype"));
		assertTrue(code.contains("@Scope3"));
		assertTrue(code.contains("@Named"));
		assertTrue(code.contains("@Alternative"));
		assertTrue(code.contains("@Inherited"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ FIELD })"));
		assertTrue(code.startsWith("/**"));

		w = CDIUtil.stereotype(PACKAGE_NAME, "S3", null, null, false, false, true, false, false);
		w.addIBinding(PACKAGE_NAME + ".B1");
		w.addStereotype(PACKAGE_NAME + ".S1");
		w.finish();
		util.waitForNonIgnoredJobs();
		ed = new SWTWorkbenchBot().activeEditor();
		assertTrue(("S3.java").equals(ed.getTitle()));
		code = ed.toTextEditor().getText();
		L.fine(code);
		assertTrue(code.contains("@Stereotype"));
		assertFalse(code.contains("@Scope3"));
		assertFalse(code.contains("@Named"));
		assertTrue(code.contains("@Alternative"));
		assertTrue(code.contains("@B1"));
		assertTrue(code.contains("@S1"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ TYPE })"));
		assertFalse(code.contains("@Inherited"));
		assertFalse(code.startsWith("/**"));
	}
	
	@Test
	public void testDecorator() {
		CDIWizard w = CDIUtil.decorator(PACKAGE_NAME, "", "java.lang.Comparable", null, true, true, false, false);
		w.finish();
		util.waitForNonIgnoredJobs();
		SWTBotEditor ed = new SWTWorkbenchBot().editorByTitle("ComparableDecorator.java");
		assertTrue(("ComparableDecorator.java").equals(ed.getTitle()));
		String code = ed.toTextEditor().getText();
		L.fine(code);
		assertTrue(code.contains("@Decorator"));
		assertTrue(code.contains("abstract class"));
		assertTrue(code.contains("@Delegate"));
		assertTrue(code.contains("@Inject"));
		assertTrue(code.contains("@Any"));
		assertTrue(code.contains("private Comparable<T> comparable;"));
		assertFalse(code.contains("final"));
		assertFalse(code.startsWith("/**"));

		w = CDIUtil.decorator(PACKAGE_NAME, "", "java.util.Map", "field", false, false, true, true);
		w.finish();
		util.waitForNonIgnoredJobs();
		ed = new SWTWorkbenchBot().editorByTitle("MapDecorator.java");
		assertTrue(("MapDecorator.java").equals(ed.getTitle()));
		code = ed.toTextEditor().getText();
		L.fine(code);
		assertTrue(code.contains("@Decorator"));
		assertFalse(code.contains("abstract"));
		assertTrue(code.contains("@Delegate"));
		assertTrue(code.contains("@Inject"));
		assertTrue(code.contains("@Any"));
		assertTrue(code.contains("private Map<K, V> field;"));
		assertTrue(code.contains("final class"));
		assertTrue(code.startsWith("/**"));
	}
	
	@Test
	public void testInterceptor() {
		CDIWizard w = CDIUtil.interceptor(PACKAGE_NAME, "I1", "B2", null, null, false);
		w.finish();
		util.waitForNonIgnoredJobs();
		SWTBotEditor ed = new SWTWorkbenchBot().editorByTitle("I1.java");
		assertTrue(("I1.java").equals(ed.getTitle()));
		String code = ed.toTextEditor().getText();
		L.fine(code);
		assertTrue(code.contains("@B2"));
		assertTrue(code.contains("@Interceptor"));
		assertTrue(code.contains("@AroundInvoke"));
		assertTrue(code.contains("public Object manage(InvocationContext ic) throws Exception {"));
		assertFalse(code.contains("final"));
		assertFalse(code.startsWith("/**"));
		
		w = CDIUtil.interceptor(PACKAGE_NAME, "I2", "B4", "java.util.Date", "sample", true);
		w.finish();
		util.waitForNonIgnoredJobs();
		ed = new SWTWorkbenchBot().editorByTitle("I2.java");
		assertTrue(("I2.java").equals(ed.getTitle()));
		code = ed.toTextEditor().getText();
		L.fine(code);
		assertTrue(code.contains("@B4"));
		assertTrue(code.contains("@Interceptor"));
		assertTrue(code.contains("@AroundInvoke"));
		assertTrue(code.contains("public Object sample(InvocationContext ic) throws Exception {"));
		assertFalse(code.contains("final"));
		assertTrue(code.startsWith("/**"));
		assertTrue(code.contains("extends Date"));
	}
	
	@Test
	public void testBeansXml() {
		CDIWizard w = new NewCDIFileWizard(CDIWizardType.BEANS_XML).run();
		w.setSourceFolder(PROJECT_NAME + "/WebContent/WEB-INF");
		assertFalse(w.canFinish());		
		w.setSourceFolder(PROJECT_NAME + "/src/" + PACKAGE_NAME.replaceAll(".", "/"));
		assertTrue(w.canFinish());
		w.cancel();
		w = new NewCDIFileWizard(CDIWizardType.BEANS_XML).run();
		assertFalse(w.canFinish());		
		w.cancel();
	}
	
	@Test
	public void testBean() {
		CDIWizard w = CDIUtil.bean(PACKAGE_NAME, "Bean1", true, true, false, false, false, false, null, null, null, null);
		w.finish();
		util.waitForNonIgnoredJobs();
		SWTBotEditor ed = new SWTWorkbenchBot().activeEditor();
		assertTrue(("Bean1.java").equals(ed.getTitle()));
		String code = ed.toTextEditor().getText();
		L.fine(code);
		assertTrue(code.contains("package cdi;"));
		assertTrue(code.contains("public abstract class Bean1 {"));
		assertFalse(code.contains("@Named"));
		assertFalse(code.contains("final"));
		assertFalse(code.startsWith("/**"));
		
		w = CDIUtil.bean(PACKAGE_NAME, "Bean2", false, false, true, true, false, false, "", null, "@Dependent", null);
		w.finish();
		util.waitForNonIgnoredJobs();
		ed = new SWTWorkbenchBot().activeEditor();
		assertTrue(("Bean2.java").equals(ed.getTitle()));
		code = ed.toTextEditor().getText();
		L.fine(code);
		assertTrue(code.contains("package cdi;"));
		assertTrue(code.contains("@Named"));
		assertFalse(code.contains("@Named("));
		assertTrue(code.contains("@Dependent"));
		assertTrue(code.contains("final class Bean2 {"));
		assertTrue(code.startsWith("/**"));

		w = CDIUtil.bean(PACKAGE_NAME, "Bean3", true, false, false, true, false, false, "TestedBean", null, "@Scope2", "Q1");
		w.finish();
		util.waitForNonIgnoredJobs();
		ed = new SWTWorkbenchBot().activeEditor();
		assertTrue(("Bean3.java").equals(ed.getTitle()));
		code = ed.toTextEditor().getText();
		L.fine(code);
		assertTrue(code.contains("package cdi;"));
		assertTrue(code.contains("@Named(\"TestedBean\")"));
		assertTrue(code.contains("@Scope2"));
		assertTrue(code.contains("@Q1"));
		assertTrue(code.contains("public class Bean3 {"));
		assertFalse(code.contains("final"));
		assertTrue(code.startsWith("/**"));
	}
	
	@Test
	public void testAnnLiteral() {
		CDIWizard w = CDIUtil.annLiteral(PACKAGE_NAME, "AnnL1", true, false, true, false, PACKAGE_NAME + ".Q1");
		w.finish();
		util.waitForNonIgnoredJobs();
		SWTBotEditor ed = new SWTWorkbenchBot().activeEditor();
		assertTrue(("AnnL1.java").equals(ed.getTitle()));
		String code = ed.toTextEditor().getText();
		L.info(code);
		assertTrue(code.contains("package cdi;"));
		assertTrue(code.contains("public final class AnnL1 extends AnnotationLiteral<Q1> implements Q1"));
		assertTrue(code.contains("public static final Q1 INSTANCE = new AnnL1();"));
		assertFalse(code.contains("abstract"));
		assertFalse(code.startsWith("/**"));
		
		w = CDIUtil.annLiteral(PACKAGE_NAME, "AnnL2", false, true, false, true, "Q2");
		w.finish();
		util.waitForNonIgnoredJobs();
		ed = new SWTWorkbenchBot().activeEditor();
		assertTrue(("AnnL2.java").equals(ed.getTitle()));
		code = ed.toTextEditor().getText();
		L.info(code);
		assertTrue(code.contains("package cdi;"));
		assertTrue(code.contains("abstract class AnnL2 extends AnnotationLiteral<Q2> implements Q2 {"));
		assertTrue(code.contains("public static final Q2 INSTANCE = new AnnL2();"));
		assertFalse(code.substring(code.indexOf("final") + 5).contains("final"));
		assertTrue(code.contains("abstract"));
		assertTrue(code.startsWith("/**"));
	}
}
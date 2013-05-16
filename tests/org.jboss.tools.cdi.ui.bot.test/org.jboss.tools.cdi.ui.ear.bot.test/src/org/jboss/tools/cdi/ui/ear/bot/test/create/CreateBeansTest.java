package org.jboss.tools.cdi.ui.ear.bot.test.create;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.cdi.ui.ear.bot.test.AbstractCDITest;
import org.jboss.tools.ui.bot.ext.types.IDELabel.Button;
import org.jboss.tools.ui.bot.ext.view.ProblemsView;
import org.junit.Test;

/**
 * 
 * @author mlabuda@redhat.com
 * @version 1.1
 */
public class CreateBeansTest extends AbstractCDITest {

	@Test
	public void testCreateBeans() {
		createStatefulBeanTest();
		addAttributeAndMethodsToStatefulBean();
		
		createStatelessBeanTest();
		addAttributeAndMethodsToStatelessBean();
		
		createNamedBeanTest();
		addAttributeAndMethodsToNamedBean();
		
		checkProjects();
	}

	private void createStatefulBeanTest() {
		SWTBot dialog = createNewFile(PROJECT_EJB_NAME,	EJB_ITEM, "Session Bean (EJB 3.x)");
		dialog.textWithLabel("Java package:").setText(BEANS_PACKAGE);
		dialog.textWithLabel("Class name:").setText(STATEFUL_BEAN_NAME);
		dialog.comboBoxWithLabel("State type:").setSelection("Stateful");
		
		dialog.button(Button.FINISH).click();
		util.waitForAll();
		
		assertTrue("Stateful Bean was not created!" , packageExplorer.isFilePresent(PROJECT_EJB_NAME, "ejbModule", 
				BEANS_PACKAGE, STATEFUL_BEAN_NAME + ".java"));
	}
	
	private void addAttributeAndMethodsToStatefulBean() {
		SWTBotEclipseEditor editor = bot.activeEditor().toTextEditor();
		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
		bot.sleep(3000);
		
		// @Named
		editor.selectLine(3);
		editor.insertText("import javax.inject.Named;");
		editor.selectLine(6);
		editor.insertText("*/ @Named");
		
		// Attribute
		editor.selectLine(10);
		editor.insertText("\nprivate String someText = \"testStatefulString\";\n\n");
		
		// Getter method 
		editor.selectLine(20);
		editor.insertText("public String getSomeText() { \n" +
				  "return someText; \n" + 
				  "} \n");
		
		// Setter method		 
		editor.selectLine(24);
		editor.insertText("public void setSomeText(String someText) { \n" + 
						  "this.someText = someText;\n" +
						  "}\n" +
						  "} \n");
		
		int k = 0;
		for (String line: editor.getLines()) {
			log.info(k + ": " + line);
			k++;
		}
		
		bot.sleep(3000);
		log.info("StatefulBean - source code modified");
		editor.saveAndClose();
	}
	
	private void createStatelessBeanTest() {
		SWTBot dialog = createNewFile(PROJECT_EJB_NAME,	EJB_ITEM, "Session Bean (EJB 3.x)");
		dialog.textWithLabel("Java package:").setText(BEANS_PACKAGE);
		dialog.textWithLabel("Class name:").setText(STATELESS_BEAN_NAME);
		dialog.comboBoxWithLabel("State type:").setSelection("Stateless");
		dialog.button(Button.FINISH).click();
		util.waitForAll();
		
		assertTrue("Stateless Bean was not created!" ,packageExplorer.isFilePresent(PROJECT_EJB_NAME, "ejbModule", 
				BEANS_PACKAGE, STATELESS_BEAN_NAME + ".java"));
	}
	
	private void addAttributeAndMethodsToStatelessBean() {
		SWTBotEclipseEditor editor = bot.activeEditor().toTextEditor();
		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
		bot.sleep(3000);
		
		// @Named
		editor.selectLine(3);
		editor.insertText("import javax.inject.Named;");
		editor.selectLine(6);
		editor.insertText("*/ @Named");
		
		// Attribute
		editor.selectLine(10);
		editor.insertText("\nprivate String someText = \"testStatelessString\";\n\n");
		
		// Getter method  	
		editor.selectLine(20);
		editor.insertText("public String getSomeText() { \n" +
				  "return someText; \n" + 
				  "} \n");
		
		// Setter method
		editor.selectLine(24);
		editor.insertText("public void setSomeText(String someText) { \n" + 
						  "this.someText = someText;\n" +
						  "}\n"+
						  "} \n");
		
		int k = 0;
		for (String line: editor.getLines()) {
			log.info(k + ": " + line);
			k++;
		}
		
		bot.sleep(3000);
		log.info("StatelessBean - source code modified");
		editor.saveAndClose();
	}
	
	private void createNamedBeanTest() {
		SWTBot dialog = createNewFile(PROJECT_WAR_NAME,	CDI_ITEM, "Bean");
		dialog.textWithLabel("Package:").setText(BEANS_PACKAGE);
		dialog.textWithLabel("Name:").setText(NAMED_BEAN);
		dialog.checkBox("Add @Named").click();
		bot.sleep(1000);
		dialog.textWithLabel("Bean Name:").setText("namedBean");
		dialog.sleep(3000);
		dialog.button(Button.FINISH).click();
		util.waitForAll();
		
		assertTrue("Named Bean was not created!" , packageExplorer.isFilePresent(PROJECT_WAR_NAME, "src", 
				BEANS_PACKAGE, NAMED_BEAN + ".java"));
	}
	
	private void addAttributeAndMethodsToNamedBean() {
		SWTBotEclipseEditor editor = bot.activeEditor().toTextEditor();
		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
		bot.sleep(3000);
		
		// @EJB
		editor.selectLine(3);
		editor.insertText("import javax.ejb.EJB;\nimport " + BEANS_PACKAGE + ".StatefulBean;\n");
		
		
		// Attribute
		editor.selectLine(8);
		editor.insertText("private String someText = \"testNamedString\";\n" +
						  "@EJB private StatefulBean statefulBean;\n");
		
		// Getter method  
		editor.selectLine(14);
		editor.insertText("public String getSomeText() { \n" +
						  "return someText; \n" + 
						  "} \n");
		
		// Setter method and stateful bean text getter method
		editor.selectLine(18);
		editor.insertText("public void setSomeText(String someText) { \n" + 
						  "this.someText = someText;\n" +
						  "}\n" +
						  "public String getManagedBeanText() { \n" +
						  "return statefulBean.getSomeText() + \"Managed\"; \n" +
						  "} \n" +
				  		  "} \n");
		
		int k = 0;
		for (String line: editor.getLines()) {
			log.info(k + ": " + line);
			k++;
		}
		
		bot.sleep(3000);
		log.info("CDI Bean - source code modified");
		editor.saveAndClose();
	}
	
	private void checkProjects() {	
		problems.show();
		SWTBotTreeItem[] errors = ProblemsView.getFilteredErrorsTreeItems(bot, null, null, null, null);
		assertTrue("Errors in problem view.", errors == null || errors.length == 0);
	}
}

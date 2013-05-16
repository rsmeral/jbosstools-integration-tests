package org.jboss.tools.cdi.ui.war.bot.test.create;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.cdi.ui.war.bot.test.AbstractCDITest;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.parts.SWTBotBrowserExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.IDELabel.Button;
import org.jboss.tools.ui.bot.ext.types.IDELabel.Menu;
import org.jboss.tools.ui.bot.ext.types.IDELabel.NewHTMLWizard;
import org.jboss.tools.ui.bot.ext.types.IDELabel.ViewGroup;
import org.jboss.tools.ui.bot.ext.view.ProblemsView;
import org.junit.Test;


public class CreateAndVerifySimpleBean extends AbstractCDITest {
	
	
	@Test
	public void createAndVerifySimpleBean(){
		createNamedBean();
		addSimpleMethod();
		createViewForBean();
		checkCDIProject();
		deployProject();
	}


	private void createNamedBean() {
		SWTBot dialog = createNewFile(PROJECT_NAME,
				CDI_ITEM, "Bean");
		dialog.textWithLabel(IDELabel.NewClassCreationWizard.CLASS_NAME).setText(BEAN_NAME);
		dialog.textWithLabel(IDELabel.NewClassCreationWizard.PACKAGE_NAME).setText(PROJECT_PACKAGE);
		dialog.checkBox("Add @Named").select();
		dialog.button(Button.FINISH).click();
		util.waitForAll();
		assertTrue("Simple Named Bean was not created!!" ,packageExplorer.isFilePresent(PROJECT_NAME, "src",PROJECT_PACKAGE, BEAN_NAME+".java"));
	}
	private void addSimpleMethod() {
		SWTBotEclipseEditor editor = bot.activeEditor().toTextEditor();
		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
		editor.selectLine(10);
		editor.insertText("private String salutation = \"Welcome to CDI!\";\n\n");
		editor.selectLine(12);
		editor.insertText("private String message = \"EL language testing\";\n\n");
		editor.selectLine(14);
		editor.insertText("public String getMessage(){ return message;}\n\n\n");
		editor.selectLine(16);
		editor.insertText("public String sayHello(){ return salutation;}\n\n\n");
		editor.saveAndClose();
	}
	
	private void createViewForBean() {

		SWTBot dialog = createNewFile(PROJECT_NAME,ViewGroup.GENERAL,Menu.FILE);
		dialog.tree().expandNode(PROJECT_NAME).select("WebContent");
		dialog.textWithLabel("File name:").setText("home.xhtml");
		dialog.button(Button.FINISH).click();
		util.waitForAll();

		SWTBotEclipseEditor editor = packageExplorer.openFile(PROJECT_NAME,
				"WebContent", "home.xhtml").toTextEditor();

		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
		editor.setText("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"\nxmlns:ui=\"http://java.sun.com/jsf/facelets\"\nxmlns:h=\"http://java.sun.com/jsf/html\"\nxmlns:f=\"http://java.sun.com/jsf/core\">\n<br/>\n #{namedBean.sayHello()}\n</html>");
		editor.saveAndClose();

		// creating web.xml
		dialog = createNewFile(PROJECT_NAME, ViewGroup.GENERAL,Menu.FILE);
		dialog.tree().expandNode(PROJECT_NAME).expandNode("WebContent").select("WEB-INF");
		dialog.textWithLabel(NewHTMLWizard.FILE_NAME).setText("web.xml");
		dialog.button(Button.FINISH).click();
		util.waitForAll();
		
		editor = packageExplorer.openFile(PROJECT_NAME,
				"WebContent","WEB-INF", "web.xml").toTextEditor();

		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
		editor.setText("<?xml version=\"1.0\" encoding=\"UTF-8\"?><web-app xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd\" version=\"3.0\"><display-name>CDIProject</display-name><servlet><servlet-name>Faces Servlet</servlet-name><servlet-class>javax.faces.webapp.FacesServlet</servlet-class><load-on-startup>1</load-on-startup></servlet><servlet-mapping><servlet-name>Faces Servlet</servlet-name><url-pattern>*.jsf</url-pattern></servlet-mapping></web-app>");
		editor.saveAndClose();
	}

	private void checkCDIProject() {
		
		problems.show();
		SWTBotTreeItem[] errors = ProblemsView.getFilteredErrorsTreeItems(bot, null, null, null, null);
		assertTrue("Errors in problem view.", errors == null || errors.length == 0);
		
	}
	private void deployProject() {
		SWTBotView serverView = open.viewOpen(ActionItem.View.ServerServers.LABEL);
		serverView.bot().tree().getAllItems()[0].select();
		ContextMenuHelper.clickContextMenu(bot.activeView().bot().tree(), Menu.ADD_AND_REMOVE);
		bot.activeShell().bot().treeWithLabel("Available:").select(PROJECT_NAME);
		bot.activeShell().bot().button(Button.ADD_WITH_ARROW).click();
		bot.activeShell().bot().button(Button.FINISH).click();
		util.waitForAll();
		
		open.viewOpen(ActionItem.View.GeneralInternalWebBrowser.LABEL);
		
		bot.sleep(20000);
		SWTBotBrowserExt bBrowser = SWTBotFactory.getBot().browserExt();
		bBrowser.goURL("http://localhost:8080/" + PROJECT_NAME);
		util.waitForBrowserLoadsPage(bBrowser);
		assertContains("Welcome to CDI!", bBrowser.getText());
		
	}

}

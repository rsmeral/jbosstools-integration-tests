package org.jboss.tools.cdi.ui.ear.bot.test.create;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.jboss.tools.cdi.ui.ear.bot.test.AbstractCDITest;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.parts.SWTBotBrowserExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel.Button;
import org.jboss.tools.ui.bot.ext.types.IDELabel.JavaProjectWizard;
import org.jboss.tools.ui.bot.ext.types.IDELabel.Menu;
import org.jboss.tools.ui.bot.ext.types.IDELabel.NewHTMLWizard;
import org.jboss.tools.ui.bot.ext.types.IDELabel.ViewGroup;
import org.junit.Test;

/**
 * 
 * @author mlabuda@redhat.com
 * @version 1.1
 *
 */
public class CreateProjectsTest extends AbstractCDITest {

	private String homePageText = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
			  "  <html xmlns=\"http://www.w3.org/1999/xhtml\"" +"\n" + 
			  "		   xmlns:h=\"http://java.sun.com/jsf/html\"" + "\n" +
			  "        xmlns:f=\"http://java.sun.com/jsf/core\"" + "\n" +
		      "        xmlns:ui=\"http://java.sun.com/jsf/facelets\">" + "\n" + "\n" +
			  "  <h:head>" + "\n" +
		      "    <title>Test</title>" + "\n" +
			  "  </h:head>" + "\n" + "\n" +
			  "  <h:body>" + "\n" +
			  "     <pre>" + "\n" +
			  "StatelessBean" + "\n" +
			  "StatefulBean" + "\n" +
			  "CDIBean" + "\n" +
			  "ManagedBean" + "\n" +
			  "</pre>" + "\n" +
			  "  </h:body>" + "\n" + 
			  "</html>";

	private String webXmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n" +
								"<web-app xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
								"xmlns=\"http://java.sun.com/xml/ns/javaee\" xmlns:web=\"http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd\" " +
								"xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd\" version=\"3.0\">" + "\n" +
								"<display-name>CDIEAR</display-name>" + "\n" +
								"<welcome-file-list>" + "\n" +
								"<welcome-file>home.xhtml</welcome-file>" + "\n" +
								"</welcome-file-list>" + "\n" +
								"<servlet>" + "\n" +
							    "<servlet-name>Faces Servlet</servlet-name>" + "\n" +
							    "<servlet-class>javax.faces.webapp.FacesServlet</servlet-class>" + "\n" +
							    "<load-on-startup>1</load-on-startup>" + "\n" +
							    "</servlet>" + "\n" +
							    "<servlet-mapping>" + "\n" +
							    "<servlet-name>Faces Servlet</servlet-name>" + "\n" +
							    "<url-pattern>/faces/*</url-pattern>" + "\n" +
							    "</servlet-mapping>" + "\n" +
							    "<servlet-mapping>" + "\n" +
							    "<servlet-name>Faces Servlet</servlet-name>" + "\n" +
							    "<url-pattern>*.xhtml</url-pattern>" + "\n" +
							    "</servlet-mapping>" + "\n" +
							    "</web-app>";
	
	@Test
	public void createWARProject() {
		createCDIProject();
		
		addEJBToClassPath();
		addCDISupportToEJBModule();
		
		createAndTestView();
		createHomePage();
		
		deployAndCheckHomePage();
	}

	private void createCDIProject() {
		// Create new WAR project module and EAR project
		util.waitForAll();
		bot.menu(Menu.FILE).menu(Menu.NEW).menu(Menu.OTHER).click();
		bot.activeShell().bot().tree().expandNode("CDI (Context and Dependency Injection)").select("CDI Web Project");
		bot.sleep(3000);
		bot.activeShell().bot().button(Button.NEXT).click();
		bot.sleep(3000);
		bot.activeShell().bot().textWithLabel(JavaProjectWizard.PROJECT_NAME).setText(PROJECT_WAR_NAME);
		bot.sleep(3000);
		bot.activeShell().bot().checkBoxInGroup("Add project to an EAR", "EAR membership").click();
		bot.sleep(3000);
		bot.activeShell().bot().comboBoxWithLabelInGroup("EAR project name:", "EAR membership").setText(PROJECT_EAR_NAME);
		bot.sleep(3000);
		bot.activeShell().bot().button(Button.FINISH).click();
		bot.sleep(3000);
		util.waitForAll();
		bot.activeShell().bot().button(Button.YES).click();
		util.waitForAll();
		
		// Create EJB project and add it into parent EAR project
		bot.menu(Menu.FILE).menu(Menu.NEW).menu(Menu.OTHER).click();
		bot.sleep(3000);
		bot.activeShell().bot().tree().expandNode("EJB").select("EJB Project");
		bot.sleep(3000);
		bot.activeShell().bot().button(Button.NEXT).click();
		bot.sleep(3000);
		bot.activeShell().bot().textWithLabel(JavaProjectWizard.PROJECT_NAME).setText(PROJECT_EJB_NAME);
		bot.sleep(3000);
		bot.activeShell().bot().checkBoxInGroup("Add project to an EAR", "EAR membership").click();
		bot.sleep(3000);
		bot.activeShell().bot().comboBoxWithLabelInGroup("EAR project name:", "EAR membership").setText(PROJECT_EAR_NAME);
		bot.sleep(3000);		
		bot.activeShell().bot().button(Button.NEXT).click();
		bot.sleep(3000);
		bot.activeShell().bot().button(Button.NEXT).click();
		bot.sleep(3000);
		bot.activeShell().bot().checkBox("Create an EJB Client JAR module to hold the client interfaces and classes").click();
		bot.sleep(3000);
		bot.activeShell().bot().button(Button.FINISH).click();
		bot.sleep(3000);
		util.waitForAll();
		bot.activeShell().bot().button(Button.YES).click();
		util.waitForAll();
		
		// Test, whether project WAR module and EAR project do exist
		assertTrue("WAR project module was not created!", packageExplorer.existsResource(PROJECT_WAR_NAME));
		assertTrue("EJB project module was not created!", packageExplorer.existsResource(PROJECT_EJB_NAME));
		assertTrue("EAR project was not created!", packageExplorer.existsResource(PROJECT_EAR_NAME));
	}

	private void addEJBToClassPath() {
		SWTBotView explorerView = open.viewOpen(ActionItem.View.GeneralProjectExplorer.LABEL);
		bot.sleep(3000);
		explorerView.bot().tree().getAllItems()[2].select();
		bot.sleep(3000);
		ContextMenuHelper.clickContextMenu(bot.activeView().bot().tree(), Menu.PROPERTIES);
		bot.sleep(3000);
		
		bot.tree().expandNode("Java Build Path").select();
		bot.sleep(3000);
		
		bot.activeShell().bot().tabItem("Projects").activate();
		bot.sleep(3000);
		
		bot.activeShell().bot().button(Button.ADD).click();
		bot.sleep(3000);
		
		bot.activeShell().bot().button("Select All").click();
		bot.activeShell().bot().sleep(3000);
		
		bot.activeShell().bot().button(Button.OK).click();
		bot.activeShell().bot().sleep(3000);
		
		bot.activeShell().bot().button(Button.OK).click();
		bot.activeShell().bot().sleep(3000);
	}
	
	private void addCDISupportToEJBModule() {
		SWTBotView explorerView = open.viewOpen(ActionItem.View.GeneralProjectExplorer.LABEL);
		bot.sleep(3000);
		
		explorerView.bot().tree().getAllItems()[1].select();
		bot.sleep(3000);
		
		ContextMenuHelper.clickContextMenu(bot.activeView().bot().tree(), "Configure", "Add CDI (Context and Dependency Injection) support...");
		bot.sleep(3000);
		
		bot.activeShell().bot().button(Button.OK).click();
		bot.sleep(3000);
	}
	
	private void createAndTestView() {

		SWTBot dialog = createNewFile(PROJECT_WAR_NAME, ViewGroup.GENERAL,
				Menu.FILE);
		bot.sleep(3000);
		dialog.tree().expandNode(PROJECT_WAR_NAME).select("WebContent");
		bot.sleep(3000);
		dialog.textWithLabel(NewHTMLWizard.FILE_NAME).setText("index.html");
		bot.sleep(3000);
		dialog.button(Button.FINISH).click();
		util.waitForAll();

		SWTBotEclipseEditor editor = packageExplorer.openFile(PROJECT_WAR_NAME,
				"WebContent", "index.html").toTextEditor();
		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
		bot.sleep(3000);
		editor.setText("<html> <head> <meta http-equiv=\"Refresh\" content=\"0; URL=home.jsf\"></head> <body>Welcome to CDI EAR</body></html>");
		editor.bot().sleep(3000);
		editor.saveAndClose();
		assertTrue("WAR project module view was not created!",
				packageExplorer.isFilePresent(PROJECT_WAR_NAME, "WebContent"));
	}
	
	private void createHomePage() {
		// Create home.xhtml 
		SWTBot dialog = createNewFile(PROJECT_WAR_NAME, ViewGroup.GENERAL,
				Menu.FILE);
		bot.sleep(3000);
		dialog.tree().expandNode(PROJECT_WAR_NAME).select("WebContent");
		bot.sleep(3000);
		dialog.textWithLabel(NewHTMLWizard.FILE_NAME).setText("home.xhtml");
		bot.sleep(3000);
		dialog.button(Button.FINISH).click();
		util.waitForAll();

		SWTBotEclipseEditor editor = packageExplorer.openFile(PROJECT_WAR_NAME, "WebContent", "home.xhtml").toTextEditor();
		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
		bot.sleep(3000);
		editor.setText(homePageText);
		editor.bot().sleep(3000);
		editor.saveAndClose();
		
		eclipse.getBot().sleep(3000);
		util.waitForAll();
		
		editor = packageExplorer.openFile(PROJECT_WAR_NAME,
				"WebContent","WEB-INF", "web.xml").toTextEditor();
		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
		bot.sleep(3000);
		editor.setText(webXmlText); 
		editor.bot().sleep(3000);
		editor.saveAndClose();
	}
	
	private void deployAndCheckHomePage() {
		SWTBotView serverView = open.viewOpen(ActionItem.View.ServerServers.LABEL);
		bot.sleep(3000);
		serverView.bot().tree().getAllItems()[0].select();
		bot.sleep(3000);
		
		ContextMenuHelper.clickContextMenu(bot.activeView().bot().tree(), Menu.ADD_AND_REMOVE);
		bot.sleep(3000);
		bot.activeShell().bot().treeWithLabel("Available:").select(PROJECT_EAR_NAME);
		bot.sleep(3000);
		bot.activeShell().bot().button(Button.ADD_WITH_ARROW).click();
		bot.sleep(3000);
		bot.activeShell().bot().button(Button.FINISH).click();
		util.waitForAll();
		
		open.viewOpen(ActionItem.View.GeneralInternalWebBrowser.LABEL);
		bot.sleep(3000);
		SWTBotBrowserExt bBrowser = SWTBotFactory.getBot().browserExt();
		bot.sleep(3000);
		bBrowser.goURL("http://localhost:8080/" + PROJECT_WAR_NAME);
		util.waitForBrowserLoadsPage(bBrowser);
		assertContains("StatelessBean" + "\n" + "StatefulBean" + "\n" + "CDIBean" + "\n" + "ManagedBean", bBrowser.getText());
	}
}

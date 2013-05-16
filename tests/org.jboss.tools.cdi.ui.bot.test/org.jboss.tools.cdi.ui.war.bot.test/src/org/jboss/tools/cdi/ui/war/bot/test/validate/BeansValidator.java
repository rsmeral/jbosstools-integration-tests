package org.jboss.tools.cdi.ui.war.bot.test.validate;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.cdi.ui.war.bot.test.AbstractCDITest;
import org.jboss.tools.ui.bot.ext.types.IDELabel.Button;
import org.jboss.tools.ui.bot.ext.view.ProblemsView;
import org.junit.Test;


public class BeansValidator extends AbstractCDITest {

	@Test
	public void testBeansXmlValidation(){
		SWTBotEclipseEditor editor = packageExplorer.openFile(PROJECT_NAME, "WebContent","WEB-INF","beans.xml").toTextEditor();
		int idx = 0;
		for (String line : editor.getLines()) {
			if (line.contains(PROJECT_PACKAGE+"."+DECORATOR_NAME)) {

				editor.selectLine(idx);
				editor.insertText("<class>"+PROJECT_PACKAGE+"."+"NonExistingClass"+"</class>");
				break;
			}
			idx++;
		}
		editor.saveAndClose();
		util.waitForAll();
		
		problems.show();
		bot.activeView().bot().tree();
		SWTBotTreeItem[] sProblems = ProblemsView.getFilteredErrorsTreeItems(bot, "There is no class \""+PROJECT_PACKAGE+".NonExistingClass", "/" + PROJECT_NAME, "beans.xml", "CDI Problem");
		assertTrue("No CDI beans.xml problem found.", sProblems != null && sProblems.length==1); 
		
	}
	
	@Test
	public void testDeleteBeansXmlAndCreateNew(){
		eclipse.deleteFile(PROJECT_NAME, "WebContent","WEB-INF","beans.xml");
		SWTBot dialog =  createNewFile(PROJECT_NAME, CDI_ITEM, "File beans.xml");
		dialog.button(Button.FINISH).click();
		util.waitForAll();
		assertTrue("beans.xml file was not created!!", packageExplorer.isFilePresent(PROJECT_NAME, "WebContent","WEB-INF","beans.xml"));
		eclipse.closeAllEditors();
		
		SWTBotEclipseEditor editor =  packageExplorer.openFile(PROJECT_NAME, "WebContent","WEB-INF","beans.xml").toTextEditor();
		editor.bot().cTabItem("Source").activate();
		
		//need to have beans tag opened
		editor.selectLine(2);
		editor.insertText("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://jboss.org/schema/cdi/beans_1_0.xsd\">");

		//insert original content
		editor.selectLine(3);
		editor.insertText("<interceptors>\n<class>"+PROJECT_PACKAGE+"."+INTERCEPTOR_NAME+"</class>\n</interceptors>\n</beans>");
		editor.insertText("<decorators>\n<class>"+PROJECT_PACKAGE+"."+DECORATOR_NAME+"</class>\n</decorators>\n");
		editor.saveAndClose();
		
		util.waitForAll();
		redeployAndVerifyInBrowser("Qualified Hello! decorating", PROJECT_NAME);	
	}
}

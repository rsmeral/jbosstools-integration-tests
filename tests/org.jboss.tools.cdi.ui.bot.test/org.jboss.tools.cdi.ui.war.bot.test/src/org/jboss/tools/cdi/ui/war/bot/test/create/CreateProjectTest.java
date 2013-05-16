package org.jboss.tools.cdi.ui.war.bot.test.create;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.jboss.tools.cdi.ui.war.bot.test.AbstractCDITest;
import org.jboss.tools.ui.bot.ext.types.IDELabel.Button;
import org.jboss.tools.ui.bot.ext.types.IDELabel.JavaProjectWizard;
import org.jboss.tools.ui.bot.ext.types.IDELabel.Menu;
import org.jboss.tools.ui.bot.ext.types.IDELabel.NewHTMLWizard;
import org.jboss.tools.ui.bot.ext.types.IDELabel.ViewGroup;
import org.junit.Test;


public class CreateProjectTest extends AbstractCDITest{
	
	@Test
	public void createAndVerifyNewProject(){
		createCDIProject();
		createView();
	}

	
	private void createCDIProject(){
		util.waitForAll();
		bot.menu(Menu.FILE).menu(Menu.NEW).menu(Menu.OTHER).click();
		bot.activeShell().bot().tree().expandNode("CDI (Context and Dependency Injection)").select("CDI Web Project");
		bot.sleep(3000);
		bot.activeShell().bot().button(Button.NEXT).click();
		bot.sleep(3000);
		bot.activeShell().bot().textWithLabel(JavaProjectWizard.PROJECT_NAME).setText(PROJECT_NAME);
		bot.activeShell().bot().button(Button.FINISH).click();
		util.waitForAll();
		bot.activeShell().bot().button(Button.YES).click();
		util.waitForAll();
		assertTrue("Project was not created!!!!", packageExplorer.existsResource(PROJECT_NAME));
	}

	private void createView(){
		
		SWTBot dialog = createNewFile(PROJECT_NAME, ViewGroup.GENERAL,Menu.FILE);
		dialog.tree().expandNode(PROJECT_NAME).select("WebContent");
		dialog.textWithLabel(NewHTMLWizard.FILE_NAME).setText("index.html");
		dialog.button(Button.FINISH).click();
		util.waitForAll();
		
		SWTBotEclipseEditor editor = packageExplorer.openFile(PROJECT_NAME,
				"WebContent", "index.html").toTextEditor();
		
		SWTBotPreferences.KEYBOARD_LAYOUT="EN_US";
		editor.setText("<html><head> <meta http-equiv=\"Refresh\" content=\"0; URL=home.jsf\"></head></html>");
		editor.saveAndClose();
		assertTrue("Project was not created!!!!", packageExplorer.isFilePresent(PROJECT_NAME,"WebContent"));
	}

}

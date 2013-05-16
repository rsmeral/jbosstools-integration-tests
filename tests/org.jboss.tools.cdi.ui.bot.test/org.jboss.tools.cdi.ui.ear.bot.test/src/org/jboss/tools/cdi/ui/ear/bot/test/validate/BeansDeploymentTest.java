package org.jboss.tools.cdi.ui.ear.bot.test.validate;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.jboss.tools.cdi.ui.ear.bot.test.AbstractCDITest;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.parts.SWTBotBrowserExt;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.junit.Test;

/**
 * 
 * @author mlabuda@redhat.com
 * @version 1.0
 * 
 */
public class BeansDeploymentTest extends AbstractCDITest {

	@Test
	public void testDeployment() {
		updatePage();
		deployAndCheck();
	}
	
	
	private void updatePage() {
		SWTBotEditorExt editor;
		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
		eclipse.openFile(PROJECT_WAR_NAME, "WebContent", "home.xhtml").toTextEditor();

		eclipse.getBot().sleep(3000);
		util.waitForAll();
		
		editor = SWTTestExt.bot.swtBotEditorExtByTitle("home.xhtml");
		bot.sleep(3000);
		int idx = 0;
		for (String line : editor.getLines()) {
			if (line.contains("CDIBean")) {
				bot.sleep(3000);
				editor.selectLine(idx);
				bot.sleep(3000);
				editor.insertText("#{namedBean.someText}");
			}
			if (line.contains("ManagedBean")) {
				bot.sleep(3000);
				editor.selectLine(idx);
				bot.sleep(3000);
				editor.insertText("#{namedBean.managedBeanText}");
			}
			if (line.contains("StatelessBean")) {
				bot.sleep(3000);
				editor.selectLine(idx);
				bot.sleep(3000);
				editor.insertText("#{statelessBean.someText}");
			}
			if (line.contains("StatefulBean")) {
				bot.sleep(3000);
				editor.selectLine(idx);
				bot.sleep(3000);
				editor.insertText("#{statefulBean.someText}");
			}
			idx++;
		}

		bot.sleep(3000);
		editor.saveAndClose();
		eclipse.closeAllEditors();
	}
	
	private void deployAndCheck() {
		SWTBotView serverView = open.viewOpen(ActionItem.View.ServerServers.LABEL);
		serverView.bot().tree().getAllItems()[0].expand().getItems()[0].select();
		bot.sleep(3000);
		
		ContextMenuHelper.clickContextMenu(bot.activeView().bot().tree(), "Full Publish");
		bot.activeShell().bot().sleep(10000);
		bot.sleep(10000);
		
		open.viewOpen(ActionItem.View.GeneralInternalWebBrowser.LABEL);
		bot.sleep(3000);
		
		SWTBotBrowserExt bBrowser = SWTBotFactory.getBot().browserExt();
		bot.sleep(3000);
		bBrowser.goURL("http://localhost:8080/" + PROJECT_WAR_NAME);
		bot.sleep(3000);
		util.waitForBrowserLoadsPage(bBrowser);
		bot.sleep(3000);
		assertContains("testStatelessString" + "\n" + "testStatefulString" + "\n" + "testNamedString" + "\n" + "testStatefulStringManaged", bBrowser.getText());
	}
	
	
}

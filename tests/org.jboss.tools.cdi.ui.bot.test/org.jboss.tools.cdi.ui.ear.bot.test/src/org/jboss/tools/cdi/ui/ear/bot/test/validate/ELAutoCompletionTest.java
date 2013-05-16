package org.jboss.tools.cdi.ui.ear.bot.test.validate;

import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.jboss.tools.cdi.ui.ear.bot.test.AbstractCDITest;
import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.parts.ContentAssistBot;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.junit.Test;

/**
 * 
 * @author mlabuda@redhat.com
 * @version 1.0
 */
public class ELAutoCompletionTest extends AbstractCDITest {


	private SWTBotEditorExt editor;
	
	@Test
	public void testELAutoCompletion() throws InterruptedException {
		openEditor();
		
		verifyStatelessBeanELAutoCompletion();
		verifyStatefulBeanELAutoCompletion();
		verifyCDIBeanELAutoCompletion();
		verifyCDIManagedBeanELAutoCompletion();
	}
	
	private void openEditor() {
		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
		eclipse.openFile(PROJECT_WAR_NAME, "WebContent", "home.xhtml").toTextEditor();

		eclipse.getBot().sleep(3000);
		util.waitForAll();
	}
	
	private void verifyStatelessBeanELAutoCompletion() {
		 editor = SWTTestExt.bot.swtBotEditorExtByTitle("home.xhtml");
		 editor.bot().sleep(3000);
		 ContentAssistBot contentAssist = editor.contentAssist();
		 editor.bot().sleep(3000);
		 int idx = 0;
		 for (String line : editor.getLines()) {
				if (line.contains("StatelessBean")) {

					// Stateful bean check
					editor.bot().sleep(3000);
					editor.selectLine(idx);
					editor.bot().sleep(3000);
					editor.insertText("#{statelessBean.someText}");
					String textForSelection = "#{statelessBean.someText}";
					editor.bot().sleep(3000);
					
					editor.save();
					bot.sleep(3000);
					
					SWTJBTExt.selectTextInSourcePane(SWTTestExt.bot,"home.xhtml", textForSelection, 2, 13);
					editor.bot().sleep(5000);
					contentAssist.checkContentAssist("statelessBean : StatelessBean", false);
					editor.bot().sleep(3000);
					SWTJBTExt.selectTextInSourcePane(SWTTestExt.bot,"home.xhtml", textForSelection, 16, 8);
					editor.bot().sleep(5000);
					contentAssist.checkContentAssist("someText : String - StatelessBean", false);
					break;
				}
				idx++;
		}
	}

	private void verifyStatefulBeanELAutoCompletion() {
		 editor = SWTTestExt.bot.swtBotEditorExtByTitle("home.xhtml");
		 editor.bot().sleep(3000);
		 ContentAssistBot contentAssist = editor.contentAssist();
		 int idx = 0;
		 for (String line : editor.getLines()) {
				if (line.contains("StatefulBean")) {

					// Stateful bean check
					editor.bot().sleep(3000);
					editor.selectLine(idx);
					editor.bot().sleep(3000);
					editor.insertText("#{statefulBean.someText}");
					editor.bot().sleep(3000);
					String textForSelection = "#{statefulBean.someText}";
					
					editor.save();
					
					SWTJBTExt.selectTextInSourcePane(SWTTestExt.bot,"home.xhtml", textForSelection, 2, 12);
					editor.bot().sleep(5000);
					contentAssist.checkContentAssist("statefulBean : StatefulBean", false);
					SWTJBTExt.selectTextInSourcePane(SWTTestExt.bot,"home.xhtml", textForSelection, 15, 8);
					editor.bot().sleep(5000);
					contentAssist.checkContentAssist("someText : String - StatefulBean", false);
					break;
				}
				idx++;
		}
	}

	
	private void verifyCDIBeanELAutoCompletion() {		
		 ContentAssistBot contentAssist = editor.contentAssist();
		 int idx = 0;
		 for (String line : editor.getLines()) {
				if (line.contains("CDIBean")) {

					// CDI Bean check
					editor.bot().sleep(3000);
					editor.selectLine(idx);
					editor.bot().sleep(3000);
					editor.insertText("#{namedBean.someText}");
					editor.bot().sleep(3000);
					String textForSelection = "#{namedBean.someText}";
					
					editor.save();
					
					SWTJBTExt.selectTextInSourcePane(SWTTestExt.bot,"home.xhtml", textForSelection, 2, 9);
					editor.bot().sleep(5000);
					contentAssist.checkContentAssist("namedBean : NamedBean", false);
					SWTJBTExt.selectTextInSourcePane(SWTTestExt.bot,"home.xhtml", textForSelection, 12, 8);
					editor.bot().sleep(5000);
					contentAssist.checkContentAssist("someText : String - NamedBean", false);
					break;
				}
				idx++;
		}
	}
	
	private void verifyCDIManagedBeanELAutoCompletion() {
		 ContentAssistBot contentAssist = editor.contentAssist();
		 int idx = 0;
		 for (String line : editor.getLines()) {
				if (line.contains("ManagedBean")) {

					// Managed Bean check
					editor.bot().sleep(3000);
					editor.selectLine(idx);
					editor.bot().sleep(3000);
					editor.insertText("#{namedBean.managedBeanText}");
					editor.bot().sleep(3000);
					String textForSelection = "#{namedBean.managedBeanText}";
					
					editor.save();
					
					SWTJBTExt.selectTextInSourcePane(SWTTestExt.bot,"home.xhtml", textForSelection, 2, 9);
					editor.bot().sleep(5000);
					contentAssist.checkContentAssist("namedBean : NamedBean", false);
					SWTJBTExt.selectTextInSourcePane(SWTTestExt.bot,"home.xhtml", textForSelection, 12, 15);
					editor.bot().sleep(5000);
					contentAssist.checkContentAssist("managedBeanText : String - NamedBean", false);
					break;
				}
				idx++;
		}
		 
		editor.close();
		eclipse.closeAllEditors();
	}
		
}
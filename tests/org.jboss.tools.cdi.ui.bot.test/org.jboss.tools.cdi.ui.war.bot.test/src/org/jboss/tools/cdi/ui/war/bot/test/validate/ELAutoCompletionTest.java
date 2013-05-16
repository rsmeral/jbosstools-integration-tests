package org.jboss.tools.cdi.ui.war.bot.test.validate;

import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.jboss.tools.cdi.ui.war.bot.test.AbstractCDITest;
import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.parts.ContentAssistBot;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.junit.Test;

public class ELAutoCompletionTest extends AbstractCDITest {
	
	@Test
	public void testELAutoCompletion() throws InterruptedException {

		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
		eclipse.openFile(PROJECT_NAME, "WebContent","home.xhtml").toTextEditor();

		util.waitForAll();
		SWTBotEditorExt editor = SWTTestExt.bot.swtBotEditorExtByTitle("home.xhtml");

		ContentAssistBot contentAssist = editor.contentAssist();
		int idx = 0;
		for (String line : editor.getLines()) {
			if (line.contains("#{")) {

				editor.selectLine(idx);
				// first write the whole expression
				editor.insertText("#{namedBean.message}");
				// and then check it is really available in content assist
				String textForSelection = "#{namedBean.message}";
				
				SWTJBTExt.selectTextInSourcePane(SWTTestExt.bot,"home.xhtml", textForSelection, 0,11);
				bot.sleep(5000);
				contentAssist.checkContentAssist("namedBean : NamedBean", false);
				SWTJBTExt.selectTextInSourcePane(SWTTestExt.bot,"home.xhtml", textForSelection, 12, 9);
				bot.sleep(5000);
				contentAssist.checkContentAssist("message : String - NamedBean", false);
				break;
			}
			idx++;
		}
		editor.saveAndClose();
	}

}

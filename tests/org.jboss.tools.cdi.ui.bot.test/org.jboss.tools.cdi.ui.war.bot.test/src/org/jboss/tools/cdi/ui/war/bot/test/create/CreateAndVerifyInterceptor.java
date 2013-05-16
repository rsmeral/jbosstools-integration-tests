package org.jboss.tools.cdi.ui.war.bot.test.create;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.cdi.ui.war.bot.test.AbstractCDITest;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.IDELabel.Button;
import org.jboss.tools.ui.bot.ext.view.ProblemsView;
import org.junit.Test;


public class CreateAndVerifyInterceptor extends AbstractCDITest {

	@Test
	public void createAndVerifyInterceptor(){
		createInterceptorBinding();
		createInterceptorBean();
		editInterceptor();
		interceptTheHelloBean();
		redeployAndVerifyInBrowser("Welcome to Interception!", PROJECT_NAME);
	}
	
	public void createInterceptorBinding() {
		SWTBot dialog = createNewFile(PROJECT_NAME,
				CDI_ITEM,
				"Interceptor Binding Annotation");
		dialog.textWithLabel(IDELabel.NewClassCreationWizard.CLASS_NAME).setText(INTERCEPTOR_BIND);
		dialog.textWithLabel(IDELabel.NewClassCreationWizard.PACKAGE_NAME).setText(PROJECT_PACKAGE);
		dialog.button(Button.FINISH).click();
		util.waitForAll();
		assertTrue("Interceptor Binding was not created!!" ,packageExplorer.isFilePresent(PROJECT_NAME, "src",PROJECT_PACKAGE, INTERCEPTOR_BIND+".java"));
	}
	public void createInterceptorBean() {
		SWTBot dialog = createNewFile(PROJECT_NAME,
				CDI_ITEM, "Interceptor");
		dialog.textWithLabel(IDELabel.NewClassCreationWizard.CLASS_NAME).setText(INTERCEPTOR_NAME);
		dialog.textWithLabel(IDELabel.NewClassCreationWizard.PACKAGE_NAME).setText(PROJECT_PACKAGE);
		dialog.button(Button.ADD_WITHOUT_DOTS).click();
		bot.activeShell().bot().text().setText(INTERCEPTOR_BIND);
		bot.sleep(3000);
		bot.activeShell().bot().button(Button.OK).click();
		dialog.button(Button.FINISH).click();
		util.waitForAll();
	}

	public void editInterceptor(){
		// edit interceptor to return some other string
		SWTBotEclipseEditor editor = packageExplorer.openFile(PROJECT_NAME,
				"src", PROJECT_PACKAGE, INTERCEPTOR_NAME+".java").toTextEditor();

		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";

		int idx = 0;
		for (String line : editor.getLines()) {
			if (line.contains("return null;")) {

				editor.selectLine(idx);
				editor.insertText("return \"Welcome to Interception!\";");
				break;
			}
			idx++;
		}
		editor.saveAndClose();

		util.waitForAll();
	}

	public void interceptTheHelloBean() {
		
		SWTBotEclipseEditor editor = packageExplorer.openFile(PROJECT_NAME,
				"src", PROJECT_PACKAGE, BEAN_NAME+".java").toTextEditor();

		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
		editor.selectLine(15);
		editor.insertText("\n @InterceptorBind");
		editor.saveAndClose();
		problems.show();
		SWTBotTreeItem[] errors = ProblemsView.getFilteredErrorsTreeItems(bot, null, null, null, null);
		assertTrue("Errors in problem view.", errors == null || errors.length == 0);
	}


}

package org.jboss.tools.cdi.ui.war.bot.test.create;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.jboss.tools.cdi.ui.war.bot.test.AbstractCDITest;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.IDELabel.Button;
import org.jboss.tools.ui.bot.ext.types.IDELabel.ViewGroup;
import org.junit.Test;


public class CreateAndVerifyDecorator extends AbstractCDITest {
	
	@Test
	public void createAndVerifyDecorator(){
		createSimpleInterfaceForDecoration();
		editInterface();
		createDecoratorBean();
		editDecorator();
		editSimpleBean();
		redeployAndVerifyInBrowser("Welcome to CDI! decorating", PROJECT_NAME);
	}

	public void createSimpleInterfaceForDecoration() {
		SWTBot dialog = createNewFile(PROJECT_NAME, ViewGroup.JAVA, "Interface");
		dialog.textWithLabel(IDELabel.NewClassCreationWizard.CLASS_NAME)
				.setText(INTERFACE_NAME);
		dialog.textWithLabel(IDELabel.NewClassCreationWizard.PACKAGE_NAME)
				.setText(PROJECT_PACKAGE);
		dialog.button(Button.FINISH).click();
		util.waitForAll();
		assertTrue("Interface was not created!!!", packageExplorer.isFilePresent(PROJECT_NAME, "src",PROJECT_PACKAGE,INTERFACE_NAME+".java"));
	}
	
	public void editInterface(){
		
		SWTBotEclipseEditor editor = packageExplorer.openFile(PROJECT_NAME,
				"src", PROJECT_PACKAGE, INTERFACE_NAME+".java").toTextEditor();

		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
		editor.selectLine(3);
		editor.insertText("\n public String sayHello();");
		editor.saveAndClose();
	}

	public void createDecoratorBean() {

		SWTBot dialog = createNewFile(PROJECT_NAME, CDI_ITEM, "Decorator");
		dialog.textWithLabel(IDELabel.NewClassCreationWizard.CLASS_NAME)
				.setText(DECORATOR_NAME);
		dialog.textWithLabel(IDELabel.NewClassCreationWizard.PACKAGE_NAME)
				.setText(PROJECT_PACKAGE);
		dialog.button(Button.ADD).click();
		bot.activeShell().bot().text().setText(INTERFACE_NAME);
		bot.sleep(3000);
		bot.activeShell().bot().button(Button.OK).click();
		dialog.button(Button.FINISH).click();
		util.waitForAll();
		assertTrue("Decorator was not created!!!!", packageExplorer.isFilePresent(PROJECT_NAME, "src",PROJECT_PACKAGE,DECORATOR_NAME+".java"));
	}
	
	public void editDecorator(){
		SWTBotEclipseEditor editor = packageExplorer.openFile(PROJECT_NAME,
				"src", PROJECT_PACKAGE, DECORATOR_NAME+".java").toTextEditor();

		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";

		int idx = 0;
		for (String line : editor.getLines()) {
			if (line.contains("return simpleInterface.sayHello();")) {

				editor.selectLine(idx);
				editor.insertText("return simpleInterface.sayHello()+\" decorating\";");
				break;
			}
			idx++;
		}
		editor.saveAndClose();
	}

	public void editSimpleBean() {
		SWTBotEclipseEditor editor = packageExplorer.openFile(PROJECT_NAME,
				"src", PROJECT_PACKAGE, BEAN_NAME + ".java").toTextEditor();

		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
		int idx = 0;
		for (String line : editor.getLines()) {
			// removing interceptor
			if (line.contains("@InterceptorBind")) {

				editor.selectLine(idx);
				editor.insertText("\n");
			}
			// implementing interface for decoration
			if (line.contains("public class " + BEAN_NAME + " {")) {
				editor.selectLine(idx);
				editor.insertText("public class " + BEAN_NAME
						+ " implements "+INTERFACE_NAME+" {");
			}
			idx++;
		}
		editor.saveAndClose();
	}

}

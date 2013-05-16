package org.jboss.tools.cdi.ui.war.bot.test.create;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.jboss.tools.cdi.ui.war.bot.test.AbstractCDITest;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.IDELabel.Button;
import org.junit.Test;


public class CreateAndVerifyQualifier extends AbstractCDITest {
	
	
	@Test
	public void createAndVerifyQualifier(){
		createQualifierAnnotation();
		createQualifiedImplemetation();
		editSimpleBean();
		editQualifiedImpl();
		redeployAndVerifyInBrowser("Qualified Hello", PROJECT_NAME);
	}
	
	public void createQualifierAnnotation(){
		SWTBot dialog = createNewFile(PROJECT_NAME,
				CDI_ITEM, "Qualifier Annotation");
		dialog.textWithLabel(IDELabel.NewClassCreationWizard.CLASS_NAME).setText(QUALIFIER_NAME);
		dialog.textWithLabel(IDELabel.NewClassCreationWizard.PACKAGE_NAME).setText(PROJECT_PACKAGE);
		dialog.button(Button.FINISH).click();
		util.waitForAll();
		assertTrue("Qualifier Annotation was not created!!", packageExplorer.isFilePresent(PROJECT_NAME, "src",PROJECT_PACKAGE,QUALIFIER_NAME+".java"));
	}
	
	public void createQualifiedImplemetation(){
		SWTBot dialog = createNewFile(PROJECT_NAME,
				CDI_ITEM, "Bean");
		dialog.textWithLabel(IDELabel.NewClassCreationWizard.CLASS_NAME).setText("SecondBean");
		dialog.textWithLabel(IDELabel.NewClassCreationWizard.PACKAGE_NAME).setText(PROJECT_PACKAGE);
		dialog.button(Button.ADD).click();
		bot.activeShell().bot().text().setText(INTERFACE_NAME);
		bot.sleep(TIME_1S);
		bot.activeShell().bot().button(Button.OK).click();
		util.waitForAll();
		bot.activeShell().bot().button(Button.FINISH).click();
		util.waitForAll();
		
		
	}
	public void editSimpleBean(){
		SWTBotEclipseEditor editor = packageExplorer.openFile(PROJECT_NAME,
				"src", PROJECT_PACKAGE, BEAN_NAME+".java").toTextEditor();

		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
		int idx = 0;
		editor.selectLine(16);
		editor.insertText("@Inject @"+QUALIFIER_NAME+" private "+INTERFACE_NAME+" simpleInterface;");
		editor.selectLine(3);
		editor.insertText("import javax.inject.Inject;\n");
		for (String line : editor.getLines()) {
					//implementing interface for decoration
			if (line.contains("public String sayHello(){ return salutation;}")){
				editor.selectLine(idx);
				editor.insertText("public String sayHello(){ return simpleInterface.sayHello();}");
			}
			idx++;
		}
		editor.saveAndClose();
	}
	
	public void editQualifiedImpl(){
		
		SWTBotEclipseEditor editor = packageExplorer.openFile(PROJECT_NAME,
				"src", PROJECT_PACKAGE, "SecondBean.java").toTextEditor();

		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
		editor.selectLine(1);
		editor.insertText("@"+QUALIFIER_NAME);

		int idx = 0;
		for (String line : editor.getLines()) {
			if (line.contains("return null;")) {

				editor.selectLine(idx);
				editor.insertText("return \"Qualified Hello!\";");
				break;
			}
			idx++;
		}
		editor.saveAndClose();
		util.waitForAll();
		
	}
	
}

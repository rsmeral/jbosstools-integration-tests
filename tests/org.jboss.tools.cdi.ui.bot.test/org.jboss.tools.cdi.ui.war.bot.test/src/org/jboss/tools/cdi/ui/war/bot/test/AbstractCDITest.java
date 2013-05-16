package org.jboss.tools.cdi.ui.war.bot.test;

import java.util.List;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.parts.SWTBotBrowserExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel.Button;
import org.jboss.tools.ui.bot.ext.types.IDELabel.Menu;

@Require(
server=@Server(),
clearProjects=false)
public abstract class AbstractCDITest extends SWTTestExt {

	//generated beans/classes names
	public final static String PROJECT_NAME="CDIProject";
	public final static String BEAN_NAME="NamedBean";
	public final static String INTERFACE_NAME="SimpleInterface";
	public final static String DECORATOR_NAME="DecoratorBean";
	public final static String PROJECT_PACKAGE="testPackage";
	public final static String CDI_ITEM="CDI (Context and Dependency Injection)";
	public final static String INTERCEPTOR_BIND="InterceptorBind";
	public final static String INTERCEPTOR_NAME="InterceptorBean";
	public final static String QUALIFIER_NAME="QualifiedHello";
	
	public SWTBot createNewFile(String projectName, String node, String ...path){
		
		packageExplorer.selectProject(projectName);
		ContextMenuHelper.clickContextMenu(bot.activeView().bot().tree(),Menu.NEW, Menu.OTHER);
		
		SWTBot wizard = bot.shell(Menu.NEW).bot();
		wizard.tree().expandNode(node).select(path);
		wizard.button(Button.NEXT).click();
		util.waitForAll();
		return wizard;
	}
	
	/**
	 * Redeploys and checks message(html) of given project on the server.
	 * @param expectedText
	 * @param projectName
	 */
	public  void redeployAndVerifyInBrowser(String expectedText,String projectName) {
		SWTBotView serverView = open.viewOpen(ActionItem.View.ServerServers.LABEL);
		SWTBotTreeItem server = serverView.bot().tree().getAllItems()[0];
		if (server.isExpanded()) {
			server.collapse();
		}

		server.expand();
		List<String> nodes = server.getNodes();
		
		String currentProjectName = "";

		for (String node : nodes) {
			if (node.contains(projectName)) {
				currentProjectName = node;
				break;
			}
		}
		server.select(currentProjectName);
		ContextMenuHelper.clickContextMenu(bot.activeView().bot().tree(),
				"Full Publish");
		util.waitForAll();

		open.viewOpen(ActionItem.View.GeneralInternalWebBrowser.LABEL);
		bot.sleep(20000);
		SWTBotBrowserExt bBrowser = SWTBotFactory.getBot().browserExt();
		bBrowser.goURL("http://localhost:8080/" + projectName);
		util.waitForBrowserLoadsPage(bBrowser);
		assertContains(expectedText, bBrowser.getText());
		bot.activeView().close();
	}
	
}

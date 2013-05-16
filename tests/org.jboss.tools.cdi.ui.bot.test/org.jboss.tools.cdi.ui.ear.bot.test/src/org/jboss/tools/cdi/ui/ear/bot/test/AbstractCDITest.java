package org.jboss.tools.cdi.ui.ear.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel.Button;
import org.jboss.tools.ui.bot.ext.types.IDELabel.Menu;

@Require(server=@Server(),clearProjects=false)
public abstract class AbstractCDITest extends SWTTestExt {

	//generated beans/classes names
	public static final String PROJECT_WAR_NAME="CDIWARProject";
	public static final String PROJECT_EAR_NAME="CDIEARProject";
	public static final String PROJECT_EJB_NAME="CDIEJBProject";
	
	public static final String BEANS_PACKAGE="bean";
	
	public static final String STATEFUL_BEAN_NAME = "StatefulBean";
	public static final String STATELESS_BEAN_NAME = "StatelessBean";
	public static final String NAMED_BEAN = "NamedBean";
	
	public static final String INTERFACE_NAME="SimpleInterface";
	public static final String DECORATOR_NAME="DecoratorBean";
	public static final String PROJECT_PACKAGE="testPackage";
	public static final String CDI_ITEM="CDI (Context and Dependency Injection)";
	public static final String EJB_ITEM="EJB";
	public static final String INTERCEPTOR_BIND="InterceptorBind";
	public static final String INTERCEPTOR_NAME="InterceptorBean";
	public static final String QUALIFIER_NAME="QualifiedHello";
	
	public SWTBot createNewFile(String projectName, String node, String ...path) {
		packageExplorer.selectProject(projectName);
		bot.sleep(3000);
		ContextMenuHelper.clickContextMenu(bot.activeView().bot().tree(),Menu.NEW, Menu.OTHER);
		
		bot.sleep(3000);
		SWTBot wizard = bot.shell(Menu.NEW).bot();
		bot.sleep(3000);
		wizard.tree().expandNode(node).select(path);
		bot.sleep(3000);
		wizard.button(Button.NEXT).click();
		util.waitForAll();
		return wizard;
	} 
	
}

package org.jboss.ide.eclipse.as.ui.bot.test.as42;

import org.jboss.ide.eclipse.as.ui.bot.test.template.DeleteServerTemplate;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

@Require(server=@Server(type=ServerType.JbossAS, version="4.2", state=ServerState.Present))
public class DeleteServerAS42Server extends DeleteServerTemplate {

}

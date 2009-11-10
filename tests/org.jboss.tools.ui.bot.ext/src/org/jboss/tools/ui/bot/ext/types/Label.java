 /*******************************************************************************
  * Copyright (c) 2007-2009 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.ui.bot.ext.types;

/**
 * Base label constants for all widgets. Naming convention is (except buttons
 * and menus) based on Eclipse platform class names of each part (e.g.
 * NewJavaProjectWizardPageOne)
 * 
 * @author jpeterka 
 */
public class Label {
	public class Menu {
		public static final String FILE = "File";
		public static final String NEW = "New";
		public static final String PROJECT = "Project";
		public static final String OTHER = "Other...";
		public static final String WINDOW = "Window";
		public static final String SHOW_VIEW = "Show View";
		public static final String OPEN_PERSPECTIVE = "Open Perspective";
		public static final String OPEN_WITH =  "Open With";
		public static final String TEXT_EDITOR = "Text Editor";
		public static final String EDIT = "Edit";
		public static final String SELECT_ALL = "Select All";
	}

	public class Button {
		public static final String NEXT = "Next >";
		public static final String BACK = "< Back";
		public static final String CANCEL = "Cancel";
		public static final String FINISH = "Finish";
		public static final String OK = "OK";
	}

	public class Shell {
		public static final String NEW_JAVA_PROJECT = "New Java Project";
		public static final String NEW_JAVA_CLASS = "New Java Class";
		public static final String NEW_HIBERNATE_MAPPING_FILE = "New Hibernate XML Mapping file (hbm.xml)";
		public static final String NEW = "New";
	}

	public class EntityGroup {
		public static final String HIBERNATE = "Hibernate";
		public static final String JAVA = "Java";
		public static final String SEAM = "Seam";
	}
	
	public class EntityLabel {
		public static final String HIBERNATE_MAPPING_FILE = "Hibernate XML Mapping file (hbm.xml)";
		public static final String JAVA_CLASS = "Class";
		public static final String JAVA_PROJECT =  "Java Project";
		public static final String SEAM_PROJECT = "Seam Web Project";
	}

	public class JavaProjectWizard {
		public static final String PROJECT_NAME = "Project name:";
	}

	public class NewClassCreationWizard {
		public static final String CLASS_NAME = "Name:";
		public static final String PACKAGE_NAME = "Package:";
	}

	public class ShowViewDialog {
		public static final String JAVA_GROUP = "Java";
		public static final String PROJECT_EXPLORER = "Project Explorer";

	}

	public class View {
		public static final String WELCOME = "Welcome";
		public static final String PROJECT_EXPLORER = "Project Explorer";
		public static final String PACKAGE_EXPLORER = "Package Explorer";
	}
	
	public class ViewGroup {
		public static final String GENERAL = "General";
		public static final String JAVA = "Java";
	}

	public class SelectPerspectiveDialog {
		public static final String JAVA = "Java";
		public static final String HIBERNATE = "Hibernate";

	}
}

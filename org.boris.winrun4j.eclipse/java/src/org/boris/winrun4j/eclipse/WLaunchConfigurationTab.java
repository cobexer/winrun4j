/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Peter Smith
 *******************************************************************************/
package org.boris.winrun4j.eclipse;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class WLaunchConfigurationTab extends AbstractLaunchConfigurationTab implements
        SelectionListener
{
    // Logging UI elements
    private Combo logLevelCombo;
    private Text logFileText;
    private Button logFileOverwriteCheck;

    // Splash UI elements
    private Text splashImageText;
    private Button splashAutoHideCheck;

    // DDE UI elements
    private Button ddeEnabledCheck;
    private Text ddeClassText;
    private Text ddeServerNameText;
    private Text ddeTopicNameText;
    private Text ddeWindowNameText;

    // Misc UI elements
    private Combo singleInstanceCombo;
    private Combo processPriorityCombo;

    public Image getImage() {
        return WActivator.getLauncherImage();
    }

    public void createControl(Composite parent) {
        Composite comp = UIHelper.createComposite(parent, parent.getFont(), 1, 1,
                GridData.FILL_BOTH);
        ((GridLayout) comp.getLayout()).verticalSpacing = 0;
        createLogEditor(comp);
        createSplashEditor(comp);
        createDDEEditor(comp);
        createMiscEditor(comp);
        setControl(comp);
    }

    private void createMiscEditor(Composite parent) {
        Font font = parent.getFont();
        Group group = new Group(parent, SWT.NONE);
        group.setText("Miscellaneous");
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(gd);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        group.setLayout(layout);
        group.setFont(font);

        // Single instance
        Label t = new Label(group, SWT.NULL);
        t.setFont(font);
        t.setText("Single Instance");
        this.singleInstanceCombo = new Combo(group, SWT.DROP_DOWN | SWT.READ_ONLY);
        this.singleInstanceCombo.setItems(new String[] { "", "process", "window", "dde" });
        this.singleInstanceCombo.setFont(font);
        this.singleInstanceCombo.addSelectionListener(this);
        this.singleInstanceCombo.addSelectionListener(this);

        // Process priority
        t = new Label(group, SWT.NULL);
        t.setFont(font);
        t.setText("Process Priority");
        this.processPriorityCombo = new Combo(group, SWT.DROP_DOWN | SWT.READ_ONLY);
        this.processPriorityCombo.setItems(new String[] { "", "idle", "below_normal", "normal",
                "above_normal", "high", "realtime" });
        this.processPriorityCombo.setFont(font);
        this.processPriorityCombo.addSelectionListener(this);
        this.processPriorityCombo.setVisibleItemCount(7);
        this.processPriorityCombo.addSelectionListener(this);
    }

    private void createDDEEditor(Composite parent) {
        Font font = parent.getFont();
        Group group = new Group(parent, SWT.NONE);
        group.setText("Dynamic Data Exchange (DDE)");
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(gd);
        GridLayout layout = new GridLayout();
        layout.numColumns = 7;
        group.setLayout(layout);
        group.setFont(font);

        // DDE enabled
        Label t = new Label(group, SWT.NULL);
        t.setFont(font);
        t.setText("Enabled");
        this.ddeEnabledCheck = new Button(group, SWT.CHECK);
        this.ddeEnabledCheck.addSelectionListener(this);
        gd = new GridData();
        gd.horizontalSpan = 6;
        this.ddeEnabledCheck.setLayoutData(gd);
        this.ddeEnabledCheck.addSelectionListener(this);

        // DDE class
        t = new Label(group, SWT.NULL);
        t.setFont(font);
        t.setText("DDE Class");
        this.ddeClassText = new Text(group, SWT.SEARCH);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 5;
        this.ddeClassText.setLayoutData(gd);
        this.ddeClassText.addSelectionListener(this);
        Button b = new Button(group, SWT.NULL);
        b.setFont(font);
        b.setText("Browse...");

        // DDE server name
        t = new Label(group, SWT.NULL);
        t.setFont(font);
        t.setText("Server Name");
        this.ddeServerNameText = new Text(group, SWT.SEARCH);
        this.ddeServerNameText.addSelectionListener(this);
        gd = new GridData();

        // DDE topic name
        t = new Label(group, SWT.NULL);
        t.setFont(font);
        t.setText("Topic");
        this.ddeTopicNameText = new Text(group, SWT.SEARCH);
        this.ddeTopicNameText.addSelectionListener(this);
        gd = new GridData();

        // DDE window name
        t = new Label(group, SWT.NULL);
        t.setFont(font);
        t.setText("Window Name");
        this.ddeWindowNameText = new Text(group, SWT.SEARCH);
        this.ddeWindowNameText.addSelectionListener(this);
        gd = new GridData();
    }

    private void createSplashEditor(Composite parent) {
        Font font = parent.getFont();
        Group group = new Group(parent, SWT.NONE);
        group.setText("Splash Screen");
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(gd);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        group.setLayout(layout);
        group.setFont(font);

        // Splash File
        Label t = new Label(group, SWT.NULL);
        t.setFont(font);
        t.setText("File");
        this.splashImageText = new Text(group, SWT.SEARCH);
        this.splashImageText.addSelectionListener(this);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        this.splashImageText.setLayoutData(gd);
        Button b = new Button(group, SWT.NULL);
        b.setFont(font);
        b.setText("Browse...");

        // Splash auto-hide
        t = new Label(group, SWT.NULL);
        t.setFont(font);
        t.setText("Auto-Hide");
        this.splashAutoHideCheck = new Button(group, SWT.CHECK);
        this.splashAutoHideCheck.addSelectionListener(this);
        gd = new GridData();
        gd.horizontalSpan = 2;
        this.splashAutoHideCheck.setLayoutData(gd);
    }

    private void createLogEditor(Composite parent) {
        Font font = parent.getFont();
        Group group = new Group(parent, SWT.NONE);
        group.setText("Logging");
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(gd);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        group.setLayout(layout);
        group.setFont(font);

        // Log level
        Label t = new Label(group, SWT.NULL);
        t.setFont(font);
        t.setText("Level");
        this.logLevelCombo = new Combo(group, SWT.DROP_DOWN | SWT.READ_ONLY);
        this.logLevelCombo.setItems(new String[] { "", "info", "warning", "error", "none" });
        this.logLevelCombo.setFont(font);
        this.logLevelCombo.addSelectionListener(this);
        gd = new GridData();
        gd.horizontalSpan = 2;
        this.logLevelCombo.setLayoutData(gd);

        // Log file
        t = new Label(group, SWT.NULL);
        t.setFont(font);
        t.setText("File");
        this.logFileText = new Text(group, SWT.SEARCH);
        this.logFileText.setMessage("No selection logs to console");
        this.logFileText.addSelectionListener(this);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        this.logFileText.setLayoutData(gd);
        Button b = new Button(group, SWT.NULL);
        b.setFont(font);
        b.setText("Browse...");

        // Log file overwrite
        t = new Label(group, SWT.NULL);
        t.setFont(font);
        t.setText("Append");
        this.logFileOverwriteCheck = new Button(group, SWT.CHECK);
        this.logFileOverwriteCheck.addSelectionListener(this);
        gd = new GridData();
        gd.horizontalSpan = 2;
        this.logFileOverwriteCheck.setLayoutData(gd);
        this.logFileOverwriteCheck.addSelectionListener(this);
    }

    public String getName() {
        return "WinRun4J";
    }

    public void initializeFrom(ILaunchConfiguration configuration) {
        try {
            UIHelper.select(logLevelCombo, configuration.getAttribute(
                    IWinRun4JLaunchConfigurationConstants.PROP_LOG_LEVEL, "info"));
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
        updateConfig(configuration, IWinRun4JLaunchConfigurationConstants.PROP_LOG_LEVEL, UIHelper
                .getSelection(logLevelCombo));
        updateConfig(configuration, IWinRun4JLaunchConfigurationConstants.PROP_LOG_FILE,
                logFileText.getText());
        updateConfig(configuration, IWinRun4JLaunchConfigurationConstants.PROP_LOG_OVERWRITE,
                logFileOverwriteCheck.getSelection());
    }

    private void updateConfig(ILaunchConfigurationWorkingCopy configuration, String property,
            boolean value) {
        updateConfig(configuration, property, value ? "true" : "false");
    }

    private void updateConfig(ILaunchConfigurationWorkingCopy configuration, String property,
            String value) {
        if (value != null) {
            configuration.setAttribute(property, value);
        } else {
            configuration.removeAttribute(property);
        }
    }

    public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(IWinRun4JLaunchConfigurationConstants.PROP_LOG_LEVEL, "info");
    }

    public void widgetDefaultSelected(SelectionEvent e) {
        setDirty(true);
        updateLaunchConfigurationDialog();
    }

    public void widgetSelected(SelectionEvent e) {
        setDirty(true);
        updateLaunchConfigurationDialog();
    }
}
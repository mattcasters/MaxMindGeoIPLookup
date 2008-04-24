/*
 * @author Daniel Einspanjer
 * @since  April-16-2008
 */

package plugin.com.maxmind.geoip;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.core.widget.ColumnInfo;
import org.pentaho.di.ui.core.widget.TableView;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class MaxMindGeoIPLookupDialog extends BaseStepDialog implements StepDialogInterface
{
	private Label        wlIpAddressFieldName;
	private Text         wIpAddressFieldName;
	private FormData     fdlIpAddressFieldName, fdIpAddressFieldName;

	private Label        wlDbLocation;
	private Text         wDbLocation;
	private FormData     fdlDbLocation, fdDbLocation;
	
	private Label        wlFields;
	private TableView    wFields;
	private FormData     fdlFields, fdFields;

	private MaxMindGeoIPLookupMeta  input;

	public MaxMindGeoIPLookupDialog(Shell parent, Object in, TransMeta transMeta, String sname)
	{
		super(parent, (BaseStepMeta)in, transMeta, sname);
		input=(MaxMindGeoIPLookupMeta)in;
	}

	public String open()
	{
		Shell parent = getParent();
		Display display = parent.getDisplay();

        shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN);
 		props.setLook(shell);
        setShellImage(shell, input);

		ModifyListener lsMod = new ModifyListener() 
		{
			public void modifyText(ModifyEvent e) 
			{
				input.setChanged();
			}
		};
		changed = input.hasChanged();

		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth  = Const.FORM_MARGIN;
		formLayout.marginHeight = Const.FORM_MARGIN;

		shell.setLayout(formLayout);
		shell.setText(Messages.getString("MaxMindGeoIPLookupDialog.Shell.Title")); //$NON-NLS-1$
		
		int middle = props.getMiddlePct();
		int margin = Const.MARGIN;

		// Stepname line
		wlStepname=new Label(shell, SWT.RIGHT);
		wlStepname.setText(Messages.getString("MaxMindGeoIPLookupDialog.StepName.Label")); //$NON-NLS-1$
 		props.setLook(wlStepname);
		fdlStepname=new FormData();
		fdlStepname.left = new FormAttachment(0, 0);
		fdlStepname.right= new FormAttachment(middle, -margin);
		fdlStepname.top  = new FormAttachment(0, margin);
		wlStepname.setLayoutData(fdlStepname);
		wStepname=new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		wStepname.setText(stepname);
 		props.setLook(wStepname);
		wStepname.addModifyListener(lsMod);
		fdStepname=new FormData();
		fdStepname.left = new FormAttachment(middle, 0);
		fdStepname.top  = new FormAttachment(0, margin);
		fdStepname.right= new FormAttachment(100, 0);
		wStepname.setLayoutData(fdStepname);

		// Typefield line
		wlIpAddressFieldName=new Label(shell, SWT.RIGHT);
		wlIpAddressFieldName.setText(Messages.getString("MaxMindGeoIPLookupDialog.IPAddressFieldName.Label")); //$NON-NLS-1$
 		props.setLook(wlIpAddressFieldName);
		fdlIpAddressFieldName=new FormData();
		fdlIpAddressFieldName.left = new FormAttachment(0, 0);
		fdlIpAddressFieldName.right= new FormAttachment(middle, -margin);
		fdlIpAddressFieldName.top  = new FormAttachment(wStepname, margin);
		wlIpAddressFieldName.setLayoutData(fdlIpAddressFieldName);
		wIpAddressFieldName=new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		wIpAddressFieldName.setText(""); //$NON-NLS-1$
 		props.setLook(wIpAddressFieldName);
		wIpAddressFieldName.addModifyListener(lsMod);
		fdIpAddressFieldName=new FormData();
		fdIpAddressFieldName.left = new FormAttachment(middle, 0);
		fdIpAddressFieldName.top  = new FormAttachment(wStepname, margin);
		fdIpAddressFieldName.right= new FormAttachment(100, 0);
		wIpAddressFieldName.setLayoutData(fdIpAddressFieldName);

		// Typefield line
		wlDbLocation=new Label(shell, SWT.RIGHT);
		wlDbLocation.setText(Messages.getString("MaxMindGeoIPLookupDialog.DBLocation.Label")); //$NON-NLS-1$
		wlDbLocation.setToolTipText(Messages.getString("MaxMindGeoIPLookupDialog.DBLocation.Tooltip"));
 		props.setLook(wlDbLocation);
		fdlDbLocation=new FormData();
		fdlDbLocation.left = new FormAttachment(0, 0);
		fdlDbLocation.right= new FormAttachment(middle, -margin);
		fdlDbLocation.top  = new FormAttachment(wIpAddressFieldName, margin);
		wlDbLocation.setLayoutData(fdlDbLocation);
		wDbLocation=new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		wDbLocation.setText(""); //$NON-NLS-1$
 		props.setLook(wDbLocation);
		wDbLocation.addModifyListener(lsMod);
		fdDbLocation=new FormData();
		fdDbLocation.left = new FormAttachment(middle, 0);
		fdDbLocation.top  = new FormAttachment(wIpAddressFieldName, margin);
		fdDbLocation.right= new FormAttachment(100, 0);
		wDbLocation.setLayoutData(fdDbLocation);

		wlFields=new Label(shell, SWT.RIGHT);
		wlFields.setText(Messages.getString("MaxMindGeoIPLookupDialog.Fields.Label")); //$NON-NLS-1$
 		props.setLook(wlFields);
		fdlFields=new FormData();
		fdlFields.left = new FormAttachment(0, 0);
		fdlFields.top  = new FormAttachment(wDbLocation, margin);
		wlFields.setLayoutData(fdlFields);

		wOK=new Button(shell, SWT.PUSH);
		wOK.setText(Messages.getString("System.Button.OK")); //$NON-NLS-1$
		wCancel=new Button(shell, SWT.PUSH);
		wCancel.setText(Messages.getString("System.Button.Cancel")); //$NON-NLS-1$

		setButtonPositions(new Button[] { wOK, wCancel }, margin, null);

		//TODO: Support list of fields to add
		//TODO: Need an enum of geoip data types
		final int fieldsRows = 0;

        final ColumnInfo[] colinf = new ColumnInfo[] {
                new ColumnInfo(
                        Messages.getString("MaxMindGeoIPLookupDialog.ColumnInfo.NewField"), ColumnInfo.COLUMN_TYPE_TEXT, false), //$NON-NLS-1$
                new ColumnInfo(
                        Messages.getString("MaxMindGeoIPLookupDialog.ColumnInfo.LookupType"), ColumnInfo.COLUMN_TYPE_CCOMBO, new String[] {"TODO"}), //$NON-NLS-1$
                new ColumnInfo(
                        Messages.getString("MaxMindGeoIPLookupDialog.ColumnInfo.IfNull"), ColumnInfo.COLUMN_TYPE_TEXT, false)}; //$NON-NLS-1$
        wFields = new TableView(transMeta, shell, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI, colinf, fieldsRows,
                lsMod, props);

		fdFields=new FormData();
		fdFields.left = new FormAttachment(0, 0);
		fdFields.top  = new FormAttachment(wlFields, margin);
		fdFields.right  = new FormAttachment(100, 0);
		fdFields.bottom = new FormAttachment(wOK, -2*margin);
		wFields.setLayoutData(fdFields);

		// Add listeners
		lsOK       = new Listener() { public void handleEvent(Event e) { ok();     } };
		lsCancel   = new Listener() { public void handleEvent(Event e) { cancel(); } };
		
		wOK.addListener    (SWT.Selection, lsOK    );
		wCancel.addListener(SWT.Selection, lsCancel);
		
		lsDef=new SelectionAdapter() { public void widgetDefaultSelected(SelectionEvent e) { ok(); } };
		
		wStepname.addSelectionListener( lsDef );
				
		// Detect X or ALT-F4 or something that kills this window...
		shell.addShellListener(	new ShellAdapter() { public void shellClosed(ShellEvent e) { cancel(); } } );

		// Set the shell size, based upon previous time...
		setSize();
		
		getData();
		input.setChanged(changed);

	
		shell.open();
		while (!shell.isDisposed())
		{
				if (!display.readAndDispatch()) display.sleep();
		}
		return stepname;
	}
	
	public void getData()
	{	
		int i;
		
		if (input.getIpAddressFieldName()!=null) wIpAddressFieldName.setText(input.getIpAddressFieldName());
		if (input.getDbLocation()!=null) wDbLocation.setText(input.getDbLocation());
		
        for (i = 0; i < input.getFieldName().length; i++)
		{
            final TableItem ti = wFields.table.getItem(i);
            if (input.getFieldName()[i] != null) ti.setText(1, input.getFieldName()[i]);
            ti.setText(2, input.getFieldLookupType()[i]);
            if (input.getFieldIfNull()[i] != null) ti.setText(3, input.getFieldIfNull()[i]);
		}
		wFields.setRowNums();
		wFields.optWidth(true);

		wStepname.selectAll();
	}
	
	private void cancel()
	{
		stepname=null;
		input.setChanged(changed);
		dispose();
	}
	
	private void ok()
	{
		if (Const.isEmpty(wStepname.getText())) return;

		stepname = wStepname.getText(); // return value

		input.setIpAddressFieldName( wIpAddressFieldName.getText() );
		input.setDbLocation( wDbLocation.getText() );
		
		//Table table = wFields.table;
		int nrfields = wFields.nrNonEmpty();
		
		input.allocate(nrfields);
		
        for (int i = 0; i < input.getFieldName().length; i++)
		{
            final TableItem ti = wFields.getNonEmpty(i);
            input.getFieldName()[i] = ti.getText(1);
			input.getFieldLookupType()[i] = ti.getText( 2 );
            input.getFieldIfNull()[i] = ti.getText(3);
		}
		
		dispose();
	}
}

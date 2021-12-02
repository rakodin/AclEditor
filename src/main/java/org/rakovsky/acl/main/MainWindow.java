package org.rakovsky.acl.main;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.rakovsky.acl.data.AppDataSource;
import org.rakovsky.acl.data.ConfigurationBean;
import org.rakovsky.acl.data.ConfigurationRepository;
import org.rakovsky.acl.data.DepTreeBean;
import org.rakovsky.acl.data.EmplBean;

import com.ibm.icu.impl.Pair;

public class MainWindow {

	protected Shell shell;
	private static List<ConfigurationBean> configurations = new ArrayList<>();// = List.of("---", "SPB", "TST", "MSK",
																				// "TMB", "ETC");
	protected Tree depTree;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy");
	private Table empTable;
	private TableColumn empUserIdCol;
	private TableColumn empPersonIdCol;
	private TableColumn empNameCol;
	private TableColumn empLoginCol;
	private Button btnDisableEmp;
	private Button btnDetailsEmp;
	private Button btnAddEmp;
	private TableColumn empStartDtCol;
	private TableColumn empEndDtCol;

	/**
	 * @wbp.nonvisual location=358,797
	 */

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			AppDataSource.configure(args[0]);
			loadConfigurations();
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {

		shell = new Shell();

		shell.setSize(778, 752);
		shell.setText("ACL Editor");

		CLabel lblNewLabel = new CLabel(shell, SWT.NONE);
		lblNewLabel.setBounds(12, 13, 109, 23);

		lblNewLabel.setText("Конфигурация");

		Combo config = new Combo(shell, SWT.READ_ONLY | SWT.DROP_DOWN | SWT.BORDER | SWT.V_SCROLL);

		config.setItems(
				configurations.stream().map(c -> c.getName()).collect(Collectors.toList()).toArray(new String[0]));

		config.select(0);
		config.setBounds(138, 10, 509, 32);

		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(8, 41, 658, 2);

		Group grpDep = new Group(shell, SWT.NONE);
		grpDep.setText("Департаменты");
		grpDep.setBounds(16, 58, 725, 310);

		depTree = new Tree(grpDep, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		depTree.setLocation(13, 6);
		depTree.setSize(697, 224);
		depTree.setLayoutData(new GridData(GridData.FILL_BOTH));

		Button btnInsertDep = new Button(grpDep, SWT.NONE);
		btnInsertDep.setLocation(12, 247);
		btnInsertDep.setSize(173, 32);
		btnInsertDep.setText("Добавить");
		btnInsertDep.setEnabled(false);

		Group grpEmp = new Group(shell, SWT.NONE);
		grpEmp.setText("Сотрудники");
		grpEmp.setBounds(20, 377, 735, 321);
//SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL
		empTable = new Table(grpEmp,
				SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL /* SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL */);
		empTable.setBounds(15, 5, 704, 238);
		empTable.setHeaderVisible(true);
		empTable.setLinesVisible(true);

		empUserIdCol = new TableColumn(empTable, SWT.NONE);
		empUserIdCol.setToolTipText("Идентификатор пользователя");
		empUserIdCol.setWidth(96);
		empUserIdCol.setText("userId");

		empPersonIdCol = new TableColumn(empTable, SWT.NONE);
		empPersonIdCol.setToolTipText("Идентификатор персоны");
		empPersonIdCol.setWidth(96);
		empPersonIdCol.setText("personId");

		empNameCol = new TableColumn(empTable, SWT.NONE);
		empNameCol.setToolTipText("Фамилия Имя Отчество");
		empNameCol.setWidth(230);
		empNameCol.setText("ФИО");

		empLoginCol = new TableColumn(empTable, SWT.NONE);
		empLoginCol.setWidth(100);
		empLoginCol.setToolTipText("Логин в системе");
		empLoginCol.setText("Логин");

		empStartDtCol = new TableColumn(empTable, SWT.NONE);
		empStartDtCol.setWidth(90);
		empStartDtCol.setToolTipText("Дата начала работы сотрудника в департаменте");
		empStartDtCol.setText("Нач. раб.");

		empEndDtCol = new TableColumn(empTable, SWT.NONE);
		empEndDtCol.setWidth(90);
		empEndDtCol.setText("Окон. раб.");
		empEndDtCol.setToolTipText("Дата начала работы сотрудника в департаменте");

		btnDisableEmp = new Button(grpEmp, SWT.NONE);
		btnDisableEmp.setLocation(17, 257);
		btnDisableEmp.setSize(194, 32);
		btnDisableEmp.setText("Включить");

		btnDetailsEmp = new Button(grpEmp, SWT.NONE);
		btnDetailsEmp.setText("Подробнее");
		btnDetailsEmp.setBounds(220, 257, 194, 32);

		btnAddEmp = new Button(grpEmp, SWT.NONE);
		btnAddEmp.setText("Добавить");
		btnAddEmp.setBounds(423, 257, 194, 32);
		
		btnDetailsEmp.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				Shell empDetailDialog = createDetailsDialog(shell, (EmplBean) btnDetailsEmp.getData());
				empDetailDialog.open();
			}
		});
		
		btnDisableEmp.addListener(SWT.Selection, new Listener() {	
			@Override
			public void handleEvent(Event arg0) {
				String option = "Disable";
				if (btnDisableEmp.getText().equals("Включить")) {
					option = "Enable";
				}
				EmplBean selected = (EmplBean) btnDisableEmp.getData();
				System.out.println(option + " employee: " + selected.toString());
			}
		});
		
		btnInsertDep.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				System.out.println("Add department to: config: " + btnInsertDep.getData("config") + " parent: " + (
						btnInsertDep.getData("parent") != null? btnInsertDep.getData("parent") : "null" ));
			}
			
		});
		
		btnAddEmp.addListener(SWT.Selection, new Listener() {			
			@Override
			public void handleEvent(Event arg0) {
				DepTreeBean selected = (DepTreeBean) btnAddEmp.getData(); 
				System.out.println("Add employee to: " + selected.toString());
			}
		});

		btnAddEmp.setEnabled(false);
		btnDetailsEmp.setEnabled(false);
		btnDisableEmp.setEnabled(false);

		config.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				ConfigurationBean conf = getConfiguration(config.getSelectionIndex());
				btnDisableEmp.setEnabled(false);
				btnDetailsEmp.setEnabled(false);
				btnAddEmp.setEnabled(false);
				if (conf.equals(ConfigurationBean.EMPTY)) {
					btnInsertDep.setEnabled(false);
				} else {
					btnInsertDep.setEnabled(true);
					btnInsertDep.setData("config", conf);
					btnInsertDep.setData("parent", null);
				}

				List<DepTreeBean> list = ConfigurationRepository.getDepartments(conf.getId());
				btnAddEmp.redraw();
				btnInsertDep.redraw();
				btnDisableEmp.redraw();
				btnDetailsEmp.redraw();
				depTree.removeAll();
				empTable.removeAll();
				buildTree(list, depTree);
			}
		});

		depTree.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				DepTreeBean selected = (DepTreeBean) arg0.item.getData();
				btnInsertDep.setData("parent", selected);
				String depTreeId = selected.getId();
				List<EmplBean> employee = ConfigurationRepository.getEmployeeList(depTreeId);
				empTable.removeAll();
				btnDetailsEmp.setEnabled(false);
				btnDisableEmp.setEnabled(false);
				btnDisableEmp.redraw();
				btnDetailsEmp.redraw();
				btnInsertDep.redraw();
				
				btnAddEmp.setEnabled(true);				
				btnAddEmp.setData(selected);
				btnAddEmp.redraw();
				
				employee.forEach(em -> {
					TableItem item = new TableItem(empTable, SWT.NULL);

					item.setData(em);
					item.setText(new String[] { em.getSuUserId(), em.getPersonId(), em.getName(), em.getLogin(),
							sdf.format(em.getStartDate()), sdf.format(em.getEndDate()) });
					Date now = new Date(System.currentTimeMillis());
					if (em.getEndDate().before(now) || em.getStartDate().after(now)) {
						item.setBackground(new Color(100, 0, 0));
					}
				});
				empTable.setLinesVisible(true);
			}

		});

		empTable.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg) {
				TableItem selected = empTable.getSelection()[0];
				EmplBean em = (EmplBean) selected.getData();
				Date now = new Date(System.currentTimeMillis());
				if (em.getEndDate().before(now) || em.getStartDate().after(now)) {
					btnDisableEmp.setText("Включить");
				} else {
					btnDisableEmp.setText("Выключить");
				}
				btnDisableEmp.setData(em);
				btnDetailsEmp.setData(em);
				
				btnDetailsEmp.setEnabled(true);
				btnDisableEmp.setEnabled(true);
				
				btnDetailsEmp.redraw();
				btnDisableEmp.redraw();
			}
		});
	}

	private List<TreeItem> getRoots(List<DepTreeBean> items, Tree depTree) {
		final List<TreeItem> ret = new ArrayList<>();
		items.stream().filter(i -> i.getParentId() == null).forEach(i -> {
			TreeItem item = new TreeItem(depTree, SWT.NULL);
			item.setData(i);
			item.setExpanded(true);
			item.setText(i.getName());
			ret.add(item);
		});
		return ret;
	}
	
	private void buildTree(List<DepTreeBean> items, Tree depTree) {
		List<TreeItem> roots = getRoots(items, depTree);
		if (roots.size() > 0) {
			roots.forEach(r -> drawTree(items, r));
		}
	}

	private void drawTree(List<DepTreeBean> buffer, TreeItem current) {
		DepTreeBean cur = (DepTreeBean) current.getData();
		for (DepTreeBean bean : buffer) {
			if (cur.getId().equals(bean.getParentId())) {
				TreeItem item = new TreeItem(current, SWT.NULL);
				item.setData(bean);
				item.setExpanded(true);
				item.setText(bean.getName());
				drawTree(buffer, item);
			}
		}
	}

	private static void loadConfigurations() {
		configurations.add(ConfigurationBean.EMPTY);
		configurations.addAll(ConfigurationRepository.getConfigurations());
	}

	private static ConfigurationBean getConfiguration(int idx) {
		return configurations.get(idx);
	}

	private Shell createDetailsDialog(Shell parent, EmplBean empl) {
		Shell empDetailsDialog = new Shell(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		empDetailsDialog.setText(empl.getName());
		empDetailsDialog.setSize(350, 350);
		int height = 28;
		int h = 20;
		for (Pair<String, String> d : empl.getColumns()) {
			Text l = new Text(empDetailsDialog, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
			l.setBounds(20, h, 300, 26);
			l.setToolTipText(d.first);
			l.setText(d.second);
			h = h + height;
		}
		Button btnClose = new Button(empDetailsDialog, SWT.NONE);
		btnClose.setLocation(20, h + height);
		btnClose.setSize(110, height);
		btnClose.setText("Закрыть");
		btnClose.setFocus();
		btnClose.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				empDetailsDialog.close();
			}
		});
		return empDetailsDialog;
	}
}

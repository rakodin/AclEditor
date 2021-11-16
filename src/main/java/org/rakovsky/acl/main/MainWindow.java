package org.rakovsky.acl.main;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.rakovsky.acl.data.AppDataSource;
import org.rakovsky.acl.data.ConfigurationBean;
import org.rakovsky.acl.data.ConfigurationRepository;
import org.rakovsky.acl.data.DepTreeBean;
import org.rakovsky.acl.data.EmplBean;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Group;

public class MainWindow {

	protected Shell shell;
	private static List<ConfigurationBean> configurations = new ArrayList<>();// = List.of("---", "SPB", "TST", "MSK",
																				// "TMB", "ETC");
	protected Tree depTree;
	private Table empTable;
	private TableColumn empNameCol;
	private TableColumn empLoginCol;

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
		shell.setSize(760, 641);
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
		grpDep.setBounds(16, 58, 725, 259);

		depTree = new Tree(grpDep, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		depTree.setLocation(13, 6);
		depTree.setSize(697, 224);
		depTree.setLayoutData(new GridData(GridData.FILL_BOTH));

		Group grpEmp = new Group(shell, SWT.NONE);
		grpEmp.setText("Сотрудники");
		grpEmp.setBounds(16, 328, 725, 275);

		empTable = new Table(grpEmp, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		empTable.setBounds(15, 5, 694, 238);
		empTable.setHeaderVisible(true);
		empTable.setLinesVisible(true);

		empNameCol = new TableColumn(empTable, SWT.NONE);
		empNameCol.setToolTipText("Фамилия Имя Отчество");
		empNameCol.setWidth(404);
		empNameCol.setText("ФИО");

		empLoginCol = new TableColumn(empTable, SWT.NONE);
		empLoginCol.setWidth(228);
		empLoginCol.setToolTipText("Логин в системе");
		empLoginCol.setText("Логин");
		config.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				List<DepTreeBean> list = ConfigurationRepository
						.getDepartments(getConfiguration(config.getSelectionIndex()));

				depTree.removeAll();

				list.forEach(b -> {
					//TODO: implement true tree traversal
					TreeItem item = null;
					if (b.getParentId() == null) {
						item = new TreeItem(depTree, SWT.NULL);
					} else {
						for (TreeItem i : depTree.getItems()) {
							DepTreeBean db = (DepTreeBean) i.getData();
							if (db.getId().equals(b.getParentId())) {
								item = new TreeItem(i, SWT.NULL);
							}
						}
					}
					if (item != null) {
						item.setData(b);
						item.setExpanded(true);
						item.setText(b.getName());
					}
				});
				// depTree.getItems() -- return array of added items
			}
		});

		depTree.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				DepTreeBean selected = (DepTreeBean) arg0.item.getData();
				String depTreeId = selected.getDepId();
				List<EmplBean> employee = ConfigurationRepository.getEmployeeList(depTreeId);
				empTable.removeAll();
				employee.forEach(em -> {
					TableItem item = new TableItem(empTable, SWT.NULL);
					item.setData(em);
					item.setText(new String[] {em.getName(), em.getLogin()});
				});
				empTable.setLinesVisible(true);
			}

		});
	}

	private static void loadConfigurations() {
		configurations.add(new ConfigurationBean(" ", "---"));
		configurations.addAll(ConfigurationRepository.getConfigurations());
	}

	private static String getConfiguration(int idx) {
		return configurations.get(idx).getId();
	}
}

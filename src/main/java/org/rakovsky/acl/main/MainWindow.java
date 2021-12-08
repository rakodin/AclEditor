package org.rakovsky.acl.main;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.rakovsky.acl.data.AppDataSource;
import org.rakovsky.acl.data.ConfigurationBean;
import org.rakovsky.acl.data.ConfigurationRepository;
import org.rakovsky.acl.data.DepTreeBean;
import org.rakovsky.acl.data.DepTreeField;
import org.rakovsky.acl.data.EmplBean;
import org.rakovsky.acl.data.EmplField;

import com.ibm.icu.impl.Pair;

public class MainWindow {

	protected Shell shell;
	private static List<ConfigurationBean> configurations = new ArrayList<>();// = List.of("---", "SPB", "TST", "MSK",
																				// "TMB", "ETC");
	private Combo config;
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
	private Button btnInsertDep;
	private TableColumn empStartDtCol;
	private TableColumn empEndDtCol;
	private Button btnEditDep;
	private Button btnDeleteDep;

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

		config = new Combo(shell, SWT.READ_ONLY | SWT.DROP_DOWN | SWT.BORDER | SWT.V_SCROLL);

		config.setItems(
				configurations.stream().map(c -> c.getName()).collect(Collectors.toList()).toArray(new String[0]));

		config.select(0);
		config.setBounds(138, 10, 509, 32);

		Group grpDep = new Group(shell, SWT.NONE);
		grpDep.setText("Департаменты");
		grpDep.setBounds(16, 58, 725, 310);

		depTree = new Tree(grpDep, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		depTree.setLocation(13, 6);
		depTree.setSize(697, 224);
		depTree.setLayoutData(new GridData(GridData.FILL_BOTH));

		btnInsertDep = new Button(grpDep, SWT.NONE);
		btnInsertDep.setLocation(12, 247);
		btnInsertDep.setSize(173, 32);
		btnInsertDep.setText("Добавить");
		btnInsertDep.setEnabled(false);

		btnEditDep = new Button(grpDep, SWT.NONE);
		btnEditDep.setText("Редактировать");
		btnEditDep.setEnabled(false);
		btnEditDep.setBounds(192, 247, 173, 32);

		btnDeleteDep = new Button(grpDep, SWT.NONE);
		btnDeleteDep.setText("Удалить");
		btnDeleteDep.setEnabled(false);
		btnDeleteDep.setBounds(372, 247, 173, 32);

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
				createDepartmentDialogWithDisposeListener(null).open();
			}
		});

		btnEditDep.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				createDepartmentDialogWithDisposeListener((DepTreeBean) btnEditDep.getData()).open();
			}
		});

		btnDeleteDep.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				int res = createYesNoDialog(shell, ((DepTreeBean) btnDeleteDep.getData()).getName()).open();
				System.out.println(res);
				if (res == SWT.YES) {
					DepTreeBean removed = (DepTreeBean) btnDeleteDep.getData();
					String parentDep = removed.getParentId();
					System.out.println("TODO: remove " + removed.getId());

					TreeItem root = findTreeItemByTreeId(depTree, removed.getId());
					if (root != null) {
						List<String> removedDeps = new ArrayList<>();
						findChildsByTreeItems(root, removedDeps);
						System.out.println("TODO: All removed deps: " + removedDeps);
						// remove deps
						ConfigurationRepository.removeDepartmentWithChilds(removedDeps);
						// reload UI
						int current = config.getSelectionIndex();
						config.deselectAll();
						config.select(current);
						onSelectCombo(config.getSelectionIndex());

						// refill tree
						depTree.deselectAll();

						DepTreeBean selected = depTree.getItemCount() > 0 ? (DepTreeBean) depTree.getItem(0).getData()
								: null; // default
						if (parentDep != null) {
							root = findTreeItemByTreeId(depTree, parentDep);
							if (root != null) {
								selected = (DepTreeBean) root.getData();
							}
						}
						if (selected != null) {
							onSelectDepartment(selected);
							// find treeitem in new tree
							TreeItem selectedItem = findTreeItemByTreeId(depTree, selected.getId());
							if (selectedItem != null) {
								depTree.setSelection(selectedItem);
								depTree.showSelection();
							}
						}
						System.out.println("TODO: select parent dep: " + selected);
					}
				}
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

		config.addSelectionListener(new SelectionListener() {

			private void handleEvent(SelectionEvent arg0) {
				onSelectCombo(config.getSelectionIndex());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				handleEvent(arg0);

			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				handleEvent(arg0);
			}
		});

		depTree.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				onSelectDepartment((DepTreeBean) ((TreeItem) arg0.item).getData());
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
		Shell dialog = new Shell(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		dialog.setText(empl.getName());
		dialog.setSize(550, 350);
		int height = 28;
		int h = 20;
		for (Pair<EmplField, String> d : EmplBean.getColumns(empl)) {
			Label l = new Label(dialog, SWT.NONE);
			l.setBounds(20, h, 200, 26);
			l.setText(d.first.getDescr());
			Text t = new Text(dialog, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
			t.setBounds(205, h, 300, 26);
			t.setToolTipText(d.first.name());
			t.setText(d.second);
			t.setData(d);
			h = h + height;
		}
		Button btnClose = new Button(dialog, SWT.NONE);
		btnClose.setLocation(20, h + height);
		btnClose.setSize(110, height);
		btnClose.setText("Закрыть");
		btnClose.setFocus();
		btnClose.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				dialog.close();
			}
		});

		return dialog;
	}

	private Shell createDepartmentOperationDialog(Shell parentShell, ConfigurationBean conf, DepTreeBean parentDep,
			DepTreeBean edited) {
		Shell dialog = new Shell(parentShell, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		final boolean forEdit;
		if (edited == null) {
			dialog.setText("Новый департамент");
			forEdit = false;
		} else {
			forEdit = true;
			dialog.setText("Редактировать департамент");
		}
		dialog.setSize(690, 210);
		int height = 28;
		int h = 20;

		final DepTreeBean newDep;
		if (forEdit) {
			newDep = edited;
			parentDep = null;
			if (edited.getParentId() != null && edited.getParentId() != " ") {
				TreeItem parent = findTreeItemByTreeId(depTree, edited.getParentId());
				if (parent != null) {
					parentDep = (DepTreeBean) parent.getData();
				}
			}
		} else {
			newDep = DepTreeBean.newDepartment(conf.getId(), parentDep != null ? parentDep.getId() : null);
		}
		for (Pair<DepTreeField, String> d : DepTreeBean.getColumns(newDep)) {
			DepTreeField field = d.first;
			if (d.first != DepTreeField.FUNC_DEPARTMENT_CD) {
				Label l = new Label(dialog, SWT.NONE);
				l.setBounds(20, h, 260, 26);
				l.setText(d.first.getDescr());
				Scrollable t;
				if (d.first == DepTreeField.CONFIGUDATION_CD) {
					t = new Text(dialog, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
					((Text) t).setText(conf.getName());
				} else if (d.first == DepTreeField.PARENT_DEPARTMENT_CD) {
					if (forEdit) {
						t = new Combo(dialog, height - 4);
						//((Combo) t).add(parentDep != null ? parentDep.getName() : "--", 0);
						drawDepCombo((Combo) t, parentDep, edited);
						
					} else {
						t = new Text(dialog, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
						((Text) t).setText(parentDep != null ? parentDep.getName() : "--");
					}
				} else {
					t = new Text(dialog, SWT.SINGLE | SWT.BORDER);
					((Text) t).setText(newDep.getName() != null? newDep.getName() : "");
					((Text) t).setTextLimit(500);
					t.setBackground(new Color(100, 100, 100));
					t.setFocus();
					t.setData(field);
				}
				((Control) t).setToolTipText(field.name());
				((Control) t).setBounds(270, h, 390, 26);
				h = h + height;
			}
		}

		Button btnSave = new Button(dialog, SWT.NONE);
		btnSave.setLocation(20, h + height);
		btnSave.setSize(110, height);
		btnSave.setText("Сохранить");
		btnSave.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				Control[] childs = dialog.getChildren();
				final AtomicBoolean found = new AtomicBoolean(false);
				Arrays.asList(childs).forEach(c -> {
					if (c.getData() != null && (c.getData() instanceof DepTreeField)
							&& ((DepTreeField) c.getData()) == DepTreeField.DESCR) {
						String newDepName = ((Text) c).getText();
						if (!newDepName.trim().isBlank()) {
							newDep.setName(newDepName.trim());
							found.set(true);
						} else {
							c.setBackground(new Color(100, 0, 0));
						}
					} else if (c instanceof Combo) {
						List<String> deps = (List) c.getData();
						int selected = ((Combo) c).getSelectionIndex();
						System.out.println("Changed dep id: " + deps.get(selected));
						newDep.setParentId(deps.get(selected));
						
					}
				});
				if (found.get()) {
					System.out.println(newDep);
					final String newBeanId;
					if (forEdit) {
						newBeanId = newDep.getId();
						ConfigurationRepository.updateDepartment(newDep);
					} else {
						newBeanId = ConfigurationRepository.insertDepartment(newDep);
						newDep.setId(newBeanId);
					}
					// todo: refresh
					dialog.setData("refresh", Boolean.TRUE);

					dialog.setData("selection", newDep);
					dialog.close();
				}
			}
		});

		Button btnClose = new Button(dialog, SWT.NONE);
		btnClose.setLocation(130, h + height);
		btnClose.setSize(110, height);
		btnClose.setText("Закрыть");
		btnClose.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				dialog.close();
			}
		});

		return dialog;
	}

	private void drawDepCombo(Combo depCombo, DepTreeBean parentDep, DepTreeBean dep) {
		// TODO Auto-generated method stub
		List<String> deps = new ArrayList<>();
		deps.add(null);
		depCombo.add("--", 0);
		int selected = 0;
		//if (parentDep != null) {
			List<DepTreeBean> avial = ConfigurationRepository.getDepartments(dep.getConfId());
			List<DepTreeBean> childs = new ArrayList<>();
			findChildsByDepTreeBean(avial, dep, childs);
			System.out.println(childs);
			 //cut all childs
			avial.removeAll(childs);
			avial.remove(dep);
			drawTreeForCombo(avial);
			//TODO: draw tree in combo and add root
			for (int i=1; i <= avial.size(); i++) {
				DepTreeBean b = avial.get(i-1);
				deps.add(b.getId());
				depCombo.add(b.getName(), i);
				if (parentDep != null && b.getId().equals(parentDep.getId())) {
					selected = i;
				}
			}
			 
		//} else {
			//System.out.println("parent dep null");
		//}
		
		depCombo.select(selected);
		depCombo.setData(deps);
	}

	private void onSelectCombo(int selectionIndex) {
		ConfigurationBean conf = getConfiguration(selectionIndex);
		btnDisableEmp.setEnabled(false);
		btnDetailsEmp.setEnabled(false);
		btnAddEmp.setEnabled(false);

		btnEditDep.setEnabled(false);
		btnDeleteDep.setEnabled(false);

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

	private void onSelectDepartment(DepTreeBean selected) {
		// DepTreeBean selected = (DepTreeBean) depTreeItem.getData();
		btnInsertDep.setData("parent", selected);
		btnDeleteDep.setData(selected);
		btnEditDep.setData(selected);

		String depTreeId = selected.getId();
		List<EmplBean> employee = ConfigurationRepository.getEmployeeList(depTreeId);
		empTable.removeAll();
		btnDetailsEmp.setEnabled(false);
		btnDisableEmp.setEnabled(false);
		btnDisableEmp.redraw();
		btnDetailsEmp.redraw();

		btnInsertDep.setEnabled(true);
		btnEditDep.setEnabled(true);
		btnDeleteDep.setEnabled(true);
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

	private TreeItem findTreeItemByTreeId(Tree tree, String treeId) {
		for (TreeItem ti: tree.getItems()) {
			DepTreeBean b = (DepTreeBean) ti.getData();
			if (b.getId().equals(treeId)) {
				return ti;
			}
			TreeItem ret = _findTreeItemByTreeId(ti, treeId);
			if (ret != null) {
				return ret;
			}
		}
		return null;
	}
	
	private TreeItem _findTreeItemByTreeId(TreeItem root, String treeId) {
		DepTreeBean rootBean = (DepTreeBean) root.getData();
		if (rootBean.getId().equals(treeId)) {
			return root;
		}
		for (TreeItem item : root.getItems()) {
			DepTreeBean b = (DepTreeBean) item.getData();
			if (b.getId().equals(treeId)) {
				return item;
			}
			if (item.getItemCount() > 0) {
				TreeItem ret = _findTreeItemByTreeId(item, treeId);
				if (ret != null) {
					return ret;
				}
			}
		}
		return null;
	}

	private Shell createDepartmentDialogWithDisposeListener(DepTreeBean edited) {
		Shell dialog = createDepartmentOperationDialog(shell, (ConfigurationBean) btnInsertDep.getData("config"),
				(DepTreeBean) btnInsertDep.getData("parent"), edited);
		dialog.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				if (dialog.getData("refresh") != null) {
					DepTreeBean selected = (DepTreeBean) dialog.getData("selection");
					int current = config.getSelectionIndex();
					config.deselectAll();
					config.select(current);
					onSelectCombo(config.getSelectionIndex());

					// refill tree
					depTree.deselectAll();
					onSelectDepartment(selected);

					// find treeitem in new tree
					TreeItem selectedItem = findTreeItemByTreeId(depTree, selected.getId());
					if (selectedItem != null) {
						depTree.setSelection(selectedItem);
						depTree.showSelection();
					}
					System.out.println("Selected dep: " + selected);
				}

			}
		});
		return dialog;
	}

	private MessageBox createYesNoDialog(Shell parent, String label) {
		MessageBox dialog = new MessageBox(parent, SWT.YES | SWT.NO | SWT.ICON_WARNING | SWT.CENTER);
		dialog.setText("Удаление \"" + label + "\"");
		dialog.setMessage("Подтвердите удаление департамента.\nУдалены будут так-же поддепартаменты\nи работники.");
		return dialog;
	}

	private static void findChildsByTreeItems(TreeItem root, List<String> cache) {
		DepTreeBean rootItem = (DepTreeBean) root.getData();
		cache.add(rootItem.getId());
		for (TreeItem item : root.getItems()) {
			DepTreeBean b = (DepTreeBean) item.getData();
			if (rootItem.getId().equals(b.getParentId())) {
				findChildsByTreeItems(item, cache);
			}
		}
	}
	
	private static void drawTreeForCombo(List<DepTreeBean> items) {
		for (DepTreeBean b: items) {
			if (b.getParentId() == null) {
				drawTreeForCombo(items, b, 1);
				//break;
			}
		}
	}
	
	private static void drawTreeForCombo(List<DepTreeBean> items, DepTreeBean parent, int level) {
		for (DepTreeBean b: items) {
			if (parent.getId().equals(b.getParentId())) {
				String t = "";
				for (int i=0; i < level; i++) {
					t +="  ";
				}
				b.setName(t + " " + b.getName());
				drawTreeForCombo(items, b ,level+1);
			}
		}
	}
	
	private static void findChildsByDepTreeBean(List<DepTreeBean> items, DepTreeBean parent, List<DepTreeBean> childs) {
		for (DepTreeBean b: items) {
			if (parent.getId().equals(b.getParentId())) {
				childs.add(b);
				findChildsByDepTreeBean(items, b, childs);
			}
		}
	}
}

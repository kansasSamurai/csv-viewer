package org.jwellman.swing.jtable;

import java.util.ArrayList;
import java.util.List;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * 
 * 
 * 
 * Originated as found at [A],[B] with javadoc [C]; see also [D] 
 * ... heavily modified and refactored.
 * 
 * [A] https://tips4java.wordpress.com/2011/05/08/table-column-manager/
 * [B] http://www.camick.com/java/source/TableColumnManager.java
 * [C] The TableColumnManager can be used to manage TableColumns. It will give the
 * user the ability to hide columns and then reshow them in their last viewed
 * position. This functionality is supported by a popup menu added to the table
 * header of the table. The TableColumnModel is still used to control the view
 * for the table. The manager will inovoke the appropriate methods of the
 * TableColumnModel to hide/show columns as required.
 * [D] http://www.tachenov.name/2016/07/13/202/
 *
 * @author rwellman
 * 
 */
public class TableColumnManager 
    implements MouseListener, ActionListener, TableColumnModelListener, PropertyChangeListener {
    
    private JTable table;
    
    private TableColumnModel tableColumnModel;
    
    private List<TableColumn> allColumns;

    private boolean menuPopup;

    /**
     * Convenience constructor for creating a TableColumnManager for a table.
     * Support for a popup menu on the table header will be enabled.
     *
     * @param table
     *            the table whose TableColumns will managed.
     */
    public TableColumnManager(JTable table) {
        this(table, true);
    }

    /**
     * Create a TableColumnManager for a table.
     *
     * @param table
     *            the table whose TableColumns will managed.
     * @param menuPopup
     *            enable or disable a popup menu to allow the users to manager the
     *            visibility of TableColumns.
     */
    public TableColumnManager(JTable table, boolean menuPopup) {
        this.table = table;
        setMenuPopup(menuPopup);

        table.addPropertyChangeListener(this);
        reset();
    }

    /**
     * Reset the TableColumnManager to only manage the TableColumns that are
     * currently visible in the table.
     *
     * Generally this method should only be invoked by the TableColumnManager when
     * the TableModel of the table is changed.
     */
    public void reset() {
        table.getColumnModel().removeColumnModelListener(this);
        tableColumnModel = table.getColumnModel();
        tableColumnModel.addColumnModelListener(this);

        // Keep a duplicate TableColumns for managing hidden TableColumns

        int count = tableColumnModel.getColumnCount();
        allColumns = new ArrayList<TableColumn>(count);

        for (int i = 0; i < count; i++) {
            allColumns.add(tableColumnModel.getColumn(i));
        }
    }

    /**
     * Get the popup support.
     *
     * @returns the popup support
     */
    public boolean isMenuPopup() {
        return menuPopup;
    }

    /**
     * Add/remove support for a popup menu to the table header. 
     * The popup menu will give the user control over which columns are visible.
     *
     * @param menuPopop
     *            when true support for displaying a popup menu is added otherwise
     *            the popup menu is removed.
     */
    public void setMenuPopup(boolean menuPopup) {

        if (menuPopup) {
            table.getTableHeader().addMouseListener(this);
        } else {
            table.getTableHeader().removeMouseListener(this);            
        }
        
        this.menuPopup = menuPopup;
    }

    /**
     * Hide a column from view in the table.
     *
     * @param modelColumn
     *            the column index from the TableModel of the column to be removed
     */
    public void hideColumn(int modelColumn) {
        int viewColumn = table.convertColumnIndexToView(modelColumn);

        if (viewColumn != -1) {
            TableColumn column = tableColumnModel.getColumn(viewColumn);
            hideColumn(column);
        }
    }

    /**
     * Hide a column from view in the table.
     *
     * @param columnName
     *            the column name of the column to be removed
     */
    public void hideColumn(Object columnName) {
        if (columnName == null)
            return;

        for (int i = 0; i < tableColumnModel.getColumnCount(); i++) {
            TableColumn column = tableColumnModel.getColumn(i);

            if (columnName.equals(column.getHeaderValue())) {
                hideColumn(column);
                break;
            }
        }
    }

    /**
     * Hide a column from view in the table.
     *
     * @param column
     *            the TableColumn to be removed from the TableColumnModel of the
     *            table
     */
    public void hideColumn(TableColumn column) {
        if (tableColumnModel.getColumnCount() == 1)
            return;

        // Ignore changes to the TableColumnModel made by the TableColumnManager

        tableColumnModel.removeColumnModelListener(this);
        tableColumnModel.removeColumn(column);
        tableColumnModel.addColumnModelListener(this);
    }

    /**
     * Show a hidden column in the table.
     *
     * @param modelColumn
     *            the column index from the TableModel of the column to be added
     */
    public void showColumn(int modelColumn) {
        for (TableColumn column : allColumns) {
            if (column.getModelIndex() == modelColumn) {
                showColumn(column);
                break;
            }
        }
    }

    /**
     * Show a hidden column in the table.
     *
     * @param columnName
     *            the column name from the TableModel of the column to be added
     */
    public void showColumn(Object columnName) {
        for (TableColumn column : allColumns) {
            if (column.getHeaderValue().equals(columnName)) {
                showColumn(column);
                break;
            }
        }
    }

    /**
     * Show a hidden column in the table. The column will be positioned at its
     * proper place in the view of the table.
     *
     * @param column
     *            the TableColumn to be shown.
     */
    private void showColumn(TableColumn column) {
        // Ignore changes to the TableColumnModel made by the TableColumnManager

        tableColumnModel.removeColumnModelListener(this);

        // Add the column to the end of the table

        tableColumnModel.addColumn(column);

        // Move the column to its position before it was hidden.
        // (Multiple columns may be hidden so we need to find the first
        // visible column before this column so the column can be moved
        // to the appropriate position)

        int position = allColumns.indexOf(column);
        int from = tableColumnModel.getColumnCount() - 1;
        int to = 0;

        for (int i = position - 1; i > -1; i--) {
            try {
                TableColumn visibleColumn = allColumns.get(i);
                to = tableColumnModel.getColumnIndex(visibleColumn.getHeaderValue()) + 1;
                break;
            } catch (IllegalArgumentException e) {
            }
        }

        tableColumnModel.moveColumn(from, to);

        tableColumnModel.addColumnModelListener(this);
    }

    //
    // Implement MouseListener
    //
    public void mousePressed(MouseEvent e) {
        checkForPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        checkForPopup(e);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    private void checkForPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JTableHeader header = (JTableHeader) e.getComponent();
            int column = header.columnAtPoint(e.getPoint());
            showPopup(column);
        }
    }

    /*
     * Show a popup containing items for all the columns found in the table column
     * manager. The popup will be displayed below the table header columns that was
     * clicked.
     *
     * @param index index of the table header column that was clicked
     */
    private void showPopup(int index) {
        final JPopupMenu popup = new SelectPopupMenu();

        // Create a JCheckBoxMenuItem for all columns managed by the table column
        // manager, checking to see if the column is shown or hidden.
        final int columnCount = tableColumnModel.getColumnCount();
        final Object headerValue = tableColumnModel.getColumn(index).getHeaderValue();
        for (TableColumn tableColumn : allColumns) {
            final Object value = tableColumn.getHeaderValue();
            final JCheckBoxMenuItem item = new JCheckBoxMenuItem(value.toString());
            item.addActionListener(this);

            try {
                tableColumnModel.getColumnIndex(value);
                item.setSelected(true);

                // Do not allow all columns to be hidden
                if (columnCount == 1) item.setEnabled(false);
                
            } catch (IllegalArgumentException e) {
                item.setSelected(false);
            }

            popup.add(item);

            // Visually select the item corresponding to the right-clicked column
            if (value == headerValue) popup.setSelected(item);
            
        }

        // Display the popup below the TableHeader
        final JTableHeader header = table.getTableHeader();
        final Rectangle r = header.getHeaderRect(index);
        popup.show(header, r.x, r.height);
    }

    //
    // Implement ActionListener
    //
    /*
     * A table column will either be added to the table or removed from the table
     * depending on the state of the menu item that was clicked.
     */
    public void actionPerformed(ActionEvent event) {
        final JMenuItem item = (JMenuItem) event.getSource();
        if (item.isSelected()) {
            showColumn(item.getText());
        } else {
            hideColumn(item.getText());
        }
    }

    //
    // Implement TableColumnModelListener
    //
    public void columnAdded(TableColumnModelEvent e) {
        // A table column was added to the TableColumnModel so we need
        // to update the manager to track this column

        TableColumn column = tableColumnModel.getColumn(e.getToIndex());

        if (allColumns.contains(column))
            return;
        else
            allColumns.add(column);
    }

    public void columnMoved(TableColumnModelEvent e) {
        if (e.getFromIndex() == e.getToIndex())
            return;

        // A table column has been moved one position to the left or right
        // in the view of the table so we need to update the manager to
        // track the new location

        int index = e.getToIndex();
        TableColumn column = tableColumnModel.getColumn(index);
        allColumns.remove(column);

        if (index == 0) {
            allColumns.add(0, column);
        } else {
            index--;
            TableColumn visibleColumn = tableColumnModel.getColumn(index);
            int insertionColumn = allColumns.indexOf(visibleColumn);
            allColumns.add(insertionColumn + 1, column);
        }
    }

    public void columnMarginChanged(ChangeEvent e) {
    }

    public void columnRemoved(TableColumnModelEvent e) {
    }

    public void columnSelectionChanged(ListSelectionEvent e) {
    }

    //
    // Implement PropertyChangeListener
    //
    public void propertyChange(PropertyChangeEvent e) {
        if ("model".equals(e.getPropertyName())) {
            if (table.getAutoCreateColumnsFromModel())
                reset();
        }
    }

    /*
     * Allows you to select a specific menu item when the popup is displayed. 
     * (i.e. this is a bug? fix)
     */
    class SelectPopupMenu extends JPopupMenu {

        private static final long serialVersionUID = 1L;

        @Override
        public void setSelected(Component sel) {
            int index = getComponentIndex(sel);
            getSelectionModel().setSelectedIndex(index);
            final MenuElement me[] = new MenuElement[2];
            me[0] = (MenuElement) this;
            me[1] = getSubElements()[index];

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    MenuSelectionManager.defaultManager().setSelectedPath(me);
                }
            });
        }
    }
    
}

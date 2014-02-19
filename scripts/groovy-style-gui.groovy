import org.orbisgis.sif.*
import groovy.swing.SwingBuilder;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

ds = mc.getDataManager().getDataSource()
sql = groovy.sql.Sql.newInstance(ds)

def sb = new SwingBuilder()
dialog = sb.panel() {
    gridLayout(columns: 2, rows: 0)    
    label("Input table")
    comboBox(id:"1", items:mc.getLayers().collect { it.getName() })
    label("New column name")
    textField(id: "2")
    label("Buffer size")
    textField(id: "3")
}

buttonClicked = JOptionPane.showConfirmDialog(UIFactory.getMainFrame(), dialog,
                        "Buffer Example",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE)

if (buttonClicked == JOptionPane.OK_OPTION) {
	println sb."1".selectedItem
	println sb."2".text
	println sb."3".text
} else {
	println "Canceled."
}

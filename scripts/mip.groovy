import org.orbisgis.sif.*
import org.orbisgis.sif.multiInputPanel.*

mip = new MultiInputPanel()
mip.addInput("1", "CheckBox", new CheckBoxChoice(false))
mip.addInput("2", "ComboBox", new ComboBoxChoice("ComboChoice1", "ComboChoice2"))
mip.addInput("3", "List", new ListChoice("ListChoice1", "ListChoice2", "ListChoice3"))
mip.addInput("4", "TextBox", new TextBoxType())
mip.addInput("5", "Password", new PasswordType())
mip.addInput("6", "NoInput", new NoInputType())

if (UIFactory.showDialog(mip)) {
    println "CheckBox checked: " + mip.getInput("1")
    println "ComboBoxChoice: " + mip.getInput("2")
    println "ListChoice: " + mip.getInput("3").split("#")
    println "TextBoxChoice: " + mip.getInput("4")
    println "PasswordChoice: " + mip.getInput("5")
    println "NoInputChoice: " + mip.getInput("6")
} else {
    println "cancelled"
}

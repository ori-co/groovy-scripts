import org.orbisgis.core.Services
import org.orbisgis.viewapi.docking.*
import javax.swing.*
import org.orbisgis.viewapi.edition.*
import groovy.swing.SwingBuilder
import org.orbisgis.view.icons.OrbisGISIcon
import org.orbisgis.mapeditorapi.MapElement;
import org.orbisgis.core.layerModel.MapContext;
import javax.swing.JList
import org.apache.log4j.Logger;
import com.vividsolutions.jts.geom.Envelope;

/**
 * A panel that manages map positionning
 */
class MyPanel implements EditorDockable {
    /**Logger is a class to log message to user */	
    Logger LOGGER = Logger.getLogger("gui."+MyPanel.class);
    /** Hold properties of the GUI panel like the title */
    DockingPanelParameters dockingParameters = new DockingPanelParameters()
    /** Panel SWING component */
    JPanel panel
    /** A Groovy class that help to build SWING components */
    SwingBuilder swing = new SwingBuilder()
    /** The current loaded map in OrbisGIS */
    MapContext mapContext
    /** SWING component, List of GeoMark */
    JList geoMarkList    
    /** Internal array of GeoMark name */
    List listData = []
    /** Array of geomark envelope (minX,maxX,minY,maxY) */
    Map envList = new HashMap() 
    /** Constructor*/
    MyPanel() {
           EditorManager editorManager = Services.getService(EditorManager.class)
           mapContext = MapElement.fetchFirstMapElement(editorManager).getMapContext();
        dockingParameters.setName("geomark")
        dockingParameters.setTitle("Geomark")
        dockingParameters.setDockActions(
                [swing.action(closure: { remove() }, smallIcon: OrbisGISIcon.getIcon("remove"), name: "Remove panel"),
                 swing.action(closure: { addGeoMark() }, smallIcon: OrbisGISIcon.getIcon("add"), name: "Add GeoMark"),
                 swing.action(closure: { goToGeoMark() }, smallIcon: OrbisGISIcon.getIcon("zoom"), name: "Zoom to GeoMark"),
                 swing.action(closure: { deleteGeoMark() }, smallIcon: OrbisGISIcon.getIcon("delete"), name: "Delete GeoMark")
                ])
        panel = swing.panel() {
            borderLayout()
            scrollPane( constraints:CENTER ) {
                geoMarkList = list(id:'listId')
            }
        }
    }
    /** The user click on Zoom to GeoMark */
    void goToGeoMark() {
    	// Read selected entry in the SWING component
    	String key = geoMarkList.getSelectedValue()
    	if(key!=null && !key.isEmpty()) {
    		// Change bounding box, extract Envelope instance from the array this.envList
	    	mapContext.setBoundingBox(envList.get(key))
    	}
    }
    /** The user click on Delete GeoMark*/
    void deleteGeoMark() {
        listData.remove(geoMarkList.getSelectedValue())
        envList.remove(geoMarkList.getSelectedValue())
        geoMarkList.listData = listData
    }
    /** The user click on Add GeoMark*/
    void addGeoMark() {
         try {
             Envelope env = mapContext.getBoundingBox()
             if(env != null) {
                 String label = env.toString()
                 Object pane = swing.optionPane(message : "Set the GeoMark name", wantsInput: true)
                 Object dialog = pane.createDialog(panel, "Choose GeoMark name")
                 dialog.show()
                 label = pane.inputValue
             listData.add(label)
              geoMarkList.listData = listData
              envList.put(label, env)
             }
         } catch(Exception ex) {
             LOGGER.error(ex.getLocalizedMessage(), ex)
         }
    }
    /**The user click on Remove panel*/
    void remove() {
    	   // Remove the panel from OrbisGIS
        EditorManager editorManager = Services.getService(EditorManager.class)
        editorManager.removeEditor(this)
    }
    
    @Override
    DockingPanelParameters getDockingParameters() {
        return dockingParameters
    }

    @Override
    JComponent getComponent() {
        return panel
    }

    /** This panel handle MapElement */
    @Override
    boolean match(org.orbisgis.viewapi.edition.EditableElement editableElement) {
        return editableElement instanceof MapElement
    }

    /** We do not store map element */
    @Override
    EditableElement getEditableElement() {
        return null
    }

	/** The user want to load another Map */
    @Override
    void setEditableElement(EditableElement editableElement) {
        if(editableElement instanceof MapElement) {
            mapContext = editableElement.getMapContext()
        }
    }
}
// Main stuff
// Get the Panel manager
EditorManager editorManager = Services.getService(EditorManager.class)
// Construct our panel
EditorDockable panel = new MyPanel()
// Tell the panel manager to loads our panel
editorManager.addEditor(panel)

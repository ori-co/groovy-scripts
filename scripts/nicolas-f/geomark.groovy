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
    Logger LOGGER = Logger.getLogger("gui."+MyPanel.class);
    DockingPanelParameters dockingParameters = new DockingPanelParameters()
    JPanel panel
    SwingBuilder swing = new SwingBuilder()
    MapContext mapContext
    JList geoMarkList    
    List listData = []
    Map envList = new HashMap() 
    
    MyPanel() {
    	   EditorManager editorManager = Services.getService(EditorManager.class)
    	   mapContext = MapElement.fetchFirstMapElement(editorManager).getMapContext();
        dockingParameters.setName("geomark")
        dockingParameters.setTitle("Geomark")
        dockingParameters.setDockActions(
                [swing.action(closure: { remove() }, smallIcon: OrbisGISIcon.getIcon("remove"), name: "Remove panel"),
                 swing.action(closure: { addGeoMark() }, smallIcon: OrbisGISIcon.getIcon("add"), name: "Add GeoMark"),
                 swing.action(closure: { goToGeoMark() }, smallIcon: OrbisGISIcon.getIcon("zoom"), name: "Zoom to GeoMark")
                ])
        panel = swing.panel() {
            borderLayout()
            scrollPane( constraints:CENTER ) {
                geoMarkList = list(id:'listId')
            }
        }
    }
    void goToGeoMark() {
    	String key = geoMarkList.getSelectedValue()
    	if(key!=null && !key.isEmpty()) {
	    	mapContext.setBoundingBox(envList.get(key))
    	}
    }
    void addGeoMark() {
    	 try {
    	 	Envelope env = mapContext.getBoundingBox()
    	 	if(env != null) {
		 	listData.add(env.toString())
     	 	geoMarkList.listData = listData
     	 	envList.put(env.toString(), env)
    	 	}
    	 } catch(Exception ex) {
    	 	LOGGER.error(ex.getLocalizedMessage(), ex)
    	 }
    }
    
    void remove() {
        EditorManager editorManager = Services.getService(EditorManager.class)
        editorManager.removeEditor(this)
    }

    DockingPanelParameters getDockingParameters() {
        return dockingParameters
    }

    JComponent getComponent() {
        return panel
    }

    boolean match(org.orbisgis.viewapi.edition.EditableElement editableElement) {
        return editableElement instanceof MapElement
    }

    EditableElement getEditableElement() {
        return null
    }

    void setEditableElement(EditableElement editableElement) {
		if(editableElement instanceof MapElement) {
			mapContext = editableElement.getMapContext()
		}
    }
}

EditorManager editorManager = Services.getService(EditorManager.class)
EditorDockable panel = new MyPanel()
editorManager.addEditor(panel)










/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.graphicaleditor;

import java.awt.Color;
import java.awt.Point;
import java.awt.datatransfer.Clipboard;
import java.awt.dnd.DnDConstants;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.graph.layout.GraphLayout;
import org.netbeans.api.visual.graph.layout.GraphLayoutFactory;
import org.netbeans.api.visual.graph.layout.GraphLayoutSupport;
import org.netbeans.api.visual.graph.layout.GridGraphLayout;
import org.netbeans.api.visual.graph.layout.TreeGraphLayout;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.layout.SceneLayout;
import org.netbeans.api.visual.model.ObjectSceneEvent;
import org.netbeans.api.visual.model.ObjectSceneEventType;
import org.netbeans.api.visual.model.ObjectSceneListener;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.datatransfer.ExClipboard;

/**
 *
 * @author blair
 */
public class MindMapGraphScene extends GraphScene<MindMapNode, MindMapEdge>{

    protected LayerWidget mainLayer;
    protected LayerWidget connectionLayer;

    protected Map<Widget, MindMapNode> widgetToNode;

    protected ExplorerManager explorerManager;

    protected SceneLayout sceneLayout;

    protected TreeGraphLayout<MindMapNode, MindMapEdge> layout;

    protected MindMapNode rootNode;

    protected Border normalBorder;

    protected Border hoveredBorder;

    protected NameTextFieldEditor nameTextFieldEditor;

    public MindMapGraphScene(ExplorerManager em) {
        mainLayer = new LayerWidget(this);
        connectionLayer = new LayerWidget(this);

        this.explorerManager = em;

        widgetToNode = new HashMap<Widget, MindMapNode>();
        addChild(mainLayer);
        addChild(connectionLayer);

        normalBorder = BorderFactory.createRoundedBorder(5, 5, new Color(24, 116, 205), new Color(60, 80, 130));
        hoveredBorder = BorderFactory.createRoundedBorder(5, 5, new Color(30, 144, 255), new Color(60, 80, 130));
        nameTextFieldEditor = new NameTextFieldEditor();
    }

    public void layout() {
        sceneLayout.invokeLayoutImmediately();
        validate();
    }

    public void setRootNode(Node node) {
        rootNode = new MindMapNode(this, node);
        rootNode.updateChildren();

        addObjectSceneListener(new ObjectSceneListener() {

            public void objectAdded(ObjectSceneEvent event, Object addedObject) {
            }

            public void objectRemoved(ObjectSceneEvent event, Object removedObject) {
            }

            public void objectStateChanged(ObjectSceneEvent event, Object changedObject, ObjectState previousState, ObjectState newState) {
                //NOP
            }

            public void selectionChanged(ObjectSceneEvent event, Set<Object> previousSelection, Set<Object> newSelection) {
                //NOP
            }

            public void highlightingChanged(ObjectSceneEvent event, Set<Object> previousHighlighting, Set<Object> newHighlighting) {

            }

            public void hoverChanged(ObjectSceneEvent event, Object previousHoveredObject, Object newHoveredObject) {
                if(previousHoveredObject != null) {
                    findWidget(previousHoveredObject).setBorder(normalBorder);
                }
                if(newHoveredObject != null) {
                    findWidget(newHoveredObject).setBorder(hoveredBorder);
                    try {
                        explorerManager.setSelectedNodes(new Node[]{((MindMapNode)newHoveredObject).getNode()});
                    } catch (PropertyVetoException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }

            public void focusChanged(ObjectSceneEvent event, Object previousFocusedObject, Object newFocusedObject) {

            }
        }, ObjectSceneEventType.OBJECT_HOVER_CHANGED);

        

        GraphLayout<MindMapNode, MindMapEdge> graphLayout = GraphLayoutFactory.createTreeGraphLayout(0, 0, 100, 100, true);
        GraphLayoutSupport.setTreeGraphLayoutRootNode(graphLayout, rootNode);
        sceneLayout = LayoutFactory.createSceneGraphLayout (this, graphLayout);
        sceneLayout.invokeLayoutImmediately();
    }

    @Override
    protected Widget attachNodeWidget(MindMapNode node) {
        LabelWidget widget = new LabelWidget(this, node.getName());

        widget.getActions().addAction(node.getPopupAction());

        widget.getActions().addAction(createObjectHoverAction());

        widget.getActions().addAction(ActionFactory.createInplaceEditorAction(nameTextFieldEditor));

        MoveStrategy strategy = new MoveStrategy() {

            public Point locationSuggested(Widget widget, Point originalLocation, Point suggestedLocation) {
                return suggestedLocation;
            }

        };

        MoveProvider provider = new MoveProvider() {

            public void movementStarted(Widget widget) {
                Node[] nodes = explorerManager.getSelectedNodes();
                Clipboard clipboard = Lookup.getDefault().lookup(ExClipboard.class);
                try {
                    clipboard.setContents(nodes[0].clipboardCut(), null);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

            public void movementFinished(Widget widget) {
                Node[] nodes = explorerManager.getSelectedNodes();
                Clipboard clipboard = Lookup.getDefault().lookup(ExClipboard.class);
                try {
                    nodes[0].getDropType(clipboard.getContents(this), DnDConstants.ACTION_COPY_OR_MOVE, 0).paste();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

            public Point getOriginalLocation(Widget widget) {
                return widget.getLocation();
            }

            public void setNewLocation(Widget widget, Point location) {
            }

        };

//        widget.getActions().addAction(ActionFactory.createMoveAction(strategy, provider));

        mainLayer.addChild(widget);

        widgetToNode.put(widget, node);

        widget.setBorder(normalBorder);

        validate();

        return widget;
    }

    @Override
    protected Widget attachEdgeWidget(MindMapEdge edge) {
        ConnectionWidget widget = new ConnectionWidget(this);
        widget.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
        widget.setRouter(RouterFactory.createOrthogonalSearchRouter(mainLayer, connectionLayer));
        connectionLayer.addChild(widget);

        validate();

        return widget;
    }

    @Override
    protected void attachEdgeSourceAnchor(MindMapEdge edge, MindMapNode oldSourceNode, MindMapNode sourceNode) {
        ConnectionWidget c = (ConnectionWidget)findWidget(edge);
        Widget widget = findWidget(sourceNode);
        Anchor a = AnchorFactory.createRectangularAnchor(widget);
        c.setSourceAnchor(a);

        validate();
    }

    @Override
    protected void attachEdgeTargetAnchor(MindMapEdge edge, MindMapNode oldTargetNode, MindMapNode targetNode) {
        ConnectionWidget c = (ConnectionWidget)findWidget(edge);
        Widget widget = findWidget(targetNode);
        Anchor a = AnchorFactory.createRectangularAnchor(widget);
        c.setTargetAnchor(a);

        validate();
    }

    protected class NameTextFieldEditor implements TextFieldInplaceEditor{

        public boolean isEnabled(Widget widget) {
            return (widget instanceof LabelWidget);
        }

        public String getText(Widget widget) {
            return ((MindMapNode)findObject(widget)).getName();
        }

        public void setText(Widget widget, String text) {
            ((MindMapNode)findObject(widget)).getNode().setName(text);
        }
        
    }
}

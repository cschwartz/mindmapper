# Introduction #

## The MindMap Model ##

All elements of the MindMap Model are _Java Beans_ and thus provide support for `PropertyChangeListeners` on attribute changes.

### MindMapFactory ###
The MindMapFactory provides methods to create new **MindMap Documents**, load them from and save them to the **File System API\*s FileObjects.**

### MindMap Document ###
A MindMap **Document** consists of a **name**, a **root node** and several **links**. The root node is immutable and cannot be replaced. The **Document** serves as a factory to create _Actions_ which can be used to change the model. Each **Document** owns a `UndoRedo.Manager` which is shared by everyone manipulating the Model through _Actions_.

### MindMap Node ###
A MindMap **Node** consists of a **name** and several **children** MindMap nodes. It thus defines a recursive tree which makes up the MindMap.

### MindMap Link ###
A MindMap **Link** connects a **source node** and a **target node**, the Link itself is described by its **name**.

### Actions ###

All actions on **Nodes**, **Links** or **Documents** are created by the owning **Document** and are executed after creation. The actions are also added to the **Documents** unique **UndoRedo.Manager**.

**createAddChildAction**(Node **parent**) adds a new MindMap Node to the parent Node.

**createAppendChildAction**(Node **parent**, Node **childToAppend**) appends the MindMap Node **child** as the last child to the **parent** node.

**createRemoveChildAction(Node**parent**, Node**childToRemove**) removes the**childToRemove**from the**parent**MindMap Node.**

**createRenameAction(Node**parent**, String**newName**) renames the**parent**MindMap Node to**newName**.**

**createReorderAction(Node**parent**, int[.md](.md)**permutation**) reorders the children of the MindMap Node**parent**according to the**permutation**.**

## Integration of the Datasystems API and the Nodes API ##
Once the platform recognizes a XML document with a namespace of `http://xml.netbeans.org/schema/MindMapperSchema` (which is associated with the MIME-type `text/x-mindmapper+xml`) a **MindMapDataLoader** instance is used to create _one_ **MindMapDataObject**. The data Object provides the following _Cookies_:
  * **OpenSupport** to _Open_ and _Close_ an editor for the document.
  * **SaveCookie** to notify the platform is saving is allowed.
  * **DocumentCookie** to query informations about the MindMap Document
The **DataObject** also provides via `getNodeDelegate()`a NetBeans Node used to display the Document in the _Project View_.

The **OpenSupport** Cookie provides a method to open a MindMap Editor (which has to be registered via the _META-INF/services_ architecture) to whom a **MindMapDocument** is passed.

The **MindMapDocument** is a NetBeans [Node](http://bits.netbeans.org/dev/javadoc/org-openide-nodes/overview-summary.html) used by all MindMap Editors to display and manipulate a wrapped MindMap **Document**. The children of this NetBeans Node are stored in a **DocumentChildren** instance.

The **DocumentChildren** class for a given MindMapDocument provides statically a **MindMapNode** wrapping the MindMap Documents Root Node.

**MindMapNodes** are NetBeans Nodes wrapping MindMap Nodes, their children are wrapped in a **NodeChildren** instance.  **MindMapNodes** provide the following Actions to an editor:
  * **Cut** (if it is not the Documents Root Node) cut the node to the clipboard,
  * **Delete** (if it is not the Documents Root Node) remove the node from its parent,
  * **New**  add a new node as a child to this node,
  * **Copy** copy this node to the clipboard,
  * **Paste** append a node from the clipboard as a child to this node,
  * **Rename** change the name of this node,
  * **Reorder** allows the reordering of the children of this node,
  * **Move Up** moves this node up in the order of children of its parent,
  * **Move Down** moves this node down in the order of children of its parent.

Further information can be found in the javadoc.


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.*;



/**
 * Tree GUI class to implement the familytree and family member objects.
 * Basically displays a way to interact with the family tree objects 
 * The GUI is separated into 4 main sections
 *  o The menu bar
 *   - contains the menubar options 
 *  o The header panel
 *   - contains the load, save and create new tree buttons 
 *  o The control panel 
 *   - contains the jTree representation of the tree object and details panel
 *     which contains the info for the current member or add and edit forms
 *  o The status panel
 *   - contains the status message
 * Assumptions:
 *  There has to a FamilyTree and FamilyMember classes
 *  The user will interact with this program with a mouse and keyboard
 *  English is the only supported language
 * @author Taaqif
 */
public class Application {

    /**
     * Creates ands sets up the gui interface as well as initializes all variables
     */
   
    private JFrame mainFrame;
    private JPanel controlPanel;
    private JPanel infoPanel;
    private final JLabel statusLabel = new JLabel("Program loaded");
    private File currentFile;
    private JTree arbre;

    private ArbreFamille currentArbreFamille;
    
	 public Application() {
	        
	        /*currentFamilyTree*/ 
	    	currentArbreFamille = new ArbreFamille();
	        currentFile = null;
	        arbre = new JTree();
	        creerInterface();
	    }
    /**
     * Calls the initilising functions to get all the different panels set up
     */
    private void creerInterface() {

        mainFrame = new JFrame("Arbre Généalogique");
        mainFrame.setSize(900, 900);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.getContentPane().setBackground(Color.WHITE);
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        //sets up the menu bar
        initMenuBar();

        //sets up the header section
        initHeaderPanel();

        //sets up the control section (main part where data is displayed)
        initControlPanel();

        //sets up the status bar
        initStatusBar();

        //displays the empty tree
        displayTree(currentArbreFamille);

        //check if user wants to continue using checkUserCOntinue function
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (checkUserContinue()) {
                    System.exit(0);
                }
            }
        });

        mainFrame.setVisible(true);
    }

    /**
     * Initializes the header panel
     */
    private void initHeaderPanel() {
        
        JLabel headerLabel = new JLabel("Welcome to the Family Tree Application", JLabel.LEFT);
        headerLabel.setFont(new Font("SansSerif", Font.PLAIN, 28));

        JButton open = new JButton("Load Tree");
        open.addActionListener(new openAction());

        JButton create = new JButton("Create New Tree");
        create.addActionListener(new createTreeAction());

        JButton saveTree = new JButton("Save Tree");
        saveTree.addActionListener(new saveAction());

        JPanel headPanel = new JPanel();
        headPanel.setLayout(new GridBagLayout());
        headPanel.setOpaque(false);
        headPanel.setBorder(new EmptyBorder(0,10,10,10));
        
        //using a grid layout to position each element
        //grid bag constraints specifies where the element will go inside the grid
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        headPanel.add(headerLabel, gbc);

        //flow layout for the buttons (next to eachother)
        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT));
        container.setOpaque(false);
        container.add(open);
        container.add(saveTree);
        container.add(create);

        gbc.gridx = 0;
        gbc.gridy = 1;
        headPanel.add(container, gbc);
        
        mainFrame.add(headPanel, BorderLayout.NORTH);
    }

    /**
     * Initializes the control panel where the bulk of data is showed 
     */
    private void initControlPanel() {
        controlPanel = new JPanel();
        
        //used to show white background from mainFrame
        controlPanel.setOpaque(false);
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        
        mainFrame.add(controlPanel, BorderLayout.CENTER);
    }

    /**
     * Initialize the menu bar which contains menu actions such as save load new and exit
     */
    private void initMenuBar() {
        JMenuBar menuBar;
        menuBar = new JMenuBar();
        mainFrame.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
//        JMenu editMenu = new JMenu("Edit");
        menuBar.add(fileMenu);
//        menuBar.add(editMenu);

        JMenuItem newAction = new JMenuItem("New");
        fileMenu.add(newAction);
        newAction.addActionListener(new createTreeAction());
        
        JMenuItem openAction = new JMenuItem("Open");
        fileMenu.add(openAction);
        openAction.addActionListener(new openAction());
        
        fileMenu.addSeparator();

        JMenuItem saveAction = new JMenuItem("Save");
        fileMenu.add(saveAction);
        saveAction.addActionListener(new saveAction());
        
        JMenuItem saveAsAction = new JMenuItem("Save As");
        fileMenu.add(saveAsAction);
        saveAsAction.addActionListener(new saveAsAction());
        
        
        JMenuItem exitAction = new JMenuItem("Exit");
        fileMenu.addSeparator();
        fileMenu.add(exitAction);
        //anonymous function as there is no need to have this as a fully encapsulated
        //actionlistner class
        exitAction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkUserContinue()) {
                    System.exit(0);
                }
            }
        });       
    }

    /**
     * Itnitialises the status bar where information such as messages are 
     * displayed to the user right at the botton of the screen
     */
    private void initStatusBar() {
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        
        mainFrame.add(statusPanel, BorderLayout.SOUTH);
        
        //set size to the mainframe
        statusPanel.setPreferredSize(new Dimension(mainFrame.getWidth(), 18));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        
        //align text to the left
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        //this is where the status message will be displayed
        statusPanel.add(statusLabel);
    }

    /**
     * Convenience method to edit the status. Basically sets the text of the 
     * label inside the status bar
     * @param status the message to display
     */
    private void editStatus(String status) {
        statusLabel.setText(status);
    }

    /**
     * Action class that implements ActionListner
     * Used to display the add relative function after clicking a button for a specified 
     * family member 
     */
    private class addRelativeAction implements ActionListener {

        private Personne member;
        //because we can call this actionlistner on any relative
        //we need to pass the member we would like to edit as a parameter
        //this then catches that parameter and does the correct actions
        public addRelativeAction(Personne member) {
            this.member = member;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //display the add relative form for the current member
            displayAddRelativeInfo(member);
        }
    }

    /**
     * Edit member action which implements ActionListner to display
     * the edit member form when a button is clicked for a specified family member
     */
    private class editMemberAction implements ActionListener {

        private Personne member;
        //because we can call this actionlistner on any relative
        //we need to pass the member we would like to edit as a parameter
        //this then catches that parameter and does the correct actions
        public editMemberAction(Personne member) {
            this.member = member;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //displays the edit member info form
            displayEditMemberInfo(member);
        }
    }
 
    /**
     * create tree action implements actionlistner to show the create tree form 
     * for a specified family member 
     */
    private class createTreeAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            if (checkUserContinue()) {
                //check if tree is not saved and reset the main variables
            	currentArbreFamille = new ArbreFamille();
                currentFile = null;
                //display the new (empty) tree
                displayTree(currentArbreFamille);
                editStatus("Blank tree created");
            }

        }
    }

    /**
     * Open action implements actionlistner which invokes a jDialogBox such that
     * the user can select a file to open within the application 
     */
    private class openAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (checkUserContinue()) {
                JFileChooser jFileChooser = new JFileChooser();
                //set file filters
                jFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("FamilyTree Files (*.ft)", "ft"));
                jFileChooser.setAcceptAllFileFilterUsed(true);
                
                int result = jFileChooser.showOpenDialog(mainFrame);
                //process jfilechooser result
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        //try to open the file, display the family tree
                        openFile(jFileChooser.getSelectedFile());
                        displayTree(currentArbreFamille);
                        editStatus("File opened from: " + (jFileChooser.getSelectedFile().getAbsolutePath()));
                    } catch (Exception j) {
                        //error
                        showErrorDialog(j);
                        editStatus("Error: " + j.getMessage());
                    }
                }
            }

        }
    }

    /**
     * Convenience method to check if the tree is loaded. Used to check if the user
     * wants to continue despite the tree being loaded
     * @return true if the tree does not have a root or if the user wishes to continue
     */
    private boolean checkUserContinue() {
        if (currentArbreFamille.hasRoot()) {
            int dialogResult = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you wish to continue? Any unsaved changes will be lost", "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
            return dialogResult == JOptionPane.YES_OPTION;
        }
        return true;
    }

    /**
     * displays the family tree object through a jTree.
     * @param familyTree the family tree to display
     */
    private void displayTree(ArbreFamille familyArbre) {

        //create the root node
        DefaultMutableTreeNode main = new DefaultMutableTreeNode("Main");
        //last selected path to keep track of the last person the user selected. 
        //Used when adding or canceling an action
        TreePath lastSelectedNode = null;
        
        //mutable tree node allowing objects as nodes
        DefaultMutableTreeNode top;
        
        //no data loaded inthe tree
        if (!familyArbre.hasRoot()) {
            top = new DefaultMutableTreeNode("No tree data found.");

        } else {
            //add the root person
            top = new DefaultMutableTreeNode(familyArbre.getRoot());
            //call the recursive method to populate the entire tree with all the 
            //details from the root family member
            createTree(top, familyArbre.getRoot());
            //if the user selected a member, set the last selected path
            lastSelectedNode = arbre.getSelectionPath();

        }
        //Create the tree and allow one selection at a time and hide the root node
        arbre = new JTree(main);
        main.add(top);
        arbre.setRootVisible(false);
        arbre.setShowsRootHandles(true);
        arbre.setEnabled(true);
        arbre.expandPath(new TreePath(main.getPath()));
        arbre.getSelectionModel().addTreeSelectionListener(new treeSelectorAction());
        arbre.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        arbre.setBorder(new EmptyBorder(0, 10, 0, 10));

        //expand all the tree nodes
        for (int i = 0; i < arbre.getRowCount(); i++) {
        	arbre.expandRow(i);
        }
        
        //have a custom renderer for the nodes of the tree
        //dim the text nodes and allow selection of the familymember object nodes
        arbre.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                Object nodeInfo = node.getUserObject();
                if (nodeInfo instanceof Personne) {
                    setTextNonSelectionColor(Color.BLACK);
                    setBackgroundSelectionColor(Color.LIGHT_GRAY);
                    setTextSelectionColor(Color.BLACK);
                    setBorderSelectionColor(Color.WHITE);
                } else {
                    setTextNonSelectionColor(Color.GRAY);
                    setBackgroundSelectionColor(Color.WHITE);
                    setTextSelectionColor(Color.GRAY);
                    setBorderSelectionColor(Color.WHITE);
                }
                setLeafIcon(null);
                setClosedIcon(null);
                setOpenIcon(null);
                super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);
                return this;
            }
        });

        //add the tree to a scrolepane so the user can scroll 
        JScrollPane treeScrollPane = new JScrollPane(arbre);
        treeScrollPane.setPreferredSize(new Dimension(250, 0));

        //create the info panel to be displayed in the control panel
        infoPanel = new JPanel();
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel promptInfo;
        JButton addNewRoot = new JButton("Add root person");
        addNewRoot.addActionListener(new addRelativeAction(null));
        if (!familyArbre.hasRoot()) {
            promptInfo = new JLabel("<html>Load a tree or add a new root person</html>");
            infoPanel.add(addNewRoot);
        } else {
            promptInfo = new JLabel("<html>Select a family member to view information</html>");
        }

        promptInfo.setFont(new Font("SansSerif", Font.PLAIN, 20));
        infoPanel.add(promptInfo, BorderLayout.NORTH);
        infoPanel.setOpaque(false);

        controlPanel.removeAll();

        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        container.setOpaque(false);
        controlPanel.add(container);

        container.setLayout(new BorderLayout());
        container.add(treeScrollPane, BorderLayout.WEST);
        container.add(infoPanel, BorderLayout.CENTER);
        
        controlPanel.add(container);
        controlPanel.validate();
        controlPanel.repaint();
        
        //scroll the tree to the last selected path
        arbre.setSelectionPath(lastSelectedNode);
    }

    /**
     * cancels the edit by returning to displaymember info form
     */
    private class cancelEditMemberAction implements ActionListener {

        Personne member;

        public cancelEditMemberAction(Personne member) {
            this.member = member;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            displayMemberInfo(member);
            editStatus("Action canceled");
        }
    }

    /**
     * if a file exists, propmpt to overwrite the saved file. If not, initiate save as action
     */
    private class saveAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (currentFile != null) {
                    int dialogResult = JOptionPane.showConfirmDialog(mainFrame, "Would You Like to overwrite the current tree?", "Warning", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        //save the file
                        saveToFile(currentFile);
                        editStatus("File saved to: " + currentFile.getPath());
                    }
                } else {
                    editStatus("File not loaded");
                    //save as instead
                    ActionListener listner = new saveAsAction();
                    listner.actionPerformed(e);

                }

            } catch (Exception j) {
                showErrorDialog(j);
                editStatus("Error: "+ j.getMessage());
            }
        }
    }

    /**
     * save the current tree as another file
     */
    private class saveAsAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser jFileChooser = new JFileChooser() {
                //check if file already exists, as to overwrite
                @Override
                public void approveSelection() {
                    File selectedFile = getSelectedFile();
                    if (selectedFile.exists() && getDialogType() == SAVE_DIALOG) {
                        int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?", "Existing file", JOptionPane.YES_NO_CANCEL_OPTION);
                        switch (result) {
                            case JOptionPane.YES_OPTION:
                                super.approveSelection();
                                return;
                            case JOptionPane.NO_OPTION:
                                return;
                            case JOptionPane.CLOSED_OPTION:
                                return;
                            case JOptionPane.CANCEL_OPTION:
                                cancelSelection();
                                return;
                        }
                    }
                    super.approveSelection();
                }
            };
            jFileChooser.setSelectedFile(new File("Family Tree.ft"));
            //Set an extension filter, so the user sees other ft files
            jFileChooser.setFileFilter(new FileNameExtensionFilter("FamilyTree Files (*.ft)", "ft"));
            //propmpt to save
            int result = jFileChooser.showSaveDialog(mainFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String filename = jFileChooser.getSelectedFile().toString();
                    if (!filename.endsWith(".ft")) {
                        filename += ".ft";
                    }
                    File file = new File(filename);

                    saveToFile(file);
                    displayTree(currentArbreFamille);
                    editStatus("File saved to: " + (file.getAbsolutePath()));
                } catch (Exception j) {
                    showErrorDialog(j);
                    editStatus("Error: "+ j.getMessage());
                }
            }
        }
    }

    /**
     * action invoked when the user selects a node from the tree
     */
    private class treeSelectorAction implements TreeSelectionListener {

        public void valueChanged(TreeSelectionEvent event) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) arbre.getLastSelectedPathComponent();

            //no selection
            if (node == null) {
                return;
            }

            //if the selection is a familymember object
            Object nodeInfo = node.getUserObject();
            if (nodeInfo instanceof Personne) {
                //display details
                displayMemberInfo((Personne) nodeInfo);
                editStatus("Display details for: " + ((Personne) nodeInfo));
            }
        }
    }

    /**
     * Saves the object to a file by using serialization 
     * @param file the file to save to
     */
    private void saveToFile(File file) {
        // save the object to file
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            //set the output streams 
            fos = new FileOutputStream(file);
            out = new ObjectOutputStream(fos);
            //write the object to the file
            out.writeObject(this.currentArbreFamille);

            out.close();
            currentFile = file;
        } catch (Exception ex) {
            throw new IllegalArgumentException("File could not be saved");
        }
    }

    /**
     * Opens a file and load the data to the existing variables
     * @param file the file to open
     */
    private void openFile(File file) {
        // read the object from file
        FileInputStream fis = null;
        ObjectInputStream in = null;
        ArbreFamille ft = null;
        try {
            //set the input streams 
            fis = new FileInputStream(file);
            in = new ObjectInputStream(fis);

            //try to assign the object
            ft = (ArbreFamille) in.readObject();
            in.close();

            currentArbreFamille.setRoot(ft.getRoot());
            currentFile = file;
            arbre = new JTree();
        } catch (Exception ex) {
            throw new IllegalArgumentException("File could not be read.");
        }

    }

    /**
     * Displays a specified member details 
     * @param member the member details to display
     */
    private void displayMemberInfo(Personne member) {
        arbre.setEnabled(true);
        
//reset the info panel
        infoPanel.removeAll();
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        // Create the gridbag layout for the components 
        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel container = new JPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        infoPanel.add(container, gbc);
        
        //set another layout for the details 
        GroupLayout layout = new GroupLayout(container);
        container.setLayout(layout);
        //dynamic gaps 
        layout.setAutoCreateGaps(true);

        //form components possibly split these into seperate functions 
        JLabel memberInfoLabel = new JLabel("Person Info: ");
        memberInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JLabel nameLabel = new JLabel("Name");
        JLabel nameTextField = new JLabel(member.getNom(), 10);
        JLabel lastnameLabel = new JLabel("Surname");
        JLabel lastnameTextField = new JLabel(member.getPrenom(), 10);
       /* JLabel maidennameLabel = new JLabel("Maiden Name");
        JLabel maidennameTextField = new JLabel();
        if (member.has(FamilyMember.Attribute.MAIDENNAME)) {
            maidennameTextField.setText(member.getMaidenName());
        } else {
            maidennameTextField.setText("-");
        }*/

        JLabel genderLabel = new JLabel("Gender");
        JLabel genderComboBox = new JLabel(member.getSexe().toString());
        
        //display the life description as a text area but style it as a label.
        //handles long text inputs this way
        /*JLabel lifeDescriptionLabel = new JLabel("Life Description");
        JTextArea lifeDescriptionTextArea = new JTextArea(5, 20);
        lifeDescriptionTextArea.setText(member.getLifeDescription());
        lifeDescriptionTextArea.setWrapStyleWord(true);
        lifeDescriptionTextArea.setLineWrap(true);
        lifeDescriptionTextArea.setOpaque(false);
        lifeDescriptionTextArea.setEditable(false);
        lifeDescriptionTextArea.setFocusable(false);
        lifeDescriptionTextArea.setBackground(UIManager.getColor("Label.background"));
        lifeDescriptionTextArea.setFont(UIManager.getFont("Label.font"));
        lifeDescriptionTextArea.setBorder(UIManager.getBorder("Label.border"));
        JScrollPane lifeDescriptionScrollPane1 = new JScrollPane(lifeDescriptionTextArea);
        lifeDescriptionScrollPane1.setBorder(UIManager.getBorder("Label.border"));
*/
//        JLabel lifeDescriptionTextArea = new JLabel( "<html>" + member.getLifeDescription()+ "</html>", 10);

        
        JLabel personneinfo = new JLabel("informations supplementaires: ");
        personneinfo.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JLabel dateNaissanceLabel = new JLabel("date de naissance:");
        JLabel dateNaissanceTextField = new JLabel(member.getDateNaissance(), 10);
        JLabel dateDecesLabel = new JLabel("date deces:");
        JLabel dateDecesTextField = new JLabel(member.getDateDeces(), 10);
        
        /*JLabel addressInfoLabel = new JLabel("Address Info: ");
        addressInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JLabel streetNoLabel = new JLabel("Street Number:");
        JLabel streetNoTextField = new JLabel(member.getAddress().getStreetNumber(), 10);
        JLabel streetNameLabel = new JLabel("Street Name:");
        JLabel streetNameTextField = new JLabel(member.getAddress().getStreetName(), 10);
        JLabel suburbLabel = new JLabel("Suburb");
        JLabel suburbTextField = new JLabel(member.getAddress().getSuburb(), 10);
        JLabel postcodeLabel = new JLabel("Postcode");
        JLabel postcodeTextField = new JLabel(member.getAddress().getPostCode() + "", 10);
*/
        JLabel relativeInfoLabel = new JLabel("Relative Info: ");
        relativeInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));

        JLabel fatherLabel = new JLabel("Father");
        JLabel fatherTextField = new JLabel();
        if (member.has(Personne.Attribute.PERE)) {
            fatherTextField.setText(member.getPere().toString());
        } else {
            fatherTextField.setText("No father on record");
        }
        JLabel motherLabel = new JLabel("Mother");
        JLabel motherTextField = new JLabel();
        if (member.has(Personne.Attribute.MERE)) {
            motherTextField.setText(member.getMere().toString());
        } else {
            motherTextField.setText("No mother on record");
        }
        JLabel spouseLabel = new JLabel("Spouse");
        JLabel spouseTextField = new JLabel();
        if (member.has(Personne.Attribute.SPOUSE)) {
            spouseTextField.setText(member.getSpouse().toString());
        } else {
            spouseTextField.setText("No spouse on record");
        }
        JLabel childrenLabel = new JLabel("Children");
        String children = "<html>";
        if (member.has(Personne.Attribute.ENFANT)) {
            for (Personne child : member.getEnfants()) {
                children += child.toString() + "<br>";
            }
            children += "</html>";
        } else {
            children = "No children on record";
        }
        JLabel childrenTextField = new JLabel(children);

        JLabel grandChildrenLabel = new JLabel("Grand Children");
        String grandChildren = "<html>";
        if (member.has(Personne.Attribute.ENFANT)) {
            for (Personne child : member.getEnfants()) {
                if (child.has(Personne.Attribute.ENFANT)) {
                    for (Personne grandChild : child.getEnfants()) {
                        grandChildren += grandChild.toString() + "<br>";
                    }
                }

            }
            grandChildren += "</html>";
        } else {
            grandChildren = "No grand children on record";
        }
        JLabel grandChildrenTextField = new JLabel(grandChildren);
        //

        // Allign all the components using the group layout notation
        //horizontl alignment 
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(memberInfoLabel)
                        .addComponent(nameLabel)
                        .addComponent(lastnameLabel)
                        //.addComponent(maidennameLabel)
                        .addComponent(genderLabel)
                        //.addComponent(lifeDescriptionLabel)
                        .addComponent(memberInfoLabel)
                        .addComponent(dateNaissanceLabel)
                        .addComponent(dateDecesLabel)
                        //.addComponent(suburbLabel)
                        //.addComponent(postcodeLabel)
                        .addComponent(relativeInfoLabel)
                        .addComponent(fatherLabel)
                        .addComponent(motherLabel)
                        .addComponent(spouseLabel)
                        .addComponent(childrenLabel)
                        .addComponent(grandChildrenLabel)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nameTextField)
                        .addComponent(lastnameTextField)
                        //.addComponent(maidennameTextField)
                        //.addComponent(lifeDescriptionScrollPane1)
                        .addComponent(genderComboBox)
                        .addComponent(memberInfoLabel)
                        .addComponent(dateNaissanceTextField)
                        .addComponent(dateDecesTextField)
                        //.addComponent(suburbTextField)
                        //.addComponent(postcodeTextField)
                        .addComponent(fatherTextField)
                        .addComponent(motherTextField)
                        .addComponent(spouseTextField)
                        .addComponent(childrenTextField)
                        .addComponent(grandChildrenTextField)
                )
        );

        // verticle alignmnet 
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(memberInfoLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel)
                        .addComponent(nameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lastnameLabel)
                        .addComponent(lastnameTextField))
                /*.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(maidennameLabel)
                        .addComponent(maidennameTextField))*/
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(genderLabel)
                        .addComponent(genderComboBox))
                /*.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lifeDescriptionLabel)
                        .addComponent(lifeDescriptionScrollPane1))*/
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(memberInfoLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(dateNaissanceLabel)
                        .addComponent(dateNaissanceTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(dateDecesLabel)
                        .addComponent(dateDecesTextField))
                /*.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(suburbLabel)
                        .addComponent(suburbTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(postcodeLabel)
                        .addComponent(postcodeTextField))*/
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(relativeInfoLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(fatherLabel)
                        .addComponent(fatherTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(motherLabel)
                        .addComponent(motherTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(spouseLabel)
                        .addComponent(spouseTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(childrenLabel)
                        .addComponent(childrenTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(grandChildrenLabel)
                        .addComponent(grandChildrenTextField))
        );

        JButton editMember = new JButton("Edit Details");
        editMember.addActionListener(new editMemberAction(member));
        JButton addRelative = new JButton("Add Relative");
        addRelative.addActionListener(new addRelativeAction(member));

        JPanel btncontainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btncontainer.add(editMember);
        btncontainer.add(addRelative);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        infoPanel.add(btncontainer, gbc);
        infoPanel.validate();
        infoPanel.repaint();
    }


    /**
     * Displays the edit member form
     * @param member he member to edit
     */
    private void displayEditMemberInfo(Personne member) {

        arbre.setEnabled(false);
        
        //reset the info panel
        infoPanel.removeAll();
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        // Create the layout
        JPanel info = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        infoPanel.add(info, gbc);
        GroupLayout layout = new GroupLayout(info);
        info.setLayout(layout);
        layout.setAutoCreateGaps(true);

        // Create the components to put in the form
        JLabel memberInfoLabel = new JLabel("Person Info: ");
        memberInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JLabel nameLabel = new JLabel("Name");
        JTextField nameTextField = new JTextField(member.getNom(), 10);
        JLabel lastnameLabel = new JLabel("Surname");
        JTextField lastnameTextField = new JTextField(member.getPrenom(), 10);
        /*JLabel maidennameLabel = new JLabel("Maiden Name");
        JTextField maidennameTextField = new JTextField(member.getMaidenName(), 10);
        if (member.getGender() != FamilyMember.Gender.FEMALE) {
            maidennameTextField.setEditable(false);
        }*/
        JLabel genderLabel = new JLabel("Gender");
        //gender combobox
        DefaultComboBoxModel<Personne.Sexe> genderList = new DefaultComboBoxModel<>();
        genderList.addElement(Personne.Sexe.FEMME);
        genderList.addElement(Personne.Sexe.HOMME);
        JComboBox<Personne.Sexe> genderComboBox = new JComboBox<>(genderList);
        genderComboBox.setSelectedItem(member.getSexe());
        //no edits allowed, see documentation
        genderComboBox.setEnabled(false);

        JLabel lifeDescriptionLabel = new JLabel("Life Description");
        /*JTextArea lifeDescriptionTextArea = new JTextArea(member.getLifeDescription(), 10, 10);
        lifeDescriptionTextArea.setLineWrap(true);
        lifeDescriptionTextArea.setWrapStyleWord(true);
        JScrollPane lifeDescriptionScrollPane1 = new JScrollPane(lifeDescriptionTextArea);
        */
        JLabel personneinfo = new JLabel("informations supplementaires: ");
        personneinfo.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JLabel dateNaissanceLabel = new JLabel("date de naissance:");
        JTextField dateNaissanceTextField = new JTextField(member.getDateNaissance(), 10);
        JLabel dateDecesLabel = new JLabel("date deces:");
        JTextField dateDecesTextField = new JTextField(member.getDateDeces(), 10);
        
        /*JLabel addressInfoLabel = new JLabel("Address Info: ");
        addressInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JLabel streetNoLabel = new JLabel("Street Number:");
        JTextField streetNoTextField = new JTextField(member.getAddress().getStreetNumber(), 10);
        JLabel streetNameLabel = new JLabel("Street Name:");
        JTextField streetNameTextField = new JTextField(member.getAddress().getStreetName(), 10);
        JLabel suburbLabel = new JLabel("Suburb");
        JTextField suburbTextField = new JTextField(member.getAddress().getSuburb(), 10);
        JLabel postcodeLabel = new JLabel("Postcode");
        JTextField postcodeTextField = new JTextField(member.getAddress().getPostCode() + "", 10);
*/
        // horizontal alignment
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(memberInfoLabel)
                        .addComponent(nameLabel)
                        .addComponent(lastnameLabel)
                        //.addComponent(maidennameLabel)
                        .addComponent(genderLabel)
                        .addComponent(lifeDescriptionLabel)
                        .addComponent(personneinfo)
                        .addComponent(dateNaissanceLabel)
                        .addComponent(dateDecesLabel)
                        //.addComponent(suburbLabel)
                       // .addComponent(postcodeLabel)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nameTextField)
                        .addComponent(lastnameTextField)
                        //.addComponent(maidennameTextField)
                        //.addComponent(lifeDescriptionScrollPane1)
                        .addComponent(genderComboBox)
                        .addComponent(personneinfo)
                        .addComponent(dateNaissanceTextField)
                        .addComponent(dateDecesTextField)
                       // .addComponent(suburbTextField)
                        //.addComponent(postcodeTextField)
                )
        );

        // vertical alignment
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(memberInfoLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel)
                        .addComponent(nameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lastnameLabel)
                        .addComponent(lastnameTextField))
                /*.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(maidennameLabel)
                        .addComponent(maidennameTextField))*/
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(genderLabel)
                        .addComponent(genderComboBox))
                /*.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lifeDescriptionLabel)
                        .addComponent(lifeDescriptionScrollPane1))*/
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(personneinfo))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(dateNaissanceLabel)
                        .addComponent(dateNaissanceTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(dateDecesLabel)
                        .addComponent(dateDecesTextField))
                /*.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(suburbLabel)
                        .addComponent(suburbTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(postcodeLabel)
                        .addComponent(postcodeTextField))*/
        );
        JButton saveMember = new JButton("Save Details");
        //this anonymous actionlistner has access to all the above fields making it easy to use without passing as parameters.
        saveMember.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //try to save the details
                    member.setNom(nameTextField.getText().trim());
                    member.setPrenom(lastnameTextField.getText().trim());
                   // member.setMaidenName(maidennameTextField.getText().trim());
                    //member.setLifeDescription(lifeDescriptionTextArea.getText().trim());
                    member.setSexe((Personne.Sexe) genderComboBox.getSelectedItem());
                    
                    member.setDateNaissance(dateDecesTextField.getText().trim());
                    member.setDateDeces(dateDecesTextField.getText().trim());
                   // member.getAddress().setSuburb(suburbTextField.getText().trim());
                   // member.getAddress().setPostCode(postcodeTextField.getText().trim());
                    displayTree(currentArbreFamille);
                    editStatus("Member "+member.toString()+" added");
                } catch (Exception d) {
                    //any errors such as incorrect names, etc will show up here informing the user
                    showErrorDialog(d);
                }
            }
        });
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new cancelEditMemberAction(member));
        
        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT));
        container.setOpaque(false);
        container.add(saveMember);
        container.add(cancel);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        infoPanel.add(container, gbc);

        infoPanel.validate();
        infoPanel.repaint();
    
    }

    /**
     * display the add relative form for a member 
     * @param member the member to add a relative
     */
    private void displayAddRelativeInfo(Personne member) {
        arbre.setEnabled(false);
        
        //reset the info panel
        infoPanel.removeAll();
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel info = new JPanel();
        // if thr tree is empty add a root person otherwise add any relative 
        JLabel memberInfoLabel = new JLabel("Add new root person", SwingConstants.LEFT);
        if (member != null) {
            memberInfoLabel.setText("Add Relative for " + member.toString());
        }

        memberInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));

//            infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
//        headPanel.setBorder(new EmptyBorder(10,10,10,10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        infoPanel.add(memberInfoLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        infoPanel.add(info, gbc);
        // Create the layout
        GroupLayout layout = new GroupLayout(info);
        info.setLayout(layout);
        layout.setAutoCreateGaps(true);

        // Create the components to put in the form
        JLabel relativeTypeLabel = new JLabel("Relative Type");
        DefaultComboBoxModel<Personne.RelativeType> relativeTypeList = new DefaultComboBoxModel<>();

        relativeTypeList.addElement(Personne.RelativeType.MERE);
        relativeTypeList.addElement(Personne.RelativeType.PERE);
        relativeTypeList.addElement(Personne.RelativeType.SPOUSE);
        relativeTypeList.addElement(Personne.RelativeType.ENFANT);
        JComboBox<Personne.RelativeType> relativeTypeComboBox = new JComboBox<>(relativeTypeList);
        
        //if empty tree, no relative type selection
        if (member == null) {

            relativeTypeComboBox.removeAllItems();
            relativeTypeComboBox.setEnabled(false);

        }

        JLabel nameLabel = new JLabel("Name");
        JTextField nameTextField = new JTextField("Jane", 10);
        JLabel lastnameLabel = new JLabel("Surname");
        JTextField lastnameTextField = new JTextField("Doe", 10);

       // JLabel maidennameLabel = new JLabel("Maiden Name");
       // JTextField maidennameTextField = new JTextField(10);

        JLabel genderLabel = new JLabel("Gender");
        DefaultComboBoxModel<Personne.Sexe> genderList = new DefaultComboBoxModel<>();
        genderList.addElement(Personne.Sexe.FEMME);
        genderList.addElement(Personne.Sexe.HOMME);
        JComboBox<Personne.Sexe> genderComboBox = new JComboBox<>(genderList);

        /*JLabel lifeDescriptionLabel = new JLabel("Life Description");
        JTextArea lifeDescriptionTextArea = new JTextArea(10, 10);
        lifeDescriptionTextArea.setLineWrap(true);
        lifeDescriptionTextArea.setWrapStyleWord(true);
        JScrollPane lifeDescriptionScrollPane1 = new JScrollPane(lifeDescriptionTextArea);
*/
        JLabel personneinfo = new JLabel("informations supplementaires: ");
        personneinfo.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JLabel dateNaissanceLabel = new JLabel("date de naissance:");
        JTextField dateNaissanceTextField = new JTextField("dd/mm/yyyy", 10);
        JLabel dateDecesLabel = new JLabel("date deces:");
        JTextField dateDecesTextField = new JTextField("df", 10);
        
        /*JLabel addressInfoLabel = new JLabel("Address Info:");
        addressInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JLabel streetNoLabel = new JLabel("Street Number:");
        JTextField streetNoTextField = new JTextField("42", 10);
        JLabel streetNameLabel = new JLabel("Street Name:");
        JTextField streetNameTextField = new JTextField("Wallaby Way", 10);
        JLabel suburbLabel = new JLabel("Suburb");
        JTextField suburbTextField = new JTextField("Sydney", 10);
        JLabel postcodeLabel = new JLabel("Postcode");
        JTextField postcodeTextField = new JTextField("6062", 10);
*/
        
        //anonymous actionlistner has access to all the above varaiables making it easier to use
        JButton saveMember = new JButton("Add Member");
        saveMember.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    //create the objects 
                    /*Address newAddress = new Address(streetNoTextField.getText(),
                            streetNameTextField.getText(),
                            suburbTextField.getText(),
                            postcodeTextField.getText());*/
                    Personne newMember = new Personne(
                            nameTextField.getText(),
                            lastnameTextField.getText(),
                            (Personne.Sexe) genderComboBox.getSelectedItem(),
                            dateNaissanceLabel.getText(),
                            dateDecesTextField.getText());
                    //newMember.setMaidenName(maidennameTextField.getText());
                    //if no root
                    if (member == null) {
                        currentArbreFamille.setRoot(newMember);
                        editStatus("Root member added");
                    } else {
                        //add the relative 
                        member.addRelative((Personne.RelativeType) relativeTypeComboBox.getSelectedItem(), newMember);
                        editStatus("New member added");
                    }
                    displayTree(currentArbreFamille);

                } catch (Exception d) {
                    showErrorDialog(d);
                }
            }
        });
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new cancelEditMemberAction(member));

        //just a way to make some QoL changes to the user experience.
        //Set the appropriate contraints based on the relative type selecgtion
        relativeTypeComboBox.addActionListener(new ActionListener() {//add actionlistner to listen for change
            @Override
            public void actionPerformed(ActionEvent e) {

                switch ((Personne.RelativeType) relativeTypeComboBox.getSelectedItem()) {//check for a match
                    case PERE:
                        genderComboBox.setSelectedItem(Personne.Sexe.HOMME);
                        //maidennameTextField.setEditable(false);
                        lastnameTextField.setText(member.getPrenom());
                        break;
                    case MERE:
                        genderComboBox.setSelectedItem(Personne.Sexe.FEMME);
                       // maidennameTextField.setEditable(true);
                        lastnameTextField.setText(member.getPrenom());
                        break;
                    case SPOUSE:
                        lastnameTextField.setText(member.getPrenom());
                        //maidennameTextField.setEditable(true);
//                        maidennameTextField.setEditable(true);
                        break;
                    case ENFANT:
                        lastnameTextField.setText(member.getPrenom());
                        //maidennameTextField.setEditable(true);
//                        maidennameTextField.setEditable(false);
                        break;
                }
            }
        });
        // horizontal alignment 
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(relativeTypeLabel)
                        .addComponent(nameLabel)
                        .addComponent(lastnameLabel)
                       // .addComponent(maidennameLabel)
                        .addComponent(genderLabel)
                        //.addComponent(lifeDescriptionLabel)
                        .addComponent(personneinfo)
                        .addComponent(dateNaissanceLabel)
                        .addComponent(dateDecesLabel)
                        //.addComponent(suburbLabel)
                        //.addComponent(postcodeLabel)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nameTextField)
                        .addComponent(relativeTypeComboBox)
                        .addComponent(lastnameTextField)
                        //.addComponent(maidennameTextField)
                        //.addComponent(lifeDescriptionScrollPane1)
                        .addComponent(genderComboBox)
                        .addComponent(personneinfo)
                        .addComponent(dateNaissanceTextField)
                        .addComponent(dateDecesTextField)
                        //.addComponent(suburbTextField)
                        //.addComponent(postcodeTextField)
                )
        );

        // verticle alignment 
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(relativeTypeLabel)
                        .addComponent(relativeTypeComboBox))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel)
                        .addComponent(nameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lastnameLabel)
                        .addComponent(lastnameTextField))
                /*.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(maidennameLabel)
                        .addComponent(maidennameTextField))*/
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(genderLabel)
                        .addComponent(genderComboBox))
                /*.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lifeDescriptionLabel)
                        .addComponent(lifeDescriptionScrollPane1))*/
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(personneinfo))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(dateNaissanceLabel)
                        .addComponent(dateNaissanceTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(dateDecesLabel)
                        .addComponent(dateDecesTextField))
                /*.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(suburbLabel)
                        .addComponent(suburbTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(postcodeLabel)
                        .addComponent(postcodeTextField))*/
        );

        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT));
        container.setOpaque(false);
        container.add(saveMember);
        container.add(cancel);

        gbc.gridx = 0;
        gbc.gridy = 2;
        infoPanel.add(container, gbc);
        infoPanel.validate();
        infoPanel.repaint();
    }

    /**
     * Recursive method to populate the jtree object for each family member of the root person
     * @param top the node to populate
     * @param root the member to get the detils from
     */
    private void createTree(DefaultMutableTreeNode top, Personne root) {
        DefaultMutableTreeNode parents = null;
        DefaultMutableTreeNode father = null;
        DefaultMutableTreeNode mother = null;
        DefaultMutableTreeNode spouse = null;
        DefaultMutableTreeNode children = null;
        DefaultMutableTreeNode child = null;
        DefaultMutableTreeNode spouseNode = null;

        if (root.has(Personne.Attribute.PARENTS) && root == currentArbreFamille.getRoot()) {
            parents = new DefaultMutableTreeNode("Parents");
            //add parent node
            top.add(parents);

            if (root.has(Personne.Attribute.PERE)) {
                father = new DefaultMutableTreeNode(root.getPere());
                //add father to parent node
                parents.add(father);
            }

            if (root.has(Personne.Attribute.MERE)) {
                mother = new DefaultMutableTreeNode(root.getMere());
                //add mother to parent node
                parents.add(mother);
            }
        }

//        }
        if (root.has(Personne.Attribute.SPOUSE)) {
            spouseNode = new DefaultMutableTreeNode("Spouse");
            spouse = new DefaultMutableTreeNode(root.getSpouse());
            //add spouse node
            spouseNode.add(spouse);
            //add the spouse node 
            top.add(spouseNode);
        }

        if (root.has(Personne.Attribute.ENFANT)) {
            children = new DefaultMutableTreeNode("Children");
            for (Personne f : root.getEnfants()) {
                child = new DefaultMutableTreeNode(f);
                //for each child, call create tree to populate their subtree nodes 
                createTree(child, f);
                //ad that child to the top node 
                children.add(child);
            }
            top.add(children);
        }

    }

    /**
     * shows a error dialog containing an error message from a exception 
     * @param e the exception to get the message from
     */
    private void showErrorDialog(Exception e) {
        JOptionPane.showMessageDialog(mainFrame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

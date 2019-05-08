

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
 */
public class Application {

    /**
     * Crcreation de l'interface et initialisation des variables
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
     * Appels des differentes fonctions pour avoir les panels 
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
     * Initialisation du header
     */
    private void initHeaderPanel() {
        
        JLabel headerLabel = new JLabel("Bienvenue a l'application d'arbre généalogique ", JLabel.LEFT);
        headerLabel.setFont(new Font("SansSerif", Font.PLAIN, 28));

        /*JButton open = new JButton("Load Tree");
        open.addActionListener(new openAction());*/

        JButton create = new JButton("nouveau arbre");
        create.addActionListener(new createTreeAction());

        /*JButton saveTree = new JButton("Save Tree");
        saveTree.addActionListener(new saveAction());*/

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
        //container.add(open);
        //container.add(saveTree);
        container.add(create);

        gbc.gridx = 0;
        gbc.gridy = 1;
        headPanel.add(container, gbc);
        
        mainFrame.add(headPanel, BorderLayout.NORTH);
    }

  
    private void initControlPanel() {
        controlPanel = new JPanel();
        
        //used to show white background from mainFrame
        controlPanel.setOpaque(false);
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        
        mainFrame.add(controlPanel, BorderLayout.CENTER);
    }

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
        
        /*JMenuItem openAction = new JMenuItem("Open");
        fileMenu.add(openAction);
        openAction.addActionListener(new openAction());*/
        
        fileMenu.addSeparator();

        /*JMenuItem saveAction = new JMenuItem("Save");
        fileMenu.add(saveAction);
        saveAction.addActionListener(new saveAction());*/
        
        /*JMenuItem saveAsAction = new JMenuItem("Save As");
        fileMenu.add(saveAsAction);
        saveAsAction.addActionListener(new saveAsAction());*/
        
        
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


    private void editStatus(String status) {
        statusLabel.setText(status);
    }

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


    

    private boolean checkUserContinue() {
        if (currentArbreFamille.hasRoot()) {
            int dialogResult = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you wish to continue? Any unsaved changes will be lost", "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
            return dialogResult == JOptionPane.YES_OPTION;
        }
        return true;
    }

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
        

        
        JLabel personneinfo = new JLabel("informations supplementaires: ");
        personneinfo.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JLabel dateNaissanceLabel = new JLabel("date de naissance:");
        JLabel dateNaissanceTextField = new JLabel(member.getDateNaissance(), 10);
        JLabel dateDecesLabel = new JLabel("date deces:");
        JLabel dateDecesTextField = new JLabel(member.getDateDeces(), 10);
        

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

    private void displayAddRelativeInfo(Personne member) {
        arbre.setEnabled(false);
        
        //reset the info panel
        infoPanel.removeAll();
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel info = new JPanel();
        // if thr tree is empty add a root person otherwise add any relative 
        JLabel memberInfoLabel = new JLabel("Ajouter la personne racine", SwingConstants.LEFT);
        if (member != null) {
            memberInfoLabel.setText("Ajouter une personne dans l'arbre de  " + member.toString());
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
                        editStatus("Racine ajoutée");
                    } else {
                        //add the relative 
                        member.addRelative((Personne.RelativeType) relativeTypeComboBox.getSelectedItem(), newMember);
                        editStatus("nouveau membre ajouté ");
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
                        
                        lastnameTextField.setText(member.getPrenom());
                        break;
                    case MERE:
                        genderComboBox.setSelectedItem(Personne.Sexe.FEMME);
                       
                        lastnameTextField.setText(member.getPrenom());
                        break;
                    case SPOUSE:
                        lastnameTextField.setText(member.getPrenom());
                       
                        break;
                    case ENFANT:
                        lastnameTextField.setText(member.getPrenom());

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
                      
                        .addComponent(genderLabel)
                        
                        .addComponent(personneinfo)
                        .addComponent(dateNaissanceLabel)
                        .addComponent(dateDecesLabel)

                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nameTextField)
                        .addComponent(relativeTypeComboBox)
                        .addComponent(lastnameTextField)

                        .addComponent(genderComboBox)
                        .addComponent(personneinfo)
                        .addComponent(dateNaissanceTextField)
                        .addComponent(dateDecesTextField)

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

                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(genderLabel)
                        .addComponent(genderComboBox))

                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(personneinfo))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(dateNaissanceLabel)
                        .addComponent(dateNaissanceTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(dateDecesLabel)
                        .addComponent(dateDecesTextField))

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

   
    private void showErrorDialog(Exception e) {
        JOptionPane.showMessageDialog(mainFrame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

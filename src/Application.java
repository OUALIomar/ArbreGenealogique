import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

// Bibliothèque que j'ai voulu utiliser pour une éventuelle sauvegarde des arbres crées, mais je n'y suis pas arrivée.
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.*;


public class Application {

     //creation de l'interface et initialisation des variables
 

    private JFrame mainFrame;
    private JPanel controlPanel;
    private JPanel infoPanel;
    private final JLabel statusLabel = new JLabel("Programme chargé");
    private File currentFile;
    private JTree arbre;

    private ArbreFamille currentArbreFamille;

    
	 public Application() {

	        

	        /*currentArbreFamille*/ 

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

        

        //met en place la barre de menu

        initMenuBar();



        //met en place le panneau en-tête "header"

        initHeaderPanel();



        //met en place la section controleur (partie où les données sont affichées)

        initControlPanel();



        //met en place la barre d'état

        initStatusBar();



        //afficher l'arbre

        displayTree(currentArbreFamille);



        //méthode qui vérifie si l'utilisateur veut continuer utilisant la fonction checkUserContinue

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

        

        JLabel headerLabel = new JLabel("Bienvenue dans l'application Arbre Généalogique ", JLabel.LEFT);
        headerLabel.setFont(new Font("SansSerif", Font.PLAIN, 28));



        /*JButton open = new JButton("Charger un arbre");

        open.addActionListener(new openAction());*/



        JButton create = new JButton("nouvel Arbre");
        create.addActionListener(new createTreeAction());



        /*JButton saveTree = new JButton("Enregistrer l'arbre");

        saveTree.addActionListener(new saveAction());*/



        JPanel headPanel = new JPanel();
        headPanel.setLayout(new GridBagLayout());
        headPanel.setOpaque(false);
        headPanel.setBorder(new EmptyBorder(0,10,10,10));

        

        //on utilise une grille pour positionner les éléments plus facilement

        //grid bag constraints sert à positionner l'élément à l'intérieur de la grille

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        headPanel.add(headerLabel, gbc);


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

        

        //Mettre l'arrière plan en blanc

        controlPanel.setOpaque(false);
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        mainFrame.add(controlPanel, BorderLayout.CENTER);

    }



    private void initMenuBar() {

        JMenuBar menuBar;
        menuBar = new JMenuBar();
        mainFrame.setJMenuBar(menuBar);
        
        
        JMenu fileMenu = new JMenu("Fichier");

//        JMenu editMenu = new JMenu("Modifier");

        menuBar.add(fileMenu);

//        menuBar.add(editMenu);



        JMenuItem newAction = new JMenuItem("Nouveau");
        fileMenu.add(newAction);
        newAction.addActionListener(new createTreeAction());

        

        /*JMenuItem openAction = new JMenuItem("Ouvrir");
        fileMenu.add(openAction);
        openAction.addActionListener(new openAction());*/

        
        fileMenu.addSeparator();


        /*JMenuItem saveAction = new JMenuItem("Enregistrer");
        fileMenu.add(saveAction);
        saveAction.addActionListener(new saveAction());*/

        

        /*JMenuItem saveAsAction = new JMenuItem("Ennregistrer sous");
        fileMenu.add(saveAsAction);
        saveAsAction.addActionListener(new saveAsAction());*/

        
        JMenuItem exitAction = new JMenuItem("Sortir");
        fileMenu.addSeparator();
        fileMenu.add(exitAction);


        //actionlistner classe

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

        

        //met en place la taille de la fenêtre

        statusPanel.setPreferredSize(new Dimension(mainFrame.getWidth(), 18));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));

        

        //aligne le texte à ggauche

        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);

        //Le message du statut sera aaffiché

        statusPanel.add(statusLabel);

    }





    private void editStatus(String status) {

        statusLabel.setText(status);

    }



    private class addRelativeAction implements ActionListener {



        private Personne member;

        //On appelle Actionlistener sur tout proche

        //On met chaque membre que l'on veut modifier en paramètres

        //Pour pouvoir obtenir le paramètre et effectuer l'action souhaitée

        public addRelativeAction(Personne member) {

            this.member = member;

        }



        @Override

        public void actionPerformed(ActionEvent e) {

            //afficher "ajouter proche" à ce membre là

            displayAddRelativeInfo(member);

        }

    }





    private class editMemberAction implements ActionListener {



        private Personne member;

        //même commentaire que pour addRelativeAction

        public editMemberAction(Personne member) {

            this.member = member;

        }



        @Override

        public void actionPerformed(ActionEvent e) {

            //Afficher les infos modifiées

            displayEditMemberInfo(member);

        }

    }



    private class createTreeAction implements ActionListener {



        @Override

        public void actionPerformed(ActionEvent e) {

            

            if (checkUserContinue()) {

                //Vérifie si l'arbre n'est pas enregistré et réinitialise les variables

            	currentArbreFamille = new ArbreFamille();
                currentFile = null;

                //Afficher le nouvel arbre

                displayTree(currentArbreFamille);
                editStatus("Arbre vide crée");

            }



        }

    }







    private boolean checkUserContinue() {

        if (currentArbreFamille.hasRoot()) {

            int dialogResult = JOptionPane.showConfirmDialog(mainFrame, "Êtes-vous sûr(e) de vouloir continuer ? Tout changement non sauvegardé sera perdu.", "Attention", JOptionPane.YES_NO_CANCEL_OPTION);
            return dialogResult == JOptionPane.YES_OPTION;

        }

        return true;

    }



    private void displayTree(ArbreFamille familyArbre) {



        //Création du noeud

        DefaultMutableTreeNode main = new DefaultMutableTreeNode("Main");

        // On utilise treePath pour savoir qui est la dernière personne sélectionnée

        //On l'utilise quand on veut faire ou annuler une action

        TreePath lastSelectedNode = null;

        

        //mutable tree node met les objets comme noeud

        DefaultMutableTreeNode top;

        

        //Message lorsqu'il n'y a encore aucun arbre

        if (!familyArbre.hasRoot()) {

            top = new DefaultMutableTreeNode("Aucun arbre saisi.");



        } else {

            //Ajouter la personne "racine"

            top = new DefaultMutableTreeNode(familyArbre.getRoot());

            // Appeler la méthode récursive qui remplit l'Arbre avec touts les infos


            createTree(top, familyArbre.getRoot());

            //Lorsuqe l'utilisateur appuie sur un membre, on choisi le dernier path

            lastSelectedNode = arbre.getSelectionPath();



        }

        //Crée l'arbre en autorisant une sélection à la fois

        arbre = new JTree(main);
        main.add(top);
        arbre.setRootVisible(false);
        arbre.setShowsRootHandles(true);
        arbre.setEnabled(true);
        arbre.expandPath(new TreePath(main.getPath()));
        arbre.getSelectionModel().addTreeSelectionListener(new treeSelectorAction());
        arbre.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        arbre.setBorder(new EmptyBorder(0, 10, 0, 10));



        // Etendre à tous les noeuds

        for (int i = 0; i < arbre.getRowCount(); i++) {

        	arbre.expandRow(i);

        }

        

        //Assombri le texte dans l'arbre, et autorise la sélection d'un membre de la famille

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



        //On ajoute sur l'arbre un scroller pour l'utilisateur 

        JScrollPane treeScrollPane = new JScrollPane(arbre);
        treeScrollPane.setPreferredSize(new Dimension(250, 0));



        //On crée le panneau d'info pour l'afficher dans le control pannel

        infoPanel = new JPanel();
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));



        JLabel promptInfo;
        JButton addNewRoot = new JButton("Ajouter la première personne de la famille");
        addNewRoot.addActionListener(new addRelativeAction(null));
        if (!familyArbre.hasRoot()) {

            promptInfo = new JLabel("<html>Construire un Arbre Généalogique.</html>");
            infoPanel.add(addNewRoot);

        } else {

            promptInfo = new JLabel("<html>Sélectionner un membre de la famille pour voir les informations le concerant.</html>");

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

        

        //On fait défiler l'arbre jusqu'au dernier Path sélectionné.

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
            editStatus("Action annulée");

        }

    }



    private class treeSelectorAction implements TreeSelectionListener {



        public void valueChanged(TreeSelectionEvent event) {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) arbre.getLastSelectedPathComponent();



            //Pas de sélection

            if (node == null) {

                return;

            }



            //si l'objet sélectionné est un membre de la famille

            Object nodeInfo = node.getUserObject();
            if (nodeInfo instanceof Personne) {

                //Afficher détails

                displayMemberInfo((Personne) nodeInfo);
                editStatus("Afficher les détails pour: " + ((Personne) nodeInfo));

            }

        }

    }



    private void displayMemberInfo(Personne member) {

        arbre.setEnabled(true);

        

        //réinitialise le panneau d'info

        infoPanel.removeAll();
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        //On crée le gridbag layout our les composants

        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();


        JPanel container = new JPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;


        infoPanel.add(container, gbc);

        //met en place un autre layout pour les détails

        GroupLayout layout = new GroupLayout(container);
        container.setLayout(layout);

        layout.setAutoCreateGaps(true);



        JLabel memberInfoLabel = new JLabel("Information sur la personne : ");
        memberInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JLabel nameLabel = new JLabel("Nom de Famille");
        JLabel nameTextField = new JLabel(member.getNom(), 10);
        JLabel lastnameLabel = new JLabel("Prénom");
        JLabel lastnameTextField = new JLabel(member.getPrenom(), 10);

        // J'ai voulu mettre en place une saisie de nom de jeune fille.
        
        /* JLabel maidennameLabel = new JLabel("Nom de jeune fille");
        JLabel maidennameTextField = new JLabel();
        if (member.has(FamilyMember.Attribute.MAIDENNAME)) {
            maidennameTextField.setText(member.getMaidenName());
        } else {
            maidennameTextField.setText("-");
        }*/



        JLabel genderLabel = new JLabel("Sexe");
        JLabel genderComboBox = new JLabel(member.getSexe().toString());

        
        JLabel personneinfo = new JLabel("informations supplementaires: ");
        personneinfo.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JLabel dateNaissanceLabel = new JLabel("date de naissance:");
        JLabel dateNaissanceTextField = new JLabel(member.getDateNaissance(), 10);
        JLabel dateDecesLabel = new JLabel("date de décès:");
        JLabel dateDecesTextField = new JLabel(member.getDateDeces(), 10);


        JLabel relativeInfoLabel = new JLabel("Information sur le proche ");
        relativeInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));


        JLabel fatherLabel = new JLabel("Père");
        JLabel fatherTextField = new JLabel();

        if (member.has(Personne.Attribute.PERE)) {

            fatherTextField.setText(member.getPere().toString());

        } else {

            fatherTextField.setText("pas de père saisi");

        }

        JLabel motherLabel = new JLabel("Mère");
        JLabel motherTextField = new JLabel();

        if (member.has(Personne.Attribute.MERE)) {

            motherTextField.setText(member.getMere().toString());

        } else {

            motherTextField.setText("Pas de mère saisie");

        }

        JLabel spouseLabel = new JLabel("Epoux");
        JLabel spouseTextField = new JLabel();

        if (member.has(Personne.Attribute.SPOUSE)) {

            spouseTextField.setText(member.getSpouse().toString());

        } else {

            spouseTextField.setText("Pas d'époux saisi");

        }

        JLabel childrenLabel = new JLabel("Enfants");
        String children = "<html>";

        if (member.has(Personne.Attribute.ENFANT)) {

            for (Personne child : member.getEnfants()) {

                children += child.toString() + "<br>";

            }

            children += "</html>";

        } else {

            children = "Pas d'enfants saisis";

        }

        JLabel childrenTextField = new JLabel(children);



        JLabel grandChildrenLabel = new JLabel("Petits enfants");
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

            grandChildren = "Pas de petits enfants saisis";

        }

        JLabel grandChildrenTextField = new JLabel(grandChildren);

        //



        // on utilise le group layout location pour aligner tous les composants 

        // alignemment horizontal

        layout.setHorizontalGroup(layout.createSequentialGroup()

                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(memberInfoLabel)
                        .addComponent(nameLabel)
                        .addComponent(lastnameLabel)
                        //.addComponent(maidennameLabel) toujours l'initiative de mettre le nom de jeune fille

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



        // alignement vertical

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



        JButton editMember = new JButton("Modification des détails");// Le bouton donne une page blanche NE FFoNCTIONNE PAS
        editMember.addActionListener(new editMemberAction(member));

        JButton addRelative = new JButton("Ajouter un membre de la famille");
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

        

        // réinitialise le panel info

        infoPanel.removeAll();
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // créer la dispostion

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



        JLabel memberInfoLabel = new JLabel("Information sur la personne ");
        memberInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JLabel nameLabel = new JLabel("Nom");
        JTextField nameTextField = new JTextField(member.getNom(), 10);
        JLabel lastnameLabel = new JLabel("Prénom");
        JTextField lastnameTextField = new JTextField(member.getPrenom(), 10);

        
        // Même commeentaire sur nom de jeune fille précédent
        /*JLabel maidennameLabel = new JLabel("Maiden Name"); 

        JTextField maidennameTextField = new JTextField(member.getMaidenName(), 10);

        if (member.getGender() != FamilyMember.Gender.FEMALE) {

            maidennameTextField.setEditable(false);

        }*/

        JLabel genderLabel = new JLabel("Sexe");

        //Combo box menu déroulant pour le sexe 

        DefaultComboBoxModel<Personne.Sexe> genderList = new DefaultComboBoxModel<>();
        genderList.addElement(Personne.Sexe.FEMME);
        genderList.addElement(Personne.Sexe.HOMME);
        JComboBox<Personne.Sexe> genderComboBox = new JComboBox<>(genderList);
        genderComboBox.setSelectedItem(member.getSexe());

        //Pas de changement sur le sexe

        genderComboBox.setEnabled(false);

        
        JLabel lifeDescriptionLabel = new JLabel("Détails sur la Personne");

        /*JTextArea lifeDescriptionTextArea = new JTextArea(member.getLifeDescription(), 10, 10); 

        lifeDescriptionTextArea.setLineWrap(true);

        lifeDescriptionTextArea.setWrapStyleWord(true);

        JScrollPane lifeDescriptionScrollPane1 = new JScrollPane(lifeDescriptionTextArea);

        */

        JLabel personneinfo = new JLabel("informations supplementaires: ");
        personneinfo.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JLabel dateNaissanceLabel = new JLabel("date de naissance:");
        JTextField dateNaissanceTextField = new JTextField(member.getDateNaissance(), 10);
        JLabel dateDecesLabel = new JLabel("date de Décès:");
        JTextField dateDecesTextField = new JTextField(member.getDateDeces(), 10);

        

        /*JLabel addressInfoLabel = new JLabel("Adresse: ");

        addressInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));

        JLabel streetNoLabel = new JLabel("Numéro de rue :");

        JTextField streetNoTextField = new JTextField(member.getAddress().getStreetNumber(), 10);

        JLabel streetNameLabel = new JLabel("Rue :");

        JTextField streetNameTextField = new JTextField(member.getAddress().getStreetName(), 10);

        JLabel suburbLabel = new JLabel("Ville");

        JTextField suburbTextField = new JTextField(member.getAddress().getSuburb(), 10);

        JLabel postcodeLabel = new JLabel("Code postale");

        JTextField postcodeTextField = new JTextField(member.getAddress().getPostCode() + "", 10);

*/

        // alignement horizontal

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



        // alignement vertical

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

        JButton saveMember = new JButton("Enregistrer les modifications");

        saveMember.addActionListener(new ActionListener() {

            @Override

            public void actionPerformed(ActionEvent e) {

                try {

                    //on essaie de sauvegarder le modifications

                    member.setNom(nameTextField.getText().trim());
                    member.setPrenom(lastnameTextField.getText().trim());


                    member.setSexe((Personne.Sexe) genderComboBox.getSelectedItem());

                    member.setDateNaissance(dateNaissanceTextField.getText().trim());
                    member.setDateDeces(dateDecesTextField.getText().trim());


                    displayTree(currentArbreFamille);
                    editStatus("Membre "+member.toString()+" ajouté");

                } catch (Exception d) {

                    //Toute erreur (ex: nom incorrect) serait normalement afichée ici

                    showErrorDialog(d);

                }

            }

        });

        JButton cancel = new JButton("Annuler");
        cancel.addActionListener(new cancelEditMemberAction(member));

        
        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT));
        container.setOpaque(false);
        container.add(saveMember);
        container.add(cancel);


        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        infoPanel.add(container, gbc);


        //infoPanel.validate();
        infoPanel.repaint();

    
    }



    private void displayAddRelativeInfo(Personne member) {

        arbre.setEnabled(false);        

        //réinitialiser info panel

        infoPanel.removeAll();
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));


        JPanel info = new JPanel();

        // Si l'arbre est vide ajouter la personne "racine" 

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

        // Creer le layout

        GroupLayout layout = new GroupLayout(info);
        info.setLayout(layout);
        layout.setAutoCreateGaps(true);


        JLabel relativeTypeLabel = new JLabel("Type de lien familial");
        DefaultComboBoxModel<Personne.RelativeType> relativeTypeList = new DefaultComboBoxModel<>();


        relativeTypeList.addElement(Personne.RelativeType.MERE);
        relativeTypeList.addElement(Personne.RelativeType.PERE);
        relativeTypeList.addElement(Personne.RelativeType.SPOUSE);
        relativeTypeList.addElement(Personne.RelativeType.ENFANT);
        JComboBox<Personne.RelativeType> relativeTypeComboBox = new JComboBox<>(relativeTypeList);

        

        //Si l'arbre est vide, nous ne sélectionnons pas le type de lien familial

        if (member == null) {

            relativeTypeComboBox.removeAllItems();
            relativeTypeComboBox.setEnabled(false);

        }


        JLabel nameLabel = new JLabel("Nom");
        JTextField nameTextField = new JTextField("Entrer un nom de Famille", 10);
        JLabel lastnameLabel = new JLabel("Prénom");
        JTextField lastnameTextField = new JTextField("Entrer un prénom", 10);


       // JLabel maidennameLabel = new JLabel("Maiden Name");

       // JTextField maidennameTextField = new JTextField(10);



        JLabel genderLabel = new JLabel("Sexe");
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
        JTextField dateNaissanceTextField = new JTextField("jj/mm/aaaa", 10);
        JLabel dateDecesLabel = new JLabel("date de décès:");
        JTextField dateDecesTextField = new JTextField("jj/mm/aaaa", 10);

        

        JButton saveMember = new JButton("Ajouter un membre");

        saveMember.addActionListener(new ActionListener() {

            @Override

            public void actionPerformed(ActionEvent e) {


                try {


                    Personne newMember = new Personne(
                            nameTextField.getText(),
                            lastnameTextField.getText(),
                            (Personne.Sexe) genderComboBox.getSelectedItem(),
                            dateNaissanceTextField.getText(),
                            dateDecesTextField.getText());

                    //newMember.setMaidenName(maidennameTextField.getText());

                    //Si pas de Personne racine

                    if (member == null) {

                        currentArbreFamille.setRoot(newMember);
                        editStatus("Racine ajoutée");

                    } else {

                        //ajouter les proches 

                        member.addRelative((Personne.RelativeType) relativeTypeComboBox.getSelectedItem(), newMember);
                        editStatus("nouveau membre ajouté ");

                    }

                    displayTree(currentArbreFamille);



                } catch (Exception d) {

                    showErrorDialog(d);

                }

            }

        });

        JButton cancel = new JButton("Annuler");
        cancel.addActionListener(new cancelEditMemberAction(member));


        //On sélectione les restrictions appropriées pour choisir le type de proche séletionner

        relativeTypeComboBox.addActionListener(new ActionListener() {//on ajoute un Actionlistner pour avoir l'information sur un changement

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

        //alignement horizontal

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



        // alignement vertical

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

            //ajouter un noeud parent

            top.add(parents);


            if (root.has(Personne.Attribute.PERE)) {
                father = new DefaultMutableTreeNode(root.getPere());
                //ajouter le père au noeud parent
                parents.add(father);

            }


            if (root.has(Personne.Attribute.MERE)) {

                mother = new DefaultMutableTreeNode(root.getMere());
                //ajouter la mère au noeud parent
                parents.add(mother);

            }

        }



//        }

        if (root.has(Personne.Attribute.SPOUSE)) {

            spouseNode = new DefaultMutableTreeNode("Epoux");
            spouse = new DefaultMutableTreeNode(root.getSpouse());
            //ajout le noued époux

            spouseNode.add(spouse);

            top.add(spouseNode);

        }


        if (root.has(Personne.Attribute.ENFANT)) {
            children = new DefaultMutableTreeNode("Enfants");
            for (Personne f : root.getEnfants()) {
                child = new DefaultMutableTreeNode(f);
                //pour chaque enfant appeler creatTree pour remplir les noueds sous-arbres
                createTree(child, f);
                
                //Ajouter cet enfant au plus haut noeud

                children.add(child);

            }

            top.add(children);

        }

    }

    private void showErrorDialog(Exception e) {

        JOptionPane.showMessageDialog(mainFrame, "Erreur: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);

    }

}
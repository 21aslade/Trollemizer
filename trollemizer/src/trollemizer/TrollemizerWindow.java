package trollemizer;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;

import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class TrollemizerWindow {

	public File chosenFile;
	private JFrame frmTrollemizer;
	private JFileChooser loadChooser;
	private JFileChooser exportChooser;
	private final JPanel panel_2 = new JPanel();
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TrollemizerWindow window = new TrollemizerWindow();
					window.frmTrollemizer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TrollemizerWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		frmTrollemizer = new JFrame();
		frmTrollemizer.setTitle("Trollemizer");
		frmTrollemizer.setBounds(100, 100, 450, 300);
		frmTrollemizer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		loadChooser = new JFileChooser();
		exportChooser = new JFileChooser();
		
		JPanel panel = new JPanel();
		frmTrollemizer.getContentPane().add(panel, BorderLayout.SOUTH);
		
		JLabel labelExportStatus = new JLabel("");
		panel.add(labelExportStatus);
		
		JPanel panel_1 = new JPanel();
		frmTrollemizer.getContentPane().add(panel_1, BorderLayout.WEST);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{127, 145, 0};
		gbl_panel_1.rowHeights = new int[]{23, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JCheckBox checkBoxRandomizeItemGfx = new JCheckBox("Randomize Item GFX");
		GridBagConstraints gbc_checkBoxRandomizeItemGfx = new GridBagConstraints();
		gbc_checkBoxRandomizeItemGfx.anchor = GridBagConstraints.NORTHWEST;
		gbc_checkBoxRandomizeItemGfx.insets = new Insets(0, 0, 5, 5);
		gbc_checkBoxRandomizeItemGfx.gridx = 0;
		gbc_checkBoxRandomizeItemGfx.gridy = 0;
		panel_1.add(checkBoxRandomizeItemGfx, gbc_checkBoxRandomizeItemGfx);
		
		JCheckBox checkBoxRandomizeBombTimers = new JCheckBox("Randomize Bomb Timers");
		GridBagConstraints gbc_checkBoxRandomizeBombTimers = new GridBagConstraints();
		gbc_checkBoxRandomizeBombTimers.insets = new Insets(0, 0, 5, 0);
		gbc_checkBoxRandomizeBombTimers.anchor = GridBagConstraints.NORTHWEST;
		gbc_checkBoxRandomizeBombTimers.gridx = 1;
		gbc_checkBoxRandomizeBombTimers.gridy = 0;
		panel_1.add(checkBoxRandomizeBombTimers, gbc_checkBoxRandomizeBombTimers);
		
		JCheckBox checkBoxRandomizeTrPegs = new JCheckBox("Randomize TR Pegs");
		GridBagConstraints gbc_checkBoxRandomizeTrPegs = new GridBagConstraints();
		gbc_checkBoxRandomizeTrPegs.insets = new Insets(0, 0, 0, 5);
		gbc_checkBoxRandomizeTrPegs.gridx = 0;
		gbc_checkBoxRandomizeTrPegs.gridy = 1;
		panel_1.add(checkBoxRandomizeTrPegs, gbc_checkBoxRandomizeTrPegs);
		
		JCheckBox checkBoxSuperLonkMode = new JCheckBox("Super Lonk Mode");
		GridBagConstraints gbc_checkBoxSuperLonkMode = new GridBagConstraints();
		gbc_checkBoxSuperLonkMode.gridx = 1;
		gbc_checkBoxSuperLonkMode.gridy = 1;
		panel_1.add(checkBoxSuperLonkMode, gbc_checkBoxSuperLonkMode);
		
		frmTrollemizer.getContentPane().add(panel_2, BorderLayout.NORTH);
		
		JButton btnLoadRom = new JButton("Load Rom");
		panel_2.add(btnLoadRom);
		
		JLabel labelRomName = new JLabel("");
		panel_2.add(labelRomName);
		btnLoadRom.addActionListener( new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	FileNameExtensionFilter filter = new FileNameExtensionFilter("ALttPR Rom File", "sfc");
		    	loadChooser.setFileFilter(filter);
		    	int successState = loadChooser.showOpenDialog(frmTrollemizer);
		    	
		    	if (successState == JFileChooser.APPROVE_OPTION) {
		    		chosenFile = loadChooser.getSelectedFile();
		    		String labelName = chosenFile.getName();
		    		if (chosenFile.getName().length() > 50) {
		    			labelName = labelName.substring(0,50) + "...";
		    		}
		    		labelRomName.setText(labelName);
		    	}
		    }
		});
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new EmptyBorder(0, 5, 0, 0));
		frmTrollemizer.getContentPane().add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new GridLayout(0, 1, 0, 0));
		
		/*JLabel lblRomDetails = new JLabel("Rom Details:");
		panel_3.add(lblRomDetails);
		
		JLabel lblType = new JLabel("(Type)");
		panel_3.add(lblType);
		
		JLabel lblLogic = new JLabel("(Logic)");
		panel_3.add(lblLogic);
		
		JLabel lblGoal = new JLabel("(Goal)");
		panel_3.add(lblGoal);
		
		JLabel lblState = new JLabel("(State)");
		panel_3.add(lblState);
		
		JLabel lblSwords = new JLabel("(Swords)");
		panel_3.add(lblSwords);
		
		JLabel lblDifficulty = new JLabel("(Difficulty)");
		panel_3.add(lblDifficulty);
		
		JLabel lblVariation = new JLabel("(Variation)");
		panel_3.add(lblVariation);*/
		
		
		// Set up the button to export the rom, invoking Trollemizer.trollemize using the current settings
		JButton btnExportRom = new JButton("Export Rom");
		panel.add(btnExportRom);
		btnExportRom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chosenFile != null) {
			    	exportChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
			    	
			    	int successState = exportChooser.showOpenDialog(frmTrollemizer);
			    	
			    	if (successState == JFileChooser.APPROVE_OPTION) {
			    		int seed = new Random().nextInt();
			    		File outputDirectory = exportChooser.getSelectedFile();
			    		String outputName = "Trollemized_" + seed + " -";
			    		boolean outputSpoiler = false;
			    		boolean randomizeTRPegs = checkBoxRandomizeTrPegs.isSelected();
			    		boolean randomizeItemGFX = checkBoxRandomizeItemGfx.isSelected();
			    		boolean randomizeBombTimers = checkBoxRandomizeBombTimers.isSelected();
			    		boolean superLonkMode = checkBoxSuperLonkMode.isSelected();
			    		
			    		Trollemizer trollemizer = new Trollemizer();
						trollemizer.randomize(seed, chosenFile, outputDirectory, outputName, outputSpoiler, randomizeTRPegs, randomizeItemGFX, randomizeBombTimers, superLonkMode);
			    	}
				}
			}
		});
	}
}


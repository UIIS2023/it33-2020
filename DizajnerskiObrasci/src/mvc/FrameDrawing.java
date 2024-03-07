package mvc;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import drawing.FrmDrawing;
import drawing.PnlDrawing;
import observer.IObserver;

import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JList;

public class FrameDrawing extends JFrame implements IObserver{

	private JPanel contentPane;
	private View pnlDrawing = new View();
	private Controller controller;

	private JToggleButton tglbtnPoint = new JToggleButton("Point");
	private JToggleButton tglbtnLine = new JToggleButton("Line");
	private JToggleButton tglbtnRectangle = new JToggleButton("Rectangle");
	private JToggleButton tglbtnCircle = new JToggleButton("Circle");
	private JToggleButton tglbtnDonut = new JToggleButton("Donut");
	private JToggleButton tglbtnHexagonAdapter = new JToggleButton("Hexagon");

	private JToggleButton tglbtnSelect = new JToggleButton("Select");
	private Color outlineColor = Color.BLACK;
	private Color innerColor = Color.WHITE;
	private JButton btnInnerColor ;
	private JButton btnOutlineColor ;
	private JButton btnModify;
	private JButton btnDelete;
	private final JPanel pnlEast = new JPanel();
	private final JButton btnToFront = new JButton("To Front");
	private final JButton btnToBack = new JButton("To Back");
	private final JButton btnBringToBack = new JButton("Bring To Back");
	private final JButton btnBringToFront = new JButton("Bring To Front");
	private final JButton btnUndo = new JButton("Undo");
	private final JButton btnRedo = new JButton("Redo");
	private final JPanel pnlWest = new JPanel();
	private final JScrollPane scrollPane = new JScrollPane();
	private DefaultListModel<String> logList = new DefaultListModel<String>(); 
	private final JList list = new JList(logList);
	private final JButton btnSaveSer = new JButton("Save Serialization");
	private final JButton btnSaveTxt = new JButton("Save Text");
	private final JButton btnLoad = new JButton("Load");
	private final JButton btnStepByStep = new JButton("Step By Step");
	
	

	 
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrameDrawing frame = new FrameDrawing();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	
	}
	
	/**
	 * Create the frame.
	 */
	
	public FrameDrawing() {
		
		setTitle("IT 33/2020 Lazar Vidic ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(350, 100, 950, 550);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		pnlDrawing.setBackground(Color.WHITE);
		contentPane.add(pnlDrawing, BorderLayout.CENTER);
		
		
		pnlDrawing.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				controller.thisMouseClicked(me);
			}
		});

		JPanel pnlNorth = new JPanel();
		pnlNorth.setBackground(Color.BLUE);
		contentPane.add(pnlNorth, BorderLayout.NORTH);

		pnlNorth.add(tglbtnPoint);
		pnlNorth.add(tglbtnLine);
		pnlNorth.add(tglbtnCircle);
		pnlNorth.add(tglbtnDonut);
		pnlNorth.add(tglbtnRectangle);
		pnlNorth.add(tglbtnHexagonAdapter);
		
		ButtonGroup btnGroup = new ButtonGroup();

		btnGroup.add(tglbtnPoint);
		btnGroup.add(tglbtnLine);
		btnGroup.add(tglbtnCircle);
		btnGroup.add(tglbtnDonut);
		btnGroup.add(tglbtnRectangle);
		btnGroup.add(tglbtnHexagonAdapter);
		
		
		btnGroup.add(tglbtnSelect);

		JPanel pnlSouth = new JPanel();
		contentPane.add(pnlSouth, BorderLayout.SOUTH);
		pnlSouth.setBackground(Color.BLUE);
		pnlSouth.add(tglbtnSelect);
		
		btnModify = new JButton("Modify");
		btnModify.setEnabled(false);
		btnModify.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				controller.modify();
			}
		});
		pnlSouth.add(btnModify);
		
		
		btnDelete = new JButton("Delete");
		btnDelete.setEnabled(false);
		pnlSouth.add(btnDelete);
		btnDelete.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				controller.delete();
			}
		});
		
		btnOutlineColor = new JButton("Outer Color");
		btnOutlineColor.setBackground(outlineColor);
		pnlSouth.add(btnOutlineColor);
		btnOutlineColor.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				 outlineColor = JColorChooser.showDialog(null, "Choose outline color",
						btnOutlineColor.getBackground());
				if (outlineColor != null)
					btnOutlineColor.setBackground(outlineColor);	
			}
		});

		btnInnerColor = new JButton("Inner Color");
		btnInnerColor.setBackground(innerColor);
		pnlSouth.add(btnInnerColor);
		
		pnlSouth.add(btnStepByStep);
		btnStepByStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.stepByStep();
			}
		});
		
		
		pnlEast.setBackground(new Color(0, 0, 255));
		
		contentPane.add(pnlEast, BorderLayout.EAST);
		//pnlEast.setLayout(new BoxLayout(pnlEast, BoxLayout.Y_AXIS));
		GridLayout gl_pnlEast = new GridLayout(0, 1);
		gl_pnlEast.setVgap(20);
		gl_pnlEast.setHgap(5);
		pnlEast.setLayout(gl_pnlEast);
		
		//btnToBack.setSize(150, 40);
		pnlEast.add(Box.createRigidArea(new Dimension(10,10)));
		pnlEast.add(btnToBack);
		//pnlEast.add(Box.createRigidArea(new Dimension(10,10)));
		btnToBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.moveShapeToBack();
			}
		});
		
		pnlEast.add(btnToFront);
		//pnlEast.add(Box.createRigidArea(new Dimension(10,10)));
		btnToFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.moveShapeToFront();
				
			}
		});	

		
		pnlEast.add(btnBringToBack);
		//pnlEast.add(Box.createRigidArea(new Dimension(10,10)));
		btnBringToBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.moveShapeBringToBack();
			}
		});
			
		
		pnlEast.add(btnBringToFront);
		//pnlEast.add(Box.createRigidArea(new Dimension(10,-50)));
		btnBringToFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.moveShapeBringToFront();
				
			}
		});
		
		
		pnlEast.add(btnUndo);
		btnUndo.setEnabled(false);
		//pnlEast.add(Box.createRigidArea(new Dimension(10,10)));
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.undoCommand();
			}
		});
		
		pnlEast.add(btnRedo);
		btnRedo.setEnabled(false);
		btnRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.redoCommand();
			}
		});
		
		pnlEast.add(btnSaveSer);
		
		btnSaveSer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int response = fileChooser.showSaveDialog(pnlDrawing);
				if(JFileChooser.APPROVE_OPTION == response)
				{
					controller.saveSerialization(fileChooser.getSelectedFile().getPath());
				}
			}
		});
		
		pnlEast.add(btnSaveTxt);
		
		btnSaveTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int response = fileChooser.showSaveDialog(pnlDrawing);
				if(JFileChooser.APPROVE_OPTION == response) {
					controller.saveText(fileChooser.getSelectedFile().getPath());
				}
			}
		});
		
		
		
		
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int response = fileChooser.showOpenDialog(pnlDrawing);
				if(JFileChooser.APPROVE_OPTION == response)
				{
					controller.loadFile(fileChooser.getSelectedFile().getPath());
				}
			}
		});
		
		pnlEast.add(btnLoad);
		
		pnlEast.add(Box.createRigidArea(new Dimension(10,10)));
		pnlWest.setBackground(new Color(0, 0, 255));
		
		contentPane.add(pnlWest, BorderLayout.WEST);
		pnlWest.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		pnlWest.add(scrollPane);
		
		scrollPane.setPreferredSize(new Dimension(200,420));
		scrollPane.setViewportView(list);
		
		logList.addElement("LOGGER : ");
		
		btnInnerColor.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				 innerColor = JColorChooser.showDialog(null, "Choose inner color",
						btnInnerColor.getBackground());
				if (innerColor != null)
					btnInnerColor.setBackground(innerColor);	
			}
		});
		
		


	}



	public DefaultListModel<String> getLogList() {
		return logList;
	}

	public void setLogList(DefaultListModel<String> logList) {
		this.logList = logList;
	}

	public View getPnlDrawing() {
		return pnlDrawing;
	}

	public void setPnlDrawing(View pnlDrawing) {
		this.pnlDrawing = pnlDrawing;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public JToggleButton getTglbtnPoint() {
		return tglbtnPoint;
	}

	public void setTglbtnPoint(JToggleButton tglbtnPoint) {
		this.tglbtnPoint = tglbtnPoint;
	}

	public JToggleButton getTglbtnLine() {
		return tglbtnLine;
	}

	public void setTglbtnLine(JToggleButton tglbtnLine) {
		this.tglbtnLine = tglbtnLine;
	}

	public JToggleButton getTglbtnRectangle() {
		return tglbtnRectangle;
	}

	public void setTglbtnRectangle(JToggleButton tglbtnRectangle) {
		this.tglbtnRectangle = tglbtnRectangle;
	}

	public JToggleButton getTglbtnCircle() {
		return tglbtnCircle;
	}

	public void setTglbtnCircle(JToggleButton tglbtnCircle) {
		this.tglbtnCircle = tglbtnCircle;
	}

	public JToggleButton getTglbtnDonut() {
		return tglbtnDonut;
	}

	public void setTglbtnDonut(JToggleButton tglbtnDonut) {
		this.tglbtnDonut = tglbtnDonut;
	}
	
	public JToggleButton getTglbtnHexagonAdapter() {
		return tglbtnHexagonAdapter;
	}

	public void setTglbtnHexagonAdapter(JToggleButton tglbtnHexagonAdapter) {
		this.tglbtnHexagonAdapter = tglbtnHexagonAdapter;
	}

	public JToggleButton getTglbtnSelect() {
		return tglbtnSelect;
	}

	public void setTglbtnSelect(JToggleButton tglbtnSelect) {
		this.tglbtnSelect = tglbtnSelect;
	}

	public Color getOutlineColor() {
		return outlineColor;
	}

	public void setOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
		btnOutlineColor.setBackground(outlineColor);
	}

	public Color getInnerColor() {
		return innerColor;
	}

	public void setInnerColor(Color innerColor) {
		this.innerColor = innerColor;
		btnInnerColor.setBackground(innerColor); 
	}

	@Override
	public void update(boolean modify, boolean delete,boolean undo,boolean redo) {
		btnModify.setEnabled(modify);
		btnDelete.setEnabled(delete);
		btnUndo.setEnabled(undo);
		btnRedo.setEnabled(redo);
		
	}

	
	
	
}



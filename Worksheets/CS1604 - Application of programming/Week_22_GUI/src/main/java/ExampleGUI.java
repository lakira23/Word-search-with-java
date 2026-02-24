//All these imports are needed for the different parts of the interface
//Read up in the Javadocs on each of these classes...
import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Random;

//Our class needs to inherit functionality from 'JPanel' and 'JActionListener'
public class ExampleGUI extends JPanel implements ActionListener 
{
	//All forms need a unique ID - ignore this!!!
    private static final long serialVersionUID = 1862962349L;
    //We are going to have three buttons
	private JButton startbutton,endbutton,exitbutton;
	//For this version of the program we are going to have two labels
	private JLabel labelonevalue,labelonecaption;
	//The timer is for updating the labels at a regular interval 
	private Timer timer;
	//Constructor for our form
    public ExampleGUI() 
    {
    	//Set up the three buttons
    	
    	//Caption for first button
        startbutton = new JButton("Start Logging");
        //Centre the text vertically
        startbutton.setVerticalTextPosition(AbstractButton.CENTER);
        //Centre the text horizontally
        startbutton.setHorizontalTextPosition(AbstractButton.CENTER);
        //Short cut key of 'S'
        startbutton.setMnemonic(KeyEvent.VK_S);
        //Create the event/action 'Start' when clicked
        startbutton.setActionCommand("Start");

        //Caption for second button
        endbutton = new JButton("Stop Logging");
        //Centre the text vertically
        endbutton.setVerticalTextPosition(AbstractButton.CENTER);
        //Centre the text horizontally
        endbutton.setHorizontalTextPosition(AbstractButton.CENTER);
        //Short cut key of 'T'
        endbutton.setMnemonic(KeyEvent.VK_T);
        //Create the event/action 'Stop' when clicked
        endbutton.setActionCommand("Stop");
        //Initially the stop button is disabled
        endbutton.setEnabled(false);
        
    	//Caption for third button
        exitbutton = new JButton("Exit");
        //Centre the text vertically
        exitbutton.setVerticalTextPosition(AbstractButton.CENTER);
        //Centre the text horizontally
        exitbutton.setHorizontalTextPosition(AbstractButton.CENTER);
        //Short cut key of 'X'
        exitbutton.setMnemonic(KeyEvent.VK_X);
        //Create the event/action 'Exit' when clicked
        exitbutton.setActionCommand("Exit");
        
        //Listen for actions from the three buttons
        //The current instance of 'this' class will process the actions for the buttons 
        startbutton.addActionListener(this);
        endbutton.addActionListener(this);
        exitbutton.addActionListener(this);

        //This is the text that is displayed when we hover the mouse over the buttons
        startbutton.setToolTipText("Click this button to start logging...");
        endbutton.setToolTipText("Click this button to stop logging...");
        exitbutton.setToolTipText("This button is the only way you can exit this application...");
        
        //Create the labels
        labelonecaption = new JLabel("The time sponsored by Accurist is:");
        labelonevalue = new JLabel("There is never enough time...");
        //Only the contents label will have a border 
        labelonevalue.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        //Add the Components to the Form
        //If they are not added then they will not appear!
        add(startbutton);
        add(endbutton);
        add(exitbutton);
        add(labelonecaption);
        add(labelonevalue);
        
        //Create a timer every second (1000 m/s)
        //The current instance of 'this' class will process the actions for the timer
        timer = new Timer(1000, this);
        //Create the event/action 'Timer' every second
        timer.setActionCommand("Timer");
        //Start the timer straight away
        timer.setInitialDelay(0);
    }
    //The method must be implemented as we have inherited from 'JActionListener'
    //Every time a button is clicked or a timer event occurs this method is run
    //A total of four events are catered for here
    public void actionPerformed(ActionEvent e) 
    {
    	//Check for the 'Start' event
    	if (e.getActionCommand().equals("Start")) 
        {
    		//Log that something has happened
        	System.out.println("Start pressed");
        	//Disable the 'Start' button
            startbutton.setEnabled(false);
            //Enable the 'Stop' button
            endbutton.setEnabled(true);
            //Start the 'Timer'
            timer.start();
        }
    	//Check for the 'Stop' event
    	if (e.getActionCommand().equals("Stop"))
        {
    		//Log that something has happened
        	System.out.println("Stop pressed");
        	//Enable the 'Start' button
            startbutton.setEnabled(true);
            //Disable the 'Stop' button
            endbutton.setEnabled(false);
            //Stop the 'Timer'
            timer.stop();
            //Reset the text for the display label
            labelonevalue.setText("There is never enough time...");
        }
    	//Check for the 'Timer' event
    	if (e.getActionCommand().equals("Timer"))
        {
    		//Log that something has happened
        	System.out.println("Timer");
        	//Get the current time and date
        	Calendar date = Calendar.getInstance();
        	//Write it to the display label
        	labelonevalue.setText(date.getTime().toString());
        	//Redraw the window
        	repaint();
        }
    	//Test for the 'Exit' action
    	if (e.getActionCommand().equals("Exit"))
        {
    		//Log that something has happened
        	System.out.println("Exit");
        	//Stop the timer
        	timer.stop();
        	//Get the parent JFrame of this panel
        	JFrame parent = (JFrame)SwingUtilities.getWindowAncestor(this);
        	//Hide the Window
        	parent.setVisible(false);
        	//Get rid of the Window
        	parent.dispose();
        }    	
    }
    //Override the super class paint method
    @Override
    public void paint(Graphics g) 
    {
    	//Call the super class paint method
    	//This is needed to make sure all the components are drawn correctly
    	super.paint(g);
    	
    	//Create a colour for our rectangle
    	Color colour = Color.BLACK;
    	
    	//We randomly decide on a colour and a position
		Random rand = new Random();
		rand.setSeed(System.nanoTime());
		
		int r = rand.nextInt(3); //0,..,2
		int x = rand.nextInt(101); //0,..,100
		int y = rand.nextInt(101); //0,..,100
		
		//Three colours at the moment
    	if (r == 0) colour = Color.RED;
    	if (r == 1) colour = Color.GREEN;
    	if (r == 2) colour = Color.BLUE;
        
    	//Set the colour
    	g.setColor(colour);
    	//Draw a rectangle at a random position
    	g.fillRect(100+x,100+y,100,100);
    }
 }
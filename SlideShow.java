import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*; 

import java.applet.*;
import java.net.*;
import java.util.ArrayList;
import java.io.*;


public class SlideShow extends JFrame implements ActionListener, WindowListener, Runnable{

	Thread pictureThread; 
	boolean flag=true;
	Panel bottomPanel;
	MediaTracker mt;
	ArrayList<Image> p=new ArrayList<Image>();
	AudioClip b_n, recv,musicAudio;
	Toolkit tk;
	JButton auto,back,next,recover,exit;
	int count,count1;
    String[] musicList,pictureList;
	
	JSlider jSlider1;
	JTextField textField1;
	JLabel label1,label2;
	
	
	public SlideShow()
	{
	  super( "SlideShow（Java幻灯片播放器）" );
	  setBounds(280,60, 960, 540 );
	 
	  getContentPane().setLayout(new BorderLayout());
	  bottomPanel = new Panel();
	  bottomPanel.setBackground(new Color(230, 230, 250));
	  
	  pictureThread = new Thread(this);
	  count = 0;
	  count1 = 0;
	  mt=new MediaTracker(this);
	  URL musicUrl=SlideShow.class.getResource("/music");
	  URL pictureUrl=SlideShow.class.getResource("/pictures");
	  try {
		  
		  musicList= Filelist.fileList(new File(musicUrl.toURI()));
		  pictureList=Filelist.fileList(new File(pictureUrl.toURI()));
	  	} catch (Exception e) {
		// TODO: handle exception
	  }
	  
	  for (int i = 0; i <pictureList.length; i++) {
		  
		p.add(Toolkit.getDefaultToolkit().getImage(SlideShow.class.getResource("/pictures/" + pictureList[i])));
		mt.addImage(p.get(i), i);
	}
	  
	      
	  b_n = Applet.newAudioClip(SlideShow.class.getResource("/clicksound/back_next.au"));
	  recv = Applet.newAudioClip(SlideShow.class.getResource("/clicksound/recover.au"));
	  musicAudio=Applet.newAudioClip(SlideShow.class.getResource("/music/夜的钢琴曲.wav"));
	  
	  label2 = new JLabel("音乐");
	  label2.setFont(new Font("宋体", Font.PLAIN, 11));
	  bottomPanel.add(label2);
	  MusicJComboBox musicComboBox= new MusicJComboBox(musicList);
	  musicComboBox.setFont(new Font("宋体", Font.PLAIN, 11));
	  bottomPanel.add(musicComboBox);
	 		    
	  jSlider1=new JSlider();
	  jSlider1.setFont(new Font("宋体", Font.PLAIN, 10));
	  jSlider1.setValue(1);
	  jSlider1.setMaximum(10);
	  //设置是否在JSlider加上刻度
      jSlider1.setPaintTicks(true);
      //设置大刻度之间的距离
      jSlider1.setMajorTickSpacing(1);
      //设置与小刻度之间的距离
      jSlider1.setMinorTickSpacing(1);
      //设置是否数字标记，若设为true，则JSlider刻度上就会有数值出现
      jSlider1.setPaintLabels(true);     
      bottomPanel.add(jSlider1);
	  	  
	  jSlider1.addChangeListener
	  (
		  new ChangeListener()
		  { 
		  	  public void stateChanged(ChangeEvent e) 
			  { 
				  textField1.setText(Integer.toString(jSlider1.getValue())); 			
			  } 
		  }
	  ); 
	  
	  textField1 = new JTextField( 2 );
	  bottomPanel.add( textField1 );
      textField1.setText("1");
      textField1.addActionListener
	    (
	    	new ActionListener()
	    	{
	    		public void actionPerformed(ActionEvent event)
	    		{ 
	    			int num = 0;
				try{
					num = Integer.parseInt(textField1.getText());
					
					}
					catch(NumberFormatException yb)
					{
						textField1.setText("输入错误");
					}	   		    		
	 	    	}
	    	}
	    );	
      
      label1 = new JLabel("自动播放速度（秒/张）");
      label1.setFont( new Font("宋体", Font.PLAIN, 9) );   
      bottomPanel.add(label1);
      
      auto = new JButton("自动播放图片");
      auto.setFont(new Font("宋体", Font.PLAIN, 12));
      bottomPanel.add(auto);
      
      recover = new JButton("回到第一张");
      recover.setFont(new Font("宋体", Font.PLAIN, 12));
      bottomPanel.add(recover);
      
      back = new JButton("上一张");
      back.setFont(new Font("宋体", Font.PLAIN, 12));
      bottomPanel.add(back);
      
      next = new JButton("下一张");
      next.setFont(new Font("宋体", Font.PLAIN, 12));
      bottomPanel.add(next);
      
      exit = new JButton("退出");
      exit.setFont(new Font("宋体", Font.PLAIN, 12));
      bottomPanel.add(exit);
      setVisible( true );
       
       back.addActionListener(this);
       next.addActionListener(this);
       recover.addActionListener(this);
       auto.addActionListener(this);
       exit.addActionListener(this);
       getContentPane().add(bottomPanel, BorderLayout.SOUTH);
       addWindowListener(this);
       
	} 
	  //音乐的切换
	class MusicJComboBox extends JComboBox {
			
	   public MusicJComboBox(String[] items) {
			super(items);
				
			this.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					int choose = e.getStateChange();
					String musicItem = e.getItem().toString();
					if (choose == ItemEvent.SELECTED) {
					 	
							 	musicAudio.stop();
								musicAudio= Applet.newAudioClip(SlideShow.class.getResource("/music/"+ musicItem));
								musicAudio.loop();
						
						} else if (choose == ItemEvent.DESELECTED) {
							return;
						}
				}
			});
		}
	}

	
	public void paint(Graphics g)
	  {
		for (int i = 0; i < p.size(); i++) {
			if (count==i) {
				g.drawImage(p.get(i),0,0,960,540,this);
			}
		}	 
	  }

	  public void update(Graphics g)
	  {
	    paint(g);
	  }

	  public void actionPerformed(ActionEvent ae)
	  {
		
	    if(ae.getSource() == exit)
	    {
	      dispose();
	      setVisible(false);
	      System.exit(0);
	    }

	    if(ae.getSource() == next)
	    {
	      b_n.play();

	      if(count < p.size())
	      {
	    	  count++;
	    	  repaint();
	      }
	    }

	    if(ae.getSource() == back)
	    {
	      b_n.play();

	      if(count == 0)
	       count=0;

	      if(count > 0)
	      {
	    	  count--;
	    	  repaint();
	      }
	    }

	    if(ae.getSource() == recover)
	    {
	      recv.play();
	      pictureThread.suspend(); 
	      count=0;
	      repaint();
	    }

	    if(ae.getSource() == auto)
	    {
	      b_n.play();

	      if(count1 < 1)
	      {
	    	  pictureThread.start();
	    	  count1++;
	      }

	      else
	      {
	    	  pictureThread.resume(); //resumes the thread
	      }
	    }
	  }
	  public void run()
	  {
		
		 
	    while(flag){
			try
	     {
			for(int i=1;i>0;i++)
	      {
	    	 
	    	  pictureThread.sleep(1000*Integer.parseInt(textField1.getText()));
	    	  count++;

	    	  if(count >=p.size())
	    	  {
	    		  flag=false; 
	    		  count = 0;
	    	  }

	    	  repaint();
	      }
	     }

			catch(InterruptedException e)
			{
				return;
			}
		}
	    
	  }

	  //handling window events
	  public void windowClosing(WindowEvent we)
	  {
	    dispose();
	    setVisible(false);
	    System.exit(0);
	  }
	  
	  @Override
	public void windowOpened(WindowEvent e) {
		  musicAudio.loop();
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	public static void main( String args[] )
	{ 
	   SlideShow pictureSlideShow=new SlideShow();
	  pictureSlideShow.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
}


class Filelist {	
	public static String[] fileList(File f) {
		    String[] list;
	
			File[] files = f.listFiles();
			list=new String[files.length]; 
			for(int i = 0; i< files.length;i++){
				list[i] = files[i].getName();
		}
		return list;
	}
}


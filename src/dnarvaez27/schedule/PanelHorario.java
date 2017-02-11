package dnarvaez27.schedule;

import java.util.Calendar;

import javax.swing.*;
import javax.swing.border.Border;

import com.dnarvaez27.componentes.buttons.ButtonTip;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class PanelHorario extends JPanel
{
	public class PanelDia extends JPanel
	{
		private static final long serialVersionUID = 1605447317975878649L;
		
		private final Cursor DEFAULT = new Cursor( Cursor.DEFAULT_CURSOR );
		
		private int dia;
		
		private JLabel diaLabel;
		
		private final Cursor HAND = new Cursor( Cursor.HAND_CURSOR );
		
		private TaskButton[ ] horas;
		
		public PanelDia( int dia )
		{
			this.dia = dia;
			
			setLayout( new BorderLayout( ) );
			setBackground( bg );
			
			JPanel panelCentral = new JPanel( );
			panelCentral.setLayout( new GridLayout( CASILLAS, 1 ) );
			panelCentral.setBackground( null );
			
			diaLabel = new JLabel( diasLabel[ dia ] );
			diaLabel.setHorizontalAlignment( SwingConstants.CENTER );
			diaLabel.setForeground( labels );
			diaLabel.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
			
			horas = new TaskButton[ CASILLAS ];
			
			for( int i = 0; i < horas.length; i++ )
			{
				horas[ i ] = new TaskButton( /* String.valueOf( i ) */ );
				horas[ i ].setBorderPainted( true );
				horas[ i ].setBorder( BorderFactory.createLineBorder( lines ) );
				horas[ i ].setPreferredSize( new Dimension( 100, 20 ) );
				horas[ i ].setForeground( fg );
				horas[ i ].setBackground( null );
				horas[ i ].setCursor( DEFAULT );
				panelCentral.add( horas[ i ] );
			}
			
			add( diaLabel, BorderLayout.NORTH );
			add( panelCentral, BorderLayout.CENTER );
		}
		
		public void addTask( ITask iTask )
		{
			for( Calendar[ ] evento : iTask.getCalendarEvents( ) )
			{
				Calendar c0 = evento[ 0 ];
				if( c0.get( Calendar.DAY_OF_WEEK ) == ( dia + ( PanelHorario.this.showDomingo ? 1 : 0 ) ) )
				{
					double h0 = c0.get( Calendar.HOUR_OF_DAY );
					double m0 = c0.get( Calendar.MINUTE );
					h0 = ( ( m0 > 0 ) && ( m0 < 30 ) ) ? h0 + 0.5 : ( m0 >= 30 ) ? h0 + 1 : h0;
					
					Calendar c1 = evento[ 1 ];
					double h1 = c1.get( Calendar.HOUR_OF_DAY );
					double m1 = c1.get( Calendar.MINUTE );
					
					int y0 = posCasilla( h0 );
					int y1 = posCasilla( h1 );
					
					if( ( m0 <= 30 ) && ( m0 > 0 ) )
					{
						y0--;
					}
					else if( m0 > 30 )
					{
						y0--;
					}
					
					if( m1 == 0 )
					{
						y1 = y1 - 1;
					}
					else
					{
						y1 = ( ( m1 > 0 ) && ( m1 <= 30 ) ) ? y1 : y1 + 1;
					}
					
					int cont = 0;
					for( int i = y0; ( i <= y1 ) && ( i >= 0 ); i++ )
					{
						if( cont == 0 )
						{
							horas[ i ].setBorder( BorderFactory.createMatteBorder( 1, 1, 0, 1, lines ) );
							horas[ i ].setText( iTask.getName( ) );
						}
						else if( cont == ( y1 - y0 ) )
						{
							horas[ i ].setBorder( BorderFactory.createMatteBorder( 0, 1, 1, 1, lines ) );
						}
						else
						{
							horas[ i ].setBorder( BorderFactory.createMatteBorder( 0, 1, 0, 1, lines ) );
						}
						cont++;
						
						horas[ i ].setBackground( iTask.getColor( ) );
						horas[ i ].setForeground( isDark( iTask.getColor( ) ) ? Color.WHITE : Color.BLACK );
						
						String toolTip = iTask.getToolTip( );
						if( ( toolTip != null ) && ( !toolTip.trim( ).replace( " ", "" ).isEmpty( ) ) )
						{
							horas[ i ].setToolTipText( toolTip );
							horas[ i ].configurar( borderToolTip, Component.CENTER_ALIGNMENT );
							horas[ i ].cambiarColorTip( horas[ i ].getBackground( ), horas[ i ].getForeground( ) );
						}
						if( actionListener != null )
						{
							horas[ i ].addActionListener( actionListener );
						}
						if( mouseListener != null )
						{
							horas[ i ].addMouseListener( mouseListener );
						}
						horas[ i ].setiTask( iTask );
						horas[ i ].setCursor( HAND );
					}
				}
			}
		}
		
		private boolean isDark( Color color )
		{
			double darkness = 1 - ( ( 0.299 * color.getRed( ) ) + ( 0.587 * color.getGreen( ) ) + ( 0.114 * color.getBlue( ) ) ) / 255;
			if( darkness < 0.5 )
			{
				return false; // It's a light color
			}
			else
			{
				return true; // It's a dark color
			}
		}
		
		public void configurar( )
		{
			setBackground( bg );
			diaLabel.setForeground( labels );
			
			for( int i = 0; i < horas.length; i++ )
			{
				horas[ i ].setBorder( BorderFactory.createLineBorder( lines ) );
				horas[ i ].setForeground( fg );
				horas[ i ].configurar( borderToolTip, CENTER_ALIGNMENT );
			}
		}
		
		public void pintarInstance( int pos )
		{
			if( horas[ pos ].getCursor( ).equals( DEFAULT ) )
			{
				horas[ pos ].setBackground( lines.brighter( ) );
			}
			
			Calendar c = Calendar.getInstance( );
			int y = c.get( Calendar.DAY_OF_WEEK ) - ( showDomingo ? 0 : 1 );
			if( dia == y )
			{
				if( horas[ pos ].getCursor( ).equals( HAND ) )
				{
					Color color = horas[ pos ].getBackground( );
					horas[ pos ].setBackground( color.brighter( ) );
					horas[ pos ].cambiarColorTip( color, fg );
				}
			}
			
			// for( int i = 0; i < horas.length; i++ )
			// {
			// if( i != pos && horas[ i ].getCursor( ).equals( DEFAULT ))
			// {
			// horas[ i ].setBackground( bg );
			// }
			// }
		}
		
		public void removeTask( ITask iTask )
		{
			for( Calendar[ ] evento : iTask.getCalendarEvents( ) )
			{
				Calendar c0 = evento[ 0 ];
				double h0 = c0.get( Calendar.HOUR_OF_DAY );
				double m0 = c0.get( Calendar.MINUTE );
				h0 = ( ( m0 > 0 ) && ( m0 < 30 ) ) ? h0 + 0.5 : ( m0 >= 30 ) ? h0 + 1 : h0;
				
				Calendar c1 = evento[ 1 ];
				double h1 = c1.get( Calendar.HOUR_OF_DAY );
				double m1 = c1.get( Calendar.MINUTE );
				
				int y0 = posCasilla( h0 );
				int y1 = posCasilla( h1 );
				
				if( ( m0 <= 30 ) && ( m0 > 0 ) )
				{
					y0--;
				}
				else if( m0 > 30 )
				{
					y0--;
				}
				
				if( m1 == 0 )
				{
					y1 = y1 - 1;
				}
				else
				{
					y1 = ( ( m1 > 0 ) && ( m1 <= 30 ) ) ? y1 : y1 + 1;
				}
				
				for( int i = y0; ( i <= y1 ) && ( i >= 0 ); i++ )
				{
					horas[ i ].setiTask( null );
					horas[ i ].setText( "" );
					horas[ i ].setBorder( BorderFactory.createLineBorder( lines ) );
					horas[ i ].setBackground( null );
					horas[ i ].setCursor( DEFAULT );
					horas[ i ].setToolTipText( "" );
					horas[ i ].removeActionListener( actionListener );
					horas[ i ].removeMouseListener( mouseListener );
				}
			}
		}
		
		public void actualizar( )
		{
			
		}
	}
	
	public class TaskButton extends ButtonTip
	{
		private static final long serialVersionUID = -2189605919183635542L;
		
		private ITask iTask;
		
		public TaskButton( )
		{
			super( );
		}
		
		public TaskButton( String txt, ITask iTask )
		{
			super( txt );
			this.iTask = iTask;
		}
		
		public ITask getiTask( )
		{
			return iTask;
		}
		
		public void setiTask( ITask iTask )
		{
			this.iTask = iTask;
		}
	}
	
	private static final long serialVersionUID = -5996038670351500277L;
	
	private ActionListener actionListener;
	
	private Color bg;
	
	private Border borderToolTip;
	
	private int cantDias;
	
	private int CASILLAS = 32;
	
	private PanelDia[ ] dias;
	
	private final String[ ] diasLabel =
	{
			"Domingo",
			"Lunes",
			"Martes",
			"Miercoles",
			"Jueves",
			"Viernes",
			"Sabado"
	};
	
	private Color fg;
	
	private int HORA_INICIO = 6;
	
	private Color labels;
	
	private JLabel[ ] labelsHoras;
	
	private Color lines;
	
	private MouseListener mouseListener;
	
	private JPanel panelCentral;
	
	private final boolean showDomingo;
	
	private final boolean showSabado;
	
	public PanelHorario( boolean sunday, boolean saturday )
	{
		setLayout( new BorderLayout( ) );
		
		showDomingo = sunday;
		showSabado = saturday;
		
		lines = Color.GRAY;
		bg = null;
		fg = Color.BLACK;
		labels = Color.BLACK;
		borderToolTip = BorderFactory.createEmptyBorder( 4, 4, 4, 4 );
		
		JPanel panelIzquierdo = new JPanel( );
		panelIzquierdo.setLayout( new BorderLayout( ) );
		panelIzquierdo.setBackground( null );
		panelIzquierdo.setBorder( BorderFactory.createEmptyBorder( 0, 5, 0, 5 ) );
		
		JPanel panelHoras = new JPanel( );
		panelHoras.setLayout( new GridLayout( CASILLAS, 1 ) );
		panelHoras.setBackground( null );
		labelsHoras = new JLabel[ CASILLAS ];
		int hora = HORA_INICIO;
		for( int i = 0; i < labelsHoras.length; i++ )
		{
			if( ( i % 2 ) == 0 )
			{
				labelsHoras[ i ] = new JLabel( formatHora( hora, 0 ) + " - " + formatHora( hora, 30 ) );
			}
			else
			{
				labelsHoras[ i ] = new JLabel( formatHora( hora++, 30 ) + " - " + formatHora( hora, 0 ) );
			}
			panelHoras.add( labelsHoras[ i ] );
		}
		
		JLabel labelEmpty = new JLabel( " " );
		labelEmpty.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		panelIzquierdo.add( labelEmpty, BorderLayout.NORTH );
		panelIzquierdo.add( panelHoras, BorderLayout.CENTER );
		
		panelCentral = new JPanel( );
		
		add( panelIzquierdo, BorderLayout.WEST );
		add( panelCentral, BorderLayout.CENTER );
		
		actualizar( );
	}
	
	public PanelHorario( boolean sunday, boolean saturday, int horaInicio, int horaFin )
	{
		HORA_INICIO = horaInicio;
		CASILLAS = ( horaFin - HORA_INICIO ) * 2;
		
		setLayout( new BorderLayout( ) );
		
		showDomingo = sunday;
		showSabado = saturday;
		
		lines = Color.GRAY;
		bg = null;
		fg = Color.BLACK;
		labels = Color.BLACK;
		borderToolTip = BorderFactory.createEmptyBorder( 4, 4, 4, 4 );
		
		JPanel panelIzquierdo = new JPanel( );
		panelIzquierdo.setLayout( new BorderLayout( ) );
		panelIzquierdo.setBackground( null );
		panelIzquierdo.setBorder( BorderFactory.createEmptyBorder( 0, 5, 0, 5 ) );
		
		JPanel panelHoras = new JPanel( );
		panelHoras.setLayout( new GridLayout( CASILLAS, 1 ) );
		panelHoras.setBackground( null );
		labelsHoras = new JLabel[ CASILLAS ];
		int hora = HORA_INICIO;
		for( int i = 0; i < labelsHoras.length; i++ )
		{
			if( ( i % 2 ) == 0 )
			{
				labelsHoras[ i ] = new JLabel( formatHora( hora, 0 ) + " - " + formatHora( hora, 30 ) );
			}
			else
			{
				labelsHoras[ i ] = new JLabel( formatHora( hora++, 30 ) + " - " + formatHora( hora, 0 ) );
			}
			panelHoras.add( labelsHoras[ i ] );
		}
		
		JLabel labelEmpty = new JLabel( " " );
		labelEmpty.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		panelIzquierdo.add( labelEmpty, BorderLayout.NORTH );
		panelIzquierdo.add( panelHoras, BorderLayout.CENTER );
		
		panelCentral = new JPanel( );
		
		add( panelIzquierdo, BorderLayout.WEST );
		add( panelCentral, BorderLayout.CENTER );
		
		actualizar( );
	}
	
	public PanelHorario( int horaInicio, int horaFin )
	{
		HORA_INICIO = horaInicio;
		CASILLAS = ( horaFin - HORA_INICIO ) * 2;
		
		setLayout( new BorderLayout( ) );
		
		showDomingo = false;
		showSabado = false;
		
		lines = Color.GRAY;
		bg = null;
		fg = Color.BLACK;
		labels = Color.BLACK;
		borderToolTip = BorderFactory.createEmptyBorder( 4, 4, 4, 4 );
		
		JPanel panelIzquierdo = new JPanel( );
		panelIzquierdo.setLayout( new BorderLayout( ) );
		panelIzquierdo.setBackground( null );
		panelIzquierdo.setBorder( BorderFactory.createEmptyBorder( 0, 5, 0, 5 ) );
		
		JPanel panelHoras = new JPanel( );
		panelHoras.setLayout( new GridLayout( CASILLAS, 1 ) );
		panelHoras.setBackground( null );
		labelsHoras = new JLabel[ CASILLAS ];
		int hora = HORA_INICIO;
		for( int i = 0; i < labelsHoras.length; i++ )
		{
			if( ( i % 2 ) == 0 )
			{
				labelsHoras[ i ] = new JLabel( formatHora( hora, 0 ) + " - " + formatHora( hora, 30 ) );
			}
			else
			{
				labelsHoras[ i ] = new JLabel( formatHora( hora++, 30 ) + " - " + formatHora( hora, 0 ) );
			}
			panelHoras.add( labelsHoras[ i ] );
		}
		
		JLabel labelEmpty = new JLabel( " " );
		labelEmpty.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
		panelIzquierdo.add( labelEmpty, BorderLayout.NORTH );
		panelIzquierdo.add( panelHoras, BorderLayout.CENTER );
		
		panelCentral = new JPanel( );
		
		add( panelIzquierdo, BorderLayout.WEST );
		add( panelCentral, BorderLayout.CENTER );
		
		actualizar( );
	}
	
	public void actualizar( )
	{
		panelCentral.removeAll( );
		
		cantDias = 5;
		if( showDomingo )
		{
			cantDias++;
			if( showSabado )
			{
				cantDias++;
			}
		}
		else if( showSabado )
		{
			cantDias++;
		}
		
		panelCentral.setLayout( new GridLayout( 1, cantDias ) );
		
		dias = new PanelDia[ cantDias ];
		
		for( int i = 0; i < dias.length; i++ )
		{
			if( !showDomingo )
			{
				dias[ i ] = new PanelDia( i + 1 );
				panelCentral.add( dias[ i ] );
			}
			else
			{
				dias[ i ] = new PanelDia( i );
				panelCentral.add( dias[ i ] );
			}
			
			dias[ i ].actualizar( );
		}
	}
	
	public void addTask( ITask iTask ) throws IllegalAccessException
	{
		if( ( iTask.getCalendarEvents( ) == null ) )
		{
			throw new IllegalArgumentException( "Los valores de Calendario no pueden ser null" );
		}
		
		for( Calendar[ ] evento : iTask.getCalendarEvents( ) )
		{
			if( evento[ 1 ].before( evento[ 0 ] ) )
			{
				throw new IllegalAccessException( "El final del task no puede estar antes del inicio" );
			}
		}
		
		for( Calendar[ ] evento : iTask.getCalendarEvents( ) )
		{
			// SUNDAY = 1
			Calendar start = evento[ 0 ];
			int dayOfWeek = start.get( Calendar.DAY_OF_WEEK );
			int dia = dayOfWeek - ( showDomingo ? 1 : 2 );
			if( !showDomingo && ( dayOfWeek == Calendar.SUNDAY ) )
			{
				continue;
			}
			if( ( dia < dias.length ) && ( dia >= 0 ) )
			{
				dias[ dia ].addTask( iTask );
			}
		}
		
		pintarInstance( );
	}
	
	private void configurar( )
	{
		setBackground( bg );
		for( JLabel labelHora : labelsHoras )
		{
			labelHora.setForeground( labels );
		}
		for( PanelDia panelDia : dias )
		{
			panelDia.configurar( );
		}
		pintarInstance( );
	}
	
	private String formatHora( int hora, int minutos )
	{
		String h = String.valueOf( hora ).length( ) == 1 ? "0" + hora : String.valueOf( hora );
		String m = String.valueOf( minutos ).length( ) == 1 ? "0" + minutos : String.valueOf( minutos );
		
		return h + ":" + m;
	}
	
	private void pintarInstance( )
	{
		// Dia
		Calendar c = Calendar.getInstance( );
		
		int y = c.get( Calendar.DAY_OF_WEEK ) - ( showDomingo ? 1 : 2 );
		if( ( y < cantDias ) && ( y >= 0 ) )
		{
			dias[ y ].setBackground( lines.brighter( ) );
		}
		
		// Hora
		double h0 = c.get( Calendar.HOUR_OF_DAY );
		double m0 = c.get( Calendar.MINUTE );
		int y0 = posCasilla( h0 );
		
		if( m0 == 0 )
		{
			y0 = y0 - 1;
		}
		else
		{
			y0 = ( ( m0 > 0 ) && ( m0 <= 30 ) ) ? y0 : y0 + 1;
		}
		
		if( ( y0 < CASILLAS ) && ( y0 >= 0 ) )
		{
			labelsHoras[ y0 ].setForeground( lines );
			for( PanelDia panelDia : dias )
			{
				panelDia.pintarInstance( y0 );
			}
		}
	}
	
	private int posCasilla( double h0 )
	{
		int cX0 = HORA_INICIO;
		int cY0 = 0;
		int cX1 = ( CASILLAS / 2 ) + HORA_INICIO;
		int cY1 = CASILLAS;
		int m = ( cY1 - cY0 ) / ( cX1 - cX0 );
		int b = -( m * HORA_INICIO );
		
		int y0 = ( int ) Math.ceil( ( ( m * h0 ) + b ) );
		
		return y0;
	}
	
	public void removeTask( ITask iTask )
	{
		for( Calendar[ ] evento : iTask.getCalendarEvents( ) )
		{
			// SUNDAY = 1
			int dayOfWeek = evento[ 0 ].get( Calendar.DAY_OF_WEEK );
			int dia = dayOfWeek - ( showDomingo ? 1 : 2 );
			if( !showDomingo && ( dayOfWeek == Calendar.SUNDAY ) )
			{
				dia = -1;
			}
			if( ( dia < dias.length ) && ( dia >= 0 ) )
			{
				dias[ dia ].removeTask( iTask );
			}
		}
		
		pintarInstance( );
	}
	
	public void setActionListener( ActionListener actionListener )
	{
		this.actionListener = actionListener;
	}
	
	public void setBg( Color bg )
	{
		this.bg = bg;
		configurar( );
	}
	
	public void setBorderToolTip( Border border )
	{
		Border empty = BorderFactory.createEmptyBorder( 4, 4, 4, 4 );
		
		borderToolTip = BorderFactory.createCompoundBorder( border, empty );
		configurar( );
	}
	
	public void setColors( Color bg, Color fg, Color label, Color line )
	{
		this.bg = bg;
		this.fg = fg;
		labels = label;
		lines = line;
		
		configurar( );
	}
	
	public void setFg( Color fg )
	{
		this.fg = fg;
		configurar( );
	}
	
	public void setLabels( Color labels )
	{
		this.labels = labels;
		configurar( );
	}
	
	public void setLines( Color lines )
	{
		this.lines = lines;
		configurar( );
	}
	
	public void setMouseListener( MouseListener mouseListener )
	{
		this.mouseListener = mouseListener;
	}
	
	public static void main( String[ ] args )
	{
		JFrame frame = new JFrame( "Test" );
		
		PanelHorario panelHorario = new PanelHorario( true, true/* , 3, 20 */ );
		Color fg = new Color( 250, 250, 250 );
		Color bg = new Color( 33, 33, 33 );
		Color label = fg;
		Color lines = bg.brighter( ).brighter( );
		panelHorario.setColors( bg, fg, label, lines );
		panelHorario.setBorderToolTip( BorderFactory.createLineBorder( new Color( 33, 33, 33 ) ) );
		
		ITask iTask = new ITask( )
		{
			@Override
			public Calendar[ ][ ] getCalendarEvents( )
			{
				Calendar[ ][ ] eventos = new Calendar[ 1 ][ 2 ];
				
				Calendar c0 = Calendar.getInstance( );
				c0.set( Calendar.DAY_OF_MONTH, 9 );
				// c0.set( Calendar.DAY_OF_MONTH, 11 );
				c0.set( Calendar.HOUR_OF_DAY, 14 );
				c0.set( Calendar.MINUTE, 0 );
				
				Calendar c1 = Calendar.getInstance( );
				// c1.set( Calendar.DAY_OF_MONTH, 11 );
				c1.set( Calendar.DAY_OF_MONTH, 9 );
				c1.set( Calendar.HOUR_OF_DAY, 15 );
				c1.set( Calendar.MINUTE, 0 );
				
				// Calendar c2 = Calendar.getInstance( );
				// c2.set( Calendar.DAY_OF_MONTH, 15 );
				// c2.set( Calendar.HOUR_OF_DAY, 15 );
				// c2.set( Calendar.MINUTE, 0 );
				//
				// Calendar c3 = Calendar.getInstance( );
				// c3.set( Calendar.DAY_OF_MONTH, 17 );
				// c3.set( Calendar.HOUR_OF_DAY, 17 );
				// c3.set( Calendar.MINUTE, 0 );
				
				eventos[ 0 ][ 0 ] = c0;
				eventos[ 0 ][ 1 ] = c1;
				
				// eventos[ 1 ][ 0 ] = c2;
				// eventos[ 1 ][ 1 ] = c3;
				
				return eventos;
			}
			
			@Override
			public Color getColor( )
			{
				return new Color( 220, 180, 10 );
			}
			
			@Override
			public String getName( )
			{
				return "Task 1";
			}
			
			@Override
			public String getToolTip( )
			{
				return "Este es el " + getName( );
			}
		};
		
		try
		{
			panelHorario.addTask( iTask );
		}
		catch( IllegalAccessException e )
		{
			e.printStackTrace( );
		}
		
		frame.add( panelHorario );
		
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.pack( );
		frame.setLocationRelativeTo( null );
		frame.setVisible( true );
		
		// panelHorario.removeTask( iTask );
	}
}
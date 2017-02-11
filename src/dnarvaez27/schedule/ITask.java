package dnarvaez27.schedule;

import java.util.Calendar;

import java.awt.Color;

public interface ITask
{
	public Color getColor( );

	public String getName( );

	public String getToolTip( );

	public Calendar[ ][ ] getCalendarEvents( );
}
package Game_objects;

import Geom.GpsPoint;
import Geom.Point3D;
import Maps.Map;
import Maps.Pixel;

import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Convert;

import Coords.MyCoords;
import GUI.Line;
/**
 * 
 * @author Netanel Ben-Isahar
 * @author daniel abargel
 * 
 * this class represnt a pacman object
 */
public class Packman {

	private int _id;
	public GpsPoint _GPS;
	public Point3D _GPSConvert;
	public Pixel _PixelLocation;
	private int _speed;
	private int _radius;
	public Path path;
	public double _time ; 


	/**
	 * this constructor build a packman.
	 * @param id the packman id.
	 * @param GpsLocation the packman gps location.
	 * @param speed the packman speed.
	 * @param radius the packman radius.
	 * @param map the game map
	 */
	public Packman(int id,GpsPoint GpsLocation,int speed,int radius ,Map map)
	{
		path = new Path();
		this._id=id;
		this._GPS=GpsLocation;
		this._speed=speed;
		this._radius=radius;
		_GPSConvert = new Point3D(GpsLocation.getLon(),GpsLocation.getLat(),GpsLocation.getAlt());
		this._PixelLocation = new Pixel(_GPSConvert, map);


	}
	
	/**
	 * this constructor build a packman.
	 * @param id the packman id.
	 * @param PixelLocation the packman pixel location.
	 * @param speed the packman speed.
	 * @param radius the packman radius.
	 * @param map the game map
	 */
	public Packman(int id,Pixel PixelLocation,int speed,int radius,Map map)
	{
		path = new Path();
		this._id=id;
		this._PixelLocation=PixelLocation;
		this._GPSConvert = new Point3D(map.Pixel2GPSPoint(PixelLocation.get_PixelX(),PixelLocation.get_PixelY()));
		this._GPS = new GpsPoint(_GPSConvert);
		this._speed=speed;
		this._radius=radius;

	}

	public int getId() {
		return _id;
	}


	public void setId(int id) {
		this._id = id;
	}


	public GpsPoint getGpsLocation() {
		return _GPS;
	}


	public void setGpsLocation(GpsPoint gpsLocation) {
		this._GPS = gpsLocation;
	}


	public Pixel getPixelLocation() {

		return _PixelLocation;

	}

	/**
	 * this function sets a new pixel location.
	 * @param PixelLocation  the packman pixel location.
	 * @param map the game map.
	 */
	public void setPixelLocation(Pixel PixelLocation,Map map) {
		this._PixelLocation = PixelLocation;
		this._GPSConvert = new Point3D(map.Pixel2GPSPoint(PixelLocation.get_PixelX(),PixelLocation.get_PixelY()));
		this._GPS = new GpsPoint(_GPSConvert);
	}


	public int getSpeed() {
		return _speed;
	}


	public void setSpeed(int speed) {
		this._speed = speed;
	}


	public int getRadius() {
		return _radius;
	}


	public void setRadius(int radius) {
		this._radius = radius;
	}

	public Point3D GPSConvert() {
		return _GPSConvert;
	}

	public void GPSConvert(Point3D xYZlocation) {
		_GPSConvert = xYZlocation;
	}
	
	/**
	 * this function calculate the packman's location after some time.
	 * @param time represents a time after game started.
	 * @param map represents the map.
	 * @return the packman's location after some time.
	 */
	public GpsPoint WhereInTime (double time ,Map map) 
	{
		double CurTime  = 0 ; 
		Line line = null;
		double dis = 0 ; 
				
		for (int i = 0; i < path.Lines.size() ; i++) 
		{
			 dis = path.Lines.get(i).distance();
		
			if (CurTime + dis/_speed  >= time)
			{
				line = path.Lines.get(i);
				break  ; 
			}
			 CurTime += dis/_speed ;	 // current time . 
		}
		if(!(line == null)) {
		Point3D Vector = line.Verctor();
		double DisTime = time - CurTime ; 
		double RelativeTime = (dis/_speed)/DisTime;
		if(RelativeTime !=  0.0 &&  DisTime != 0.0)
		Vector = new Point3D(Vector.x()/RelativeTime ,Vector.y()/RelativeTime ,Vector.z()/RelativeTime );
		Point3D Start = line.getGpsStart();
		Start.GPS2Meter();
		Start.add(Vector);
		Start.Meter2GPS();
		setPixelLocation(map.GPSPoint2Pixel(Start), map);
		}
		
		return this._GPS;
 	}

}

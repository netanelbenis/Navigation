package Coords;
import Geom.Point3D;

public class MyCoords implements coords_converter  {
	
	final int earth_r=6371000;
	/**
	 * This function convert the GPS point to meter and add a vector to the point.
	 * and converts back the to new GPS Point
	 * 
	 * @param gps represent a GPS point
	 * @param local_vector_in_meter  represent a vector in meter
	 * @return the new 3D point we found
	 */
	@Override
	public Point3D add(Point3D gps, Point3D local_vector_in_meter) {
		// TODO Auto-generated method stub
		
		gps.GPS2Meter();
		gps.add(local_vector_in_meter);
		gps.Meter2GPS();
		Point3D returnP = new Point3D(gps);
		return returnP;
	}
	/**
	 * This function computes the 2D distance (in meters) between the two GPS points 
	 * we used the file Excel that was send to use by Boaz formola.
	 * 
	 * @param gps0 represent the first point
	 * @param gps1 represent the second point
	 * @return the distance 2D in meters between two points
	 */
	public double distance2d(Point3D gps0, Point3D gps1) 
	{
		double lon_norm=Math.cos(gps0.x()*(Math.PI/180));
		System.out.println(lon_norm);

		double diff_x=gps1.x()-gps0.x();
		double diff_y=gps1.y()-gps0.y();


		double diff_rad_x=diff_x*(Math.PI/180);
		double diff_rad_y=diff_y*(Math.PI/180);

		double x_meter=Math.sin(diff_rad_x)*earth_r;
		double y_meter=Math.sin(diff_rad_y)*earth_r*lon_norm;
		return Math.sqrt(Math.pow(x_meter, 2)+Math.pow(y_meter, 2));
	}
	/**
	 * This function computes the 3D distance (in meters) between the two GPS points
	 * we used the file Excel that was send to use by Boaz formola.
	 * 
	 * @param gps0 represent the first point
	 * @param gps1 represent the second point
	 * @return the distance 3D in meters between the two points
	 */
	@Override
	public double distance3d(Point3D gps0, Point3D gps1) {
		// TODO Auto-generated method stub

		double diff_z=gps1.z()-gps0.z();
		double xy_distance=distance2d(gps0, gps1);

		return Math.sqrt(Math.pow(xy_distance, 2)+Math.pow(diff_z, 2));
	}
	/**
	 * This function converse the gps points to meter 
	 * and computes the 3D vector (in meters) between the two GPS's points
	 * 
	 * @param gps0 represent the first gps point
	 * @param gps1 represent the second gps point
	 * @return the 3D vector between the two points
	 */
	@Override
		public Point3D vector3D(Point3D gps0, Point3D gps1) {
		// TODO Auto-generated method stub
		gps0.GPS2Meter();
		gps1.GPS2Meter();
		double X = gps0.x() - gps1.x();
		double Y = gps0.y() - gps1.y();
		double Z = gps0.z() - gps1.z();
		return new Point3D(X, Y, Z);
	}
	/**
	 * This function computes the polar representation of the 3D vector be
	 * gps0--gps1 the function calculate the azimuth, the elevation and the distance - from coords_converter
	 * 
	 * @param gps0  represent the first gps point
	 * @param gps1 represent the second gps point
	 * @return an array- arr[0]- azimuth, arr[1]- elevation, arr[2]- distance
	 */
	@Override
	public double[] azimuth_elevation_dist(Point3D gps0, Point3D gps1) {
		double[] AzimuthArr = new double[3] ; 
	
		double elevation = gps0.up_angle(gps1);
		
		double dis = distance3d(gps0, gps1);

		double angle1 = Math.toRadians(gps0.x());
		double angle2 = Math.toRadians((gps1.x()));
		double diff_lon = Math.toRadians(gps0.y()-gps1.y());
		double y = Math.sin(diff_lon) * Math.cos(angle2);
		double x = Math.cos(angle1)*Math.sin(angle2) - Math.sin(angle1)*Math.cos(angle2)*Math.cos(diff_lon);
		double azimut = Math.atan2(y,x);
		azimut = Math.toDegrees(azimut);
		azimut = ((azimut + 360) % 360);
		
		
		
		AzimuthArr[0] =360- azimut; AzimuthArr[1] = elevation; AzimuthArr[2] = dis; 
		return AzimuthArr;
	}
	/**
	 * This function verify if this point is a valid lat, lon , alt coordinate:
	 * [-180,+180],[-90,+90],[-450, +inf] - from coords_converter
	 * 
	 * @param p represent a point 
	 * @return true if the gps point is valid and false otherwise 
	 */
	@Override
	public boolean isValid_GPS_Point(Point3D p) {
		// TODO Auto-generated method stub
		return p.x()<90&&p.x()>-90&&p.y()>-180&&p.y()<180&&p.z()>-450;
	}



}
